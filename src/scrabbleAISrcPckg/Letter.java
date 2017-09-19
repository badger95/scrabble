package scrabbleAISrcPckg;

import javafx.event.EventHandler;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.input.*;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class Letter extends StackPane {
    private int value = 0;
    private char character;
    private Rectangle rectangle;
    private Text text;
    private boolean isCommittedToBoard = false;

    Letter() {
    }

    Letter(int value, char character) {
        this.value = value;
        this.character = character;
        rectangle = new Rectangle(50,50);
        text = new Text(this.toString());
        rectangle.setFill(Color.TAN);
        rectangle.setStroke(Color.CHOCOLATE);
        getChildren().addAll(rectangle,text);

        setOnDragDetected(new EventHandler <MouseEvent>() {
            public void handle(MouseEvent event) {
                Dragboard db = startDragAndDrop(TransferMode.MOVE);
                db.setDragView(snapshot(null, new WritableImage(51,51)));
                db.setDragViewOffsetX(35);
                db.setDragViewOffsetY(35);
                ClipboardContent content = new ClipboardContent();
                content.putString(text.getText());
                db.setContent(content);

                event.consume();
            }
        });

    }

    public int getCharacter() {
        return character;
    }

    public void setCharacter(char character) {
        this.character = character;
    }

    public int getValue() {

        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String toString() {
        return "" + character;
    }
}
