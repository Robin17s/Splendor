package gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ApplicationStart extends Application {
    private static ApplicationStart instance;

    private Stage primary;

    @Override
    public void start(Stage primary) {
        ApplicationStart.instance = this;
        this.primary = primary;

        setScene(new StartScreen());

        primary.setTitle("Splendor - G53 Half-baked Edition");
        primary.show();
    }

    public void setScene(Pane pane) {
        Scene scene = new Scene(pane, 1280, 720);
        primary.setScene(scene);
    }

    public static void main(String[] args) { launch(args); }

    public static ApplicationStart getInstance() { return instance; }
}