package gui;

import domain.Player;
import domain.i18n.I18n;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.Objects;

/**
 * Screen responsible for showing the winner of the game.
 */
public class WinnerScreen extends BorderPane {

    /**
     * Instantiates the screen, and sets it up for rendering.
     */
    public WinnerScreen() {
        this.setBackground(new Background(
                new BackgroundImage(
                        new Image(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("assets/title_screen_felt.jpg"))),
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.CENTER,
                        BackgroundSize.DEFAULT)
        ));

        VBox winnersBox = new VBox();
        winnersBox.setAlignment(Pos.CENTER);
        winnersBox.setSpacing(20);
        winnersBox.setPadding(new Insets(50));

        Label title = new Label(I18n.translate("winnerscreen.winners"));
        title.setFont(new Font("Arial", 48));
        title.setTextFill(Color.WHITE);

        winnersBox.getChildren().add(title);

        for (Player.PlayerDTO winner : ApplicationStart.getInstance().getController().getWinners()) {
            Label winnerLabel = new Label(winner.name());
            winnerLabel.setFont(new Font("Arial", 26));
            winnerLabel.setTextFill(Color.WHITE);
            winnersBox.getChildren().add(winnerLabel);
        }

        this.setCenter(winnersBox);
    }
}

