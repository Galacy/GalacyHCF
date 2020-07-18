package galacy.galacyhcf.providers;

public class SQLStatements {
    public static final String createPlayersTable = "CREATE TABLE IF NOT EXISTS `players` (`id` int UNIQUE PRIMARY KEY AUTO_INCREMENT,`created_at` datetime,`updated_at` datetime,`username` varchar(255),`xuid` varchar(255),`rank` int,`balance` int,`faction_id` int);";
    public static final String createFactionsTable = "CREATE TABLE IF NOT EXISTS `factions` (`id` int UNIQUE PRIMARY KEY AUTO_INCREMENT,`created_at` datetime,`updated_at` datetime,`name` varchar(255),`balance` int,`dtr` double, `max_dtr` double,`home` varchar(255),`leader_id` varchar(255));";
    public static final String createClaimsTable = "CREATE TABLE IF NOT EXISTS `claims` (`id` int UNIQUE PRIMARY KEY AUTO_INCREMENT,`created_at` datetime,`updated_at` datetime,`type` int,`faction_id` int,`faction_name` varchar(255),`x1` int,`x2` int,`z1` int,`z2` int);";

    public static final String createFaction = "INSERT INTO factions(created_at, updated_at, name, balance, dtr, max_dtr, leader_id) VALUES ('$created_at', '$updated_at', '$name', $balance, $dtr, $max_dtr, '$leader_id');";
    public static final String playerById = "SELECT * FROM players WHERE xuid='$xuid';";
    public static final String playerByName = "SELECT * FROM players WHERE username='$username';";
    public static final String playerByFactionId = "SELECT * FROM players WHERE faction_id=$faction_id;";
    public static final String factionByName = "SELECT * FROM factions WHERE name='$name';";
    public static final String factionById = "SELECT * FROM factions WHERE id=$id;";
    public static final String disbandFactionByName = "DELETE FROM factions WHERE name='$name';";
    public static final String updateFactionBalanceById = "UPDATE factions SET balance=$balance, updated_at='$updated_at' WHERE id=$id;";
    public static final String updatePlayerBalanceById = "UPDATE players SET balance=$balance, updated_at='$updated_at' WHERE xuid='$xuid';";
    public static final String setPlayerFactionById = "UPDATE players SET faction_id=$faction_id, updated_at='$updated_at' WHERE xuid='$xuid';";
    public static final String factionMembersById = "SELECT * FROM players WHERE faction_id=$faction_id;";
    public static final String removeAllMembersById = "UPDATE players SET faction_id=0, updated_at='$updated_at' WHERE faction_id=$faction_id;";
    public static final String createPlayer = "INSERT INTO players(created_at, updated_at, xuid, rank, balance, faction_id) VALUES('$created_at', '$updated_at', '$xuid', $rank, $balance, $faction_id);";
    public static final String updatePlayerUsernameById = "UPDATE players SET username='$username', updated_at='$updated_at' WHERE xuid='$xuid';";
    public static final String updateLeaderById = "UPDATE factions SET leader_id='$leader_id', updated_at='$updated_at' WHERE id=$id;";
    public static final String updateHomeById = "UPDATE factions SET home='$home', updated_at='$updated_at' WHERE id=$id;";
    public static final String updateDtrById = "UPDATE factions SET dtr=$dtr, updated_at='$updated_at' WHERE id=$id;";
    public static final String updateMaxDtrById = "UPDATE factions SET max_dtr=$max_dtr, updated_at='$updated_at' WHERE id=$id;";
    public static final String allClaims = "SELECT * FROM claims;";
    public static final String createFactionClaim = "INSERT INTO claims(created_at, updated_at, type, faction_id, faction_name, x1, x2, z1, z2) VALUES ('$created_at', '$updated_at', $type, $faction_id, '$faction_name', $x1, $x2, $z1, $z2);";
    public static final String deleteClaimsByFactionId = "DELETE FROM claims WHERE faction_id=$faction_id;";
    public static final String dtrFactions = "SELECT * FROM factions WHERE dtr < max_dtr;";
    public static final String updatePlayerRank = "UPDATE players SET rank=$rank, updated_at='$updated_at' WHERE xuid='$xuid';";

}
