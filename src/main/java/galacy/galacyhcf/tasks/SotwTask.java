package galacy.galacyhcf.tasks;

import cn.nukkit.scheduler.PluginTask;
import cn.nukkit.utils.TextFormat;
import galacy.galacyhcf.GalacyHCF;
import galacy.galacyhcf.utils.Utils;

public class SotwTask extends PluginTask<GalacyHCF> {

    public int time = 60 * 60;
    public boolean started = true;

    public SotwTask(GalacyHCF plugin) {
        super(plugin);
    }

    @Override
    public void onRun(int i) {
        if (started) {
            time--;
            if (time == 0) {
                GalacyHCF.instance.getServer().broadcastMessage(Utils.prefix + TextFormat.YELLOW + "SoTW is now done! you'll receive all the damages you take, be careful!");
                started = false;
            }
        }
    }
}
