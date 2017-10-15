package scrabbleAISrcPckg;

import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;


class LetterContainer extends StackPane {

    private Text text; // what actually gets displayed
    private Rectangle rectangle;
    private boolean containsLetter = false;
    private Paint originalColor;
    private String bonusText = ""; // denotes bonus text or star
    private Location location;


    LetterContainer(String displaySting, Paint originalColor, final int row, final int col, GridPane parent) {
        location = new Location(col, row, parent);
        text = new Text(displaySting);
        bonusText = displaySting;
        rectangle = new Rectangle();
        rectangle.setStroke(Color.CHOCOLATE);
        rectangle.setFill(originalColor);
        this.originalColor = originalColor;
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
                this.originalColor = rectangle.getFill();
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

        setOnDragDone(event -> {
            if (event.getTransferMode() == TransferMode.MOVE &&
                    event.getGestureSource() != event.getGestureTarget()) {
                removeLetter();
            } else {
                addLetter(event.getDragboard().getString());
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

    }

    void addLetter(String character) {
        text.setText(character);
        rectangle.setFill(Color.SADDLEBROWN);
        setStyle("-fx-font: 12 arial;");
        containsLetter = true;
        if (this.getParent() instanceof Board) {
            Board.addLetterToRowColOnBoard(character.charAt(0), this);
        }
        Board.printBoard();
    }

    void removeLetter() {
        text.setText(bonusText);
        rectangle.setFill(originalColor);
        containsLetter = false;
        if (bonusText.equals("★")) {
            setStyle("-fx-font: 40 arial;");
        }
        if (this.getParent() instanceof Board) {
            Board.clearSpaceOnBoard(this);
        }
        Board.printBoard();
    }

    String getText() {
        return text.getText();
    }

    Location getLocation() {
        return location;
    }

    String getBonusText() {
        return bonusText;
    }

    class Location {

        private int row;
        private int col;
        private GridPane parent;

        Location(int row, int col, GridPane parentContainer){
            this.row = row;
            this.col = col;
            this.parent = parentContainer;
        }

        int getRow() {
            return row;
        }

        int getCol() {
            return col;
        }

        GridPane getParent() {
            return parent;
        }

        boolean equals(Location location) {
            return (location.parent.equals(parent) && row == location.row && col == location.col);
        }
    }
}
