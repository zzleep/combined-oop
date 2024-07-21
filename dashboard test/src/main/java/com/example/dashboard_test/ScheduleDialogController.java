package com.example.dashboard_test;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ScheduleDialogController {

    @FXML
    private ComboBox<String> dateComboBox;

    @FXML
    private TextField sectionCodeField;

    @FXML
    private TextField subjectCodeField;

    @FXML
    private TextField subjectNameField;

    @FXML
    private TextField startTimeField;

    @FXML
    private TextField endTimeField;
    @FXML
    private ComboBox<String> professorNameComboBox;

    @FXML
    public void initialize() {
        dateComboBox.getItems().addAll("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");
        getProfessorNames();
    }

    private void getProfessorNames() {
        List<String> professorNames = new ArrayList<>();
        String query = "SELECT userName FROM users WHERE userRole = 'professor'";
        try (Connection connection = DatabaseHandler.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                professorNames.add(resultSet.getString("userName")); // Corrected column name
            }
            professorNameComboBox.getItems().addAll(professorNames); // Populate the ComboBox
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }





    @FXML
    public void insertSchedule() {
        String selectedProfessorName = professorNameComboBox.getValue();
        if (selectedProfessorName != null) {
            Integer professorUserId = getProfessorUserId(selectedProfessorName);
            if (professorUserId != null) {
                String sectionCode = sectionCodeField.getText();
                String subjectCode = subjectCodeField.getText();
                String subjectName = subjectNameField.getText();
                String date = dateComboBox.getValue();
                String startTime = startTimeField.getText();
                String endTime = endTimeField.getText();

                String insertQuery = "INSERT INTO Schedules (userId, course_section, subject_code, subject, date, start_time, end_time) VALUES (?, ?, ?, ?, ?, ?, ?)";

                try (Connection connection = DatabaseHandler.getConnection();
                     PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

                    preparedStatement.setInt(1, professorUserId);
                    preparedStatement.setString(2, sectionCode);
                    preparedStatement.setString(3, subjectCode);
                    preparedStatement.setString(4, subjectName);
                    preparedStatement.setString(5, date);
                    preparedStatement.setString(6, startTime);
                    preparedStatement.setString(7, endTime);

                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Schedule created successfully!");
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Error", "Failed to create schedule.");
                    }
                } catch (SQLException e) {
                    showAlert(Alert.AlertType.ERROR, "Error", "An error occurred: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                showAlert(Alert.AlertType.WARNING, "Warning", "Professor user ID not found.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Warning", "No professor selected.");
        }
    }

    private Integer getProfessorUserId(String professorName) {
        String query = "SELECT userId FROM users WHERE userName = ?";
        professorName = professorName.trim(); // Trim the professorName to remove any leading or trailing spaces
        System.out.println("Executing query: " + query + " with professorName: " + professorName); // Log the query and name
        try (Connection connection = DatabaseHandler.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, professorName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("userId"); // Corrected column name
                } else {
                    System.out.println("No user found with the userName: " + professorName); // Log if no user is found
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage()); // Log any SQL exceptions
            e.printStackTrace();
        }
        return null;
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}