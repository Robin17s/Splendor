package gui;

import domain.DevelopmentCard;
import domain.GemAmount;
import domain.NobleCard;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.util.Objects;


public final class BoardScreen extends Pane {

    private GridPane gridPane;
    public BoardScreen() {
        this.setBackground(new Background(
                new BackgroundImage(
                        new Image(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("assets/title_screen_felt.jpg"))),
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.CENTER,
                        BackgroundSize.DEFAULT)
        ));

        gridPane = new GridPane();
        gridPane.setLayoutX(10);
        gridPane.setLayoutY(10);
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(15);
        gridPane.setScaleX(0.5);
        gridPane.setScaleY(0.5);
        // Add the grid to the pane
        this.getChildren().add(gridPane);
    }

    public void ShowGems() {
        int rowCounter = 0;
        for (GemAmount gemAmount : ApplicationStart.getInstance().getController().getGemStack()){
            Image img = new Image(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("assets/"+gemAmount.getType()+".PNG")));
            ImageView imgView = new ImageView(img);
            gridPane.add(imgView, 0, rowCounter);
            rowCounter++;
        }
    }

    public void ShowNobles() {
        int colCounter = 1;
        for (NobleCard noble : ApplicationStart.getInstance().getController().getNobles()){
            Image img = new Image(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("assets/"+noble.getAssetName()+".JPG")));
            ImageView imgView = new ImageView(img);
            gridPane.add(imgView, colCounter, 0);
            colCounter++;
        }
    }

    public void ShowDevelopmentCardPiles() {
        Image img1 = new Image(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("assets/DevLevel3Back.JPG")));
        ImageView imgView1 = new ImageView(img1);
        gridPane.add(imgView1, 1, 1);

        Image img2 = new Image(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("assets/DevLevel2Back.JPG")));
        ImageView imgView2 = new ImageView(img2);
        gridPane.add(imgView2, 1, 2);

        Image img3 = new Image(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("assets/DevLevel1Back.JPG")));
        ImageView imgView3 = new ImageView(img3);
        gridPane.add(imgView3, 1, 3);
    }

    public void ShowDevelopmentCards() {

        for (int row = 3;row>=1;row--) {
            for (int column = 2; column < 6; column++) {
                DevelopmentCard card = ApplicationStart.getInstance().getController().getDevelopmentCardsOntable()[3-row][column-2];
                Image img = new Image(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("assets/"+card.getAssetName()+".JPG")));
                Button b = new Button();
                ImageView imgView = new ImageView(img);
                imgView.setFitHeight(300);
                imgView.setFitWidth(200);
                b.setGraphic(imgView);
                b.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Card Clicked");
                        alert.setHeaderText(null);
                        alert.setContentText(card.showCard());

                        alert.showAndWait();
                        //controller.takeDevelopmentCard(card);
                    }
                });
                gridPane.add(b, column, row);
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
            Font font = new Font(32);
            Button playerButton = new Button(playerText);
            playerButton.setFont(font);
            playerButton.setMaxWidth(Double.MAX_VALUE);
            playersBox.getChildren().add(playerButton);
        }

        // Add the VBox to the BoardScreen
        gridPane.add(playersBox, gridPane.getColumnCount(), 0, 1, 3);
    }

}
