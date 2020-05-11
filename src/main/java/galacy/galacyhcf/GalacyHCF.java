package galacy.galacyhcf;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import galacy.galacyhcf.faction.FactionsManager;
import galacy.galacyhcf.provider.MySQL;
import io.github.cdimascio.dotenv.Dotenv;

public class GalacyHCF extends PluginBase {

    public static GalacyHCF instance;
    public MySQL mysql;
    public Dotenv dotenv;
    public FactionsManager factionsManager;

    @Override
    public void onEnable() {
        if(getDataFolder().mkdir()) {
            getLogger().info("Successfully created data folder.");
        }

        dotenv = Dotenv.configure().directory(getDataFolder().getPath()).load();
        instance = this;
        mysql = new MySQL(dotenv.get("DB_USERNAME"), dotenv.get("DB_PASSWORD"), dotenv.get("DB_NAME"));
        factionsManager = new FactionsManager(mysql);
        getLogger().info(TextFormat.GREEN + "[HCF] Everything was loaded successfully.");
    }

    @Override
    public void onDisable() {
        mysql.close();
    }

}
