package gui;

import domain.DevelopmentCard;
import domain.GemAmount;
import domain.NobleCard;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.util.Objects;


public final class BoardScreen extends BorderPane {
    private final GridPane pane = new GridPane();

    public BoardScreen() {
        this.setBackground(new Background(
                new BackgroundImage(
                        new Image(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("assets/title_screen_felt.jpg"))),
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.CENTER,
                        BackgroundSize.DEFAULT)
        ));

        this.setCenter(pane);
    }

    // en hier dan ewa gem logica okay brent? ja
    public void showGems() {
        VBox box = new VBox(2);

        for (GemAmount amount : ApplicationStart.getInstance().getController().getGemStack()) {
            Image image = new Image(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("assets/" + amount.getType().name().toLowerCase() + ".png")));
            Button button = new Button();
            button.setBackground(null);
            ImageView view = new ImageView(image);

            view.setFitWidth(100);
            view.setFitHeight(100);
            view.setPreserveRatio(true);

            button.setGraphic(view);
            button.setOnAction(event -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Gem Clicked");
                alert.setHeaderText(null);
                alert.setContentText(amount.showGems());
                alert.showAndWait();
            });

            box.getChildren().add(button);
            box.getChildren().add(view);
        }

        this.setLeft(box);
    }

    public void showNobles() {
        HBox box = new HBox(2);

        for (NobleCard noble : ApplicationStart.getInstance().getController().getNobles()) {
            Image image = new Image(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("assets/" + noble.getAssetName() + ".jpg")));
            ImageView view = new ImageView(image);
            box.getChildren().add(view);
        }

        this.setTop(box);
    }

    public void showDevelopmentCardPiles() {
        Image level3Image = new Image(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("assets/devlevel3back.jpg")));
        ImageView level3View = new ImageView(level3Image);

        Image level2Image = new Image(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("assets/devlevel2back.jpg")));
        ImageView level2View = new ImageView(level2Image);

        Image level1Image = new Image(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("assets/devlevel1back.jpg")));
        ImageView level1View = new ImageView(level1Image);

        pane.add(level3View, 0, 0);
        pane.add(level2View, 0, 1);
        pane.add(level1View, 0, 2);
    }

    public void showDevelopmentCards() {
        for (byte b = 0; b < 3; b++) {
            for (byte c = 0; c < 4; c++) {
                DevelopmentCard card = ApplicationStart.getInstance().getController().getDevelopmentCardsOntable()[b][c];
                Image image = new Image(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("assets/" + card.getAssetName() + ".jpg")));
                Button button = new Button();
                ImageView view = new ImageView(image);

                button.setGraphic(view);
                button.setOnAction(event -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Card Clicked");
                    alert.setHeaderText(null);
                    alert.setContentText(card.showCard());

                    alert.showAndWait();
                });

                pane.add(button, c + 1, 2 - b);
            }
        }
    }
    public void showPlayers() {
        int numPlayers = ApplicationStart.getInstance().getController().givePlayers().size();

        // Create a VBox layout for the players
        VBox playersBox = new VBox();
        playersBox.setAlignment(Pos.CENTER);
        playersBox.setSpacing(10);
        playersBox.setPadding(new Insets(10));

        // Add the players to the VBox layout
        for (int i = 0; i < numPlayers; i++) {
            String playerName = ApplicationStart.getInstance().getController().givePlayers().get(i).getName();
            int playerPoints = ApplicationStart.getInstance().getController().givePlayers().get(i).getPrestige();
            String playerText = String.format("%s - %d prestige points\n%s", playerName, playerPoints, ApplicationStart.getInstance().getController().givePlayers().get(i).getGemsAsString());
            Font font = new Font(16);
            Button playerButton = new Button(playerText);
            playerButton.setFont(font);
            playerButton.setMaxWidth(Double.MAX_VALUE);

            playerButton.setOnAction(event -> {
                PlayerInfoScreen playerInfoScreen = new PlayerInfoScreen();
                ApplicationStart.getInstance().setScene(playerInfoScreen);
            });

            if (i == ApplicationStart.getInstance().getController().getCurrentPlayerIndex()) {
                playerButton.setStyle("-fx-background-color: yellow;");
            }

            playersBox.getChildren().add(playerButton);
        }

        // Add the VBox to the BoardScreen
        this.setRight(playersBox);
    }

}
