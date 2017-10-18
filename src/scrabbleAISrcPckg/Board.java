package scrabbleAISrcPckg;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.util.*;


class Board extends GridPane {

    private static Set<LetterContainer> newlyPopulatedContainers = new HashSet<>();
    private static Map<LetterContainer, Boolean> containersWithCommittedLetters = new HashMap<>();

    private static char[][] virtualBoard = new char[15][15];
    private static List<Row> oneDimensionalBoard = new ArrayList<>();

    static final String TRIPLE_WORD_SCORE = "Triple\nWord\nScore";
    static final String DOUBLE_LETTER_SCORE = "Double\n Letter\n Score";
    static final String DOUBLE_WORD_SCORE = "Double\n Word\n Score";
    static final String TRIPLE_LETTER_SCORE = "Triple\nLetter\nScore";
    static final String STAR = "★";

    Board() {
        buildVirtualBoard();
        buildOneDimensionalCopyOfBoard();
        for (int row = 0; row < 15; row++) {
            for (int col = 0; col < 15; col++) {
                LetterContainer square;
                if (isCheckIfTripleWordScoreCoordinates(row, col)) {
                    square = new LetterContainer(TRIPLE_WORD_SCORE, Color.INDIANRED, row, col, this);
                } else if (checkIfDoubleLetterScoreCoordinates(row, col)) {
                    square = new LetterContainer(DOUBLE_LETTER_SCORE, Color.LIGHTBLUE, row, col, this);
                } else if (checkIfDoubleWordScoreCoordinates(row, col)) {
                    square = new LetterContainer(DOUBLE_WORD_SCORE, Color.SALMON, row, col, this);
                } else if (isCheckIfTripleLetterScoreCoordinates(row, col)) {
                    square = new LetterContainer(TRIPLE_LETTER_SCORE, Color.DEEPSKYBLUE, row, col, this);
                } else if (row == 7 && col == 7) {
                    square = new LetterContainer(STAR, Color.SALMON, row, col, this);
                    square.setStyle("-fx-font: 40 arial;");
                } else {
                    square = new LetterContainer("", Color.TAN, row, col, this);
                }
                add(square, col, row);
            }
        }
        setPadding(new Insets(0,15,0,15));
    }

    private boolean isCheckIfTripleLetterScoreCoordinates(int row, int col) {
        return (row == 1 || row == 5 || row == 9 || row == 13) &&
                (col == 1 || col == 5 || col == 9 || col == 13);
    }

    private boolean checkIfDoubleWordScoreCoordinates(int row, int col) {
        return (row == 1 || row == 2 || row == 3 || row == 4 || row == 10 || row == 11 || row == 12 || row == 13) &&
                (col == row || (col + row == 14));
    }

    private boolean isCheckIfTripleWordScoreCoordinates(int row, int col) {
        return ((row == 0 || row == 14) && (col == 0 || col == 7 || col == 14)) ||
                (row == 7 && (col == 0 || col == 14));
    }

    private boolean checkIfDoubleLetterScoreCoordinates(int row, int col) {
        return (row == 0 && col == 3 || row == 0 && col == 11 || row == 2 && col == 6 ||
                row == 2 && col == 8 || row == 3 && col == 0 || row == 3 && col == 7 ||
                row == 3 && col == 14 || row == 6 && col == 2 || row == 6 && col == 6 ||
                row == 6 && col == 8 || row == 6 && col == 12 || row == 7 && col == 3 ||
                row == 7 && col == 11 || row == 8 && col == 2 || row == 8 && col == 6 ||
                row == 8 && col == 8 || row == 8 && col == 12 || row == 11 && col == 0 ||
                row == 11 && col == 7 || row == 11 && col == 14 || row == 14 && col == 3 ||
                row == 14 && col == 11);
    }

    private void buildVirtualBoard() {
        for (int row = 0; row < 15; row++) {
            for (int col = 0; col < 15; col++) {
                virtualBoard[row][col] = ' ';
            }
        }
    }

    static void addLetterToRowColOnBoard(char c, LetterContainer letterContainer) {
        newlyPopulatedContainers.add(letterContainer);
        containersWithCommittedLetters.put(letterContainer, true);
        int row = letterContainer.getLocation().getRow();
        int col = letterContainer.getLocation().getCol();
        if (virtualBoard[row][col] == ' ') {
            virtualBoard[row][col] = c;
        }
    }

    static void clearSpaceOnBoard(LetterContainer letterContainer) {
        int col = letterContainer.getLocation().getCol();
        int row = letterContainer.getLocation().getRow();
        virtualBoard[row][col] = ' ';
        newlyPopulatedContainers.remove(letterContainer);
    }

    static void commitAllNewlyPopulatedContainers() {
        for (LetterContainer letterContainer : newlyPopulatedContainers) {
            containersWithCommittedLetters.put(letterContainer, true);
            letterContainer.setDisable(true);
        }
    }

    static void printBoard() {
        if (!containersWithCommittedLetters.isEmpty()) {
            System.out.println("-------------------------------------------------------------------------------" +
                    "------------------------------------------------------------------------");
            for (int row = 0; row < 15; row++) {
                System.out.print("|    ");
                for (int col = 0; col < 15; col++) {
                    System.out.print(virtualBoard[col][row] + "    |    ");
                }
                System.out.println();
                System.out.println("-------------------------------------------------------------------------------" +
                        "------------------------------------------------------------------------");
            }
        }
    }

    private static void buildOneDimensionalCopyOfBoard() {
        // copy references to the 15 rows in the virtual board
        for (int i = 0; i < 15; i++) {
            char[] elements = new char[15];
            System.arraycopy(virtualBoard[i], 0, elements, 0, 15);
            Row row = new Row(elements, false);
            oneDimensionalBoard.add(row);
        }
        // copy references to the 15 columns of the virtual board
        for (int i = 0; i < 15; i++) {
            char[] elements = new char[15];
            for (int k = 0; k < 15; k++) {
                elements[k] = virtualBoard[k][i];
            }
            Row row = new Row(elements, true);
            oneDimensionalBoard.add(row);
        }
    }

    private static char[][] transposeBoard(char [][] board){
        char[][] transposed = new char[board[0].length][board.length];
        for (int i = 0; i < board.length; i++)
            for (int j = 0; j < board[0].length; j++)
                transposed[j][i] = board[i][j];
        return transposed;
    }

    static char[][] getVirtualBoard() {
        return virtualBoard;
    }

    static List<Row> getOneDimensionalBoard() {
        return oneDimensionalBoard;
    }

    LetterContainer getRefToSquareByRowColumn(int col, int row) {
        ObservableList<Node> children = getChildren();
        for (Node n : children) {
            if (getColumnIndex(n) == col && getRowIndex(n) == row) {
                return (LetterContainer) n;
            }
        }
        return null;
    }
}
