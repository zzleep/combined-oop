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

import java.io.IOException;

public class ClassApplication extends Application {
    private ClassController classController;

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
