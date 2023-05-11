package gui;

import domain.DevelopmentCard;
import domain.DomainController;
import domain.NobleCard;
import domain.i18n.I18n;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.util.Objects;

/**
 * Screen responsible for showing a specific Player's info.
 */
public class PlayerInfoScreen extends BorderPane {
    private final GridPane pane = new GridPane();
    DomainController domainController = ApplicationStart.getInstance().getController();
    private int selectedPlayerIndex;

    /**
     * Instantiates a new PlayerInfoScreen, and initialises everything for the screen to start rendering.
     * <p>
     * Note: Calling this constructor only instantiates the screen. This doesn't mean it will start rendering. You will have to set that using {@link ApplicationStart#setScene(Pane)}
     * @param selectedPlayerIndex The index of the Player whose stats are being loaded
     */
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
        showPlayerCards();
    }

    /**
     * Renders the specific development cards owned by the player.
     */
    public void showPlayerCards() {
        int row = 0;
        int column = 0;
        if (!domainController.givePlayers().get(domainController.getCurrentPlayerIndex()).developmentCards().equals(0)){
            for (DevelopmentCard card : domainController.givePlayers().get(selectedPlayerIndex).developmentCards()){
                Image image = new Image(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("assets/" + card.getAssetName() + ".jpg")));
                Button button = new Button();
                ImageView view = new ImageView(image);

                button.setGraphic(view);
                button.setOnAction(event -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle(I18n.translate("playerinfoscreen.playercards.clicked.title"));
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
        if (domainController.givePlayers().get(selectedPlayerIndex).nobleCard() != null){
            NobleCard card = domainController.givePlayers().get(selectedPlayerIndex).nobleCard();
            Image image = new Image(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("assets/" + card.getAssetName() + ".jpg")));
            Button button = new Button();
            ImageView view = new ImageView(image);

            button.setGraphic(view);
            button.setOnAction(event -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(I18n.translate("playerinfoscreen.playercards.clicked.title"));
                alert.setHeaderText(null);
                alert.setContentText(card.showCard());

                alert.showAndWait();
            });

            pane.add(button, column, row);
        }
    }

    /**
     * Renders the players on screen.
     */
    public void showPlayers() {
        int numPlayers = ApplicationStart.getInstance().getController().givePlayers().size();

        // Create a VBox layout for the players
        VBox playersBox = new VBox();
        playersBox.setAlignment(Pos.CENTER);
        playersBox.setSpacing(10);
        playersBox.setPadding(new Insets(10));

        // Add the players to the VBox layout
        for (int i = 0; i < numPlayers; i++) {
            String playerName = ApplicationStart.getInstance().getController().givePlayers().get(i).name();
            int playerPoints = ApplicationStart.getInstance().getController().givePlayers().get(i).prestige();
            String playerText = I18n.translate("boardscreen.players.text", playerName, String.valueOf(playerPoints), ApplicationStart.getInstance().getController().getPlayerGemsAsString(ApplicationStart.getInstance().getController().givePlayers().get(i)));
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

        // Player box can take up to 20% of the available screen width, and 90% of its height
        playersBox.  minWidthProperty().bind(this. widthProperty().multiply(0.20));
        playersBox. prefWidthProperty().bind(this. widthProperty().multiply(0.20));
        playersBox.  maxWidthProperty().bind(this. widthProperty().multiply(0.20));
        playersBox. minHeightProperty().bind(this.heightProperty().multiply(0.90));
        playersBox.prefHeightProperty().bind(this.heightProperty().multiply(0.90));
        playersBox. maxHeightProperty().bind(this.heightProperty().multiply(0.90));

        playersBox.setTranslateX(-65);
        playersBox.setTranslateY(145);

        // Add the VBox to the BoardScreen
        this.setRight(playersBox);
    }

    /**
     * Returns to the board screen.
     */
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
