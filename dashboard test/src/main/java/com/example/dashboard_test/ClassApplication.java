package com.example.dashboard_test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.stage.Stage;
import javafx.scene.SceneAntialiasing;
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
import java.io.IOException;

public class ClassApplication extends Application {
    private HelloController classController;

    @Override
    public void start(Stage stage) throws IOException {
        // Load FXML and controller
        FXMLLoader fxmlLoader = new FXMLLoader(ClassApplication.class.getResource("Class.fxml"));
        Pane fxmlContent = fxmlLoader.load();
        classController = fxmlLoader.getController();

        // Wrap content in a scroll pane
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(fxmlContent);
        scrollPane.setPannable(true);
        scrollPane.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);

        // Create and set scene
        Scene scene = new Scene(new StackPane(scrollPane), 1259, 741, true, SceneAntialiasing.BALANCED);
        scene.getStylesheets().add(ClassApplication.class.getResource("application.css").toExternalForm());

        // Set the scene on the stage
        stage.setTitle("PUP SRC ROOM SCHEDULER");
        stage.setScene(scene);
        stage.show();

        // Start the scheduler when the application starts
        classController.startScheduler();

        // Stop the scheduler and perform cleanup when the application exits
        stage.setOnCloseRequest(event -> {
            classController.stopSchedulers();
            System.out.println("Application closed. Schedulers stopped.");
        });
    }

    public static void main(String[] args) {
        launch();
    }
}
