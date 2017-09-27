package scrabbleAISrcPckg;

import javafx.scene.image.WritableImage;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class BoardLetterContainer extends LetterContainer {

    private Paint originalColor; // for repopulating square's original color
    private String bonusText = ""; // denotes bonus text or star
    private boolean containsLetter = false;

    BoardLetterContainer() {
        super("", -1, -1);
    }

    BoardLetterContainer(String displaySting, final int row, final int col) {
        super(displaySting, row, col);
        setColor(Color.TAN);
        colorBorder(Color.CHOCOLATE);
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


    void addLetter(RackLetterContainer letter) {
        getChildren().add(letter);
    }

    void setBonusText(String bonusText) {
        this.bonusText = bonusText;
    }

    public String getBonusText() {
        return bonusText;
    }

}
