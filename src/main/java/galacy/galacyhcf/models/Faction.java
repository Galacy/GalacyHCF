package galacy.galacyhcf.models;

import cn.nukkit.utils.TextFormat;
import galacy.galacyhcf.GalacyHCF;
import galacy.galacyhcf.provider.MySQL;
import galacy.galacyhcf.provider.SQLStatements;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Faction {
    public MySQL mysql;

    public int id;
    public Date createdAt;
    public Date updatedAt;
    public String name;
    public int balance;
    public int dtr;
    public String home;
    public int leaderId;

    public Faction(MySQL db, String factionName) {
        mysql = db;
        String sql = SQLStatements.factionByName.replace("$name", factionName);
        try {
            ResultSet result = mysql.query(sql);
            if(result.next()) {
                id = result.getInt("id");
                createdAt = result.getDate("created_at");
                updatedAt = result.getDate("updated_at");
                name = factionName;
                balance = result.getInt("balance");
                dtr = result.getInt("dtr");
                home = result.getString("home");
                leaderId = result.getInt("leader_id");
            } else {
                GalacyHCF.instance.getLogger().info(TextFormat.RED + "[MySQL]: Couldn't find the faction.");
            }
        } catch (SQLException e) {
            GalacyHCF.instance.getLogger().info(TextFormat.RED + "[MySQL]: Had issues finding faction by name: " + e);
        }
    }

    public void disband() {
        // TODO: Remove claims too.
        String sql = SQLStatements.disbandFactionByName.replace("$name", name);
        try {
            mysql.exec(sql);
        } catch (SQLException e) {
            GalacyHCF.instance.getLogger().info(TextFormat.RED + "[MySQL]: Had issues deleting faction by name: " + e);
        }

        sql = SQLStatements.setPlayerFactionById.replace("$faction_id", String.valueOf(0)).replace("$xuid", String.valueOf(leaderId));
        try {
            mysql.exec(sql);
        } catch (SQLException e) {
            GalacyHCF.instance.getLogger().info(TextFormat.RED + "[MySQL]: Had issues remove faction from leader: " + e);
        }
    }
}
