package gui;

import domain.DomainController;
import domain.Player;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.stream.Collectors;

public final class AddPlayersScreen extends BorderPane {
    private final TextField usernameField;
    private final TextField yobField;

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

        VBox center = new VBox();
        Label usernameLabel = new Label("Username");
        this.usernameField = new TextField();
        Label yobLabel = new Label("Year of Birth");
        this.yobField = new TextField();
        Button addPlayerButton = new Button("Add");

        addPlayerButton.setOnAction(this::onAddPlayerButtonClick);
        center.getChildren().addAll(usernameLabel, usernameField, yobLabel, yobField, addPlayerButton);

        this.setBottom(bottomBox);
        this.setCenter(center);
    }

    private void onBackButtonClick(ActionEvent event) { ApplicationStart.getInstance().setScene(new StartScreen()); }

    private void onPlayButtonClick(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText("Splendor | Not Yet Implemented");
        alert.setContentText("The part of the application you are trying to access is not yet implemented. Please try again later.\n\nPlayer list:\n" + addPlayerList());

        alert.showAndWait();
    }

    private String addPlayerList() {
        List<Player> players = ApplicationStart.getInstance().getController().givePlayers();

        return players.stream().map(player -> String.format("%s: %d", player.getName(), player.getDateOfBirth())).collect(Collectors.joining("\n"));
    }

    private void onAddPlayerButtonClick(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        String username = usernameField.getText();
        String yob = yobField.getText();

        if (username == null || yob == null) {
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Missing arguments");
            alert.setContentText("You haven't filled in the required amount of arguments.");
            alert.showAndWait();

            return;
        }

        int year;

        try {
            year = Integer.parseInt(yob);
        } catch (NumberFormatException exception) {
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("NaN");
            alert.setContentText("The year you entered is not a number.");
            alert.showAndWait();

            return;
        }

        String result = ApplicationStart.getInstance().getController().playerLogOn(username, year);

        alert.setHeaderText(result);
        alert.setAlertType(Alert.AlertType.WARNING);
        alert.setTitle("Warning");

        switch (result) {
            case "Player already added!" -> alert.setContentText("The player is already added.");
            case "Player not found!" -> alert.setContentText("The requested player has not been found.");
            case "Player added successfully!" -> {
                alert.setAlertType(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setContentText("The player has been successfully added to the list.");
            }
            default -> {
                alert.setAlertType(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Unable to parse the information returned.");
            }
        }

        alert.showAndWait();
    }
}