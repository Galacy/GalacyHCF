package galacy.galacyhcf.commands;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.command.defaults.VanillaCommand;
import cn.nukkit.utils.TextFormat;
import galacy.galacyhcf.GalacyHCF;
import galacy.galacyhcf.models.GPlayer;
import galacy.galacyhcf.utils.Utils;

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
        setAliases(new String[]{"faction", "hcf", "fac", "factions", "team", "t", "hardcorefaction", "hardcorefactions"});
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
                    if (GalacyHCF.playersManager.inFaction((GPlayer) sender)) {
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
                    // TODO
                    break;
                case "leave":
                case "quit":
                    // TODO
                    break;
                case "home":
                    // TODO
                    break;
                case "stuck":
                    // TODO
                    break;
                case "sethome":
                    // TODO
                    break;
                case "deposit":
                    // TODO
                    break;
                case "info":
                    // TODO
                    break;
                case "chat":
                case "c":
                    // TODO
                    break;
                case "who":
                    // TODO
                    break;
                case "withdraw":
                    // TODO
                    break;
                case "members":
                    // TODO
                    break;
                case "kick":
                    // TODO
                    break;
                case "invite":
                    // TODO
                    break;
                case "accept":
                    // TODO
                    break;
                case "leader":
                case "setleader":
                    // TODO
                    break;
                case "deny":
                    // TODO
                    break;
            }
        }
        return false;
    }


}
