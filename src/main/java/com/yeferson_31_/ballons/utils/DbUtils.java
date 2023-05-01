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
        try {
            PreparedStatement checkStatement = getConnection().prepareStatement("SELECT * FROM player_balloons WHERE player_uuid = ?");
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
        }
    }
    public static String deletePlayerBalloon(String playerUUID) {
        try {
            PreparedStatement statement = getConnection().prepareStatement("DELETE FROM player_balloons WHERE player_uuid = ?");
            statement.setString(1, playerUUID);
            statement.executeUpdate();
            close();
        } catch (SQLException e) {
            // e.printStackTrace();
        }
        return null;
    }

    // method to get the balloon type for a player
    public static String getPlayerBalloon(String playerUUID) {
        try {
            PreparedStatement statement = getConnection().prepareStatement("SELECT balloon_type FROM player_balloons WHERE player_uuid = ?");
            statement.setString(1, playerUUID);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return result.getString("balloon_type");
            }
            close();
        } catch (SQLException e) {
            // e.printStackTrace();
        }
        return null;
    }
}
