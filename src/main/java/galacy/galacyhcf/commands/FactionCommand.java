package galacy.galacyhcf.commands;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.command.defaults.VanillaCommand;
import cn.nukkit.utils.TextFormat;
import galacy.galacyhcf.GalacyHCF;
import galacy.galacyhcf.models.Faction;
import galacy.galacyhcf.models.GPlayer;
import galacy.galacyhcf.utils.Utils;

import java.util.Date;

public class FactionCommand extends VanillaCommand {
    public static final String[] helpList = new String[] {
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
        setAliases(new String[]{"faction", "hcf", "fac", "factions", "team", "f", "hardcorefaction", "hardcorefactions"});
        commandParameters.put("default",
                new CommandParameter[]{
                        new CommandParameter("argument", CommandParamType.STRING, true),
                        new CommandParameter("value", CommandParamType.STRING, true)
                });
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (sender instanceof GPlayer) {
            if (args.length == 0) {
                sender.sendMessage(Utils.prefix + TextFormat.RED + "Invalid argument ! use the command /f help to see the commands.");
            }
            switch (args[0].toLowerCase()) {
//                case "freeze":
//
//                    break;
                case "help":
                case "?":
                    sender.sendMessage(Utils.prefix + TextFormat.RED + "Faction commands list!");
                    for (String help : helpList) {
                        sender.sendMessage(TextFormat.GREEN + "-" + TextFormat.GRAY + help);
                    }
                    break;
//                case "setdtr":
//
//                    break;
                case "claim":
                    // TODO
                    break;
                case "create":
                case "make":
                    if (((GPlayer) sender).factionId != 0) {
                        sender.sendMessage(Utils.prefix + TextFormat.RED + "Please leave your faction before doing this!");

                        break;
                    }
                    if(args[1] != null) {
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
                        GalacyHCF.instance.getServer().broadcastMessage("Faction "+args[1]+" was just created by "+sender.getName());
                    } else sender.sendMessage(Utils.prefix + TextFormat.RED + "/f create <faction name>");

                    break;
                case "disband":
                case "delete":
                    if (((GPlayer) sender).factionId == 0) sender.sendMessage(Utils.prefix + TextFormat.RED + "You're not in a faction!"); else {
                        Faction faction = new Faction(GalacyHCF.mysql, ((GPlayer) sender).factionId);
                        if (faction.leaderId == ((GPlayer) sender).xuid) {
                            faction.disband();
                            sender.sendMessage(Utils.prefix + TextFormat.GREEN + "Your faction was successfully disbanded.");
                        } else sender.sendMessage(Utils.prefix + TextFormat.RED + "You're not the leader of this faction!");
                    }

                    break;
                case "leave":
                case "quit":
                    if (((GPlayer) sender).factionId == 0) sender.sendMessage(Utils.prefix + TextFormat.RED + "You're not in a faction!"); else {
                        Faction faction = new Faction(GalacyHCF.mysql, ((GPlayer) sender).factionId);
                        if (faction.leaderId == ((GPlayer) sender).xuid) {
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
                    if (((GPlayer) sender).factionId == 0) sender.sendMessage(Utils.prefix + TextFormat.RED + "You're not in a faction!"); else {
                        Faction faction = new Faction(GalacyHCF.mysql, ((GPlayer) sender).factionId);
                        // TODO: Stop him from using home from enemy claim.
                        if (faction.home.isEmpty()) sender.sendMessage(Utils.prefix + TextFormat.RED + "Your faction doesn't have a home set."); else {
                            String[] pos = faction.home.split(",");
                            // TODO: Start tp task
                        }
                    }
                    break;
                case "stuck":
                    // TODO
                    break;
                case "sethome":
                    if (((GPlayer) sender).factionId == 0) sender.sendMessage(Utils.prefix + TextFormat.RED + "You're not in a faction!"); else {

                    }
                    break;
                case "deposit":
                    if (((GPlayer) sender).factionId == 0) sender.sendMessage(Utils.prefix + TextFormat.RED + "You're not in a faction!"); else {
                        if (((GPlayer) sender).balance < 1) sender.sendMessage(Utils.prefix + TextFormat.RED + "You don't have enough money in your balance!"); else {

                        }
                    }
                    break;
                case "info":
                    if (((GPlayer) sender).factionId == 0) sender.sendMessage(Utils.prefix + TextFormat.RED + "You're not in a faction!"); else {

                    }
                    break;
                case "chat":
                case "c":
                    if (((GPlayer) sender).factionId == 0) sender.sendMessage(Utils.prefix + TextFormat.RED + "You're not in a faction!"); else {

                    }
                    break;
                case "who":
                    // TODO
                    break;
                case "withdraw":
                    // TODO
                    break;
                case "members":
                    if (((GPlayer) sender).factionId == 0) sender.sendMessage(Utils.prefix + TextFormat.RED + "You're not in a faction!"); else {

                    }
                    break;
                case "kick":
                    if (((GPlayer) sender).factionId == 0) sender.sendMessage(Utils.prefix + TextFormat.RED + "You're not in a faction!"); else {

                    }
                    break;
                case "invite":
                    if (((GPlayer) sender).factionId == 0) sender.sendMessage(Utils.prefix + TextFormat.RED + "You're not in a faction!"); else {

                    }
                    break;
                case "accept":
                    // TODO
                    break;
                case "leader":
                case "setleader":
                    if (((GPlayer) sender).factionId == 0) sender.sendMessage(Utils.prefix + TextFormat.RED + "You're not in a faction!"); else {
                        if (args[1].isEmpty()) sender.sendMessage(Utils.prefix + TextFormat.RED + "/f setleader <player name>"); else {
                            Player p = sender.getServer().getPlayerExact(args[1]);
                            if (p instanceof GPlayer && p.isOnline()) {
                                if (((GPlayer) p).factionId != ((GPlayer) sender).factionId) {
                                    sender.sendMessage(Utils.prefix + TextFormat.RED + "He's not in the same faction as you!");

                                    break;
                                }
                                Faction faction = new Faction(GalacyHCF.mysql, ((GPlayer) sender).factionId);
                                if (faction.leaderId == ((GPlayer) sender).xuid) {
                                    faction.updateLeader((GPlayer) p);
                                    sender.sendMessage(Utils.prefix + TextFormat.GREEN + "You've successfully updated the leader of your faction");
                                    for (GPlayer player : faction.onlineMembers()) {
                                        player.sendMessage(Utils.prefix + TextFormat.GREEN + p.getName() + " is the new faction leader!");
                                    }
                                } else sender.sendMessage(Utils.prefix + TextFormat.RED + "You're not the leader of this faction!");
                            }
                        }
                    }
                    break;
                case "deny":
                    // TODO
                    break;
            }
        }
        return false;
    }
}
