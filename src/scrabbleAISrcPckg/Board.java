package scrabbleAISrcPckg;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;


public class Board extends GridPane {

    public Board() {
        for (int row = 0; row < 15; row++) {
            for (int col = 0; col < 15; col++) {
                LetterContainer square = new LetterContainer("");
                if (row == 0 && col == 0 || row == 0 && col == 7 || row == 0 && col == 14
                        || row == 7 && col == 0 || row == 7 && col == 14 || row == 14 && col == 0
                        || row == 14 && col == 7 || row == 14 && col == 14) {
                    square.setColor(Color.INDIANRED);
                    square.setDisplayText("Triple\nWord\nScore");
                } else if (row == 0 && col == 3 || row == 0 && col == 11 || row == 2 && col == 6 ||
                        row == 2 && col == 8 || row == 3 && col == 0 || row == 3 && col == 7 ||
                        row == 3 && col == 14 || row == 6 && col == 2 || row == 6 && col == 6||
                        row == 6 && col == 8 || row == 6 && col == 12 || row == 7 && col == 3 ||
                        row == 7 && col == 11 || row == 8 && col == 2 || row == 8 && col == 6 ||
                        row == 8 && col == 8 || row == 8 && col == 12 || row == 11 && col == 0 ||
                        row == 11 && col == 7 || row == 11 && col == 14 || row == 14 && col == 3 ||
                        row == 14 && col == 11) {
                    square.setColor(Color.LIGHTBLUE);
                    square.setDisplayText("Double\n Letter\n Score");
                } else if (row == 1 && col == 1 || row == 13 && col == 1 || row == 2 && col == 2 ||
                        row == 12 && col == 2 || row == 3 && col == 3 || row == 11 && col == 3 ||
                        row == 4 && col == 4 || row == 10 && col == 4 || row == 1 && col == 13 ||
                        row == 2 && col == 12 || row == 3 && col == 11 || row == 4 && col == 10 ||
                        row == 10 && col == 10 || row == 11 && col == 11 || row == 12 && col == 12 ||
                        row == 13 && col == 13) {
                    square.setColor(Color.SALMON);
                    square.setDisplayText("Double\n Word\n Score");
                } else if (row == 1 && col == 5 || row == 1 && col == 9 || row == 5 && col == 1 ||
                        row == 5 && col == 5 || row == 5 && col == 9 || row == 5 && col == 13 ||
                        row == 9 && col == 1 || row == 9 && col == 5 || row == 9 && col == 9 ||
                        row == 9 &&  col == 13 || row == 13 && col == 5 || row == 13 && col == 9) {
                    square.setColor(Color.DEEPSKYBLUE);
                    square.setDisplayText("Triple\nLetter\nScore");
                } else if (row == 7 && col == 7) {
                    square.setColor(Color.SALMON);
                    square.setDisplayText("★");
                    square.setStyle("-fx-font: 40 arial;");
                }
                else {
                    square.setColor(Color.TAN);
                }
                square.colorBorder(Color.CHOCOLATE);
                add(square, col, row);
            }
        }
        setPadding(new Insets(25,25,25,25));
    }


}
