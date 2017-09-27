package scrabbleAISrcPckg;

import javafx.scene.image.WritableImage;
import javafx.scene.input.*;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class LetterContainer extends StackPane {

    private Text text;
    private Rectangle rectangle;
    private Paint originalColor; // for repopulating square's original color
    private String bonusText = ""; // denotes bonus text or star
    private boolean containsLetter = false;
    private int row;
    private int col;

    LetterContainer() {
        new LetterContainer("", -1, -1);
    }

    LetterContainer(String displaySting, final int row, final int col) {
        this.row = row;
        this.col = col;
        text = new Text(displaySting);
        rectangle = new Rectangle();
        rectangle.setHeight(50);
        rectangle.setWidth(50);
        getChildren().addAll(rectangle, text);

        setOnDragOver(event -> {
            event.acceptTransferModes(TransferMode.MOVE);
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
            if (db.hasString() && !containsLetter) {
                addLetter(db.getString());
                event.setDropCompleted(true);
            } else {
                event.setDropCompleted(false);
            }
            event.consume();
        });

        setOnDragDetected(event -> {
            if (containsLetter) {
                Dragboard db = startDragAndDrop(TransferMode.MOVE);
                db.setDragView(snapshot(null, new WritableImage(51, 51)));
                db.setDragViewOffsetX(35);
                db.setDragViewOffsetY(35);
                ClipboardContent content = new ClipboardContent();
                content.putString(text.getText());
                removeLetter();
                db.setContent(content);

                event.consume();
            }
        });

        setOnDragDone(event -> {
            if (event.getTransferMode() == TransferMode.MOVE &&
                    event.getGestureSource() != event.getGestureTarget()) {
                removeLetter();
            } else {
                addLetter(event.getDragboard().getString());
            }

            event.consume();
        });
    }

    private void addLetter(String character) {
        text.setText(character);
        rectangle.setFill(Color.SADDLEBROWN);
        Board.addLetterToRowColOnBoard(character.toCharArray()[0], this);
        setStyle("-fx-font: 12 arial;");
        containsLetter = true;
        Board.printBoard();
    }

    private void removeLetter() {
        text.setText(bonusText);
        Board.clearSpaceOnBoard(this);
        rectangle.setFill(originalColor);
        containsLetter = false;
        if (bonusText.equals("★")) {
            setStyle("-fx-font: 40 arial;");
        }
        Board.printBoard();
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

    void setBonusText(String bonusText) {
        this.bonusText = bonusText;
    }

    void addLetter(Letter letter) {
        getChildren().add(letter);
    }

    public int[] getCoordinates() {
        return new int[] {row, col};
    }

    public String getBonusText() {
        return bonusText;
    }

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }

}
