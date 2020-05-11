package galacy.galacyhcf;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import galacy.galacyhcf.provaider.MySQL;


public class GalacyHCF extends PluginBase {

    public static GalacyHCF instance;

    @Override
    public void onEnable() {
        instance = this;
        new MySQL();
        getServer().getLogger().info(TextFormat.GREEN + "[HFC] Successfully charged.");
    }

    @Override
    public void onDisable() {
        MySQL.close();
    }

}
