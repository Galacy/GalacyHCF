package galacy.galacyhcf.providers;

import cn.nukkit.utils.TextFormat;
import galacy.galacyhcf.GalacyHCF;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQL {

    public Connection connection;
    private final String host;
    private final String username;
    private final String password;
    private final String database;

    public MySQL(String host, String username, String password, String database) {
        this.host = host;
        this.username = username;
        this.password = password;
        this.database = database;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + host + "/" + database + "?autoReconnect=true&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", username, password);
            if (connection != null) {
                GalacyHCF.instance.getLogger().info(TextFormat.AQUA + "[MySQL]: Connection to the established database.");

                exec(SQLStatements.createPlayersTable);
                exec(SQLStatements.createFactionsTable);
                exec(SQLStatements.createClaimsTable);
            }
        } catch (ClassNotFoundException | SQLException e) {
            GalacyHCF.instance.getLogger().critical(TextFormat.RED + "[MySQL]: Could not connect to the database: " + e);
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

    public void reconnect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + host + "/" + database + "?autoReconnect=true&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", username, password);
        } catch (ClassNotFoundException | SQLException e) {
            GalacyHCF.instance.getLogger().info(TextFormat.RED + "[MySQL]: Could not reconnect to the database: " + e);
        }
    }

    public void exec(String query) throws SQLException {
        if (connection.isClosed()) reconnect();
        connection.createStatement().execute(query);
    }

    public ResultSet query(String query) throws SQLException {
        if (connection.isClosed()) reconnect();
        return connection.createStatement().executeQuery(query);
    }
}
