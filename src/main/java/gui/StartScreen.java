package gui;

import domain.i18n.I18n;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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
import java.util.Optional;

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
        Label versionLabel = new Label(I18n.translate("startscreen.version"));
        Button playButton = new Button(I18n.translate("startscreen.play"));
        Button languageButton = new Button(I18n.translate("startscreen.language"));
        Button quitButton = new Button(I18n.translate("startscreen.quit"));

        titleLabel.setFont(Font.font(32));
        titleLabel.setStyle("-fx-font-weight: bold");

        versionLabel.setFont(Font.font(16));
        VBox.setMargin(versionLabel, new Insets(0, 0, 64, 0));

        playButton.setMinWidth(250);
        playButton.setMinHeight(50);
        VBox.setMargin(playButton, new Insets(8, 0, 8, 0));
        playButton.setOnAction(this::onPlayButtonClick);

        languageButton.setMinWidth(250);
        languageButton.setMinHeight(50);
        VBox.setMargin(languageButton, new Insets(8, 0, 8, 0));
        languageButton.setOnAction(this::onLanguageButtonClick);

        quitButton.setMinWidth(250);
        quitButton.setMinHeight(50);
        VBox.setMargin(quitButton, new Insets(8, 0, 8, 0));
        quitButton.setOnAction(this::onQuitButtonClick);

        box.getChildren().addAll(titleLabel, versionLabel, playButton, languageButton, quitButton);
        this.getChildren().add(box);
    }

    private void onPlayButtonClick(ActionEvent event) { ApplicationStart.getInstance().setScene(new AddPlayersScreen()); }

    private void onLanguageButtonClick(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle(I18n.translate("startscreen.language"));
        alert.setHeaderText(I18n.translate("startscreen.language"));
        alert.setContentText(I18n.translate("startscreen.language.message"));
        alert.getButtonTypes().add(new ButtonType("English (US)"));
        alert.getButtonTypes().add(new ButtonType("Nederlands (België)"));

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isEmpty()) return;

        String locale = switch (result.get().getText()) {
            case "English (US)" -> "en_US";
            case "Nederlands (België)" -> "nl_BE";
            default -> I18n.current_locale;
        };

        I18n.loadTranslationFile(locale);
        ApplicationStart.getInstance().setScene(new StartScreen()); // Reload pane
    }

    private void onQuitButtonClick(ActionEvent event) { Platform.exit(); }
}