package scrabbleAISrcPckg;

import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.util.*;


class Board extends GridPane {

    private static Set<LetterContainer> newlyPopulatedContainers = new HashSet<>();
    private static Map<LetterContainer, Boolean> containersWithCommittedLetters = new HashMap<>();
    private static char[][] virtualBoard = new char[15][15];

    private static final String TRIPLE_WORD_SCORE = "Triple\nWord\nScore";
    private static final String DOUBLE_LETTER_SCORE = "Double\n Letter\n Score";
    private static final String DOUBLE_WORD_SCORE = "Double\n Word\n Score";
    private static final String TRIPLE_LETTER_SCORE = "Triple\nLetter\nScore";
    private static final String STAR = "★";


    Board() {
        buildVirtualBoard();
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
                    System.out.print(virtualBoard[row][col] + "    |    ");
                }
                System.out.println();
                System.out.println("-------------------------------------------------------------------------------" +
                        "------------------------------------------------------------------------");
            }
        }
    }

    private static int getMoveScore(List<Word> words , Player player) {
        int moveScore = 0;
        for (Word word : words) {
            moveScore = getWordScore(word);
        }
        moveScore += player.getLetterRack().isEmpty() ? 0 : 50;
        return moveScore;
    }

    private static int getWordScore(Word word) {
        int wordScore = 0;
        int numTripleWordBonuses = 0;
        int numDoubleWordBonuses = 0;
        int i = 0;
        LetterContainer[] containers = word.getContainersOfWord();
        for (char c : word.getWordAsString().toCharArray()) {
            LetterContainer container = containers[i];
            int letterScore = LetterBag.letterScoreMappings.get(c);
            switch (container.getBonusText()) {
                case DOUBLE_WORD_SCORE:
                case STAR:
                    numDoubleWordBonuses++;
                    break;
                case TRIPLE_LETTER_SCORE:
                    letterScore = letterScore * 3;
                    break;
                case DOUBLE_LETTER_SCORE:
                    letterScore = letterScore * 2;
                    break;
                case TRIPLE_WORD_SCORE:
                    numTripleWordBonuses++;
                    break;
            }
            wordScore += letterScore;
            i++;
        }
        wordScore = wordScore * 2 * numDoubleWordBonuses * 3 * numTripleWordBonuses;
        return wordScore;
    }

}
