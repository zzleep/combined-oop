package com.example.dashboard_test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/oop_main";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    private Connection connection;

    public DatabaseHandler() {
        try {
            connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle connection error
        }
    }

    public List<String> getAllSections() {
        List<String> sections = new ArrayList<>();
        String query = "SELECT DISTINCT course_section FROM schedules";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                sections.add(resultSet.getString("course_section"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle query error
        }
        return sections;
    }

    public List<String> getAllSubjects() {
        List<String> subjects = new ArrayList<>();
        String query = "SELECT DISTINCT subject FROM schedules";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                subjects.add(resultSet.getString("subject"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle query error
        }
        return subjects;
    }

    public int getScheduleId(String section, String subject) {
        String query = "SELECT schedule_id FROM schedules WHERE course_section = ? AND subject = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, section);
            statement.setString(2, subject);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("schedule_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle query error
        }
        return -1; // Return -1 if not found or error
    }

    public int getUserId(int scheduleId) {
        String query = "SELECT userId FROM schedules WHERE schedule_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, scheduleId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("userId");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle query error
        }
        return -1; // Return -1 if not found or error
    }

    public int getRoomId(int roomNumber) {
        String query = "SELECT room_id FROM rooms WHERE room_number = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, roomNumber);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("room_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle query error
        }
        return -1; // Return -1 if not found or error
    }

    public boolean insertOccupancy(int scheduleId, int roomId, int userId, String section, String subject,
                                   String startTime, String endTime, LocalDate date) {
        String insertQuery = "INSERT INTO occupancy (schedule_id, room_id, userId, course_section, subject, start_time, end_time, date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
            statement.setInt(1, scheduleId);
            statement.setInt(2, roomId);
            statement.setInt(3, userId);
            statement.setString(4, section);
            statement.setString(5, subject);
            statement.setString(6, startTime);
            statement.setString(7, endTime);
            statement.setString(8, date.toString());

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle insertion error
            return false;
        }
    }

    // Close connection method
    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
