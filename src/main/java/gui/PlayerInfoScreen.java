package gui;

import domain.DevelopmentCard;
import domain.DomainController;
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

public class PlayerInfoScreen extends BorderPane {
    private final GridPane pane = new GridPane();
    DomainController domainController = ApplicationStart.getInstance().getController();
    private int selectedPlayerIndex;
    public PlayerInfoScreen(int selectedPlayerIndex) {
        this.selectedPlayerIndex = selectedPlayerIndex;
        this.setBackground(new Background(
                new BackgroundImage(
                        new Image(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("assets/title_screen_felt.jpg"))),
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.CENTER,
                        BackgroundSize.DEFAULT)
        ));
        pane.setPadding(new Insets(10));
        pane.setHgap(25);
        pane.setVgap(25);
        this.setCenter(pane);
        showPlayers();
        showPlayerDevelopmentCards();
    }

    public void refresh(){
        showPlayers();
    }

    public void showPlayerDevelopmentCards() {
        if (!domainController.givePlayers().get(domainController.getCurrentPlayerIndex()).getDevelopmentCards().equals(0)){
            int row = 0;
            int column = 0;

            for (DevelopmentCard card : domainController.givePlayers().get(selectedPlayerIndex).getDevelopmentCards()){
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

                pane.add(button, column, row);
                column++;
                if (column == 6){
                    column = 0;
                    row++;
                }
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
                loadBoardScreen();
            });

            if (i == ApplicationStart.getInstance().getController().getCurrentPlayerIndex()) {
                playerButton.setStyle("-fx-background-color: yellow;");
            }

            playersBox.getChildren().add(playerButton);
        }

        // Add the VBox to the BoardScreen
        this.setRight(playersBox);
    }

    private void loadBoardScreen(){
        BoardScreen boardScreen = new BoardScreen();
        ApplicationStart.getInstance().setScene(boardScreen);
        boardScreen.showGems();
        boardScreen.showNobles();
        boardScreen.showDevelopmentCardPiles();
        boardScreen.showDevelopmentCards();
        boardScreen.showPlayers();
    }
}
