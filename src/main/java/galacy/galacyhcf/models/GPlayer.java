package galacy.galacyhcf.models;

import cn.nukkit.Player;
import cn.nukkit.network.SourceInterface;
import cn.nukkit.utils.TextFormat;
import galacy.galacyhcf.GalacyHCF;
import galacy.galacyhcf.providers.SQLStatements;
import galacy.galacyhcf.utils.Utils;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GPlayer extends Player {

    // Currently USELESS so I commented it: public int rowId;
    public Date createdAt;
    public Date updatedAt;
    public String dbUsername;
    public int xuid;
    public int rank;
    public int balance;
    public int factionId;

    public GPlayer(SourceInterface interfaz, Long clientID, String ip, int port) {
        super(interfaz, clientID, ip, port);
        loadData();
        if (!username.equals(dbUsername)) {
            try {
                String currentTime = Utils.dateFormat.format(new java.util.Date());
                String sql = SQLStatements.updatePlayerUsernameById.
                        replace("$updated_at", currentTime).
                        replace("$xuid", getLoginChainData().getXUID()).
                        replace("$username", username);

                GalacyHCF.mysql.exec(sql);
                GalacyHCF.instance.getLogger().info(dbUsername + " has changed his name to " + username);
            }  catch (SQLException e) {
                GalacyHCF.instance.getLogger().info(TextFormat.RED + "[MySQL]: Had issues changing player username: " + e);
            }
        }
    }

    public void loadData() {
        try {
            String sql = SQLStatements.playerById.replace("$xuid", getLoginChainData().getXUID());
            ResultSet result = GalacyHCF.mysql.query(sql);
            if(result.next()) {
                // Currently USELESS so I commented it: rowId = result.getInt("id");
                createdAt = result.getDate("created_at");
                updatedAt = result.getDate("updated_at");
                dbUsername = result.getString("username");
                xuid = result.getInt("xuid");
                rank = result.getInt("rank");
                balance = result.getInt("balance");
                factionId = result.getInt("faction_id");
            } else {
                GalacyHCF.instance.getLogger().info(TextFormat.YELLOW + "[MySQL]: Couldn't find the player in the database, creating a new one.");
                try {
                    String currentTime = Utils.dateFormat.format(new java.util.Date());
                    sql = SQLStatements.createPlayer.
                            replace("$created_at", currentTime).
                            replace("$updated_at", currentTime).
                            replace("$username", username).
                            replace("$xuid", getLoginChainData().getXUID()).
                            replace("$rank", "0").
                            replace("$balance", "0").
                            replace("$faction_id", "0");

                    GalacyHCF.mysql.exec(sql);
                } catch (SQLException e) {
                    GalacyHCF.instance.getLogger().info(TextFormat.RED + "[MySQL]: Had issues creating a new player: " + e);
                }
            }
        } catch (SQLException e) {
            GalacyHCF.instance.getLogger().info(TextFormat.RED + "[MySQL]: Had issues finding faction by name: " + e);
        }
    }

    public void leaveFaction() {
        String sql = SQLStatements.setPlayerFactionById.replace("$faction_id", "0").replace("$xuid", String.valueOf(xuid));
        try {
            GalacyHCF.mysql.exec(sql);
        } catch (SQLException e) {
            GalacyHCF.instance.getLogger().info(TextFormat.RED + "[MySQL]: Had issues removing player from faction: " + e);
        }

        factionId = 0;
    }
}
