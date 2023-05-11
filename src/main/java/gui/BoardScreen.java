package gui;

import domain.DevelopmentCard;
import domain.GemAmount;
import domain.NobleCard;
import domain.i18n.I18n;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Screen responsible for rendering the game board, and its data.
 */
public final class BoardScreen extends BorderPane {
    private final GridPane pane = new GridPane();
    private List<GemAmount.GemAmountDTO> gemsPicked = new ArrayList<>();

    private boolean finalRoundPopupFlag = false;
    private boolean noCardFlag = false;

    /**
     * Instantiates a new BoardScreen, and initialises everything for the screen to start rendering.
     * <p>
     * Note: Calling this constructor only instantiates the screen. This doesn't mean it will start rendering. You will have to set that using {@link ApplicationStart#setScene(Pane)}
     */
    public BoardScreen() {
        this.setBackground(new Background(
                new BackgroundImage(
                        new Image(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("assets/title_screen_felt.jpg"))),
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.CENTER,
                        BackgroundSize.DEFAULT)
        ));

        // Middle screen can take up to 75% of the available screen width, and 85% of its height
        pane.minWidthProperty().bind(this.widthProperty().multiply(0.75));
        pane.prefWidthProperty().bind(this.widthProperty().multiply(0.75));
        pane.maxWidthProperty().bind(this.widthProperty().multiply(0.75));
        pane.minHeightProperty().bind(this.heightProperty().multiply(0.85));
        pane.prefHeightProperty().bind(this.heightProperty().multiply(0.85));
        pane.maxHeightProperty().bind(this.heightProperty().multiply(0.85));

        pane.setScaleX(0.75);
        pane.setScaleY(0.75);
        pane.setScaleShape(true);
        pane.setTranslateX(-100);
        pane.setTranslateY(-100);

