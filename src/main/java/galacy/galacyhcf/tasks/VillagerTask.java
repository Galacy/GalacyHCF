package galacy.galacyhcf.tasks;

import cn.nukkit.scheduler.PluginTask;
import galacy.galacyhcf.GalacyHCF;
import galacy.galacyhcf.entities.LogoutVillager;

public class VillagerTask extends PluginTask<GalacyHCF> {

    public int time = 60;
    public LogoutVillager villager;

    public VillagerTask(GalacyHCF plugin, LogoutVillager villager) {
        super(plugin);
        this.villager = villager;
    }

    @Override
    public void onRun(int i) {
        if (villager.isClosed()) {
            cancel();

            return;
        }
        time--;
        if (time < 1) {
            if (villager.isClosed()) villager.close();
            cancel();
        }
    }
}
