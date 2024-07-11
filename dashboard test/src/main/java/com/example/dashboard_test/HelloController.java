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
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.scene.paint.Color;
import javafx.geometry.HPos;
import javafx.geometry.VPos;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
public class HelloController {

    @FXML
    private HBox contentHBox;


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

    @FXML
    private void handleSidebarButton2() {
        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Class.fxml"));
            Parent parent = fxmlLoader.load();

            // Wrap the parent in a ScrollPane
            ScrollPane scrollPane = new ScrollPane(parent);
            scrollPane.setPannable(true);
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

            // Create a new scene with specified dimensions and add the scrollPane to it
            Scene scene = new Scene(scrollPane, 1280, 720); // Set desired width and height here
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Class Window"); // Optionally set a title for the new window
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSidebarButton3() {
        // Clear existing content
        contentHBox.getChildren().clear();

        // Create new content for Class Schedule button
        VBox vbox = new VBox();

        HBox hbox1 = new HBox();
        hbox1.setPrefHeight(37);
        hbox1.setPrefWidth(1078);
        Label label = new Label("ECO, KIMBERLY A.");
        label.setTextFill(javafx.scene.paint.Color.RED);
        label.setPrefHeight(37);
        label.setPrefWidth(1092);
        Button addScheduleButton = new Button("Add Schedule");
        addScheduleButton.setPrefHeight(37);
        addScheduleButton.setPrefWidth(333);
        addScheduleButton.setOnAction(event -> showBookingDialog());
        hbox1.getChildren().addAll(label, addScheduleButton);

        TableView table1 = createTableView();
        TableView table2 = createTableView();
        TableView table3 = createTableView();

        // Add new content to vbox
        vbox.getChildren().addAll(hbox1, table1, table2, table3);

        // Add vbox to contentHBox
        contentHBox.getChildren().add(vbox);
    }

    private TableView createTableView() {
        TableView table = new TableView();
        table.setPrefHeight(200);
        table.setPrefWidth(1078);

        TableColumn courseColumn = new TableColumn("Bachelor of Science in Industrial Engineering");
        courseColumn.setPrefWidth(1077);

        TableColumn col1 = new TableColumn("#");
        col1.setPrefWidth(75);
        TableColumn col2 = new TableColumn("Section Code");
        col2.setPrefWidth(165);
        TableColumn col3 = new TableColumn("Subject Code");
        col3.setPrefWidth(188);
        TableColumn col4 = new TableColumn("Subject");
        col4.setPrefWidth(164);
        TableColumn col5 = new TableColumn("Date");
        col5.setPrefWidth(132);
        TableColumn col6 = new TableColumn("Time");
        col6.setPrefWidth(353);

        courseColumn.getColumns().addAll(col1, col2, col3, col4, col5, col6);
        table.getColumns().add(courseColumn);

        return table;
    }

}