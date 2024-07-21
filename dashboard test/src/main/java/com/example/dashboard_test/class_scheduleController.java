package com.example.dashboard_test;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

public class class_scheduleController {

    @FXML
    private TableView<Schedule> table;

    @FXML
    private Text userNameText;

    @FXML
    private TableColumn<Schedule, Number> colScheduleId;
    @FXML
    private TableColumn<Schedule, Number> colSubjectCode;
    @FXML
    private TableColumn<Schedule, String> colCourseSection;
    @FXML
    private TableColumn<Schedule, String> colSubject;
    @FXML
    private TableColumn<Schedule, String> colDate;

    @FXML
    private TableColumn<Schedule, String> colTime;

    @FXML
    private TableColumn<Schedule, Number> colScheduleIdIT;
    @FXML
    private TableColumn<Schedule, Number> colSubjectCodeIT;
    @FXML
    private TableColumn<Schedule, String> colCourseSectionIT;
    @FXML
    private TableColumn<Schedule, String> colSubjectIT;
    @FXML
    private TableColumn<Schedule, String> colDateIT;

    @FXML
    private TableColumn<Schedule, String> colTimeIT;

    @FXML
    private TableColumn<Schedule, Number> colScheduleIdECE;
    @FXML
    private TableColumn<Schedule, Number> colSubjectCodeECE;
    @FXML
    private TableColumn<Schedule, String> colCourseSectionECE;
    @FXML
    private TableColumn<Schedule, String> colSubjectECE;
    @FXML
    private TableColumn<Schedule, String> colDateECE;

    @FXML
    private TableColumn<Schedule, String> colTimeECE;
    @FXML
    private TableView<Schedule> tableIE;
    @FXML
    private TableView<Schedule> tableIT;
    @FXML
    private TableView<Schedule> tableECE;


    @FXML
    private Button adsched;

    private String userRole = (String) SessionManager.getAttribute("userRole");
    public void initialize() {
        colScheduleId.setCellValueFactory(new PropertyValueFactory<>("scheduleId"));
        colSubjectCode.setCellValueFactory(new PropertyValueFactory<>("subjectCode"));
        colCourseSection.setCellValueFactory(new PropertyValueFactory<>("courseSection"));
        colSubject.setCellValueFactory(new PropertyValueFactory<>("subject"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("timePeriod"));
        colScheduleIdIT.setCellValueFactory(new PropertyValueFactory<>("scheduleId"));
        colSubjectCodeIT.setCellValueFactory(new PropertyValueFactory<>("subjectCode"));
        colCourseSectionIT.setCellValueFactory(new PropertyValueFactory<>("courseSection"));
        colSubjectIT.setCellValueFactory(new PropertyValueFactory<>("subject"));
        colDateIT.setCellValueFactory(new PropertyValueFactory<>("date"));
        colTimeIT.setCellValueFactory(new PropertyValueFactory<>("timePeriod"));

        colScheduleIdECE.setCellValueFactory(new PropertyValueFactory<>("scheduleId"));
        colSubjectCodeECE.setCellValueFactory(new PropertyValueFactory<>("subjectCode"));
        colCourseSectionECE.setCellValueFactory(new PropertyValueFactory<>("courseSection"));
        colSubjectECE.setCellValueFactory(new PropertyValueFactory<>("subject"));
        colDateECE.setCellValueFactory(new PropertyValueFactory<>("date"));
        colTimeECE.setCellValueFactory(new PropertyValueFactory<>("timePeriod"));



        tableIE.setItems(FXCollections.observableArrayList(dbHandler.getIESchedules()));
        tableIT.setItems(FXCollections.observableArrayList(dbHandler.getITSchedules()));
        tableECE.setItems(FXCollections.observableArrayList(dbHandler.getECESchedules()));

        tableIE.refresh();
        tableIT.refresh();
        tableECE.refresh();

        String currentUserName = getCurrentUserName();
        userNameText.setText(currentUserName);

        if (!"admin".equals(userRole)) {
            adsched.setDisable(true);
        }
    }


    private DatabaseHandler dbHandler = new DatabaseHandler();
    private Stage helloStage; // Declaration of the helloStage variable

    private int findColumnIndexById(TableView<?> table, String id) {
        for (int i = 0; i < table.getColumns().size(); i++) {
            if (id.equals(table.getColumns().get(i).getId())) {
                return i;
            }
        }
        return -1; // Column not found
    }
    @FXML
    private void handleSidebarButton2() {
        try {
            // Close the current helloStage
            if (helloStage != null) {
                helloStage.close();
            }

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Class.fxml"));
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

    private String getCurrentUserName() {
        String userName = "User"; // Default name if user ID does not match
        try {
            // Retrieve the current userId from the session
            Object userIdObj = SessionManager.getAttribute("userId");
            if (userIdObj != null) {
                int currentUserId = Integer.parseInt(userIdObj.toString());

                // Use DatabaseHandler to query the database
                DatabaseHandler dbHandler = new DatabaseHandler();
                userName = dbHandler.getUserNameById(currentUserId);

                dbHandler.closeConnection();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userName;
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
