package com.example.dashboard_test;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class BookingDialogController {

    @FXML
    private ComboBox<String> sectionComboBox;

    @FXML
    private ComboBox<String> subjectComboBox;

    @FXML
    private TextField startTimePicker;

    @FXML
    private TextField endTimePicker;

    @FXML
    private DatePicker datePicker;

    @FXML
    public void initialize() {
        // Initialize combo boxes
        sectionComboBox.getItems().addAll("Section 1", "Section 2", "Section 3");
        subjectComboBox.getItems().addAll("Subject 1", "Subject 2", "Subject 3");

        setupTimePicker(startTimePicker);
        setupTimePicker(endTimePicker);
    }

    @FXML
    private void handleSave() {
        // Implement saving logic here
        String section = sectionComboBox.getValue();
        String subject = subjectComboBox.getValue();
        String startTime = startTimePicker.getText();
        String endTime = endTimePicker.getText();
        String date = datePicker.getValue().toString();

        // Display values for debugging purposes
        System.out.println("Section: " + section);
        System.out.println("Subject: " + subject);
        System.out.println("Start Time: " + startTime);
        System.out.println("End Time: " + endTime);
        System.out.println("Date: " + date);

        // Close the dialog
        Stage stage = (Stage) sectionComboBox.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleExit() {
        // Close the dialog
        Stage stage = (Stage) sectionComboBox.getScene().getWindow();
        stage.close();
    }

    private void setupTimePicker(TextField timePicker) {
        timePicker.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // Focus lost
                String text = timePicker.getText();
                try {
                    LocalTime time = LocalTime.parse(text, DateTimeFormatter.ofPattern("HH:mm"));
                    timePicker.setText(time.format(DateTimeFormatter.ofPattern("HH:mm")));
                } catch (DateTimeParseException e) {
                    timePicker.setText("");
                    Alert alert = new Alert(AlertType.ERROR, "Invalid time format. Please use HH:mm.");
                    alert.showAndWait();
                }
            }
        });
    }
}
