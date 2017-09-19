package scrabbleAISrcPckg;

import javafx.event.EventHandler;
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
    private String originalValue;

    Letter() {
    }

    Letter(int value, char character) {
        this.value = value;
        this.character = character;
        rectangle = new Rectangle(50,50);
        text = new Text(this.toString());
        rectangle.setFill(Color.SADDLEBROWN);
        rectangle.setStroke(Color.CHOCOLATE);
        getChildren().addAll(rectangle,text);

        setOnDragDetected(event -> {
            Dragboard db = startDragAndDrop(TransferMode.MOVE);
            db.setDragView(snapshot(null, new WritableImage(51,51)));
            db.setDragViewOffsetX(35);
            db.setDragViewOffsetY(35);
            ClipboardContent content = new ClipboardContent();
            content.putString(text.getText());
            originalValue = text.getText();
            text.setText("");
            db.setContent(content);

            event.consume();
        });

        setOnDragDone(new EventHandler <DragEvent>() {
            public void handle(DragEvent event) {
                if (event.getTransferMode() == TransferMode.MOVE) {
                    text.setText("");
                } else {
                    text.setText(originalValue);
                }

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
