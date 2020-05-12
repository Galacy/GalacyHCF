package galacy.galacyhcf.managers;

import cn.nukkit.Player;
import cn.nukkit.utils.TextFormat;
import galacy.galacyhcf.GalacyHCF;
import galacy.galacyhcf.providers.MySQL;
import galacy.galacyhcf.providers.SQLStatements;
import galacy.galacyhcf.utils.Utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class FactionsManager {
    public MySQL mysql;

    public FactionsManager(MySQL db) {
        mysql = db;
    }

    public Boolean factionExists(String name) {
        String sql = SQLStatements.factionByName.replace("$name", name);
        try {
            ResultSet results = mysql.query(sql);
            if(results.next()) return true;
        } catch (SQLException e) {
            GalacyHCF.instance.getLogger().info(TextFormat.RED + "[MySQL]: Had issues finding faction by name: " + e);
        }

        return false;
    }

    public void createFaction(String name, Player player) {
        if (factionExists(name)) {
            player.sendMessage(TextFormat.RED + "Faction name exists already, please chose another one.");

            return;
        }
        String currentTime = Utils.dateFormat.format(new Date());

        String sql = SQLStatements.createFaction.replace("$created_at", currentTime).
                replace("$updated_at", currentTime).
                replace("$name", name).
                replace("$balance", "0").
                replace("$dtr", "5").
                replace("$leader_id", player.getLoginChainData().getXUID());
        try {
            mysql.exec(sql);
        } catch (SQLException e) {
            GalacyHCF.instance.getLogger().info(TextFormat.RED + "[MySQL]: Had issues creating a new faction: " + e);
        }
    }
}
