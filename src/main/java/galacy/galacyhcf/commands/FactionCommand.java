package galacy.galacyhcf.commands;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.command.defaults.VanillaCommand;
import cn.nukkit.level.Position;
import cn.nukkit.utils.TextFormat;
import galacy.galacyhcf.GalacyHCF;
import galacy.galacyhcf.managers.ClaimProcess;
import galacy.galacyhcf.managers.FactionsManager;
import galacy.galacyhcf.models.Claim;
import galacy.galacyhcf.models.Faction;
import galacy.galacyhcf.models.GPlayer;
import galacy.galacyhcf.utils.Utils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FactionCommand extends VanillaCommand {
    public static final String[] helpList = new String[]{
            "/f create <name>",
            "/f disband",
            "/f leave",
            "/f claim",
            "/f sethome",
            "/f home",
            "/f stuck",
            "/f balance",
            "/f who <player>",
            "/f info <faction>",
            "/f withdraw <amount>",
            "/f deposit <amount>",
            "/f invite <name>",
            "/f kick  <name>",
            "/f map",
            "/f leader <name>",
            "/f members"
    };

    public FactionCommand(String name) {
        super(name, "Factions Command", "/f help");
        setAliases(new String[]{"faction", "hcf", "fac", "factions", "f"});
        commandParameters.put("default",
                new CommandParameter[]{
                        new CommandParameter("argument", CommandParamType.STRING, true),
                        new CommandParameter("value", CommandParamType.STRING, true)
                });
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (sender instanceof GPlayer) {
            if (((GPlayer) sender).redisData().pvptime != 0)
                sender.sendMessage(Utils.prefix + TextFormat.RED + "You can not do this while your pvp is disabled.");
            else {
                if (args.length == 0) {
                    sender.sendMessage(Utils.prefix + TextFormat.RED + "Wrong usage. You can do '/f help' to get a list of the commands.");
                } else {
                    switch (args[0].toLowerCase()) {
                        default:
                            sender.sendMessage(Utils.prefix + TextFormat.RED + "Command not found. You can do '/f help' to get a list of the commands.");
                            break;
                        case "help":
                        case "?":
                            sender.sendMessage(Utils.prefix + TextFormat.RED + "Faction commands list!");
                            for (String help : helpList) {
                                sender.sendMessage(TextFormat.GREEN + "-" + TextFormat.GRAY + help);
                            }
                            break;
                        case "claim":
                            if (((GPlayer) sender).factionId == 0)
                                sender.sendMessage(Utils.prefix + TextFormat.RED + "You're not in a faction!");
                            else {
                                Faction faction = new Faction(GalacyHCF.mysql, ((GPlayer) sender).factionId);
                                if (!faction.leaderId.equals(((GPlayer) sender).xuid))
                                    sender.sendMessage(Utils.prefix + TextFormat.RED + "You're not the leader of this faction!");
                                else {
                                    if (((GPlayer) sender).claimProcess != null) {
                                        if (((GPlayer) sender).claimProcess.expired()) {
                                            sender.sendMessage(Utils.prefix + TextFormat.RED + "You've already started claiming.");
                                            break;
                                        }
                                    }
                                    ((GPlayer) sender).claimProcess = new ClaimProcess(Claim.factionClaim, (GPlayer) sender);
                                    sender.sendMessage(Utils.prefix + TextFormat.GREEN + "Claiming process start, please click on the first block!");
                                }
                            }
                            break;
                        case "create":
                        case "make":
                            if (((GPlayer) sender).factionId != 0) {
                                sender.sendMessage(Utils.prefix + TextFormat.RED + "Please leave your faction before doing this!");

                                break;
                            }
                            if (args.length == 2) {
                                if (args[1].length() > 15) {
                                    sender.sendMessage(Utils.prefix + TextFormat.RED + "Faction name too big!");

                                    break;
                                }
                                if (args[1].length() < 4) {
                                    sender.sendMessage(Utils.prefix + TextFormat.RED + "Faction name can't be less than 4 characters!");

                                    break;
                                }
                                if (!Utils.factionRgx.matcher(args[1]).matches()) {
                                    sender.sendMessage(Utils.prefix + TextFormat.RED + "You can't use special characters!");

                                    break;
                                }

                                GalacyHCF.factionsManager.createFaction(args[1], ((GPlayer) sender));
                                ((GPlayer) sender).joinFaction(new Faction(GalacyHCF.mysql, args[1]).id);
                                GalacyHCF.instance.getServer().broadcastMessage(Utils.prefix + TextFormat.GRAY + "Faction " + args[1] + " was just created by " + sender.getName());
                            } else sender.sendMessage(Utils.prefix + TextFormat.RED + "/f create <faction name>");

                            break;
                        case "disband":
                        case "delete":
                            if (((GPlayer) sender).factionId == 0)
                                sender.sendMessage(Utils.prefix + TextFormat.RED + "You're not in a faction!");
                            else {
                                Faction faction = new Faction(GalacyHCF.mysql, ((GPlayer) sender).factionId);
                                if (!faction.leaderId.equals(((GPlayer) sender).xuid))
                                    sender.sendMessage(Utils.prefix + TextFormat.RED + "You're not the leader of this faction!");
                                else {
                                    faction.disband();
                                    sender.sendMessage(Utils.prefix + TextFormat.GREEN + "Your faction was successfully disbanded.");
                                }
                            }

                            break;
                        case "leave":
                        case "quit":
                            if (((GPlayer) sender).factionId == 0)
                                sender.sendMessage(Utils.prefix + TextFormat.RED + "You're not in a faction!");
                            else {
                                Faction faction = new Faction(GalacyHCF.mysql, ((GPlayer) sender).factionId);
                                if (faction.leaderId.equals(((GPlayer) sender).xuid)) {
                                    sender.sendMessage(Utils.prefix + TextFormat.RED + "You have to set a new leader before leaving!");
                                } else {
                                    if (faction.createdAt.before(new Date(System.currentTimeMillis() - (1000 * 60 * 60 * 24)))) {
                                        ((GPlayer) sender).leaveFaction();
                                        sender.sendMessage(Utils.prefix + TextFormat.YELLOW + "You have successfully left your faction.");
                                        for (GPlayer player : faction.onlineMembers()) {
                                            player.sendMessage(Utils.prefix + TextFormat.YELLOW + sender.getName() + " has left the faction.");
                                        }
                                    } else {
                                        sender.sendMessage(Utils.prefix + TextFormat.RED + "You have to wait at least 24 hours before disbanding your faction!");
                                    }
                                }
                            }
                            break;
                        case "home":
                            if (((GPlayer) sender).factionId == 0)
                                sender.sendMessage(Utils.prefix + TextFormat.RED + "You're not in a faction!");
                            else {
                                Claim claim = GalacyHCF.claimsManager.findClaim(((GPlayer) sender).getFloorX(), ((GPlayer) sender).getFloorZ());
                                if (claim != null && claim.factionId != ((GPlayer) sender).factionId)
                                    sender.sendMessage(Utils.prefix + TextFormat.RED + "You can not do this inside an enemy claim. Use /f stuck.");
                                else {
                                    Faction faction = new Faction(GalacyHCF.mysql, ((GPlayer) sender).factionId);
                                    if (((GPlayer) sender).fightTime != 0)
                                        sender.sendMessage(Utils.prefix + TextFormat.RED + "You can not do this while you're in combat.");
                                    else {
                                        if (faction.home == null)
                                            sender.sendMessage(Utils.prefix + TextFormat.RED + "Your faction doesn't have a home set.");
                                        else {
                                            if (faction.home.equals(""))
                                                sender.sendMessage(Utils.prefix + TextFormat.RED + "Your faction doesn't have a home set.");
                                            else {
                                                String[] aa = faction.home.split(",");
                                                if (aa.length != 3)
                                                    sender.sendMessage(Utils.prefix + TextFormat.RED + "Your faction doesn't have a home set.");
                                                else {
                                                    ((GPlayer) sender).homeTeleport = true;
                                                    ((GPlayer) sender).moved = false;
                                                    ((GPlayer) sender).teleportTime = 10;
                                                    ((GPlayer) sender).teleportPosition = new Position(Integer.parseInt(aa[0]), Integer.parseInt(aa[1]), Integer.parseInt(aa[2]), sender.getServer().getDefaultLevel());
                                                    sender.sendMessage(Utils.prefix + TextFormat.GREEN + "You'll be teleported to your faction home in 10 seconds... DON'T MOVE!");
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            break;
                        case "stuck":
                            Claim claim = GalacyHCF.claimsManager.findClaim(((GPlayer) sender).getFloorX(), ((GPlayer) sender).getFloorZ());
                            if (claim == null)
                                sender.sendMessage(Utils.prefix + TextFormat.RED + "You're not inside an enemy faction claim, use /f home.");
                            else {
                                if (claim.factionId == ((GPlayer) sender).factionId)
                                    sender.sendMessage(Utils.prefix + TextFormat.RED + "You're not inside an enemy faction claim, use /f home.");
                                else {
                                    Faction faction = new Faction(GalacyHCF.mysql, ((GPlayer) sender).factionId);
                                    if (((GPlayer) sender).fightTime != 0)
                                        sender.sendMessage(Utils.prefix + TextFormat.RED + "You can not do this while you're in combat.");
                                    else {
                                        if (faction.home == null)
                                            sender.sendMessage(Utils.prefix + TextFormat.RED + "Your faction doesn't have a home set.");
                                        else {
                                            if (faction.home.equals(""))
                                                sender.sendMessage(Utils.prefix + TextFormat.RED + "Your faction doesn't have a home set.");
                                            else {
                                                String[] aa = faction.home.split(",");
                                                if (aa.length != 3)
                                                    sender.sendMessage(Utils.prefix + TextFormat.RED + "Your faction doesn't have a home set.");
                                                else {
                                                    ((GPlayer) sender).stuckTeleport = true;
                                                    ((GPlayer) sender).moved = false;
                                                    ((GPlayer) sender).teleportTime = 60;
                                                    ((GPlayer) sender).teleportPosition = new Position(Integer.parseInt(aa[0]), Integer.parseInt(aa[1]), Integer.parseInt(aa[2]), sender.getServer().getDefaultLevel());
                                                    sender.sendMessage(Utils.prefix + TextFormat.GREEN + "You'll be teleported to your faction home in 1 minute... DON'T MOVE!");
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            break;
                        case "sethome":
                            if (((GPlayer) sender).factionId == 0)
                                sender.sendMessage(Utils.prefix + TextFormat.RED + "You're not in a faction!");
                            else {
                                Faction faction = new Faction(GalacyHCF.mysql, ((GPlayer) sender).factionId);
                                if (!faction.leaderId.equals(((GPlayer) sender).xuid))
                                    sender.sendMessage(Utils.prefix + TextFormat.RED + "You're not the leader of this faction!");
                                else {
                                    if (!((GPlayer) sender).getLevel().getName().equals(sender.getServer().getDefaultLevel().getName()))
                                        sender.sendMessage(Utils.prefix + TextFormat.RED + "You can only set your home in the normal world.");
                                    else {
                                        Claim found = GalacyHCF.claimsManager.findClaim(((GPlayer) sender).getFloorX(), ((GPlayer) sender).getFloorZ());
                                        if (found == null || found.factionId != ((GPlayer) sender).factionId)
                                            sender.sendMessage(Utils.prefix + TextFormat.RED + "You can only set your home inside of your faction's claim.");
                                        else {
                                            faction.updateHome((int) ((GPlayer) sender).getX(), (int) ((GPlayer) sender).getY(), (int) ((GPlayer) sender).getZ());
                                            sender.sendMessage(Utils.prefix + TextFormat.GREEN + "You've successfully updated your faction's home.");
                                        }
                                    }
                                }
                            }
                            break;
                        case "balance":
                            if (((GPlayer) sender).factionId == 0)
                                sender.sendMessage(Utils.prefix + TextFormat.RED + "You're not in a faction!");
                            else {
                                Faction faction = new Faction(GalacyHCF.mysql, ((GPlayer) sender).factionId);
                                sender.sendMessage(Utils.prefix + TextFormat.GREEN + "Your faction's balance is at " + TextFormat.YELLOW + faction.balance + "$");
                            }
                            break;
                        case "deposit":
                            if (((GPlayer) sender).factionId == 0)
                                sender.sendMessage(Utils.prefix + TextFormat.RED + "You're not in a faction!");
                            else {
                                if (((GPlayer) sender).balance < 1)
                                    sender.sendMessage(Utils.prefix + TextFormat.RED + "You don't have enough money in your balance!");
                                else {
                                    if (args.length == 1)
                                        sender.sendMessage(Utils.prefix + TextFormat.RED + "/f deposit <amount>");
                                    else {
                                        int newBalance;
                                        try {
                                            newBalance = Integer.parseInt(args[1]);
                                        } catch (NumberFormatException exception) {
                                            sender.sendMessage(Utils.prefix + TextFormat.RED + "The amount has to be a real number. /f deposit <amount>");

                                            break;
                                        }
                                        if (((GPlayer) sender).balance < newBalance)
                                            sender.sendMessage(Utils.prefix + TextFormat.RED + "That's more money than what you have!");
                                        else {
                                            Faction faction = new Faction(GalacyHCF.mysql, ((GPlayer) sender).factionId);
                                            faction.updateBalance(faction.balance + newBalance);
                                            ((GPlayer) sender).updateBalance(((GPlayer) sender).balance - newBalance);
                                            sender.sendMessage(Utils.prefix + TextFormat.GREEN + "You've successfully put " + TextFormat.YELLOW + newBalance + "$ " + TextFormat.GREEN + "in your faction's balance.");
                                        }
                                    }
                                }
                            }
                            break;
                        case "info":
                            if (args.length == 1) {
                                if (((GPlayer) sender).factionId == 0)
                                    sender.sendMessage(Utils.prefix + TextFormat.RED + "You're not in a faction!");
                                else {
                                    Faction faction = new Faction(GalacyHCF.mysql, ((GPlayer) sender).factionId);
                                    factionInfo(sender, faction);
                                }
                            } else {
                                if (!GalacyHCF.factionsManager.factionExists(args[1]))
                                    sender.sendMessage(Utils.prefix + TextFormat.RED + "Faction does not exist.");
                                else {
                                    Faction faction = new Faction(GalacyHCF.mysql, args[1]);
                                    factionInfo(sender, faction);
                                }
                            }

                            break;
                        case "chat":
                        case "c":
                            if (((GPlayer) sender).factionId == 0)
                                sender.sendMessage(Utils.prefix + TextFormat.RED + "You're not in a faction!");
                            else {
                                if (((GPlayer) sender).chatType == GPlayer.Chat.Public) {
                                    ((GPlayer) sender).chatType = GPlayer.Chat.Faction;
                                    sender.sendMessage(Utils.prefix + TextFormat.GREEN + "Your chat is now set to faction mode, only your faction members can see your messages.");
                                } else {
                                    ((GPlayer) sender).chatType = GPlayer.Chat.Public;
                                    sender.sendMessage(Utils.prefix + TextFormat.GREEN + "Your chat is now set to public mode, everyone can see your messages.");
                                }
                            }
                            break;
                        case "who":
                            if (args.length == 1)
                                sender.sendMessage(Utils.prefix + TextFormat.RED + "/f who <player>");
                            else {
                                Player player = sender.getServer().getPlayer(args[1]);
                                if (player == null)
                                    sender.sendMessage(Utils.prefix + TextFormat.RED + "This player doesn't seem to be online!");
                                else {
                                    if (player instanceof GPlayer) {
                                        if (((GPlayer) player).factionId == 0)
                                            sender.sendMessage(Utils.prefix + TextFormat.YELLOW + player.getName() + " does not have a faction.");
                                        else {
                                            Faction faction = new Faction(GalacyHCF.mysql, ((GPlayer) player).factionId);
                                            sender.sendMessage(Utils.prefix + TextFormat.YELLOW + player.getName() + "'s faction is: " + TextFormat.YELLOW + faction.name + ".");
                                        }
                                    }
                                }
                            }
                            break;
                        case "withdraw":
                            if (((GPlayer) sender).factionId == 0)
                                sender.sendMessage(Utils.prefix + TextFormat.RED + "You're not in a faction!");
                            else {
                                Faction faction = new Faction(GalacyHCF.mysql, ((GPlayer) sender).factionId);
                                if (!faction.leaderId.equals(((GPlayer) sender).xuid))
                                    sender.sendMessage(Utils.prefix + TextFormat.RED + "You're not the leader of this faction!");
                                else {
                                    if (args.length == 1)
                                        sender.sendMessage(Utils.prefix + TextFormat.RED + "/f withdraw <amount>");
                                    else {
                                        int newBalance;
                                        try {
                                            newBalance = Integer.parseInt(args[1]);
                                        } catch (NumberFormatException exception) {
                                            sender.sendMessage(Utils.prefix + TextFormat.RED + "The amount has to be a real number. /f withdraw <amount>");

                                            break;
                                        }
                                        if (faction.balance < newBalance)
                                            sender.sendMessage(Utils.prefix + TextFormat.RED + "Your faction doesn't have that much money!");
                                        else {
                                            faction.updateBalance(faction.balance - newBalance);
                                            ((GPlayer) sender).updateBalance(((GPlayer) sender).balance + newBalance);
                                            sender.sendMessage(Utils.prefix + TextFormat.GREEN + "You've successfully withdrawn " + TextFormat.YELLOW + newBalance + "$ " + TextFormat.GREEN + "from your faction's balance.");
                                        }
                                    }
                                }
                            }
                            break;
                        case "members":
                            if (((GPlayer) sender).factionId == 0)
                                sender.sendMessage(Utils.prefix + TextFormat.RED + "You're not in a faction!");
                            else {
                                Faction faction = new Faction(GalacyHCF.mysql, ((GPlayer) sender).factionId);
                                ArrayList<String> members = faction.members();
                                StringBuilder names = new StringBuilder();
                                for (int i = 0; i < members.toArray().length; i++) {
                                    if (i == (members.toArray().length + 1))
                                        names.append(members.get(i)).append(".");
                                    else
                                        names.append(members.get(i)).append(", ");
                                }
                                sender.sendMessage(Utils.prefix + TextFormat.YELLOW + "Your faction's members are: " + TextFormat.YELLOW + names);
                            }
                            break;
                        case "kick":
                            if (((GPlayer) sender).factionId == 0)
                                sender.sendMessage(Utils.prefix + TextFormat.RED + "You're not in a faction!");
                            else {
                                Faction faction = new Faction(GalacyHCF.mysql, ((GPlayer) sender).factionId);
                                if (!faction.leaderId.equals(((GPlayer) sender).xuid))
                                    sender.sendMessage(Utils.prefix + TextFormat.RED + "You're not the leader of this faction!");
                                else {
                                    if (args.length == 1)
                                        sender.sendMessage(Utils.prefix + TextFormat.RED + "/f kick <member>");
                                    else {
                                        Player member = sender.getServer().getPlayer(args[1]);
                                        if (member == null)
                                            sender.sendMessage(Utils.prefix + TextFormat.RED + "This player doesn't seem to be online!");
                                        else {
                                            if (member instanceof GPlayer) {
                                                if (((GPlayer) member).factionId != ((GPlayer) sender).factionId)
                                                    sender.sendMessage(Utils.prefix + TextFormat.RED + member.getName() + " is not in your faction!");
                                                else {
                                                    ((GPlayer) member).leaveFaction();
                                                    for (GPlayer player : faction.onlineMembers()) {
                                                        player.sendMessage(Utils.prefix + TextFormat.GREEN + member.getName() + " was kicked from the faction.");
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            break;
                        case "invite":
                            if (((GPlayer) sender).factionId == 0)
                                sender.sendMessage(Utils.prefix + TextFormat.RED + "You're not in a faction!");
                            else {
                                if (args.length == 1)
                                    sender.sendMessage(Utils.prefix + TextFormat.RED + "/f invite <player>");
                                else {
                                    Faction faction = new Faction(GalacyHCF.mysql, ((GPlayer) sender).factionId);
                                    if (!faction.leaderId.equals(((GPlayer) sender).xuid))
                                        sender.sendMessage(Utils.prefix + TextFormat.RED + "You're not the leader of this faction!");
                                    else {
                                        if (faction.members().toArray().length >= FactionsManager.MaxMembers)
                                            sender.sendMessage(Utils.prefix + TextFormat.RED + "Your faction is full and you can't make any more invitations!");
                                        else {
                                            Player player = sender.getServer().getPlayer(args[1]);
                                            if (player == null)
                                                sender.sendMessage(Utils.prefix + TextFormat.RED + "This player doesn't seem to be online!");
                                            else {
                                                if (player instanceof GPlayer) {
                                                    if (((GPlayer) player).factionId != 0)
                                                        sender.sendMessage(Utils.prefix + TextFormat.RED + player.getName() + " is already in a faction!");
                                                    else {
                                                        if (((GPlayer) player).invitedTo != 0)
                                                            sender.sendMessage(Utils.prefix + TextFormat.RED + player.getName() + " has an invitation pending, try later!");
                                                        else {
                                                            ((GPlayer) player).inviteTo(faction.id);
                                                            sender.sendMessage(Utils.prefix + TextFormat.GREEN + "You've successfully sent an invitation to " + player.getName() + ".");
                                                            player.sendMessage(Utils.prefix + TextFormat.GREEN + sender.getName() + " has invited you to join his faction " + faction.name + ", you have 30 seconds to accept using the command " + TextFormat.ITALIC + TextFormat.YELLOW + "/f accept" + TextFormat.RESET + TextFormat.GREEN + ".");
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            break;
                        case "accept":
                            if (((GPlayer) sender).invitedTo == 0)
                                sender.sendMessage(Utils.prefix + TextFormat.RED + "You have no invitation to accept.");
                            else {
                                Faction faction = new Faction(GalacyHCF.mysql, ((GPlayer) sender).invitedTo);
                                if (faction.members().toArray().length >= FactionsManager.MaxMembers)
                                    sender.sendMessage(Utils.prefix + TextFormat.RED + "This faction is full and you can't join anymore");
                                else {
                                    ((GPlayer) sender).joinFaction(faction.id);
                                    sender.sendMessage(Utils.prefix + TextFormat.GREEN + "You've successfully joined the faction " + faction.name);
                                    for (GPlayer player : faction.onlineMembers()) {
                                        player.sendMessage(Utils.prefix + TextFormat.GREEN + sender.getName() + " has joined your faction.");
                                    }
                                }
                            }
                            break;
                        case "leader":
                        case "setleader":
                            if (((GPlayer) sender).factionId == 0)
                                sender.sendMessage(Utils.prefix + TextFormat.RED + "You're not in a faction!");
                            else {
                                if (args.length == 1)
                                    sender.sendMessage(Utils.prefix + TextFormat.RED + "/f leader <player name>");
                                else {
                                    Player p = sender.getServer().getPlayerExact(args[1]);
                                    if (p == null)
                                        sender.sendMessage(Utils.prefix + TextFormat.RED + "This player doesn't seem to be online!");
                                    else {
                                        if (p instanceof GPlayer) {
                                            if (((GPlayer) p).factionId != ((GPlayer) sender).factionId) {
                                                sender.sendMessage(Utils.prefix + TextFormat.RED + "He's not in the same faction as you!");

                                                break;
                                            }
                                            Faction faction = new Faction(GalacyHCF.mysql, ((GPlayer) sender).factionId);
                                            if (faction.leaderId.equals(((GPlayer) sender).xuid)) {
                                                faction.updateLeader((GPlayer) p);
                                                sender.sendMessage(Utils.prefix + TextFormat.GREEN + "You've successfully updated the leader of your faction");
                                                for (GPlayer player : faction.onlineMembers()) {
                                                    player.sendMessage(Utils.prefix + TextFormat.GREEN + p.getName() + " is the new faction leader!");
                                                }
                                            } else
                                                sender.sendMessage(Utils.prefix + TextFormat.RED + "You're not the leader of this faction!");
                                        }
                                    }
                                }
                            }
                            break;
                        case "map":
                            if (((GPlayer) sender).mapShown)
                                sender.sendMessage(Utils.prefix + TextFormat.RED + "Claims map is already shown.");
                            else {
                                ((GPlayer) sender).showMap();
                                sender.sendMessage(Utils.prefix + TextFormat.GREEN + "Claims map is now shown.");
                            }

                            break;
                    }
                }
            }
        }
        return false;
    }

    public void factionInfo(CommandSender sender, @NotNull Faction faction) {
        List<GPlayer> onlineMembers = faction.onlineMembers();
        StringBuilder onlineNames = new StringBuilder();
        for (int i = 0; i < onlineMembers.toArray().length; i++) {
            if (i == (onlineMembers.toArray().length + 1))
                onlineNames.append(onlineMembers.get(i).getName()).append(".");
            else
                onlineNames.append(onlineMembers.get(i).getName()).append(", ");
        }
        ArrayList<String> members = faction.members();

        sender.sendMessage(TextFormat.YELLOW + "Faction: " + TextFormat.BLUE + faction.name + TextFormat.GRAY + " [" + members.toArray().length + "/" + FactionsManager.MaxMembers + "]");
        sender.sendMessage(TextFormat.YELLOW + "Online: " + TextFormat.GREEN + onlineNames + TextFormat.BLUE + "(" + onlineMembers.toArray().length + "/" + members.toArray().length + ")");
        sender.sendMessage(TextFormat.YELLOW + "Leader: " + TextFormat.GREEN + faction.leaderName());
        sender.sendMessage(TextFormat.YELLOW + "DTR: " + TextFormat.GREEN + faction.dtr + "/" + faction.maxDtr());
        sender.sendMessage(TextFormat.YELLOW + "Home: " + TextFormat.GREEN + (faction.home == null ? "not set." : faction.home));
    }
}
