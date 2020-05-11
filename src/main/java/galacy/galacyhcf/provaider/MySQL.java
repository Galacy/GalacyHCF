package galacy.galacyhcf.provaider;

import cn.nukkit.utils.TextFormat;
import galacy.galacyhcf.GalacyHCF;

import java.sql.*;

public class MySQL extends Credentials {

    private static final String url = "jdbc:mysql://localhost:3306/" + database;

    public static Connection connection;

    public MySQL() {
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, username, password);
            if (connection != null) {
                GalacyHCF.instance.getLogger().info(TextFormat.AQUA + "[MySQL]: Connection to the established database.");
            }
        } catch (ClassNotFoundException | SQLException e) {
            GalacyHCF.instance.getLogger().info(TextFormat.RED + "[MySQL]: Could not connect to the database: " + e);
        }
    }

    public static void close() {
        try {
            connection.close();
            GalacyHCF.instance.getLogger().info(TextFormat.AQUA + "[MySQL]: The connection to the database was terminated.");
        } catch (SQLException e) {
            GalacyHCF.instance.getLogger().info(TextFormat.RED + "[MySQL]: Could not close the database connection: " + e);
        }
    }

    public static ResultSet execute(String query) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeQuery(query);
    }
}
