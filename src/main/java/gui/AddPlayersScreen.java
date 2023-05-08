package gui;

import domain.Player;
import domain.i18n.I18n;
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

        Button backButton = new Button(I18n.translate("addplayersscreen.back"));
        backButton.setOnAction(this::onBackButtonClick);

        Region buttonSpacer = new Region();
        buttonSpacer.setPadding(new Insets(4));

        Button playButton = new Button(I18n.translate("addplayerssscreen.play"));
        playButton.setOnAction(this::onPlayButtonClick);

        bottomBox.setPadding(new Insets(8));
        bottomBox.getChildren().addAll(spacer, backButton, buttonSpacer, playButton);

        VBox center = new VBox();
        center.setPadding(new Insets(16));

        Label usernameLabel = new Label(I18n.translate("addplayersscreen.username"));
        this.usernameField = new TextField();

        Region space = new Region();
        space.setPadding(new Insets(4));

        Label yobLabel = new Label(I18n.translate("addplayersscreen.yob"));
        this.yobField = new TextField();

        Region space2 = new Region();
        space2.setPadding(new Insets(4));

        Button addPlayerButton = new Button(I18n.translate("addplayersscreen.add"));

        addPlayerButton.setOnAction(this::onAddPlayerButtonClick);
        center.getChildren().addAll(usernameLabel, usernameField, space, yobLabel, yobField, space2, addPlayerButton);

        VBox leftPane = new VBox();
        this.playerPane = new ListView<>();
        Button removePlayerButton = new Button(I18n.translate("addplayersscreen.removeplayer"));

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
        /*ApplicationStart.getInstance().getController().playerLogOn("robin", 2000);
        addPlayerToPane("robin", 2000);
        ApplicationStart.getInstance().getController().playerLogOn("friso", 2004);
        addPlayerToPane("friso", 2004);*/
    }

    private void onBackButtonClick(ActionEvent event) { ApplicationStart.getInstance().setScene(new StartScreen()); }

    private void onPlayButtonClick(ActionEvent event) {
        if (ApplicationStart.getInstance().getController().givePlayers().size() < 2) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(I18n.translate("addplayersscreen.playbuttonclick.warning"));
            alert.setHeaderText(I18n.translate("addplayersscreen.playbuttonclick.notenoughplayers"));
            alert.setContentText(I18n.translate("addplayersscreen.playbuttonclick.notenoughplayers.explanation"));

            alert.showAndWait();
            return;
        }

        try {
            /*ApplicationStart.getInstance().getController().startGame();
            PlayerInfoScreen playerInfoScreen = new PlayerInfoScreen();
            ApplicationStart.getInstance().setScene(playerInfoScreen);*/

            BoardScreen boardScreen = new BoardScreen();
            ApplicationStart.getInstance().setScene(boardScreen);
            ApplicationStart.getInstance().getController().startGame();
            //ApplicationStart.getInstance().getController().addItemsToPlayers();
            boardScreen.showGems();
            boardScreen.showNobles();
            boardScreen.showDevelopmentCardPiles();
            boardScreen.showDevelopmentCards();
            boardScreen.showPlayers();
        } catch (IOException exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(I18n.translate("addplayersscreen.playbuttonclick.fatalerror"));
            alert.setHeaderText(I18n.translate("addplayersscreen.playbuttonclick.failedtostart"));
            alert.setContentText(I18n.translate("addplayersscreen.playbuttonclick.failedtostart.explanation"));

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
            alert.setTitle(I18n.translate("addplayersscreen.playerbuttonclick.error"));
            alert.setHeaderText(I18n.translate("addplayersscreen.playerbuttonclick.missingarguments"));
            alert.setContentText(I18n.translate("addplayersscreen.playerbuttonclick.missingarguments.arguments"));
            alert.showAndWait();

            return;
        }

        int year;

        try {
            year = Integer.parseInt(yob);
        } catch (NumberFormatException exception) {
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setTitle(I18n.translate("addplayersscreen.playerbuttonclick.error"));
            alert.setHeaderText(I18n.translate("addplayersscreen.playerbuttonclick.nan"));
            alert.setContentText(I18n.translate("addplayersscreen.playerbuttonclick.nan.explanation"));
            alert.showAndWait();

            return;
        }

        String result = ApplicationStart.getInstance().getController().playerLogOn(username, year);

        alert.setHeaderText(result);
        alert.setAlertType(Alert.AlertType.WARNING);
        alert.setTitle(I18n.translate("addplayersscreen.playerbuttonclick.warning"));

        boolean proceed = false;

        switch (result) {
            case "Player already added!" -> alert.setContentText(I18n.translate("addplayersscreen.playerbuttonclick.alreadyadded"));
            case "Player not found!" -> alert.setContentText(I18n.translate("addplayersscreen.playerbuttonclick.notfound"));
            case "Player added successfully!" -> {
                alert.setAlertType(Alert.AlertType.INFORMATION);
                //alert.setTitle("Success");
                alert.setTitle(I18n.translate("addplayersscreen.playerbuttonclick.success"));
                alert.setContentText(I18n.translate("addplayersscreen.playerbuttonclick.success.message"));
                proceed = true;
            }
            default -> {
                alert.setAlertType(Alert.AlertType.ERROR);
                alert.setTitle(I18n.translate("addplayersscreen.playerbuttonclick.error"));
                alert.setContentText(I18n.translate("addplayersscreen.playerbuttonclick.error.parse"));
            }
        }

        alert.showAndWait();

        if (!(proceed)) return;

        addPlayerToPane(username, year);
    }

    private void addPlayerToPane(String username, int year) { playerPane.getItems().add("%s - %d".formatted(username, year)); }

    private void onRemovePlayerButtonClick(ActionEvent event) {
        if (playerPane.selectionModelProperty().get().getSelectedItems().size() != 0){
            String selectedItem = playerPane.selectionModelProperty().get().getSelectedItems().get(0);
            playerPane.getItems().remove(selectedItem);
            String name = selectedItem.split("-")[0].trim();
            int year = Integer.parseInt(selectedItem.split("-")[1].trim());

            ApplicationStart.getInstance().getController().removePlayerFromGame(name, year);
        }
    }
}