package gui;

import domain.DomainController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class UC2DEMOGUI extends Application {
    private static UC2DEMOGUI instance;
    private final DomainController controller = new DomainController();
    private BoardScreen boardScreen;

    private Stage primary;

    @Override
    public void start(Stage primary) throws IOException {
        UC2DEMOGUI.instance = this;
        this.primary = primary;
        boardScreen = new BoardScreen(controller);
        setScene(boardScreen);
        controller.startGame();
        boardScreen.ShowCards();
        primary.setTitle("UC2 DEMO");
        primary.show();
    }

    public void setScene(Pane pane) {
        Scene scene = new Scene(pane, 1280, 720);
        primary.setScene(scene);
    }

    public static void main(String[] args) { launch(args); }
}