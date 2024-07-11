package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Pane;
import javafx.scene.SceneAntialiasing;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/RoomTab.fxml"));
        Pane fxmlContent = fxmlLoader.load();
        
        ScrollPane scroll = new ScrollPane();
        scroll.setContent(fxmlContent);
        scroll.setPannable(true);
        scroll.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
        
        Scene scene = new Scene(new StackPane(scroll), 1259, 741, true, SceneAntialiasing.BALANCED);
        Image logo = new Image("puplogo.png");
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        
        stage.getIcons().add(logo);
        stage.setTitle("PUP SRC ROOM SCHEDULER");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