        this.setCenter(pane);
    }

    /**
     * Re-renders every component on the screen.
     */
    public void refreshScreen() {
        showGems();
        showPlayers();
        showNobles();
        showDevelopmentCardPiles();
        showDevelopmentCards();
        finalRoundPopup();
        switchToWinnerScreen();
        refreshScreenWhenNoCard();
    }

    public void refreshScreenWhenNoCard(){
        // :(
        // refreshScreen leaves the old card displayed
        if (!noCardFlag)
            return;
        BoardScreen boardScreen = new BoardScreen();
        ApplicationStart.getInstance().setScene(boardScreen);

        //boardScreen.refreshScreen(); -> 100%cpu and 8gb ram
        boardScreen.showGems();
        boardScreen.showNobles();
        boardScreen.showDevelopmentCardPiles();
        boardScreen.showDevelopmentCards();
        boardScreen.showPlayers();
        finalRoundPopup();
        switchToWinnerScreen();
        noCardFlag = false;
    }

    /**
     * Checks whether or not the final round should be played, and if so, displays a popup showing it.
     */
    public void finalRoundPopup(){
        if (ApplicationStart.getInstance().getController().isFinalRound() && !finalRoundPopupFlag){
            Alert popup = new Alert(Alert.AlertType.INFORMATION);
            popup.setTitle("Message");
            popup.setHeaderText(null);
            popup.setContentText("Final round");
            popup.showAndWait();
            finalRoundPopupFlag = true;
        }
    }

    /**
     * Checks whether or not a winner has been decided, and if so, displays the winner screen.
     */
    public void switchToWinnerScreen(){
        if (ApplicationStart.getInstance().getController().getCurrentPlayerIndex() == ApplicationStart.getInstance().getController().givePlayers().size()){
            ApplicationStart.getInstance().setScene(new WinnerScreen());
        }
    }

    /**
     * Renders the part of the screen where the gems are rendered.
     */
    public void showGems() {
        //rode box in paint
        VBox box = new VBox(2);

        for (GemAmount.GemAmountDTO amount : ApplicationStart.getInstance().getController().getGemStack()) {
            Image image = new Image(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("assets/" + amount.type().name().toLowerCase() + ".png")));
            // zwarte rondjes in paint
            Button button = new Button();
            button.setBackground(null);
            ImageView view = new ImageView(image);

            view.setFitWidth(100);
            view.setFitHeight(100);
            view.setPreserveRatio(true);

            button.setGraphic(view);
            button.setOnAction(event -> {

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle(I18n.translate("boardscreen.gems.title"));
                alert.setHeaderText(I18n.translate(amount.type().getTranslationKey()));
                alert.setContentText(I18n.translate("boardscreen.gems.explanation"));

                ButtonType buttonTypeOne = new ButtonType(I18n.translate("boardscreen.gems.takethree", "" + (gemsPicked.size() + 1)));
                ButtonType buttonTypeTwo = new ButtonType(I18n.translate("boardscreen.gems.taketwo"));
                ButtonType buttonTypeCancel = new ButtonType(I18n.translate("message.no"), ButtonBar.ButtonData.CANCEL_CLOSE);

                alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeCancel);

                Optional<ButtonType> result = alert.showAndWait();
                String actionResult = "";
                if (result.get() == buttonTypeOne) {
                    if (!gemsPicked.contains(amount))
                        gemsPicked.add(amount);

                    if (gemsPicked.size() == 3) {
                        actionResult = ApplicationStart.getInstance().getController().takeThreeGemsOfDifferentTypes(gemsPicked);
                        nobleAlert();
                        gemsPicked.clear();
                    }
                } else if (result.get() == buttonTypeTwo) {
                    actionResult = ApplicationStart.getInstance().getController().takeTwoGemsOfTheSameType(amount);
                    nobleAlert();
                    gemsPicked.clear();
                }

                if (!actionResult.equals("")) {
                    Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                    alert2.setTitle(I18n.translate("boardscreen.gems.title"));
                    alert2.setHeaderText(I18n.translate("boardscreen.gems.title"));
                    alert2.setContentText(actionResult);
                    alert2.showAndWait();

                    refreshScreen();
                }

            });

            //label voor aantal beschikbare gems in een stapel
            Label lbl = new Label("" + amount.amount());
            lbl.setFont(Font.font("Impact", 27));
            lbl.setTextFill(Color.WHITE);

            //button (met view van asset) toevoegen aan Horizontal box
            //label toevoegen na de button
            //groene boxen in paint
            HBox hbox = new HBox(button, lbl);
            hbox.setAlignment(Pos.BOTTOM_LEFT);
            box.getChildren().add(hbox);
            HBox.setMargin(lbl, new Insets(2, 2, 2, -15));
        }
        Button skipTurnButton = new Button(I18n.translate("player.skip.turn"));
        skipTurnButton.setOnAction(event -> {
            Alert turnSkippedAlert = new Alert(Alert.AlertType.INFORMATION);
            turnSkippedAlert.setTitle(I18n.translate("player.skip.turn.popup.title"));
            turnSkippedAlert.setHeaderText(I18n.translate("player.skip.turn.popup.info", ApplicationStart.getInstance().getController().givePlayers().get(ApplicationStart.getInstance().getController().getCurrentPlayerIndex()).name()));
            turnSkippedAlert.showAndWait();
            ApplicationStart.getInstance().getController().skipTurn();
            nobleAlert();
            refreshScreen();
        });
        box.getChildren().add(skipTurnButton);

        // Gem box can take up to 10% of the available screen width, and 100% of its height
        box.minWidthProperty().bind(this.widthProperty().multiply(0.1));
        box.prefWidthProperty().bind(this.widthProperty().multiply(0.1));
        box.maxWidthProperty().bind(this.widthProperty().multiply(0.1));
        box.minHeightProperty().bind(this.heightProperty());
        box.prefHeightProperty().bind(this.heightProperty());
        box.maxHeightProperty().bind(this.heightProperty());

        this.setLeft(box);
    }

    /**
     * Renders the part of the screen where the nobles are present.
     */
    public void showNobles() {
        HBox box = new HBox(2);

        for (NobleCard.NobleCardDTO noble : ApplicationStart.getInstance().getController().getNobles()) {
            Image image = new Image(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("assets/" + noble.assetName() + ".jpg")));
            ImageView view = new ImageView(image);
            box.getChildren().add(view);
        }

        // Noble box can take up to 90% of the available screen width, and 15% of its height
        box.minWidthProperty().bind(this.widthProperty().multiply(0.90));
        box.prefWidthProperty().bind(this.widthProperty().multiply(0.90));
        box.maxWidthProperty().bind(this.widthProperty().multiply(0.90));
        box.minHeightProperty().bind(this.heightProperty().multiply(0.15));
        box.prefHeightProperty().bind(this.heightProperty().multiply(0.15));
        box.maxHeightProperty().bind(this.heightProperty().multiply(0.15));

        box.setTranslateX(250);
        box.translateXProperty().bind(this.widthProperty().multiply(0.25).divide(2));

        this.setTop(box);
    }

    /**
     * Renders the piles of (invisible) development cards.
     */
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

    /**
     * Renders the visible development cards on screen.
     */
    public void showDevelopmentCards() {
        for (byte row = 0; row < 3; row++) {
            for (byte column = 0; column < 4; column++) {
                DevelopmentCard.DevelopmentCardDTO card = ApplicationStart.getInstance().getController().getDevelopmentCardsOnTable()[row][column];
                if (card == null){
                    noCardFlag = true;
                    continue;
                }
                Image image = new Image(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("assets/" + card.assetName() + ".jpg")));
                Button button = new Button();
                ImageView view = new ImageView(image);

                button.setGraphic(view);
                button.setOnAction(event -> {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle(I18n.translate("boardscreen.developmentcards.buy.title"));
                    //alert.setHeaderText(card.showCard());
                    alert.setHeaderText(I18n.translate("boardscreen.developmentcards.buy.explanation"));

                    ButtonType buttonTypeOne = new ButtonType(I18n.translate("message.yes"));

                    ButtonType buttonTypeCancel = new ButtonType(I18n.translate("message.no"), ButtonBar.ButtonData.CANCEL_CLOSE);

                    alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);
                    /*if (!ApplicationStart.getInstance().getController().canPlayerAffordCard(card)) {
                        Node yesButton = alert.getDialogPane().lookupButton(buttonTypeOne);
                        yesButton.setDisable(true);
                    }*/

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == buttonTypeOne) {
                        /*// if canPlayerAffordCard then takeDevelopmentCard
                        if (ApplicationStart.getInstance().getController().canPlayerAffordCard(card)) {
                            ApplicationStart.getInstance().getController().takeDevelopmentCard(card);
                            refreshScreen();
                        }*/
                        int playerIndexBeforeAction = ApplicationStart.getInstance().getController().getCurrentPlayerIndex();
                        String actionResult = ApplicationStart.getInstance().getController().takeDevelopmentCard(card);
                        Alert buyAlert = new Alert(Alert.AlertType.INFORMATION);
                        buyAlert.setTitle(I18n.translate("boardscreen.devcards.print.action"));
                        buyAlert.setHeaderText(null);
                        buyAlert.setContentText(actionResult);
                        buyAlert.showAndWait();
                        if (playerIndexBeforeAction != ApplicationStart.getInstance().getController().getCurrentPlayerIndex())
                            nobleAlert();
                        refreshScreen();
                    } else {
                        // alertje toevoegen
                    }
                });

                pane.add(button, column + 1, 2 - row);
            }
        }
    }

    /**
     * Checks whether or not the current player can be visited by a Noble, and shows the popup when true.
     */
    private void nobleAlert() {
        List<NobleCard.NobleCardDTO> ref = new ArrayList<>();
        if (ApplicationStart.getInstance().getController().canPlayerGetNobleCard(ref)) {
            Alert nobleAlert = new Alert(Alert.AlertType.CONFIRMATION);
            nobleAlert.setTitle(I18n.translate("boardscreen.devcards.print.noble"));
            String nobleString = "";
            List<ButtonType> buttons = new ArrayList<>();
            for (int i = 1; i < ref.size() + 1; i++) {
                nobleString += String.format("%d: %s \n", i, ApplicationStart.getInstance().getController().showNobleCard(ref.get(i - 1)));
                buttons.add(new ButtonType(String.format("%d", i)));
            }
            nobleAlert.getButtonTypes().setAll(buttons);
            nobleAlert.setHeaderText(nobleString);
            Optional<ButtonType> nobleChoice = nobleAlert.showAndWait();
            if (nobleChoice.isPresent()) {
                ButtonType iets = nobleChoice.get();
                ApplicationStart.getInstance().getController().setPlayerNoble(ref.get(Integer.parseInt(iets.getText()) - 1));
            }
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

            playerButton.setUserData(i);

            playerButton.setOnAction(event -> {
                PlayerInfoScreen playerInfoScreen = new PlayerInfoScreen((int) playerButton.getUserData());
                ApplicationStart.getInstance().setScene(playerInfoScreen);
            });

            if (i == ApplicationStart.getInstance().getController().getCurrentPlayerIndex()) {
                playerButton.setStyle("-fx-background-color: yellow;");
            }

            playersBox.getChildren().add(playerButton);
        }

        // Player box can take up to 20% of the available screen width, and 90% of its height
        playersBox.minWidthProperty().bind(this.widthProperty().multiply(0.20));
        playersBox.prefWidthProperty().bind(this.widthProperty().multiply(0.20));
        playersBox.maxWidthProperty().bind(this.widthProperty().multiply(0.20));
        playersBox.minHeightProperty().bind(this.heightProperty().multiply(0.90));
        playersBox.prefHeightProperty().bind(this.heightProperty().multiply(0.90));
        playersBox.maxHeightProperty().bind(this.heightProperty().multiply(0.90));

        playersBox.setTranslateX(-150);

        // Add the VBox to the BoardScreen
        this.setRight(playersBox);
    }

}
