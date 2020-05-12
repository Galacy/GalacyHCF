package galacy.galacyhcf;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import galacy.galacyhcf.managers.CommandManager;
import galacy.galacyhcf.listerners.EventsListener;
import galacy.galacyhcf.managers.FactionsManager;
import galacy.galacyhcf.managers.PlayersManager;
import galacy.galacyhcf.providers.MySQL;
import io.github.cdimascio.dotenv.Dotenv;

public class GalacyHCF extends PluginBase {

    public static GalacyHCF instance;
    public static Dotenv dotenv;
    public static FactionsManager factionsManager;
    public static PlayersManager playersManager;

    public MySQL mysql;

    @Override
    public void onEnable() {
        // Loading resources
        if(getDataFolder().mkdir()) {
            getLogger().info("Successfully created data folder.");
        }

        // Static variables
        dotenv = Dotenv.configure().directory(getDataFolder().getPath()).load();
        instance = this;
        mysql = new MySQL(dotenv.get("DB_USERNAME"), dotenv.get("DB_PASSWORD"), dotenv.get("DB_NAME"));

        // Managers
        factionsManager = new FactionsManager(mysql);
        playersManager = new PlayersManager(mysql);

        // Commands
        CommandManager.register();

        // Listeners
        getServer().getPluginManager().registerEvents(new EventsListener(), this);

        getLogger().info(TextFormat.GREEN + "[HCF] Everything was loaded successfully.");
    }

    @Override
    public void onDisable() {
        mysql.close();
    }

}
