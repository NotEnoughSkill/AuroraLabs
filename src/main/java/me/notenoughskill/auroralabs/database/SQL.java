package me.notenoughskill.auroralabs.database;

import me.notenoughskill.auroralabs.AuroraLabsPlugin;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.*;

public class SQL {
    public String host;

    public String database;

    public String username;

    public String password;

    public int port;

    public boolean useSSL;

    private String table = "auroralabs";

    private AuroraLabsPlugin plugin;

    public SQL(AuroraLabsPlugin plugin) {
        this.plugin = plugin;
        FileConfiguration config = plugin.getConfig();
        if (config.getBoolean("MYSQL.enabled")) {
            System.out.println("[Aurora Labs] Trying to connect to the database.");
            try {
                this.host = config.getString("MYSQL.hostname");
                this.database = config.getString("MYSQL.database");
                this.username = config.getString("MYSQL.username");
                this.password = config.getString("MYSQL.password");
                this.port = config.getInt("MYSQL.port");
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = getNewConnection();
                String command = "CREATE TABLE IF NOT EXISTS auroralabs (UUID Text, Vials Text)";
                PreparedStatement statement = connection.prepareStatement(command);
                statement.execute();
                System.out.println("[Aurora Labs] MySQL Connected!");
                statement.close();
                connection.close();
            } catch (Exception e) {
                System.out.println("[Aurora Labs] MySQL Error!");
                e.printStackTrace();
            }
        }
    }

    public void setTable(String table) {
        this.table = table;
    }

    public Connection getNewConnection() throws SQLException {
        FileConfiguration config = this.plugin.getConfig();
        if (config.getBoolean("MYSQL.enabled")) {
            return DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database + "?verifyServerCertificate=false&useSSL=" + this.useSSL, this.username, this.password);
        }
        return DriverManager.getConnection("jdbc:sqlite:plugins/AuroraLabs/database.db");
    }

    public void insert(Connection connection, String uuid, String value) {
        try {
            String command = "INSERT INTO " + this.table + " (UUID, Vials) VALUES ('" + uuid + "','" + value + "');";
            PreparedStatement statement = connection.prepareStatement(command);
            statement.execute();
            statement.close();
        } catch (SQLException exception) {
            System.out.println("[Aurora Labs] Error trying to insert to the database!");
            exception.printStackTrace();
        }
    }

    public void update(Connection connection, String uuid, String value) {
        try {
            String command = "UPDATE " + this.table + " SET Vials='" + value + "' WHERE UUID='" + uuid + "'";
            PreparedStatement statement = connection.prepareStatement(command);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException exception) {
            System.out.println("[Aurora Labs] Error trying to update to the database!");
            exception.printStackTrace();
        }
    }
}
