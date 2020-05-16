package galacy.galacyhcf.models;

import cn.nukkit.Player;
import cn.nukkit.level.Position;
import cn.nukkit.network.SourceInterface;
import cn.nukkit.network.protocol.UpdateBlockPacket;
import cn.nukkit.utils.TextFormat;
import galacy.galacyhcf.GalacyHCF;
import galacy.galacyhcf.managers.ClaimProcess;
import galacy.galacyhcf.providers.SQLStatements;
import galacy.galacyhcf.scoreboardapi.scoreboard.SimpleScoreboard;
import galacy.galacyhcf.utils.Utils;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GPlayer extends Player {

    // Currently USELESS so I commented it: public int rowId;
    public Date createdAt;
    public Date updatedAt;
    public String dbUsername;
    public String xuid;
    public int rank;
    public int balance;
    public int factionId;

    // Local cached data
    public boolean homeTeleport = false;
    public boolean stuckTeleport = false;
    public int teleportTime;
    public Position teleportPosition;

    public boolean moved = false;
    public int fightTime = 0;

    public int invitedTo = 0;
    public SimpleScoreboard sb;
    public Chat chatType = Chat.Public;
    public ClaimProcess claimProcess;
    public Claim claim;

    public void loadData() {
        try {
            ResultSet result = GalacyHCF.mysql.query(SQLStatements.playerById.replace("$xuid", getLoginChainData().getXUID()));
            if (result.next()) {
                // Currently USELESS so I commented it: rowId = result.getInt("id");
                createdAt = result.getDate("created_at");
                updatedAt = result.getDate("updated_at");
                dbUsername = result.getString("username");
                xuid = result.getString("xuid");
                rank = result.getInt("rank");
                balance = result.getInt("balance");
                factionId = result.getInt("faction_id");

                if (!getName().equals(dbUsername)) {
                    try {
                        String currentTime = Utils.dateFormat.format(new java.util.Date());

                        GalacyHCF.mysql.exec(SQLStatements.updatePlayerUsernameById.
                                replace("$updated_at", currentTime).
                                replace("$xuid", getLoginChainData().getXUID()).
                                replace("$username", getName()));
                        GalacyHCF.instance.getLogger().info(dbUsername + " has changed his name to " + getName());
                        dbUsername = getName();
                    } catch (SQLException e) {
                        GalacyHCF.instance.getLogger().info(TextFormat.RED + "[MySQL]: Had issues changing player username: " + e);
                    }
                }
            } else {
                GalacyHCF.instance.getLogger().info(TextFormat.YELLOW + "[MySQL]: Couldn't find the player in the database, creating a new one.");
                try {
                    String currentTime = Utils.dateFormat.format(new java.util.Date());
                    GalacyHCF.mysql.exec(SQLStatements.createPlayer.
                            replace("$created_at", currentTime).
                            replace("$updated_at", currentTime).
                            replace("$username", username).
                            replace("$xuid", getLoginChainData().getXUID()).
                            replace("$rank", "0").
                            replace("$balance", "0").
                            replace("$faction_id", "0"));

                    loadData();
                } catch (SQLException e) {
                    getServer().getLogger().info(TextFormat.RED + "[MySQL]: Had issues creating a new player: " + e);
                }
            }
            result.close();
        } catch (SQLException e) {
            getServer().getLogger().info(TextFormat.RED + "[MySQL]: Had issues finding faction by name: " + e);
        }
    }


    public GPlayer(SourceInterface interfaz, Long clientID, String ip, int port) {
        super(interfaz, clientID, ip, port);
    }

    public void leaveFaction() {
        try {
            GalacyHCF.mysql.exec(SQLStatements.setPlayerFactionById.
                    replace("$faction_id", "0").
                    replace("$xuid", xuid).
                    replace("$updated_at", Utils.dateFormat.format(new java.util.Date())));
            factionId = 0;
        } catch (SQLException e) {
            getServer().getLogger().info(TextFormat.RED + "[MySQL]: Had issues removing player from faction: " + e);
        }
    }

    public void updateBalance(int newBalance) {
        try {
            GalacyHCF.mysql.exec(SQLStatements.updatePlayerBalanceById.
                    replace("$balance", String.valueOf(newBalance)).
                    replace("$xuid", xuid).
                    replace("$updated_at", Utils.dateFormat.format(new java.util.Date())));
            balance = newBalance;
        } catch (SQLException e) {
            getServer().getLogger().info(TextFormat.RED + "[MySQL]: Had issues updating player balance: " + e);
        }
    }

    public void inviteTo(int factionId) {
        invitedTo = factionId;
        getServer().getScheduler().scheduleDelayedTask(GalacyHCF.instance, () -> invitedTo = 0, 20 * 30, true);
    }

    public void joinFaction(int newFactionId) {
        try {
            GalacyHCF.mysql.exec(SQLStatements.setPlayerFactionById.
                    replace("$faction_id", String.valueOf(newFactionId)).
                    replace("$xuid", xuid).
                    replace("$updated_at", Utils.dateFormat.format(new java.util.Date())));
            factionId = newFactionId;
        } catch (SQLException e) {
            getServer().getLogger().info(TextFormat.RED + "[MySQL]: Had issues adding player to faction: " + e);
        }
    }

    public void buildPillar(int x, int y, int z, int firstBlockId, int secondBlockId) {
        GalacyHCF.instance.getServer().getScheduler().scheduleTask(GalacyHCF.instance, () -> {
            int blocks = 0;
            for (int i = y; i < 128; i++) {
                UpdateBlockPacket packet = new UpdateBlockPacket();
                packet.x = x;
                packet.y = i;
                packet.z = z;
                if (blocks == 4) {
                    packet.blockRuntimeId = firstBlockId;
                    blocks = 0;
                } else {
                    packet.blockRuntimeId = secondBlockId;
                }
                dataPacket(packet);

                blocks++;
            }
        }, true);
    }

    public enum Chat {
        Public,
        Faction
    }
}
