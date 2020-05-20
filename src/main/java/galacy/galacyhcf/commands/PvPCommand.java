package galacy.galacyhcf.commands;

import cn.nukkit.command.CommandSender;
import cn.nukkit.command.defaults.VanillaCommand;
import cn.nukkit.utils.TextFormat;
import galacy.galacyhcf.GalacyHCF;
import galacy.galacyhcf.models.GPlayer;
import galacy.galacyhcf.utils.Utils;

public class PvPCommand extends VanillaCommand {
    public PvPCommand(String name) {
        super(name, "Turn on your pvp", "/pvp");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (sender instanceof GPlayer) {
            if (((GPlayer) sender).pvptimer) {
                sender.sendMessage(Utils.prefix + TextFormat.GREEN + "Your PvP is now enabled!");
                ((GPlayer) sender).redisData().pvptime = 0;
                ((GPlayer) sender).pvptimer = false;
                ((GPlayer) sender).redisData().update(GalacyHCF.redis);
            } else sender.sendMessage(Utils.prefix + TextFormat.RED + "Your pvp is already enabled.");
        }
        return false;
    }
}