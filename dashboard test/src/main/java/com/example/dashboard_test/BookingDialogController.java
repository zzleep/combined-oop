package com.example.dashboard_test;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class BookingDialogController {

    @FXML
    private ComboBox<String> professorComboBox;

    @FXML
    private ComboBox<String> sectionComboBox;

    @FXML
    private ComboBox<String> subjectComboBox;

    @FXML
    private ComboBox<String> startTimePicker;

    @FXML
    private ComboBox<String> endTimePicker;

    @FXML
    private DatePicker datePicker;

    private int roomNumber; // Variable to store the room number

    private DatabaseHandler databaseHandler; // Database handler instance

    public BookingDialogController() {
        // Initialize DatabaseHandler
        databaseHandler = new DatabaseHandler();
    }

    @FXML
    public void initialize() {
        int userId = (int) SessionManager.getAttribute("userId"); // Ensure this is correctly fetching the userId

        // Initialize combo boxes
        initializeProfessorComboBox();
        initializeTimePicker(startTimePicker);
        initializeTimePicker(endTimePicker);

        // Add listener to professorComboBox here to ensure it's not null
        professorComboBox.setOnAction(event -> {
            String selectedProfessor = professorComboBox.getSelectionModel().getSelectedItem();
            if (selectedProfessor != null) {
                updateSectionComboBox(selectedProfessor);
            }
        });

        // Add listener to sectionComboBox
        sectionComboBox.setOnAction(event -> {
            String selectedSection = sectionComboBox.getSelectionModel().getSelectedItem();
            if (selectedSection != null) {
                updateSubjectComboBox(selectedSection);
            }
        });
    }

    // Modify this method to handle fetching of professor's userId along with their name
    private void initializeProfessorComboBox() {
        Map<String, Integer> professorsData = databaseHandler.getAllProfessorsWithIds();
        if (professorsData != null) {
            professorComboBox.getItems().addAll(professorsData.keySet());
        }
    }

    private void updateSectionComboBox(String selectedProfessor) {
        sectionComboBox.getItems().clear();
        // Retrieve the userId within the method
        int userId = databaseHandler.getProfessorUserId(selectedProfessor);
        if (userId != -1) {
            List<String> sections = databaseHandler.getSectionsForProfessor(userId, selectedProfessor);
            sectionComboBox.getItems().addAll(sections);
        }
    }

    // Adjusted to use professor's userId
    private void updateSubjectComboBox(String selectedSection) {
        subjectComboBox.getItems().clear();
        int userId = databaseHandler.getProfessorUserId(professorComboBox.getSelectionModel().getSelectedItem());
        List<String> subjects = databaseHandler.getSubjectsForSection(userId, selectedSection);
        subjectComboBox.getItems().addAll(subjects);
    }

    private void initializeTimePicker(ComboBox<String> timePicker) {
        // Clear items to avoid duplicates on re-initialization
        timePicker.getItems().clear();

        // Populate the time options (HH:mm 24-hour format)
        for (int hour = 0; hour < 24; hour++) {
            for (int minute = 0; minute < 60; minute += 15) {
                String time = String.format("%02d:%02d", hour, minute);
                timePicker.getItems().add(time);
            }
        }

        // Debug print to check items added
        System.out.println("TimePicker items: " + timePicker.getItems());

        timePicker.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // Focus lost
                String text = timePicker.getSelectionModel().getSelectedItem(); // Get selected item
                if (text != null) {
                    try {
                        // Validate and format time input
                        LocalTime time = LocalTime.parse(text, DateTimeFormatter.ofPattern("HH:mm"));
                        timePicker.getSelectionModel().select(time.format(DateTimeFormatter.ofPattern("HH:mm")));
                    } catch (DateTimeException e) {
                        // Clear selection on parse error
                        timePicker.getSelectionModel().clearSelection();
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid time format. Please use HH:mm.");
                        alert.showAndWait();
                    }
                }
            }
        });
    }

    @FXML
    private void handleSave() {
        String professor = professorComboBox.getSelectionModel().getSelectedItem();
        String section = sectionComboBox.getSelectionModel().getSelectedItem();
        String subject = subjectComboBox.getSelectionModel().getSelectedItem();
        String startTime = startTimePicker.getSelectionModel().getSelectedItem();
        String endTime = endTimePicker.getSelectionModel().getSelectedItem();
        LocalDate date = datePicker.getValue();

        // Validate input
        if (professor == null || section == null || subject == null || startTime == null || endTime == null || date == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please fill in all fields.");
            alert.showAndWait();
            return;
        }

        // Query schedules table to get schedule_id and userId
        int scheduleId = databaseHandler.getScheduleId(section, subject);
        int userId = databaseHandler.getUserId(scheduleId);

        // Query rooms table to get room_id based on roomNumber
        int roomId = databaseHandler.getRoomId(roomNumber);

        // Insert data into occupancy table
        if (scheduleId != -1 && userId != -1 && roomId != -1) {
            boolean success = databaseHandler.insertOccupancy(scheduleId, roomId, userId, section, subject, startTime, endTime, date);

            if (success) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Booking saved successfully.");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to save booking.");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to retrieve necessary data.");
            alert.showAndWait();
        }

        // Close the dialog
        closeDialog();
    }

    @FXML
    private void handleExit() {
        // Close the dialog without saving
        closeDialog();
    }

    private void closeDialog() {
        Stage stage = (Stage) sectionComboBox.getScene().getWindow();
        stage.close();
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber; // Set the room number received from ClassController
    }

    // Cleanup method
    public void cleanup() {
        databaseHandler.closeConnection();
    }
}
