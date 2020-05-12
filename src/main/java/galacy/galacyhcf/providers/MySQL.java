package galacy.galacyhcf.providers;

import cn.nukkit.utils.TextFormat;
import galacy.galacyhcf.GalacyHCF;

import java.sql.*;

public class MySQL {

    public Connection connection;

    public MySQL(String username, String password, String database) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + database + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", username, password);
            if (connection != null) {
                GalacyHCF.instance.getLogger().info(TextFormat.AQUA + "[MySQL]: Connection to the established database.");

                exec("CREATE TABLE IF NOT EXISTS `players` (`id` int UNIQUE PRIMARY KEY AUTO_INCREMENT,`created_at` datetime,`updated_at` datetime,`username` varchar(255),`xuid` int,`rank` int,`balance` int,`faction_id` int);");
                exec("CREATE TABLE IF NOT EXISTS `factions` (`id` int UNIQUE PRIMARY KEY AUTO_INCREMENT,`created_at` datetime,`updated_at` datetime,`name` varchar(255),`balance` int,`dtr` int,`home` varchar(255),`leader_id` int);");
                exec("CREATE TABLE IF NOT EXISTS `claims` (`id` int UNIQUE PRIMARY KEY AUTO_INCREMENT,`created_at` datetime,`updated_at` datetime,`type` int,`faction_id` int,`faction_name` varchar(255),`x1` int,`x2` int,`z1` int,`z2` int);");
            }
        } catch (ClassNotFoundException | SQLException e) {
            GalacyHCF.instance.getLogger().info(TextFormat.RED + "[MySQL]: Could not connect to the database: " + e);
        }
    }

    public void close() {
        try {
            if(connection.isClosed()) return; else connection.close();
            GalacyHCF.instance.getLogger().info(TextFormat.AQUA + "[MySQL]: The connection to the database was terminated.");
        } catch (SQLException e) {
            GalacyHCF.instance.getLogger().info(TextFormat.RED + "[MySQL]: Could not close the database connection: " + e);
        }
    }

    public Boolean exec(String query) throws SQLException {
        return connection.createStatement().execute(query);
    }

    public ResultSet query(String query) throws SQLException {
        return connection.createStatement().executeQuery(query);
    }
}
