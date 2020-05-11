package galacy.galacyhcf.managers;

import cn.nukkit.Player;
import galacy.galacyhcf.providers.MySQL;

public class PlayersManager {

    public MySQL mysql;

    public PlayersManager(MySQL db) {
        mysql = db;
    }

    public boolean inFaction(Player player) {
        // TODO
        return false;
    }
}
