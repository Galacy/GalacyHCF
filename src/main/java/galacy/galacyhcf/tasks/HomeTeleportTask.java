package galacy.galacyhcf.tasks;

import cn.nukkit.Player;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.scheduler.PluginTask;
import galacy.galacyhcf.GalacyHCF;
import galacy.galacyhcf.models.GPlayer;

public class HomeTeleportTask extends PluginTask<GalacyHCF> {
    public HomeTeleportTask(GalacyHCF owner) {
        super(owner);
    }

    @Override
    public void onRun(int i) {
        for (Player player : GalacyHCF.instance.getServer().getOnlinePlayers().values()) {
            if (player instanceof GPlayer) {
                if (((GPlayer) player).homeTeleport) {
                    //player.
                }
            }
        }
    }
}
