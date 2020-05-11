package galacy.galacyhcf.faction;

import cn.nukkit.Player;
import galacy.galacyhcf.provider.MySQL;

public class FactionsManager {
    public MySQL mysql;

    public FactionsManager(MySQL db) {
        mysql = db;
    }

    public void factionExists(String name) {}

    public void createFaction(String name, Player player) {}

    public void disbandFaction(String name) {}
}
