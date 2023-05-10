package gui;

import domain.DomainController;
import domain.i18n.I18n;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Entrypoint for the GUI application. Should not be loaded directly, but rather through {@link main.Main#main(String[])}.
 */
public class ApplicationStart extends Application {
    private static ApplicationStart instance;
    private final DomainController controller = new DomainController();

    private Stage primary;

    /**
     * The main entry point for all JavaFX applications. The start method is called after the init method has returned, and after the system is ready for the application to begin running.
     * NOTE: This method is called on the JavaFX Application Thread.
     * @param primary the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     */
    @Override
    public void start(Stage primary) {
        I18n.loadTranslationFile("en_US");

        ApplicationStart.instance = this;
        this.primary = primary;

        setScene(new StartScreen());

        primary.setTitle("Splendor - G53 Edition");
        primary.show();
    }

    /**
     * Replaces the currently rendering screen with the one specified.
     * @param pane The pane to render instead.
     */
    public void setScene(Pane pane) {
        Scene scene = new Scene(pane, 1600, 900);
        primary.setScene(scene);
    }

    /**
     * The main entrypoint for the GUI Application.
     * @param args The JVM startup args
     */
    public static void main(String[] args) { launch(args); }

    /**
     * Returns the instance of this Splendor game.
     * @return The instance.
     */
    public static ApplicationStart getInstance() { return instance; }

    /**
     * Fetches the DomainController associated with this instance of Splendor.
     * @return The DomainController
     */
    public DomainController getController() { return controller; }
}