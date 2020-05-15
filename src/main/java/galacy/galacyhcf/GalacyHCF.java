package galacy.galacyhcf;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import galacy.galacyhcf.commands.FactionCommand;
import galacy.galacyhcf.commands.PingCommand;
import galacy.galacyhcf.listerners.EventsListener;
import galacy.galacyhcf.managers.ClaimsManager;
import galacy.galacyhcf.managers.FactionsManager;
import galacy.galacyhcf.providers.MySQL;
import galacy.galacyhcf.tasks.CombatTask;
import galacy.galacyhcf.tasks.ScoreboardTask;
import galacy.galacyhcf.tasks.TeleportTask;
import io.github.cdimascio.dotenv.Dotenv;

public class GalacyHCF extends PluginBase {

    public static GalacyHCF instance;
    public static Dotenv dotenv;
    public static FactionsManager factionsManager;
    public static ClaimsManager claimsManager;
    public static MySQL mysql;

    @Override
    public void onEnable() {
        // Loading resources
        if (getDataFolder().mkdir()) {
            getLogger().info("Successfully created data folder.");
        }

        // Static variables
        dotenv = Dotenv.configure().directory(getDataFolder().getPath()).load();
        instance = this;
        mysql = new MySQL(dotenv.get("DB_USERNAME"), dotenv.get("DB_PASSWORD"), dotenv.get("DB_NAME"));

        // Managers
        factionsManager = new FactionsManager(mysql);
        claimsManager = new ClaimsManager(mysql);


        // Commands
        getServer().getCommandMap().register("GalacyHCF", new FactionCommand("faction"));
        getServer().getCommandMap().register("GalacyHCF", new PingCommand("ping"));

        // Tasks
        getServer().getScheduler().scheduleRepeatingTask(new ScoreboardTask(this), 10);
        getServer().getScheduler().scheduleRepeatingTask(new TeleportTask(this), 20);
        getServer().getScheduler().scheduleRepeatingTask(new CombatTask(this), 20);

        // Listeners
        getServer().getPluginManager().registerEvents(new EventsListener(), this);

        getLogger().info(TextFormat.GREEN + "[HCF] Everything was loaded successfully.");
    }

    @Override
    public void onDisable() {
        mysql.close();
    }

}
