package galacy.galacyhcf.listerners;

import cn.nukkit.block.BlockSignPost;
import cn.nukkit.block.BlockWallSign;
import cn.nukkit.blockentity.BlockEntitySign;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.SignChangeEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.level.Level;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.TextFormat;
import galacy.galacyhcf.utils.Utils;

public class Elevator implements Listener {

    @EventHandler
    public void on(SignChangeEvent event) {
        if (event.getLine(0).toLowerCase().equals("[elevator]")) {
            switch (event.getLine(1).toLowerCase()) {
                case "up":
                    event.setLine(0, TextFormat.GREEN + "[Elevator]");
                    event.setLine(1, "UP");
                    event.setLine(2, "");
                    event.setLine(3, "");

                    break;

                case "down":
                    event.setLine(0, TextFormat.GREEN + "[Elevator]");
                    event.setLine(1, "DOWN");
                    event.setLine(2, "");
                    event.setLine(3, "");

                    break;
            }
        }
    }

    @EventHandler
    public void on(PlayerInteractEvent event) {
        if (event.getBlock() instanceof BlockSignPost || event.getBlock() instanceof BlockWallSign) {
            BlockEntitySign sign = (BlockEntitySign) event.getBlock().getLevel().getBlockEntity(event.getTouchVector());
            if (sign.getText()[0].equals(TextFormat.GREEN + "[Elevator]")) {
                switch (sign.getText()[1]) {
                    case "UP":
                        int y = findHighestBlockFrom(event.getTouchVector(), sign.getLevel());
                        event.getPlayer().teleport(new Vector3(sign.x, y, sign.z));

                        break;

                    case "DOWN":
                        int y2 = findLowestBlockFrom(event.getTouchVector(), sign.getLevel());
                        if (y2 <= 0) {
                            event.getPlayer().sendMessage(Utils.prefix + TextFormat.YELLOW + "Couldn't teleport you, there's no space down there.");
                        } else event.getPlayer().teleport(new Vector3(sign.x, y2, sign.z));

                        break;
                }
            }
        }
    }

    private int findHighestBlockFrom(Vector3 pos, Level level) {
        for (int y = (int) pos.y; y < 255; y++) {
            if (level.getBlockIdAt((int) pos.x, y, (int) pos.z) == 0) return y;
        }

        return 256;
    }

    private int findLowestBlockFrom(Vector3 pos, Level level) {
        for (int y = (int) pos.y; y > 0; y--) {
            if (level.getBlockIdAt((int) pos.x, y, (int) pos.z) == 0) return (y - 1);
        }

        return 0;
    }
}
