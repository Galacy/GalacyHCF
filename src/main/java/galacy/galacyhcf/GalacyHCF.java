package galacy.galacyhcf;

import cn.nukkit.entity.Entity;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import galacy.galacyhcf.borders.SpawnBorder;
import galacy.galacyhcf.borders.WorldBorder;
import galacy.galacyhcf.commands.*;
import galacy.galacyhcf.entities.EnderPearl;
import galacy.galacyhcf.entities.ItemDrop;
import galacy.galacyhcf.entities.LogoutVillager;
import galacy.galacyhcf.listerners.EventsListener;
import galacy.galacyhcf.managers.ClaimsManager;
import galacy.galacyhcf.managers.FactionsManager;
import galacy.galacyhcf.managers.ShopsManager;
import galacy.galacyhcf.providers.MySQL;
import galacy.galacyhcf.providers.Redis;
import galacy.galacyhcf.tasks.*;
import io.github.cdimascio.dotenv.Dotenv;

public class GalacyHCF extends PluginBase {

    public static GalacyHCF instance;
    public static Dotenv dotenv;
    public static FactionsManager factionsManager;
    public static ClaimsManager claimsManager;
    public static WorldBorder worldBorder;
    public static SpawnBorder spawnBorder;
    public static ShopsManager shopsManager;
    public static MySQL mysql;
    public static Redis redis;
    public static SotwTask sotwTask;
    public static DtrRegenerationTask dtrRegenerationTask;

    @Override
    public void onEnable() {
        // Loading resources
        if (getDataFolder().mkdir()) {
            getLogger().info("Successfully created data folder.");
        }

        // Static variables
        dotenv = Dotenv.configure().directory(getDataFolder().getPath()).load();
        instance = this;
        mysql = new MySQL(dotenv.get("DB_HOST"), dotenv.get("DB_USERNAME"), dotenv.get("DB_PASSWORD"), dotenv.get("DB_NAME"));
        redis = new Redis(dotenv.get("REDIS_HOST"));

        // Managers
        factionsManager = new FactionsManager(mysql);
        claimsManager = new ClaimsManager(mysql);
        worldBorder = new WorldBorder(-2000, -2000, 2000, 2000);
        shopsManager = new ShopsManager(getDataFolder().getPath() + "/shops.json");

        // Commands
        getServer().getCommandMap().register("GalacyHCF", new FactionCommand("faction"));
        getServer().getCommandMap().register("GalacyHCF", new PingCommand("ping"));
        getServer().getCommandMap().register("GalacyHCF", new LivesCommand("lives"));
        getServer().getCommandMap().register("GalacyHCF", new ReviveCommand("revive"));
        getServer().getCommandMap().register("GalacyHCF", new PvPCommand("pvp"));
        getServer().getCommandMap().register("GalacyHCF", new SotwCommand("sotw"));
        getServer().getCommandMap().register("GalacyHCF", new StatsCommand("stats"));
        getServer().getCommandMap().register("GalacyHCF", new OresCommand("ores"));
        getServer().getCommandMap().register("GalacyHCF", new BalanceCommand("balance"));
        getServer().getCommandMap().register("GalacyHCF", new PayCommand("pay"));
        getServer().getCommandMap().register("GalacyHCF", new CoordsCommand("coords"));
        getServer().getCommandMap().register("GalacyHCF", new ShopCommand("shop"));
        getServer().getCommandMap().register("GalacyHCF", new KitCommand("kit"));
        // FIXME: getServer().getCommandMap().register("GalacyHCF", new CraftCommand("craft"));
        getServer().getCommandMap().register("GalacyHCF", new TLCommand("tl"));
        getServer().getCommandMap().register("GalacyHCF", new SetRankCommand("setrank"));

        // Tasks
        getServer().getScheduler().scheduleRepeatingTask(new ScoreboardTask(this), 10, true);
        getServer().getScheduler().scheduleRepeatingTask(new TeleportTask(this), 20);
        getServer().getScheduler().scheduleRepeatingTask(new CombatTask(this), 20);
        getServer().getScheduler().scheduleRepeatingTask(new PvPTask(this), 20);
        getServer().getScheduler().scheduleRepeatingTask(new BardTask(this), 20);
        getServer().getScheduler().scheduleRepeatingTask(dtrRegenerationTask = new DtrRegenerationTask(this), 20 * 120);
        getServer().getScheduler().scheduleRepeatingTask(new MessageAdTask(this), 20 * 120);
        getServer().getScheduler().scheduleRepeatingTask(new GarbageCollectionTask(this), 20 * 60 * 60, true);

        // Listeners
        getServer().getPluginManager().registerEvents(new EventsListener(), this);
        //getServer().getPluginManager().registerEvents(new Elevator(), this);
        //getServer().getPluginManager().registerEvents(new FoundDiamond(), this);

        // Entities
        Entity.registerEntity("EnderPearl", EnderPearl.class, true);
        Entity.registerEntity("Item", ItemDrop.class, true);
        Entity.registerEntity("LogoutVillager", LogoutVillager.class, true);

        getLogger().info(TextFormat.GREEN + "[HCF] Everything is loaded successfully.");
    }

    @Override
    public void onDisable() {
        mysql.close();
        redis.close();
    }

}
