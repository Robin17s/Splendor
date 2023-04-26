package gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.Objects;

public final class StartScreen extends Pane {
    public StartScreen() {
        // TODO wrap in something safer
        this.setBackground(new Background(
                new BackgroundImage(
                        new Image(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("assets/title_screen_felt.jpg"))),
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.CENTER,
                        BackgroundSize.DEFAULT)
        ));

        VBox box = new VBox();

        box.setAlignment(Pos.CENTER);
        box.prefWidthProperty().bind(super.widthProperty());
        box.prefHeightProperty().bind(super.heightProperty());
        box.setBackground(new Background(new BackgroundFill(new Color(0.9, 0.9, 0.9, 0.5), CornerRadii.EMPTY, new Insets(128, 256, 128, 256))));

        Label titleLabel = new Label("Splendor");
        Label versionLabel = new Label("G53 still somewhat Half-baked Edition");
        Button playButton = new Button("Play");
        Button quitButton = new Button("Quit");

        titleLabel.setFont(Font.font(32));
        titleLabel.setStyle("-fx-font-weight: bold");

        versionLabel.setFont(Font.font(16));
        VBox.setMargin(versionLabel, new Insets(0, 0, 64, 0));

        playButton.setMinWidth(250);
        playButton.setMinHeight(50);
        VBox.setMargin(playButton, new Insets(8, 0, 8, 0));
        playButton.setOnAction(this::onPlayButtonClick);

        quitButton.setMinWidth(250);
        quitButton.setMinHeight(50);
        VBox.setMargin(quitButton, new Insets(8, 0, 8, 0));
        quitButton.setOnAction(this::onQuitButtonClick);

        box.getChildren().addAll(titleLabel, versionLabel, playButton, quitButton);
        this.getChildren().add(box);
    }

    private void onPlayButtonClick(ActionEvent event) {
        ApplicationStart.getInstance().setScene(new AddPlayersScreen());
        }

    private void onQuitButtonClick(ActionEvent event) { Platform.exit(); }
}