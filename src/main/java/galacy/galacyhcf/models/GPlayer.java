package galacy.galacyhcf.models;

import cn.nukkit.Player;
import cn.nukkit.network.SourceInterface;
import galacy.galacyhcf.providers.MySQL;

import java.sql.Date;

public class GPlayer extends Player {

    public int rowId;
    public Date createdAt;
    public Date updatedAt;


    public GPlayer(SourceInterface interfaz, Long clientID, String ip, int port) {
        super(interfaz, clientID, ip, port);
        // TODO: Load data from database;
    }
}
