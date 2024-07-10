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
    private void handleSidebarButton1() {
        contentHBox.getChildren().clear();
        // Creating the UI elements from the provided FXML code
        Label profName = new Label("Eco, Kimberly A.");
        profName.setLayoutX(165.0);
        profName.setLayoutY(70.0);
        profName.setPrefHeight(46.0);
        profName.setPrefWidth(1064.0);
        profName.setStyle("-fx-background-color: White;");
        profName.setTextFill(Color.WHITE);

        Text text = new Text("Eco, Kimberly A.");
        text.setFill(Color.rgb(148, 4, 4));
        text.setLayoutX(190.0);
        text.setLayoutY(102.0);
        text.setFont(Font.font("System Bold", 22.0));
        text.setWrappingWidth(174.9367218017578);

        Pane container = new Pane();
        container.setId("container");
        container.setLayoutX(168.0);
        container.setLayoutY(133.0);
        container.setPrefHeight(639.0);
        container.setPrefWidth(1064.0);
        container.setStyle("-fx-background-color: white;");

        Pane roomContainer = new Pane();
        roomContainer.setId("room-container");
        roomContainer.setLayoutX(14.0);
        roomContainer.setLayoutY(14.0);
        roomContainer.setPrefHeight(486.0);
        roomContainer.setPrefWidth(1036.0);
        roomContainer.setStyle("-fx-background-color: white;");

        Pane titleContainer = new Pane();
        titleContainer.setId("title-container");
        titleContainer.setPrefHeight(54.0);
        titleContainer.setPrefWidth(1036.0);
        titleContainer.setStyle("-fx-background-color: maroon;");

        Text classRoomText = new Text("CLASSROOMS");
        classRoomText.setId("classRoomText");
        classRoomText.setFill(Color.WHITE);
        classRoomText.setLayoutX(348.0);
        classRoomText.setLayoutY(35.0);
        classRoomText.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        classRoomText.setWrappingWidth(307.26910400390625);
        classRoomText.setFont(Font.font("System Bold", 23.0));

        titleContainer.getChildren().add(classRoomText);
        roomContainer.getChildren().add(titleContainer);

        // Repeat for the other elements such as GridPane, Buttons, and Text
        // Example for the first GridPane
        GridPane gridPane1 = new GridPane();
        gridPane1.setLayoutX(143.0);
        gridPane1.setLayoutY(100.0);
        gridPane1.setPrefHeight(72.0);
        gridPane1.setPrefWidth(728.0);

        for (int i = 0; i < 10; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setHgrow(Priority.SOMETIMES);
            col.setMinWidth(10.0);
            col.setPrefWidth(100.0);
            gridPane1.getColumnConstraints().add(col);
        }

        RowConstraints row = new RowConstraints();
        row.setMinHeight(10.0);
        row.setPrefHeight(30.0);
        row.setVgrow(Priority.SOMETIMES);
        gridPane1.getRowConstraints().add(row);

        // Adding buttons to the first grid pane
        Button button101 = new Button("101");
        button101.setId("buttons1");
        button101.setMaxWidth(52.0);
        button101.setPrefHeight(43.0);
        GridPane.setHalignment(button101, HPos.CENTER);
        GridPane.setValignment(button101, VPos.CENTER);
        button101.setOnAction(event -> showDialog());
        gridPane1.add(button101, 0, 0);

        // Repeat for the other buttons (example for button 103)
        Button button103 = new Button("103");
        button103.setId("buttons1");
        button103.setMaxWidth(52.0);
        button103.setPrefHeight(43.0);
        GridPane.setHalignment(button103, HPos.CENTER);
        GridPane.setValignment(button103, VPos.CENTER);
        gridPane1.add(button103, 2, 0);

        // Add all the other buttons similarly
        // ...

        roomContainer.getChildren().add(gridPane1);

        // Repeat for other grid panes, texts, and panes
        // ...

        container.getChildren().add(roomContainer);
        contentHBox.getChildren().add(container);

        // Add the container to the content HBox
        contentHBox.getChildren().add(profName);
        contentHBox.getChildren().add(text);
        contentHBox.getChildren().add(container);
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