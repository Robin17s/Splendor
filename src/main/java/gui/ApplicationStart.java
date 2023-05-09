package gui;

import domain.DomainController;
import domain.i18n.I18n;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ApplicationStart extends Application {
    private static ApplicationStart instance;
    private final DomainController controller = new DomainController();

    private Stage primary;

    @Override
    public void start(Stage primary) {
        I18n.loadTranslationFile("en_US");

        ApplicationStart.instance = this;
        this.primary = primary;

        setScene(new StartScreen());

        primary.setTitle("Splendor - G53 Edition");
        primary.show();
    }

    public void setScene(Pane pane) {
        Scene scene = new Scene(pane, 1600, 900);
        primary.setScene(scene);
    }

    public static void main(String[] args) { launch(args); }

    public static ApplicationStart getInstance() { return instance; }

    public DomainController getController() { return controller; }
}