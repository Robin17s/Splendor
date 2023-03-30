package gui;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

public final class AddPlayersScreen extends BorderPane {
    public AddPlayersScreen() {
        HBox bottomBox = new HBox();
        bottomBox.setBackground(new Background(new BackgroundFill(new Color(0.9, 0.9, 0.9, 1.0), CornerRadii.EMPTY, Insets.EMPTY)));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button backButton = new Button("< Back");
        backButton.setOnAction(this::onBackButtonClick);

        Region buttonSpacer = new Region();
        buttonSpacer.setPadding(new Insets(4));

        Button playButton = new Button("Play >");
        playButton.setOnAction(this::onPlayButtonClick);

        bottomBox.setPadding(new Insets(8));
        bottomBox.getChildren().addAll(spacer, backButton, buttonSpacer, playButton);

        this.setBottom(bottomBox);
        this.setCenter(new Pane());
    }

    private void onBackButtonClick(ActionEvent event) { ApplicationStart.getInstance().setScene(new StartScreen()); }

    private void onPlayButtonClick(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText("Splendor | Not Yet Implemented");
        alert.setContentText("The part of the application you are trying to access is not yet implemented. Please try again later.");

        alert.showAndWait();
    }
}