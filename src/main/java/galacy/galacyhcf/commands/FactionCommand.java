package galacy.galacyhcf.commands;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.command.defaults.VanillaCommand;
import cn.nukkit.utils.TextFormat;
import galacy.galacyhcf.utils.Utils;

public class FactionCommand extends VanillaCommand {
    public FactionCommand(String name) {
        super(name, "Factions Command", "/f help");
        setAliases(new String[]{"faction", "hcf", "fac", "factions", "team", "t", "hardcorefaction", "hardcorefactions"});
        commandParameters.put("default",
                new CommandParameter[]{
                        new CommandParameter("argument", CommandParamType.STRING, true),
                        new CommandParameter("value", CommandParamType.STRING, true)
                });
    }

    public String[] getHelp() {
        return new String[] {
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
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (sender instanceof Player) { //Replace with future custom Player class
            Player player = (Player) sender;
            if (args.length == 0) {
                player.sendMessage(Utils.getPrefix + TextFormat.RED + "Invalid argument ! use the command /f help to see the commands.");
            }
            switch (args[0].toLowerCase()) {
                case "freeze":
                    //Coming soon features
                    break;
                case "help":
                case "?":
                    player.sendMessage(Utils.getPrefix + TextFormat.RED + "Faction commands list!");
                    for (String help : getHelp()) {
                        player.sendMessage(TextFormat.GREEN + "-" + TextFormat.GRAY + help);
                    }
                    break;
                case "setdtr":
                    //Coming soon features
                    break;
                case "claim":
                    //Coming soon features
                    break;
                case "create":
                case "make":
                    break;
                case "disband":
                case "delete":
                    //Coming soon features
                    break;
                case "leave":
                case "quit":
                    //Coming soon features
                    break;
                case "home":
                    //Coming soon features
                    break;
                case "stuck":
                    //Coming soon features
                    break;
                case "sethome":
                    //Coming soon features
                    break;
                case "deposit":
                    //Coming soon features
                    break;
                case "info":
                    //Coming soon features
                    break;
                case "chat":
                case "c":
                    //Coming soon features
                    break;
                case "who":
                    //Coming soon features
                    break;
                case "withdraw":
                    //Coming soon features
                    break;
                case "members":
                    //Coming soon features
                    break;
                case "kick":
                    //Coming soon features
                    break;
                case "invite":
                    //Coming soon features
                    break;
                case "accept":
                    //Coming soon features
                    break;
                case "leader":
                case "setleader":
                    //Coming soon features
                    break;
                case "deny":
                    //Coming soon features
                    break;
            }
        }
        return false;
    }


}
