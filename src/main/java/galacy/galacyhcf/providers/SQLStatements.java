package galacy.galacyhcf.providers;

public class SQLStatements {
    public static final String createFaction = "INSERT INTO factions(created_at, updated_at, name, balance, dtr, leader_id) VALUES ('$created_at', '$updated_at', '$name', $balance, $dtr, $leader_id);";
    public static final String playerById = "SELECT * FROM players WHERE xuid=$xuid;";
    public static final String factionByName = "SELECT * FROM factions WHERE name='$name';";
    public static final String disbandFactionByName = "DELETE FROM factions WHERE name='$name';";
    public static final String setPlayerFactionById = "UPDATE players SET faction_id=$faction_id WHERE xuid='$xuid';";
    public static final String factionMembersByName = "SELECT * FROM players WHERE faction_id=$faction_id;";
}
