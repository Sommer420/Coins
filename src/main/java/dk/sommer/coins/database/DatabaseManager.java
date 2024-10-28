package dk.sommer.coins.database;

import com.zaxxer.hikari.HikariDataSource;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.platform.core.annotation.Component;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.UUID;

@Component
public class DatabaseManager {

    private Connection connection;
    private String dbPath;

    public DatabaseManager(String dbPath) {
        this.dbPath = dbPath;
        openConnection();
        createTableIfNotExists();
    }

    public void setDbPath(String dbPath) {
        this.dbPath = dbPath;
    }

    public void openConnection() {
        try {
            File dbFile = new File(dbPath);
            if (!dbFile.exists()) {
                dbFile.getParentFile().mkdirs();
                dbFile.createNewFile();
            }

            if (connection != null && !connection.isClosed()) {
                return;
            }
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
        } catch (SQLException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        if (!isConnectionValid()) {
            openConnection();
        }
        return connection;
    }

    public boolean isConnectionValid() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void createTableIfNotExists() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS player_data (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "uuid TEXT NOT NULL," +
                "coins INTEGER NOT NULL)";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public double getCoins(OfflinePlayer player){
        addPlayerToDb(player.getPlayer());
        try {
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT coins FROM player_data WHERE uuid = ?");
            statement.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                return resultSet.getDouble("coins");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void addCoins(OfflinePlayer player, double amount){
        try {
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE player_data SET coins = coins + ? WHERE uuid = ?");
            statement.setDouble(1, amount);
            statement.setString(2, player.getUniqueId().toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeCoins(OfflinePlayer player, double amount) {
        try {
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE player_data SET coins = coins - ? WHERE uuid = ?");
            statement.setDouble(1, amount);
            statement.setString(2, player.getUniqueId().toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void setCoins(OfflinePlayer player, double amount) {
        try {
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE player_data SET coins = ? WHERE uuid = ?");
            statement.setDouble(1, amount);
            statement.setString(2, player.getUniqueId().toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void addPlayerToDb(Player player){
        try {
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT OR IGNORE INTO player_data (uuid, coins) VALUES (?, ?)");
            statement.setString(1, player.getUniqueId().toString());
            statement.setDouble(2, 0);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
