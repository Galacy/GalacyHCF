package galacy.galacyhcf.tasks;

import cn.nukkit.Player;
import cn.nukkit.scheduler.PluginTask;
import cn.nukkit.utils.TextFormat;
import galacy.galacyhcf.GalacyHCF;
import galacy.galacyhcf.models.GPlayer;
import galacy.galacyhcf.utils.Utils;

public class TeleportTask extends PluginTask<GalacyHCF> {

    public TeleportTask(GalacyHCF plugin) {
        super(plugin);
    }

    @Override
    public void onRun(int i) {
        for (Player player : GalacyHCF.instance.getServer().getOnlinePlayers().values()) {
            if (player instanceof GPlayer) {
                if (((GPlayer) player).homeTeleport || ((GPlayer) player).stuckTeleport) {
                    ((GPlayer) player).teleportTime--;
                    if (((GPlayer) player).teleportTime <= 0) {
                        ((GPlayer) player).homeTeleport = false;
                        ((GPlayer) player).stuckTeleport = false;
                        player.teleport(((GPlayer) player).teleportPosition);
                        player.sendMessage(Utils.prefix + TextFormat.GRAY + "You've been teleported to your faction's home.");
                    }
                }
            }
        }
    }
}
