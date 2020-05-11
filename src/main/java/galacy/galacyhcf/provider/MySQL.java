package galacy.galacyhcf.provider;

import cn.nukkit.utils.TextFormat;
import galacy.galacyhcf.GalacyHCF;

import java.sql.*;

public class MySQL {

    public Connection connection;

    public MySQL(String username, String password, String database) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+database, username, password);
            if (connection != null) {
                GalacyHCF.instance.getLogger().info(TextFormat.AQUA + "[MySQL]: Connection to the established database.");
            }
        } catch (ClassNotFoundException | SQLException e) {
            GalacyHCF.instance.getLogger().info(TextFormat.RED + "[MySQL]: Could not connect to the database: " + e);
        }
    }

    public void close() {
        try {
            connection.close();
            GalacyHCF.instance.getLogger().info(TextFormat.AQUA + "[MySQL]: The connection to the database was terminated.");
        } catch (SQLException e) {
            GalacyHCF.instance.getLogger().info(TextFormat.RED + "[MySQL]: Could not close the database connection: " + e);
        }
    }

    public ResultSet exec(String query) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeQuery(query);
    }
}
