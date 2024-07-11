package com.example.dashboard_test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import javafx.scene.SceneAntialiasing;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        AnchorPane root = fxmlLoader.load();

        // Pass the initial stage to the HelloController
        HelloController controller = fxmlLoader.getController();
        controller.setHelloStage(stage);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(root);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        Scene scene = new Scene(scrollPane, 1280, 720, true, SceneAntialiasing.BALANCED);
        scene.getStylesheets().add(getClass().getResource("dashboard.css").toExternalForm());
        stage.setTitle("PUP SRC ROOM SCHEDULER");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
