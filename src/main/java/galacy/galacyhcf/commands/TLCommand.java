package galacy.galacyhcf.commands;

import cn.nukkit.command.CommandSender;
import cn.nukkit.command.defaults.VanillaCommand;
import cn.nukkit.utils.TextFormat;
import galacy.galacyhcf.GalacyHCF;
import galacy.galacyhcf.models.Faction;
import galacy.galacyhcf.models.GPlayer;
import galacy.galacyhcf.utils.Utils;

public class TLCommand extends VanillaCommand {
    public TLCommand(String name) {
        super(name, "Send your position to your faction members!", "/tl");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (sender instanceof GPlayer) {
            if (((GPlayer) sender).rank == GPlayer.DEFAULT) {
                sender.sendMessage(Utils.prefix + TextFormat.RED + "You need a rank to use this command. You can get one at galacy.me/shop.");
            } else if (((GPlayer) sender).factionId != 0) {
                Faction faction = new Faction(GalacyHCF.mysql, ((GPlayer) sender).factionId);
                for (GPlayer player : faction.onlineMembers()) {
                    player.sendMessage(Utils.prefix + TextFormat.GREEN + sender.getName() + TextFormat.YELLOW + "'s position is X: " + ((GPlayer) sender).getFloorX() +
                            " Y: " + ((GPlayer) sender).getFloorY() +
                            " Z: " + ((GPlayer) sender).getFloorZ() + ".");
                }
            }
        }
        return false;
    }
}