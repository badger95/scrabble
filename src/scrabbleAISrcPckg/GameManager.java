package scrabbleAISrcPckg;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static scrabbleAISrcPckg.Board.*;

public class GameManager {

    private final Board board; //reference to board

    GameManager(Board board) {
        this.board = board;
    }

    private static int getMoveScore(List<Word> words, Player player) {
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

    // Naively search all four squares adjacent to each newly committed tile
    Set<LetterContainer> findEmptyAdjacentSquares(Word newlyPlacedWord) {
        LetterContainer[] letterContainersOfWord = newlyPlacedWord.getContainersOfWord();
        Set<LetterContainer> emptyAdjacentSquares = new HashSet<>();
        for (LetterContainer letterContainer : letterContainersOfWord) {
            int row = letterContainer.getLocation().getRow();
            int col = letterContainer.getLocation().getCol();
            int oneRowDown = row < 14 ? row + 1 : -1;
            int oneRowUp = row > 0 ? row - 1 : -1;
            int oneColRight = col < 14 ? col + 1 : -1;
            int oneColLeft = col > 0 ? col - 1 : -1;
            // clockwise check here ^ -> down <-
            if (oneRowUp != -1 && checkSquareIsEmpty(oneRowUp, col)) {
                LetterContainer emptyLC = board.getRefToSquareByRowColumn(oneRowUp, col);
                emptyAdjacentSquares.add(emptyLC);
            }
            if (oneColRight != -1 && checkSquareIsEmpty(row, oneColRight)) {
                LetterContainer emptyLC = board.getRefToSquareByRowColumn(row, oneColRight);
                emptyAdjacentSquares.add(emptyLC);
            }
            if (oneRowDown != -1 && checkSquareIsEmpty(oneRowDown, col)) {
                LetterContainer emptyLC = board.getRefToSquareByRowColumn(oneRowDown, col);
                emptyAdjacentSquares.add(emptyLC);
            }
            if (oneColLeft != -1 && checkSquareIsEmpty(row, oneColLeft)) {
                LetterContainer emptyLC = board.getRefToSquareByRowColumn(row, oneColLeft);
                emptyAdjacentSquares.add(emptyLC);
            }
        }

        return emptyAdjacentSquares;
    }

    private boolean checkSquareIsEmpty(int row, int col) {
        return getVirtualBoard()[row][col] == ' ';
    }

//    Map<LetterContainer, Set<Character>> updatePlaybleCharsForCrossCheckedSqaures(Word word) {
//        Set<LetterContainer> letterContainers = findEmptyAdjacentSquares(word);
//        for (LetterContainer letterContainer : letterContainers) {
//
//        }
//    }


//    static boolean validateBoard() {
//        for (Row row : Board.getOneDimensionalBoard()) {
//            if (!validateRow(Row row)) {
//                return false;
//            }
//        }
//
//        return true;
//    }
//
//    private static boolean validateRow(Row row) {
//
//    }
}
