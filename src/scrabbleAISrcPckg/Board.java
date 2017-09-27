package scrabbleAISrcPckg;

import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


class Board extends GridPane {

    static Set<LetterContainer> newlyPopulatedContainers = new HashSet<LetterContainer>();
    static Map<LetterContainer, Boolean> containersWithCommittedLetters = new HashMap<>();
    static char[][] virtualBoard = new char[15][15];

    private static final String TRIPLE_WORD_SCORE = "Triple\nWord\nScore";
    private static final String DOUBLE_LETTER_SCORE = "Double\n Letter\n Score";
    private static final String DOUBLE_WORD_SCORE = "Double\n Word\n Score";
    private static final String TRIPLE_LETTER_SCORE = "Triple\nLetter\nScore";


    Board() {
        buildVirtualBoard();
        for (int row = 0; row < 15; row++) {
            for (int col = 0; col < 15; col++) {
                LetterContainer square = new LetterContainer("", row, col);
                if (row == 0 && col == 0 || row == 0 && col == 7 || row == 0 && col == 14
                        || row == 7 && col == 0 || row == 7 && col == 14 || row == 14 && col == 0
                        || row == 14 && col == 7 || row == 14 && col == 14) {
                    square.setColor(Color.INDIANRED);
                    square.setDisplayText(TRIPLE_WORD_SCORE);
                    square.setBonusText(TRIPLE_WORD_SCORE);
                } else if (row == 0 && col == 3 || row == 0 && col == 11 || row == 2 && col == 6 ||
                        row == 2 && col == 8 || row == 3 && col == 0 || row == 3 && col == 7 ||
                        row == 3 && col == 14 || row == 6 && col == 2 || row == 6 && col == 6||
                        row == 6 && col == 8 || row == 6 && col == 12 || row == 7 && col == 3 ||
                        row == 7 && col == 11 || row == 8 && col == 2 || row == 8 && col == 6 ||
                        row == 8 && col == 8 || row == 8 && col == 12 || row == 11 && col == 0 ||
                        row == 11 && col == 7 || row == 11 && col == 14 || row == 14 && col == 3 ||
                        row == 14 && col == 11) {
                    square.setColor(Color.LIGHTBLUE);
                    square.setDisplayText(DOUBLE_LETTER_SCORE);
                    square.setBonusText(DOUBLE_LETTER_SCORE);
                } else if (row == 1 && col == 1 || row == 13 && col == 1 || row == 2 && col == 2 ||
                        row == 12 && col == 2 || row == 3 && col == 3 || row == 11 && col == 3 ||
                        row == 4 && col == 4 || row == 10 && col == 4 || row == 1 && col == 13 ||
                        row == 2 && col == 12 || row == 3 && col == 11 || row == 4 && col == 10 ||
                        row == 10 && col == 10 || row == 11 && col == 11 || row == 12 && col == 12 ||
                        row == 13 && col == 13) {
                    square.setColor(Color.SALMON);
                    square.setDisplayText(DOUBLE_WORD_SCORE);
                    square.setBonusText(DOUBLE_WORD_SCORE);
                } else if (row == 1 && col == 5 || row == 1 && col == 9 || row == 5 && col == 1 ||
                        row == 5 && col == 5 || row == 5 && col == 9 || row == 5 && col == 13 ||
                        row == 9 && col == 1 || row == 9 && col == 5 || row == 9 && col == 9 ||
                        row == 9 &&  col == 13 || row == 13 && col == 5 || row == 13 && col == 9) {
                    square.setColor(Color.DEEPSKYBLUE);
                    square.setDisplayText(TRIPLE_LETTER_SCORE);
                    square.setBonusText(TRIPLE_LETTER_SCORE);
                } else if (row == 7 && col == 7) {
                    square.setColor(Color.SALMON);
                    square.setDisplayText("★");
                    square.setStyle("-fx-font: 40 arial;");
                    square.setBonusText("★");
                }
                else {
                    square.setColor(Color.TAN);
                }
                square.colorBorder(Color.CHOCOLATE);
                add(square, col, row);
            }
        }
        setPadding(new Insets(0,15,0,15));
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
        int[] coords = letterContainer.getCoordinates();
        int row = coords[0];
        int col = coords[1];
        if (virtualBoard[row][col] == ' ') {
            virtualBoard[row][col] = c;
        }
    }

    static void clearSpaceOnBoard(LetterContainer letterContainer) {
        int[] coords = letterContainer.getCoordinates();
        int row = coords[0];
        int col = coords[1];
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
        System.out.println("-------------------------------------------------------------------------------" +
                "------------------------------------------------------------------------");
        for (int row = 0; row < 15; row++) {
            System.out.print("|    ");
            for (int col = 0; col <15; col++) {
                System.out.print(virtualBoard[row][col] + "    |    ");
            }
            System.out.println();
            System.out.println("-------------------------------------------------------------------------------" +
                    "------------------------------------------------------------------------");
        }
    }

}
