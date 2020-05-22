package galacy.galacyhcf.commands;

import cn.nukkit.command.CommandSender;
import cn.nukkit.command.defaults.VanillaCommand;
import cn.nukkit.utils.TextFormat;
import galacy.galacyhcf.models.GPlayer;
import galacy.galacyhcf.utils.Utils;

public class CoordsCommand extends VanillaCommand {
    public CoordsCommand(String name) {
        super(name, "Coords Command", "/coords");
        setAliases(new String[]{"pos", "xyz"});
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (sender instanceof GPlayer) {
            if (((GPlayer) sender).coords) {
                ((GPlayer) sender).coords = false;
                sender.sendMessage(Utils.prefix + TextFormat.YELLOW + "Coords disabled.");
            } else {
                ((GPlayer) sender).coords = true;
                sender.sendMessage(Utils.prefix + TextFormat.GREEN + "Coords enabled.");
            }
        }
        return false;
    }
}