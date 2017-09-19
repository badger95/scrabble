package scrabbleAISrcPckg;

import javafx.scene.input.*;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class LetterContainer extends StackPane {

    private Text text;
    private Letter letter;
    private Rectangle rectangle;
    private Paint originalColor;
    boolean containsLetter = false;

    public LetterContainer() {
        new LetterContainer("");
    }

    public LetterContainer(String displaySting) {
        text = new Text(displaySting);
        rectangle = new Rectangle();
        rectangle.setHeight(50);
        rectangle.setWidth(50);
        setPrefSize(50, 50);
        getChildren().addAll(rectangle, text);

        setOnDragOver(event -> {
            event.acceptTransferModes(TransferMode.ANY);
            event.consume();
        });

        setOnDragEntered(event -> {
                if (event.getGestureSource() != this &&
                        event.getDragboard().hasString() && !containsLetter) {
                    originalColor = rectangle.getFill();
                    rectangle.setFill(Color.YELLOW);
                }
                event.consume();
        });

        setOnDragExited(event -> {
            if (!containsLetter) {
                rectangle.setFill(originalColor);
            }
            event.consume();
        });

        setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            event.acceptTransferModes(TransferMode.MOVE);
            boolean success = false;
            if (db.hasString() && !containsLetter) {
                text.setText(db.getString());
                rectangle.setFill(Color.SADDLEBROWN);
                success = true;
                containsLetter = true;
                setStyle("-fx-font: 12 arial;"); //required to make star square font size smaller
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    public void setColor(Color c) {
        rectangle.setFill(c);
    }

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }

    public void setDisplayText(String text) {
        this.text.setText(text);
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    public void colorBorder(Color color) {
        rectangle.setStroke(color);
    }

    public Letter getLetter() {
        return letter;
    }

    public void setLetter(Letter letter) {
        this.letter = letter;
        getChildren().add(letter);
    }

}
