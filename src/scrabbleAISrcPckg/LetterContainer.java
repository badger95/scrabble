package scrabbleAISrcPckg;

import javafx.event.EventHandler;
import javafx.scene.image.WritableImage;
import javafx.scene.input.*;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class LetterContainer extends StackPane {

    private String originalValue; // used to repopulate display value on invalid user drag
    private Text text;
    private Letter letter;
    private Rectangle rectangle;
    private Paint originalColor;
    boolean containsLetter = false;
    private String descriptiveText = "";

    public LetterContainer() {
        new LetterContainer("");
    }

    public LetterContainer(String displaySting) {
        text = new Text(displaySting);
        rectangle = new Rectangle();
        rectangle.setHeight(50);
        rectangle.setWidth(50);
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

        setOnDragDetected(event -> {
            if (containsLetter) {
                Dragboard db = startDragAndDrop(TransferMode.MOVE);
                db.setDragView(snapshot(null, new WritableImage(51, 51)));
                db.setDragViewOffsetX(35);
                db.setDragViewOffsetY(35);
                ClipboardContent content = new ClipboardContent();
                originalValue = text.getText();
                content.putString(text.getText());
                rectangle.setFill(originalColor);
                text.setText(descriptiveText);
                if (descriptiveText.equals("★")) {
                    setStyle("-fx-font: 40 arial;");
                }
                db.setContent(content);

                event.consume();
            }
        });

        setOnDragDone(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                if (event.getTransferMode() == TransferMode.MOVE) {
                    text.setText(descriptiveText);
                    rectangle.setFill(originalColor);
                    containsLetter = false;
                    if (descriptiveText.equals("★")) {
                        setStyle("-fx-font: 40 arial;");
                    }
                } else {
                    text.setText(originalValue);
                    rectangle.setFill(Color.SADDLEBROWN);
                    setStyle("-fx-font: 12 arial;");
                }

                event.consume();
            }
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

    public String getDescriptiveText() {
        return descriptiveText;
    }

    public void setDescriptiveText(String descriptiveText) {
        this.descriptiveText = descriptiveText;
    }

    public Letter getLetter() {
        return letter;
    }

    public void setLetter(Letter letter) {
        this.letter = letter;
        getChildren().add(letter);
    }

}
