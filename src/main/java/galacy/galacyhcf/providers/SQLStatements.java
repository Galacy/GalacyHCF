package galacy.galacyhcf.providers;

public class SQLStatements {
    public static final String createFaction = "INSERT INTO factions(created_at, updated_at, name, balance, dtr, leader_id) VALUES ('$created_at', '$updated_at', '$name', $balance, $dtr, $leader_id);";
    public static final String playerById = "SELECT * FROM players WHERE xuid=$xuid;";
    public static final String factionByName = "SELECT * FROM factions WHERE name='$name';";
    public static final String factionById = "SELECT * FROM factions WHERE id=$id;";
    public static final String disbandFactionByName = "DELETE FROM factions WHERE name='$name';";
    public static final String updateFactionBalanceById = "UPDATE factions SET balance=$balance, updated_at='$updated_at' WHERE id=$id;";
    public static final String updatePlayerBalanceById = "UPDATE players SET balance=$balance, updated_at='$updated_at' WHERE xuid=$xuid;";
    public static final String setPlayerFactionById = "UPDATE players SET faction_id=$faction_id, updated_at='$updated_at' WHERE xuid=$xuid;";
    public static final String factionMembersById = "SELECT * FROM players WHERE faction_id=$faction_id;";
    public static final String removeAllMembersById = "UPDATE players SET faction_id=0, updated_at='$updated_at' WHERE faction_id=$faction_id;";
    public static final String createPlayer = "INSERT INTO players(created_at, updated_at, xuid, rank, balance, faction_id) VALUES('$created_at', '$updated_at', $xuid, $rank, $balance, $faction_id);";
    public static final String updatePlayerUsernameById = "UPDATE players SET username='$username', updated_at='$updated_at' WHERE xuid=$xuid;";
    public static final String updateLeaderById = "UPDATE factions SET leader_id=$leader_id, updated_at='$updated_at' WHERE id=$id;";
}
