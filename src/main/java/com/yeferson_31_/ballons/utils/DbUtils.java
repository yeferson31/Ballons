package com.yeferson_31_.ballons.utils;
import com.yeferson_31_.ballons.config.LangConfig;

import java.sql.*;

public class DbUtils {
    private static String host = LangConfig.Msg.DatabaseConfigHost.toString();
    private static Integer port = Integer.parseInt(LangConfig.Msg.DatabaseConfigPort.toString());
    private static String database = LangConfig.Msg.DatabaseConfigName.toString();
    private static String username = LangConfig.Msg.DatabaseConfigUsername.toString();
    private static String password = LangConfig.Msg.DatabaseConfigPassword.toString();
    private static Connection connection;

    public static Connection getConnection() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
        } catch (SQLException e) {
            // e.printStackTrace();
            System.out.println("An error ocurred while connecting to the database, please check database configuration in config.yml, exception:\n" + e.getMessage());
        }
        return connection;
    }

    public static Boolean setupDatabaseTables() {
        Boolean result = false;
        try {
            String sql = "CREATE TABLE IF NOT EXISTS player_balloons (player_uuid varchar(255) NOT NULL, balloon_type varchar(255) NOT NULL)";
            PreparedStatement checkStatement = getConnection().prepareStatement(sql);
            int st = checkStatement.executeUpdate();

            result = st == 1;
            close();
            //System.out.println("Database created successfully.");
        } catch (SQLException e) {
            // e.printStackTrace();
            System.out.println("An error ocurred while creating the database, please check database configuration in config.yml, exception:\n" + e.getMessage());
            close();
        }
        return result;
    }
    public static Boolean setupDatabase() {
        Boolean result = false;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port , username, password);
            String sql = "CREATE DATABASE IF NOT EXISTS " + (!database.equals("") ? database : "balloons");
            PreparedStatement checkStatement = connection.prepareStatement(sql);
            int st = checkStatement.executeUpdate();
            close();
            if (st == 1) {
                result = setupDatabaseTables();
            } else {
                result = true;
            }
            //System.out.println("Database created successfully.");
        } catch (SQLException e) {
            // e.printStackTrace();
            System.out.println("An error ocurred while creating the database, please check database configuration in config.yml, exception:\n" + e.getMessage());
            close();
        }
        return result;
    }

    public static void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            // e.printStackTrace();
        }
    }

    // method to set the balloon type for a player
    public static void setPlayerBalloon(String playerUUID, String balloonType) {
        getConnection();
        try {
            PreparedStatement checkStatement = connection.prepareStatement("SELECT * FROM player_balloons WHERE player_uuid = ?");
            checkStatement.setString(1, playerUUID);
            ResultSet resultSet = checkStatement.executeQuery();

            if (resultSet.next()) {
                // Player exists, update balloon type
                PreparedStatement updateStatement = connection.prepareStatement("UPDATE player_balloons SET balloon_type = ? WHERE player_uuid = ?");
                updateStatement.setString(1, balloonType);
                updateStatement.setString(2, playerUUID);
                updateStatement.executeUpdate();
            } else {
                // Player doesn't exist, create new entry
                PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO player_balloons (player_uuid, balloon_type) VALUES (?, ?)");
                insertStatement.setString(1, playerUUID);
                insertStatement.setString(2, balloonType);
                insertStatement.executeUpdate();
            }
            close();
        } catch (SQLException e) {
            // e.printStackTrace();
            close();
        }
    }
    public static String deletePlayerBalloon(String playerUUID) {
        getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM player_balloons WHERE player_uuid = ?");
            statement.setString(1, playerUUID);
            statement.executeUpdate();
            close();
        } catch (SQLException e) {
            // e.printStackTrace();
            close();
        }
        return null;
    }

    // method to get the balloon type for a player
    public static String getPlayerBalloon(String playerUUID) {
        getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT balloon_type FROM player_balloons WHERE player_uuid = ?");
            statement.setString(1, playerUUID);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return result.getString("balloon_type");
            }
            close();
        } catch (SQLException e) {
            // e.printStackTrace();
            close();
        }
        return null;
    }
}
