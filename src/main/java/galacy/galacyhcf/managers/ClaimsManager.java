package galacy.galacyhcf.managers;

import cn.nukkit.utils.TextFormat;
import galacy.galacyhcf.GalacyHCF;
import galacy.galacyhcf.models.Claim;
import galacy.galacyhcf.models.Faction;
import galacy.galacyhcf.providers.MySQL;
import galacy.galacyhcf.providers.SQLStatements;
import galacy.galacyhcf.utils.Utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class ClaimsManager {
    public MySQL mysql;
    public ArrayList<Claim> claims = new ArrayList<>();

    public ClaimsManager(MySQL mysql) {
        this.mysql = mysql;
        loadData();
    }

    public void loadData() {
        claims.clear();
        try {
            ResultSet results = mysql.query(SQLStatements.allClaims);
            Claim spawn = null;
            while (results.next()) {
                Claim claim = new Claim(
                        results.getInt("id"),
                        results.getDate("created_at"),
                        results.getDate("updated_at"),
                        results.getInt("type"),
                        results.getInt("faction_id"),
                        results.getString("faction_name"),
                        results.getInt("x1"),
                        results.getInt("x2"),
                        results.getInt("z1"),
                        results.getInt("z2"));
                claims.add(claim);
                if (claim.type == Claim.spawnClaim) spawn = claim;
            }
            if (spawn == null) {
                GalacyHCF.instance.getLogger().info(TextFormat.YELLOW + "Spawn not found, creating a new one 100x100.");
                String currentTime = Utils.dateFormat.format(new Date());
                try {
                    mysql.exec(SQLStatements.createFactionClaim.
                            replace("$created_at", currentTime).
                            replace("$updated_at", currentTime).
                            replace("$type", String.valueOf(Claim.spawnClaim)).
                            replace("$faction_id", "0").
                            replace("$faction_name", "Spawn").
                            replace("$x1", "-25").
                            replace("$x2", "25").
                            replace("$z1", "-25").
                            replace("$z2", "25")
                    );
                } catch (SQLException e) {
                    GalacyHCF.instance.getLogger().info(TextFormat.RED + "[MySQL]: Had issues creating new claim: " + e);
                }
                loadData();
            } else {
                GalacyHCF.spawnBorder = new SpawnBorder(spawn.x1, spawn.z1, spawn.x2, spawn.z2);
                GalacyHCF.instance.getLogger().info(TextFormat.AQUA + "Spawn successfulyl loaded.");
            }
        } catch (SQLException e) {
            GalacyHCF.instance.getLogger().info(TextFormat.RED + "[MySQL]: Had issues loading all claims: " + e);
        }
        GalacyHCF.instance.getLogger().info(TextFormat.AQUA + "Successfully loaded " + claims.toArray().length + " claims.");
    }

    public boolean containsClaim(int x1, int x2, int z1, int z2) {
        for (Claim claim : claims) {
            if (((claim.x1 <= x1 && claim.x2 >= x1) || (claim.x1 <= x2 && claim.x2 >= x2)) && ((claim.z1 <= z1 && claim.z2 >= z1) || (claim.z1 <= z2 && claim.z2 >= z2))) {
                return true;
            }
        }

        return false;
    }

    public Claim findClaim(int x, int z) {
        for (Claim claim : claims) {
            if ((claim.x1 <= x && claim.x2 >= x) && (claim.z1 <= z && claim.z2 >= z)) {
                return claim;
            }
        }

        return null;
    }

    public void tryClaiming(ClaimProcess process) {
        int x1 = Math.min((int) process.firstPos.x, (int) process.secondPos.x);
        int z1 = Math.min((int) process.firstPos.z, (int) process.secondPos.z);
        int x2 = Math.max((int) process.firstPos.x, (int) process.secondPos.x);
        int z2 = Math.max((int) process.firstPos.z, (int) process.secondPos.z);

        process.price = ((x2 - x1 + 1) * (z2 - z1 + 1)) * 5;

        process.player.sendMessage(Utils.prefix + TextFormat.GREEN + "Your claim will cost your faction " + TextFormat.YELLOW + process.price + "$" + TextFormat.GREEN + ", write 'yes' or 'accept' in the chat to purchase it.");
    }

    public boolean createClaim(ClaimProcess process) {
        int x1 = Math.min((int) process.firstPos.x, (int) process.secondPos.x);
        int z1 = Math.min((int) process.firstPos.z, (int) process.secondPos.z);
        int x2 = Math.max((int) process.firstPos.x, (int) process.secondPos.x);
        int z2 = Math.max((int) process.firstPos.z, (int) process.secondPos.z);

        if (containsClaim(x1, x2, z1, z2))
            process.player.sendMessage(Utils.prefix + TextFormat.RED + "Sorry but this land has already been claimed.");
        else {
            Faction faction = new Faction(GalacyHCF.mysql, process.player.factionId);
            if (faction.id == 0) {
                process.player.sendMessage(Utils.prefix + TextFormat.RED + "Claim failed because you're not in a faction.");
                return false;
            }
            String currentTime = Utils.dateFormat.format(new Date());
            try {
                mysql.exec(SQLStatements.createFactionClaim.
                        replace("$created_at", currentTime).
                        replace("$updated_at", currentTime).
                        replace("$type", String.valueOf(Claim.factionClaim)).
                        replace("$faction_id", String.valueOf(faction.id)).
                        replace("$faction_name", faction.name).
                        replace("$x1", String.valueOf(x1)).
                        replace("$x2", String.valueOf(x2)).
                        replace("$z1", String.valueOf(z1)).
                        replace("$z2", String.valueOf(z2))
                );
            } catch (SQLException e) {
                GalacyHCF.instance.getLogger().info(TextFormat.RED + "[MySQL]: Had issues creating new claim: " + e);
            }
            process.player.getServer().getScheduler().scheduleTask(GalacyHCF.instance, this::loadData, true);
            return true;
        }

        return false;
    }

    public void deleteFactionClaims(int factionId) {
        try {
            mysql.exec(SQLStatements.deleteClaimsByFactionId.replace("$faction_id", String.valueOf(factionId)));
            GalacyHCF.instance.getServer().getScheduler().scheduleTask(GalacyHCF.instance, this::loadData, true);
        } catch (SQLException e) {
            GalacyHCF.instance.getLogger().info(TextFormat.RED + "[MySQL]: Had issues deleting all faction claims: " + e);
        }
    }
}

