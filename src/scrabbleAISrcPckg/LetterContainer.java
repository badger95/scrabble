package scrabbleAISrcPckg;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public abstract class LetterContainer extends StackPane {

    Text text;
    Rectangle rectangle;
    private int row;
    private int col;


    LetterContainer(String displaySting, final int row, final int col) {
        this.row = row;
        this.col = col;
        text = new Text(displaySting);
        rectangle = new Rectangle();
        rectangle.setHeight(50);
        rectangle.setWidth(50);
        getChildren().addAll(rectangle, text);
    }

    public String getText() {
        return text.getText();
    }

    void setColor(Color c) {
        rectangle.setFill(c);
    }

    void setDisplayText(String text) {
        this.text.setText(text);
    }

    void colorBorder(Color color) {
        rectangle.setStroke(color);
    }

    int[] getCoordinates() {
        return new int[] {row, col};
    }
}
