package galacy.galacyhcf.tasks;

import cn.nukkit.level.Level;
import cn.nukkit.scheduler.PluginTask;
import cn.nukkit.utils.ThreadCache;
import galacy.galacyhcf.GalacyHCF;

public class GarbageCollectionTask extends PluginTask<GalacyHCF> {

    public GarbageCollectionTask(GalacyHCF plugin) {
        super(plugin);
    }

    @Override
    public void onRun(int i) {
        for (Level level : GalacyHCF.instance.getServer().getLevels().values()) {
            level.doChunkGarbageCollection();
            level.unloadChunks(true);
        }

        ThreadCache.clean();
        System.gc();
    }
}