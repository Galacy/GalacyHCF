package galacy.galacyhcf.managers;

import galacy.galacyhcf.models.GPlayer;
import galacy.galacyhcf.providers.MySQL;

public class PlayersManager {

    public MySQL mysql;

    public PlayersManager(MySQL db) {
        mysql = db;
    }

    public boolean inFaction(GPlayer player) {
        // TODO
        return false;
    }
}
