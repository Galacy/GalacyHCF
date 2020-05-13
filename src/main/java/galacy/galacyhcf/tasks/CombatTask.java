package galacy.galacyhcf.tasks;

import cn.nukkit.Player;
import cn.nukkit.scheduler.PluginTask;
import galacy.galacyhcf.GalacyHCF;
import galacy.galacyhcf.models.GPlayer;

public class CombatTask extends PluginTask<GalacyHCF> {

    public CombatTask(GalacyHCF plugin) {
        super(plugin);
    }

    @Override
    public void onRun(int i) {
        for (Player player : GalacyHCF.instance.getServer().getOnlinePlayers().values()) {
            if (player instanceof GPlayer) {
                if (((GPlayer) player).fightTime != 0) {
                    ((GPlayer) player).fightTime--;
                }
            }
        }
    }
}
