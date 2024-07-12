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

    public List<String> getAllSections(int userId) {
        List<String> sections = new ArrayList<>();
        String query = "SELECT DISTINCT s.course_section " +
                "FROM schedules s " +
                "JOIN users u ON s.userId = u.userId " +
                "WHERE u.userId = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                sections.add(resultSet.getString("course_section"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle query error
        }
        return sections;
    }

    public List<String> getAllSubjects(int userId) {
        List<String> subjects = new ArrayList<>();
        String query = "SELECT DISTINCT s.subject " +
                "FROM schedules s " +
                "JOIN users u ON s.userId = u.userId " +
                "WHERE u.userId = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
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

    public void updateRoomStatus(LocalDate date) {
        // SQL query to update rooms status
        String updateQuery = "UPDATE rooms r " +
                "JOIN occupancy o ON r.room_id = o.room_id " +
                "SET r.status = CASE " +
                "WHEN o.date = ? THEN 'occupied' " +
                "ELSE 'scheduled' " +
                "END " +
                "WHERE r.room_id IN (SELECT DISTINCT room_id FROM occupancy WHERE date = ?)";

        try (PreparedStatement statement = connection.prepareStatement(updateQuery)) {
            statement.setString(1, date.toString());
            statement.setString(2, date.toString());

            int rowsAffected = statement.executeUpdate();
            System.out.println("Rooms status updated successfully. Rows affected: " + rowsAffected);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle update error
        }
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

    public List<Occupancy> getOccupancyData() {
        List<Occupancy> occupancies = new ArrayList<>();
        String query = "SELECT o.occupancy_id, r.room_number, u.userName, o.course_section, o.subject, " +
                "CONCAT(o.start_time, ' - ', o.end_time) AS time, " +
                "CASE " +
                "   WHEN DATE(o.date) = CURDATE() THEN ro.status " + // Today's occupancy
                "   WHEN DATE(o.date) > CURDATE() THEN 'Scheduled' " + // Future dates
                "END AS status " +
                "FROM occupancy o " +
                "JOIN rooms r ON o.room_id = r.room_id " +
                "JOIN users u ON o.userId = u.userId " +
                "JOIN rooms ro ON o.room_id = ro.room_id"; // Adjust the joins as necessary

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int occupancyId = resultSet.getInt("occupancy_id");
                String room = resultSet.getString("room_number");
                String professor = resultSet.getString("userName");
                String courseSection = resultSet.getString("course_section");
                String subject = resultSet.getString("subject");
                String time = resultSet.getString("time");
                String status = resultSet.getString("status");

                Occupancy occupancy = new Occupancy(occupancyId, room, professor, courseSection, subject, time, status);
                occupancies.add(occupancy);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle query error
        }
        return occupancies;
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
