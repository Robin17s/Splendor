package gui;

import domain.DevelopmentCard;
import domain.DomainController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import java.util.Objects;

import static javafx.geometry.Pos.CENTER;

public final class BoardScreen extends Pane {

    private GridPane gridPane;
    private final DomainController controller;
    public BoardScreen(DomainController controller) {
        this.controller = controller;

        this.setBackground(new Background(
                new BackgroundImage(
                        new Image(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("assets/title_screen_felt.jpg"))),
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.CENTER,
                        BackgroundSize.DEFAULT)
        ));

        gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setAlignment(CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(15);

        // Add the grid to the pane
        this.getChildren().add(gridPane);
    }

    public void ShowCards() {

        for (int row = 2;row>=0;row--) {
            for (int column = 0; column < 4; column++) {
                DevelopmentCard card = controller.getDevelopmentCardsOntable()[2-row][column];
                Image img = new Image(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("assets/"+card.getAssetName()+".JPG")));
                Button b = new Button();
                b.setGraphic(new ImageView(img));
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
}
