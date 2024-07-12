package com.example.dashboard_test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.application.Platform;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ClassController {

    private Stage helloStage;

    @FXML
    private TableColumn<Occupancy, Void> deleteColumn;

    private Stage bookingStage; // To keep track of BookingDialog stage
    private int currentRoomNumber;
    // A map to store button IDs and corresponding room numbers
    private Map<String, Integer> buttonRoomMap = new HashMap<>();

    @FXML
    private HBox contentHBox;

    @FXML
    private TableView<Occupancy> occupancyTable;

    @FXML
    private TableColumn<Occupancy, Integer> idColumn;

    @FXML
    private TableColumn<Occupancy, String> roomColumn;

    @FXML
    private TableColumn<Occupancy, String> professorColumn;

    @FXML
    private TableColumn<Occupancy, String> courseSectionColumn;

    @FXML
    private TableColumn<Occupancy, String> subjectColumn;

    @FXML
    private TableColumn<Occupancy, String> timeColumn;

    @FXML
    private TableColumn<Occupancy, String> statusColumn;

    @FXML
    private Text userNameText;

    @FXML
    private TextField searchField;

    private ObservableList<Occupancy> originalData = FXCollections.observableArrayList();
    private ObservableList<Occupancy> filteredData = FXCollections.observableArrayList();

    private ScheduledExecutorService roomStatusScheduler;
    private ScheduledExecutorService deletePastRecordsScheduler;

    // Initialize method to populate buttonRoomMap
    @FXML
    public void initialize() {
        buttonRoomMap.put("button101", 101);
        buttonRoomMap.put("button102", 102);
        buttonRoomMap.put("button103", 103);
        buttonRoomMap.put("button104", 104);
        buttonRoomMap.put("button105", 105);
        buttonRoomMap.put("button106", 106);
        buttonRoomMap.put("button107", 107);
        buttonRoomMap.put("button108", 108);
        buttonRoomMap.put("button109", 109);
        buttonRoomMap.put("button110", 110);

        buttonRoomMap.put("button201", 201);
        buttonRoomMap.put("button202", 202);
        buttonRoomMap.put("button203", 203);
        buttonRoomMap.put("button204", 204);
        buttonRoomMap.put("button205", 205);
        buttonRoomMap.put("button206", 206);
        buttonRoomMap.put("button207", 207);
        buttonRoomMap.put("button208", 208);
        buttonRoomMap.put("button209", 209);
        buttonRoomMap.put("button210", 210);

        buttonRoomMap.put("button301", 301);
        buttonRoomMap.put("button302", 302);
        buttonRoomMap.put("button303", 303);
        buttonRoomMap.put("button304", 304);
        buttonRoomMap.put("button305", 305);
        buttonRoomMap.put("button306", 306);
        buttonRoomMap.put("button307", 307);
        buttonRoomMap.put("button308", 308);
        buttonRoomMap.put("button309", 309);
        buttonRoomMap.put("button310", 310);
        buttonRoomMap.put("button311", 311);

        buttonRoomMap.put("button401", 401);
        buttonRoomMap.put("button402", 402);
        buttonRoomMap.put("button403", 403);
        buttonRoomMap.put("button404", 404);
        buttonRoomMap.put("button405", 405);
        buttonRoomMap.put("button406", 406);
        buttonRoomMap.put("button407", 407);
        buttonRoomMap.put("button408", 408);
        buttonRoomMap.put("button409", 409);
        buttonRoomMap.put("button410", 410);

        // Set up the columns in the table
        idColumn.setCellValueFactory(new PropertyValueFactory<>("occupancyId"));
        roomColumn.setCellValueFactory(new PropertyValueFactory<>("room"));
        professorColumn.setCellValueFactory(new PropertyValueFactory<>("professor"));
        courseSectionColumn.setCellValueFactory(new PropertyValueFactory<>("courseSection"));
        subjectColumn.setCellValueFactory(new PropertyValueFactory<>("subject"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        loadOriginalData();
        occupancyTable.setItems(originalData);

        // Add listener to searchField
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                occupancyTable.setItems(originalData);
            } else {
                filterData(newValue);
            }
        });

        // Assuming getCurrentUserName() is a method that retrieves the current user's name
        String currentUserName = getCurrentUserName();
        userNameText.setText(currentUserName);

        deleteColumn.setCellFactory(col -> new ButtonCell(occupancyTable, new DatabaseHandler()));

        // Load data from the database initially
        refreshOccupancyTable();

        // Start the scheduler to update room status every hour
        startRoomStatusScheduler();

        // Start the scheduler to delete past occupancy records daily
        startDeletePastRecordsScheduler();
    }

    // Start the scheduler to update room status every hour
    public void startScheduler() {
        startRoomStatusScheduler();
        startDeletePastRecordsScheduler();
    }

    // Method to refresh occupancy table data
    private void refreshOccupancyTable() {
        DatabaseHandler dbHandler = new DatabaseHandler();
        List<Occupancy> occupancyData = dbHandler.getOccupancyData();
        ObservableList<Occupancy> data = FXCollections.observableArrayList(occupancyData);
        occupancyTable.setItems(data);
        dbHandler.closeConnection();
    }

    private void loadOriginalData() {
        List<Occupancy> occupancies = dbHandler.getOccupancyData(); // Fetch occupancy data
        originalData.clear(); // Clear existing data
        originalData.addAll(occupancies); // Add all fetched data
    }

    @FXML
    private void filterData(String searchText) {
        filteredData.clear();
        for (Occupancy occupancy : originalData) {
            if (occupancy.matchesSearch(searchText.toLowerCase())) {
                filteredData.add(occupancy);
            }
        }
        occupancyTable.setItems(filteredData);
    }

    // Initialize your DatabaseHandler instance somewhere in your class
    private DatabaseHandler dbHandler = new DatabaseHandler();

    // Start the scheduler to update room status every hour
    private void startRoomStatusScheduler() {
        roomStatusScheduler = Executors.newScheduledThreadPool(1);
        roomStatusScheduler.scheduleAtFixedRate(() -> {
            // Update room status in the database
            dbHandler.updateRoomStatus(LocalDate.now()); // Pass current date
            refreshOccupancyTable(); // Refresh occupancy table after update
        }, 0, 1, TimeUnit.HOURS); // Run every hour
    }


    // Start the scheduler to delete past occupancy records daily
    private void startDeletePastRecordsScheduler() {
        deletePastRecordsScheduler = Executors.newScheduledThreadPool(1);
        deletePastRecordsScheduler.scheduleAtFixedRate(() -> {
            // Delete past occupancy records in the database
            DatabaseHandler dbHandler = new DatabaseHandler();
            dbHandler.deletePastOccupancies();
            dbHandler.closeConnection();
        }, 0, 1, TimeUnit.DAYS); // Run every day
    }

    // Stop both schedulers when exiting the application
    public void stopSchedulers() {
        if (roomStatusScheduler != null && !roomStatusScheduler.isShutdown()) {
            roomStatusScheduler.shutdown();
        }
        if (deletePastRecordsScheduler != null && !deletePastRecordsScheduler.isShutdown()) {
            deletePastRecordsScheduler.shutdown();
        }
    }

    @FXML
    private void handleRoomButton(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        String buttonId = clickedButton.getId();
        Integer roomNumber = buttonRoomMap.get(buttonId);

        if (roomNumber != null) {
            // Handle room booking logic with the corresponding room number
            System.out.println("Room number: " + roomNumber);
            // For example, open the booking dialog for the specific room
            showDialog(roomNumber);
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
    public void showDialog(ActionEvent actionEvent) {
        Button sourceButton = (Button) actionEvent.getSource();
        int roomNumber = Integer.parseInt(sourceButton.getId().substring(7)); // Extract room number from button ID
        showDialog(roomNumber);
    }

    private void showDialog(int roomNumber) {
        currentRoomNumber = roomNumber;

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Room Booking");
        dialog.setHeaderText("Do you want to book room " + roomNumber + "?");

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

            // Access the controller of the dialog
            BookingDialogController controller = fxmlLoader.getController();
            controller.setRoomNumber(currentRoomNumber); // Pass room number to dialog controller

            // Set up stage asynchronously
            Platform.runLater(() -> {
                bookingStage = new Stage();
                bookingStage.setTitle("Course and Subject Booking");
                bookingStage.setScene(new Scene(parent));
                bookingStage.initModality(Modality.APPLICATION_MODAL);
                bookingStage.showAndWait();

                // After dialog closes, refresh occupancy table
                refreshOccupancyTable();

                // Update room status immediately after booking
                dbHandler.updateRoomStatus(LocalDate.now());
                refreshOccupancyTable(); // Refresh occupancy table after immediate update
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void filterOccupancyData(KeyEvent event) {
        filterData(searchField.getText());
    }

    @FXML
    private void handleSidebarButton1() {
        // Handle button 1 action
    }

    @FXML
    private void handleSidebarButton3() {
        try {
            // Close the current helloStage
            if (helloStage != null) {
                helloStage.close();
            }

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("class_schedule.fxml"));
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private TableView<Object> createTableView() {
        TableView<Object> table = new TableView<>();
        table.setPrefHeight(200);
        table.setPrefWidth(1078);

        TableColumn<Object, Object> courseColumn = new TableColumn<>("Bachelor of Science in Industrial Engineering");
        courseColumn.setPrefWidth(1077);

        TableColumn<Object, Object> col1 = new TableColumn<>("#");
        col1.setPrefWidth(75);
        TableColumn<Object, Object> col2 = new TableColumn<>("Section Code");
        col2.setPrefWidth(165);
        TableColumn<Object, Object> col3 = new TableColumn<>("Subject Code");
        col3.setPrefWidth(188);
        TableColumn<Object, Object> col4 = new TableColumn<>("Subject");
        col4.setPrefWidth(164);
        TableColumn<Object, Object> col5 = new TableColumn<>("Date");
        col5.setPrefWidth(132);
        TableColumn<Object, Object> col6 = new TableColumn<>("Time");
        col6.setPrefWidth(353);

        courseColumn.getColumns().addAll(col1, col2, col3, col4, col5, col6);
        table.getColumns().add(courseColumn);

        return table;
    }

}
