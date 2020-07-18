package galacy.galacyhcf.tasks;

import cn.nukkit.scheduler.PluginTask;
import cn.nukkit.utils.TextFormat;
import galacy.galacyhcf.GalacyHCF;
import galacy.galacyhcf.utils.Utils;

public class MessageAdTask extends PluginTask<GalacyHCF> {

    public MessageAdTask(GalacyHCF plugin) {
        super(plugin);
    }

    @Override
    public void onRun(int i) {
        GalacyHCF.instance.getServer().broadcastMessage(Utils.prefix + "You can purchase a rank from our store at " + TextFormat.GOLD + "Galacy.tebex.io" + TextFormat.GRAY + ".");
    }
}