package galacy.galacyhcf.providers;

import cn.nukkit.utils.TextFormat;
import galacy.galacyhcf.GalacyHCF;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQL {

    public Connection connection;

    public MySQL(String username, String password, String database) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + database + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", username, password);
            if (connection != null) {
                GalacyHCF.instance.getLogger().info(TextFormat.AQUA + "[MySQL]: Connection to the established database.");

                exec(SQLStatements.createPlayersTable);
                exec(SQLStatements.createFactionsTable);
                exec(SQLStatements.createClaimsTable);
            }
        } catch (ClassNotFoundException | SQLException e) {
            GalacyHCF.instance.getLogger().info(TextFormat.RED + "[MySQL]: Could not connect to the database: " + e);
        }
    }

    public void close() {
        try {
            if (connection.isClosed()) return;
            else connection.close();
            GalacyHCF.instance.getLogger().info(TextFormat.AQUA + "[MySQL]: The connection to the database was terminated.");
        } catch (SQLException e) {
            GalacyHCF.instance.getLogger().info(TextFormat.RED + "[MySQL]: Could not close the database connection: " + e);
        }
    }

    public void exec(String query) throws SQLException {
        connection.createStatement().execute(query);
    }

    public ResultSet query(String query) throws SQLException {
        return connection.createStatement().executeQuery(query);
    }
}
