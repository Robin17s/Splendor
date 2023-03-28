package gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ApplicationStart extends Application {
    @Override
    public void start(Stage primary) {
        StartScreen root = new StartScreen();
        Scene scene = new Scene(root, 1280, 720);

        primary.setScene(scene);
        primary.setTitle("Splendor - G53 Half-baked Edition");
        primary.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}