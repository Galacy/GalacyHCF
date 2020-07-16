package galacy.galacyhcf.commands;

import cn.nukkit.block.BlockCraftingTable;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.defaults.VanillaCommand;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.TextFormat;
import galacy.galacyhcf.models.GPlayer;
import galacy.galacyhcf.utils.Utils;

public class CraftCommand extends VanillaCommand {
    public CraftCommand(String name) {
        super(name, "Craft without using a crafting table!", "/craft");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (sender instanceof GPlayer) {
            GPlayer player = (GPlayer) sender;
            if (player.rank == GPlayer.DEFAULT) {
                player.sendMessage(Utils.prefix + TextFormat.RED + "You need a rank to use this command. You can get one at galacy.me/shop.");
            } else {
                player.getLevel().setBlock(new Vector3(player.x, 100, player.z), new BlockCraftingTable(), true, true);
                BlockEntity table = player.getLevel().getBlockEntity(new Vector3(player.x, 100, player.z));
                // TODO
            }
        }
        return false;
    }
}
