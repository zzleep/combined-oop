package com.example.dashboard_test;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.application.Platform;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
public class HelloController {


    @FXML
    private void showDialog() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Room Booking");
        dialog.setHeaderText("Do you want to book a room?");

        ButtonType yesButtonType = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButtonType = new ButtonType("No", ButtonBar.ButtonData.NO);
        dialog.getDialogPane().getButtonTypes().addAll(yesButtonType, noButtonType);

        Button yesButton = (Button) dialog.getDialogPane().lookupButton(yesButtonType);
        yesButton.addEventFilter(ActionEvent.ACTION, event -> {
            dialog.close();
            showBookingDialog();
            event.consume(); // Prevent the dialog from closing
        });

        dialog.getDialogPane().lookupButton(noButtonType).addEventFilter(ActionEvent.ACTION, event -> dialog.close());

        dialog.showAndWait();
    }

    private void showBookingDialog() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("BookingDialog.fxml"));
            Parent parent = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Course and Subject Booking");
            stage.setScene(new Scene(parent));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}