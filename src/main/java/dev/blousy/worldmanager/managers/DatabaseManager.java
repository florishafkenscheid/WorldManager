package dev.blousy.worldmanager.managers;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.sql.*;
import java.util.UUID;


public class DatabaseManager {
    private static String URL = "";

    public static void initialize() {
        URL = "jdbc:sqlite:plugins/Lobby/players.db";
        try (Connection connection = DriverManager.getConnection(URL)) {
            // Create the coordinates table if it does not exist
            String sql = "CREATE TABLE IF NOT EXISTS coordinates ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "UUID TEXT NOT NULL,"
                    + "world TEXT NOT NULL,"
                    + "x INTEGER NOT NULL,"
                    + "y INTEGER NOT NULL,"
                    + "z INTEGER NOT NULL"
                    + ");";
            connection.prepareStatement(sql).execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void saveCoordinates(UUID playerUUID, String world, int x, int y, int z) {
        try (Connection connection = DriverManager.getConnection(URL)) {
            // Check if the player's coordinates already exist in the database
            String checkSql = "SELECT COUNT(*) FROM coordinates WHERE UUID = ? AND world = ?;";
            PreparedStatement checkStatement = connection.prepareStatement(checkSql);
            checkStatement.setString(1, playerUUID.toString());
            checkStatement.setString(2, world);
            ResultSet checkResultSet = checkStatement.executeQuery();
            int count = 0;
            if (checkResultSet.next()) {
                count = checkResultSet.getInt(1);
            }

            // If the coordinates don't exist, insert a new row
            if (count == 0) {
                String insertSql = "INSERT INTO coordinates (UUID, world, x, y, z) VALUES (?, ?, ?, ?, ?);";
                PreparedStatement insertStatement = connection.prepareStatement(insertSql);
                insertStatement.setString(1, playerUUID.toString());
                insertStatement.setString(2, world);
                insertStatement.setInt(3, x);
                insertStatement.setInt(4, y);
                insertStatement.setInt(5, z);
                insertStatement.executeUpdate();
            }
            // Otherwise, update the existing row
            else {
                String updateSql = "UPDATE coordinates SET x = ?, y = ?, z = ? WHERE UUID = ? AND world = ?;";
                PreparedStatement statement = connection.prepareStatement(updateSql);
                statement.setInt(1, x);
                statement.setInt(2, y);
                statement.setInt(3, z);
                statement.setString(4, playerUUID.toString());
                statement.setString(5, world);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Location getCoordinates(UUID playerUUID, String world) {
        Location location = null;
        try (Connection connection = DriverManager.getConnection(URL)) {
            String sql = "SELECT * FROM coordinates WHERE UUID = ? AND world = ? ORDER BY id DESC LIMIT 1;";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, playerUUID.toString());
            statement.setString(2, world);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                double x = resultSet.getDouble("x");
                double y = resultSet.getDouble("y");
                double z = resultSet.getDouble("z");
                location = new Location(Bukkit.getWorld(world), x, y, z);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return location;
    }

    public static void printCoordinates(UUID playerUuid) {
        try (Connection connection = DriverManager.getConnection(URL)) {
            String sql = "SELECT * FROM coordinates WHERE UUID = ? ORDER BY id DESC LIMIT 1;";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, playerUuid.toString());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                double x = resultSet.getDouble("x");
                double y = resultSet.getDouble("y");
                double z = resultSet.getDouble("z");
                String world = resultSet.getString("world");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}