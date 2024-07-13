package com.example.dashboard_test;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

public class class_scheduleController {

    private DatabaseHandler dbHandler = new DatabaseHandler();
    private Stage helloStage; // Declaration of the helloStage variable

    @FXML
    private void handleSidebarButton2() {
        try {
            // Close the current helloStage
            if (helloStage != null) {
                helloStage.close();
            }

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("class.fxml"));
            Parent parent = fxmlLoader.load();

            // Wrap the parent in a ScrollPane
            ScrollPane scrollPane = new ScrollPane(parent);
            scrollPane.setPannable(true);
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

            // Create a new scene with specified dimensions and add the scrollPane to it
            Scene scene = new Scene(scrollPane, 1280, 720); // Set desired width and height here
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Class Window"); // Optionally set a title for the new window
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void showBookingDialog() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("scheduleDialog.fxml"));
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

    @FXML
    private void handleSidebarButton1() {
        try {
            // Close the current helloStage
            if (helloStage != null) {
                helloStage.close();
            }

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
            Parent parent = fxmlLoader.load();

            // Wrap the parent in a ScrollPane
            ScrollPane scrollPane = new ScrollPane(parent);
            scrollPane.setPannable(true);
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

            // Create a new scene with specified dimensions and add the scrollPane to it
            Scene scene = new Scene(scrollPane, 1280, 720); // Set desired width and height here
            scene.getStylesheets().add(getClass().getResource("dashboard.css").toExternalForm());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Class Window"); // Optionally set a title for the new window
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
