package gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.Objects;

import static javafx.geometry.Pos.CENTER;

public final class BoardScreen extends Pane {
    public BoardScreen() {

        this.setBackground(new Background(
                new BackgroundImage(
                        new Image(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("assets/title_screen_felt.jpg"))),
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.CENTER,
                        BackgroundSize.DEFAULT)
        ));

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setAlignment(CENTER);
        gridPane.setHgap(160);
        gridPane.setVgap(130);

        // Add your grid elements here
        Label label1 = new Label("Grid Element 1");
        Label label2 = new Label("Grid Element 2");
        Label label3 = new Label("Grid Element 3");
        Label label4 = new Label("Grid Element 4");
        Label label5 = new Label("Grid Element 5");
        Label label6 = new Label("Grid Element 6");
        Label label7 = new Label("Grid Element 7");
        Label label8 = new Label("Grid Element 8");
        Label label9 = new Label("Grid Element 9");
        Label label10 = new Label("Grid Element 10");
        Label label11 = new Label("Grid Element 11");
        Label label12 = new Label("Grid Element 12");
        Label label13 = new Label("Grid Element 13");
        Label label14 = new Label("Grid Element 14");
        Label label15 = new Label("Grid Element 15");
        Label label16 = new Label("Grid Element 16");
        Label label17 = new Label("Grid Element 17");
        Label label18 = new Label("Grid Element 18");
        Label label19 = new Label("Grid Element 19");
        Label label20 = new Label("Grid Element 20");
        Label label21 = new Label("Grid Element 21");
        Label label22 = new Label("Grid Element 22");
        Label label23 = new Label("Grid Element 23");
        Label label24 = new Label("Grid Element 24");
        Label label25 = new Label("Grid Element 25");


        // Add the elements to the grid
        gridPane.add(label1, 0, 0);
        gridPane.add(label2, 1, 0);
        gridPane.add(label3, 2, 0);
        gridPane.add(label4, 3, 0);
        gridPane.add(label5, 4, 0);
        gridPane.add(label6, 0, 1);
        gridPane.add(label7, 1, 1);
        gridPane.add(label8, 2, 1);
        gridPane.add(label9, 3, 1);
        gridPane.add(label10, 4, 1);
        gridPane.add(label11, 0, 2);
        gridPane.add(label12, 1, 2);
        gridPane.add(label13, 2, 2);
        gridPane.add(label14, 3, 2);
        gridPane.add(label15, 4, 2);
        gridPane.add(label16, 0, 3);
        gridPane.add(label17, 1, 3);
        gridPane.add(label18, 2, 3);
        gridPane.add(label19, 3, 3);
        gridPane.add(label20, 4, 3);
        gridPane.add(label21, 0, 4);
        gridPane.add(label22, 1, 4);
        gridPane.add(label23, 2, 4);
        gridPane.add(label24, 3, 4);
        gridPane.add(label25, 4, 4);


        // Add the grid to the pane
        this.getChildren().add(gridPane);
    }
}
