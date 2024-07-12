package com.example.dashboard_test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public List<String> getSubjectsForSection(int userId, String section) {
        List<String> subjects = new ArrayList<>();
        String query = "SELECT DISTINCT s.subject " +
                "FROM schedules s " +
                "JOIN users u ON s.userId = u.userId " +
                "WHERE u.userId = ? AND s.course_section = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setString(2, section);
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

    public Map<String, Integer> getAllProfessorsWithIds() {
        Map<String, Integer> professors = new HashMap<>();
        String query = "SELECT userId, userName FROM users WHERE userRole = 'Professor'";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                professors.put(resultSet.getString("userName"), resultSet.getInt("userId"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return professors;
    }

    public int getProfessorUserId(String professorName) {
        String query = "SELECT userId FROM users WHERE userName = ? AND userRole = 'Professor'";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, professorName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("userId");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if not found or error
    }

    public List<String> getSectionsForProfessor(int userId, String professor) {
        List<String> sections = new ArrayList<>();
        String query = "SELECT DISTINCT s.course_section " +
                "FROM schedules s " +
                "JOIN users u ON s.userId = u.userId " +
                "WHERE u.userId = ? AND u.userName = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setString(2, professor);
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

    public String getUserNameById(int userId) {
        String query = "SELECT userName FROM users WHERE userId = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("userName");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "User"; // Default name if user ID does not match or an error occurs
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

    public void updateRoomStatus(LocalDate currentDate) {
        // SQL query to update room status based on the specified conditions
        String updateQuery = "UPDATE rooms r " +
                "LEFT JOIN occupancy o ON r.room_id = o.room_id AND o.date >= CURDATE() " +
                "SET r.status = CASE " +
                "WHEN o.room_id IS NULL THEN 'available' " +
                "WHEN o.date = CURDATE() THEN 'occupied' " +
                "WHEN o.date > CURDATE() THEN 'scheduled' " +
                "ELSE r.status " + // Keep current status if none of the above conditions are met
                "END";

        try (PreparedStatement statement = connection.prepareStatement(updateQuery)) {
            int rowsAffected = statement.executeUpdate();
            System.out.println("Rooms status updated successfully. Rows affected: " + rowsAffected);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle update error
        }
    }

    public Map<Integer, String> getAllRoomStatuses() {
        Map<Integer, String> roomStatuses = new HashMap<>();
        String query = "SELECT room_number, status FROM rooms";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                roomStatuses.put(resultSet.getInt("room_number"), resultSet.getString("status"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roomStatuses;
    }

    public boolean insertOccupancy(int scheduleId, int roomId, int userId, String section, String subject, String startTime, String endTime, LocalDate date) {
        // Check for time overlap
        String overlapCheckQuery = "SELECT COUNT(*) FROM occupancy WHERE room_id = ? AND date = ? AND ((start_time < ? AND end_time > ?) OR (start_time < ? AND end_time > ?))";
        try (PreparedStatement overlapCheckStmt = connection.prepareStatement(overlapCheckQuery)) {
            overlapCheckStmt.setInt(1, roomId);
            overlapCheckStmt.setString(2, date.toString());
            overlapCheckStmt.setString(3, endTime); // New booking's end time is after existing start time
            overlapCheckStmt.setString(4, startTime); // Existing booking's end time is after new start time
            overlapCheckStmt.setString(5, startTime); // New booking's start time is before existing end time
            overlapCheckStmt.setString(6, endTime); // Existing booking's start time is before new end time
            ResultSet overlapResult = overlapCheckStmt.executeQuery();
            if (overlapResult.next() && overlapResult.getInt(1) > 0) {
                // Overlap found, reject the booking
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        // If no overlap, proceed with insertion
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
            return false;
        }
    }

    public List<Occupancy> getOccupancyData() {
        List<Occupancy> occupancies = new ArrayList<>();
        String query = "SELECT o.occupancy_id, r.room_number, u.userName, o.course_section, o.subject, " +
                "CONCAT(o.start_time, ' - ', o.end_time) AS time, ro.status " +
                "FROM occupancy o " +
                "JOIN rooms r ON o.room_id = r.room_id " +
                "JOIN users u ON o.userId = u.userId " +
                "JOIN rooms ro ON o.room_id = ro.room_id " +
                "ORDER BY o.date"; // Assuming 'date' is the column name in your database

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

    public void deletePastOccupancies() {
        String deleteQuery = "DELETE FROM occupancy WHERE date < ?";
        LocalDate currentDate = LocalDate.now();

        try (PreparedStatement statement = connection.prepareStatement(deleteQuery)) {
            statement.setString(1, currentDate.toString());
            int rowsAffected = statement.executeUpdate();
            System.out.println(rowsAffected + " records deleted.");
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle delete error
        }
    }

    public void deleteOccupancy(int occupancyId) {
        String query = "DELETE FROM occupancy WHERE occupancy_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, occupancyId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle delete error
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
