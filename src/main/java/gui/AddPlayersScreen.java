package gui;

import domain.Player;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public final class AddPlayersScreen extends BorderPane {
    private final TextField usernameField;
    private final TextField yobField;
    private final ListView<String> playerPane;

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
        center.setPadding(new Insets(16));

        Label usernameLabel = new Label("Username");
        this.usernameField = new TextField();

        Region space = new Region();
        space.setPadding(new Insets(4));

        Label yobLabel = new Label("Year of Birth");
        this.yobField = new TextField();

        Region space2 = new Region();
        space2.setPadding(new Insets(4));

        Button addPlayerButton = new Button("Add");

        addPlayerButton.setOnAction(this::onAddPlayerButtonClick);
        center.getChildren().addAll(usernameLabel, usernameField, space, yobLabel, yobField, space2, addPlayerButton);

        VBox leftPane = new VBox();
        this.playerPane = new ListView<>();
        Button removePlayerButton = new Button("-");

        removePlayerButton.prefWidthProperty().bind(leftPane.widthProperty().subtract(16));
        removePlayerButton.setOnAction(this::onRemovePlayerButtonClick);

        leftPane.getChildren().add(playerPane);
        leftPane.getChildren().add(removePlayerButton);

        VBox.setVgrow(playerPane, Priority.ALWAYS);
        VBox.setMargin(removePlayerButton, new Insets(8));

        ApplicationStart.getInstance().getController().givePlayers().forEach(player -> addPlayerToPane(player.getName(), player.getDateOfBirth()));

        this.setLeft(leftPane);
        this.setBottom(bottomBox);
        this.setCenter(center);


        // Quick Debug bypass method, PUT IN COMMENT IF YOU DONT NEED
        ApplicationStart.getInstance().getController().playerLogOn("thomas", 1987);
        addPlayerToPane("thomas", 1987);
        ApplicationStart.getInstance().getController().playerLogOn("brent", 2005);
        addPlayerToPane("brent", 2005);
        ApplicationStart.getInstance().getController().playerLogOn("robin", 2000);
        addPlayerToPane("robin", 2000);
        ApplicationStart.getInstance().getController().playerLogOn("friso", 2004);
        addPlayerToPane("friso", 2004);
    }

    private void onBackButtonClick(ActionEvent event) { ApplicationStart.getInstance().setScene(new StartScreen()); }

    private void onPlayButtonClick(ActionEvent event) {
        if (ApplicationStart.getInstance().getController().givePlayers().size() < 2) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Splendor | Not Enough Players!");
            alert.setContentText("There aren't enough players to start the game. Please add more players, and try again.");

            alert.showAndWait();
            return;
        }

        try {
            BoardScreen boardScreen = new BoardScreen();
            ApplicationStart.getInstance().setScene(boardScreen);
            ApplicationStart.getInstance().getController().startGame();
            boardScreen.ShowGems();
            boardScreen.ShowNobles();
            boardScreen.ShowDevelopmentCardPiles();
            boardScreen.ShowDevelopmentCards();
            boardScreen.showPlayers();
        } catch (IOException exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fatal Error");
            alert.setHeaderText("Splendor | Failed to start game!");
            alert.setContentText("Something went wrong whilst trying to start the game. Splendor will now exit.");

            alert.showAndWait();
            Platform.exit();
        }
    }

    private String addPlayerList() {
        List<Player> players = ApplicationStart.getInstance().getController().givePlayers();

        return players.stream().map(player -> String.format("%s - %d", player.getName(), player.getDateOfBirth())).collect(Collectors.joining("\n"));
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

        boolean proceed = false;

        switch (result) {
            case "Player already added!" -> alert.setContentText("The player is already added.");
            case "Player not found!" -> alert.setContentText("The requested player has not been found.");
            case "Player added successfully!" -> {
                alert.setAlertType(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setContentText("The player has been successfully added to the list.");
                proceed = true;
            }
            default -> {
                alert.setAlertType(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Unable to parse the information returned.");
            }
        }

        alert.showAndWait();

        if (!(proceed)) return;

        addPlayerToPane(username, year);
    }

    private void addPlayerToPane(String username, int year) { playerPane.getItems().add("%s - %d".formatted(username, year)); }

    private void onRemovePlayerButtonClick(ActionEvent event) {
        String selectedItem = playerPane.selectionModelProperty().get().getSelectedItems().get(0);
        playerPane.getItems().remove(selectedItem);
        String name = selectedItem.split("-")[0].trim();
        int year = Integer.parseInt(selectedItem.split("-")[1].trim());

        ApplicationStart.getInstance().getController().removePlayerFromGame(name, year);
    }
}