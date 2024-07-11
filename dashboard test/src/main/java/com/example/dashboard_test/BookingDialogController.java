package com.example.dashboard_test;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class BookingDialogController {

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

    @FXML
    public void initialize() {
        // Initialize combo boxes
        sectionComboBox.getItems().addAll("Section 1", "Section 2", "Section 3");
        subjectComboBox.getItems().addAll("Subject 1", "Subject 2", "Subject 3");

        // Initialize time pickers with 24-hour format
        initializeTimePicker(startTimePicker);
        initializeTimePicker(endTimePicker);
    }

    private void initializeTimePicker(ComboBox<String> timePicker) {
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
                        LocalTime time = LocalTime.parse(text, DateTimeFormatter.ofPattern("HH:mm"));
                        timePicker.getSelectionModel().select(time.format(DateTimeFormatter.ofPattern("HH:mm")));
                    } catch (DateTimeParseException e) {
                        timePicker.getSelectionModel().clearSelection(); // Clear selection on parse error
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid time format. Please use HH:mm.");
                        alert.showAndWait();
                    }
                }
            }
        });
    }

    @FXML
    private void handleSave() {
        String section = sectionComboBox.getSelectionModel().getSelectedItem();
        String subject = subjectComboBox.getSelectionModel().getSelectedItem();
        String startTime = startTimePicker.getSelectionModel().getSelectedItem();
        String endTime = endTimePicker.getSelectionModel().getSelectedItem();
        String date = datePicker.getValue().toString();

        // Validate input
        if (section == null || subject == null || startTime == null || endTime == null || date.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please fill in all fields.");
            alert.showAndWait();
            return;
        }

        // Display values for debugging purposes
        System.out.println("Section: " + section);
        System.out.println("Subject: " + subject);
        System.out.println("Start Time: " + startTime);
        System.out.println("End Time: " + endTime);
        System.out.println("Date: " + date);
        System.out.println("Room Number: " + roomNumber); // Log the room number

        // Save to database - Implement your database saving logic here

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
}
