package gui;

import domain.DomainController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class UC2DEMOGUI extends Application {
    private static UC2DEMOGUI instance;
    private final DomainController controller = new DomainController();

    private Stage primary;

    @Override
    public void start(Stage primary) {
        UC2DEMOGUI.instance = this;
        this.primary = primary;

        setScene(new BoardScreen());

        primary.setTitle("UC2 DEMO");
        primary.show();
    }

    public void setScene(Pane pane) {
        Scene scene = new Scene(pane, 1280, 720);
        primary.setScene(scene);
    }

    public static void main(String[] args) { launch(args); }

    public static UC2DEMOGUI getInstance() { return instance; }

    public DomainController getController() { return controller; }
}