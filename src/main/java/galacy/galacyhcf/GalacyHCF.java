package galacy.galacyhcf;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import galacy.galacyhcf.provider.MySQL;
import io.github.cdimascio.dotenv.Dotenv;

public class GalacyHCF extends PluginBase {

    public static GalacyHCF instance;
    public static MySQL mysql;
    public static Dotenv dotenv;

    @Override
    public void onEnable() {
        dotenv = Dotenv.load();
        instance = this;
        mysql = new MySQL(dotenv.get("USERNAME"), dotenv.get("PASSWORD"), dotenv.get("DATABASE"));
        getServer().getLogger().info(TextFormat.GREEN + "[HCF] Everything was loaded successfully.");
    }

    @Override
    public void onDisable() {
        mysql.close();
    }

}
