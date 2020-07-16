package galacy.galacyhcf.listerners;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockID;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.math.BlockVector3;
import cn.nukkit.utils.TextFormat;

import java.util.ArrayList;

public class FoundDiamond implements Listener {
    private final ArrayList<String> blocks = new ArrayList<>();

    @EventHandler(ignoreCancelled = true)
    public void on(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (block.getId() == BlockID.DIAMOND_ORE) {
            if (!blocks.contains(block.asBlockVector3().toString())) {
                int count = 0;
                for (int x = block.getFloorX() - 4; x <= block.getFloorX() + 4; x++) {
                    for (int y = block.getFloorY() - 4; y <= block.getFloorY() + 4; y++) {
                        for (int z = block.getFloorZ() - 4; z <= block.getFloorZ() + 4; z++) {
                            BlockVector3 b = block.getLevel().getBlock(x, y, z).asBlockVector3();
                            if (!blocks.contains(b.toString())) {
                                blocks.add(b.toString());
                                count++;
                            }
                        }
                    }
                }
                event.getPlayer().getServer().broadcastMessage(TextFormat.AQUA + "[FD]" + TextFormat.WHITE + event.getPlayer().getName() + " found " + count + " diamonds.");
            }
            blocks.remove(block.asBlockVector3().toString());
        }
    }
}
