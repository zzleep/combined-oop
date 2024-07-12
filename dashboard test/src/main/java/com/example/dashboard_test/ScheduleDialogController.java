package com.example.dashboard_test;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

public class ScheduleDialogController {
    @FXML
    private ComboBox<String> dateComboBox;

    @FXML
    public void initialize() {
        dateComboBox.getItems().addAll("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");
    }



}
