package galacy.galacyhcf.tasks;

import cn.nukkit.Player;
import cn.nukkit.scheduler.PluginTask;
import cn.nukkit.utils.TextFormat;
import galacy.galacyhcf.GalacyHCF;
import galacy.galacyhcf.models.GPlayer;
import galacy.galacyhcf.utils.Utils;

public class PvPTask extends PluginTask<GalacyHCF> {

    public PvPTask(GalacyHCF plugin) {
        super(plugin);
    }

    @Override
    public void onRun(int i) {
        for (Player player : GalacyHCF.instance.getServer().getOnlinePlayers().values()) {
            if (player instanceof GPlayer) {
                if (((GPlayer) player).pvptimer) {
                    ((GPlayer) player).redisData().pvptime--;
                    if (((GPlayer) player).redisData().pvptime == 0) {
                        ((GPlayer) player).pvptimer = false;
                        ((GPlayer) player).redisData().update(GalacyHCF.redis);
                        player.sendMessage(Utils.prefix + TextFormat.YELLOW + "PvP timer expired, your PvP is now enabled!");
                    }
                }
            }
        }
    }
}