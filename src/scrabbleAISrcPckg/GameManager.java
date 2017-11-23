package scrabbleAISrcPckg;

import com.sun.prism.paint.Color;

import java.util.*;

import static scrabbleAISrcPckg.Board.*;

public class GameManager {

    static Map<LetterContainer, Boolean> containersWithCommittedLetters = new HashMap<>();
    private final Board board;
    private static final Set<Character> ALPHABET = new HashSet<>();
    static WordChecker wordChecker = new WordChecker();
    private static Map<LetterContainer, Set<Character>> playableCharsForEachAdjacentSquare = new HashMap<>();

    GameManager(Board board) {
        this.board = board;
        buildAlphabetSet(ALPHABET);
    }
    
    private static void buildAlphabetSet(final Set<Character> characters) {
        characters.add('a');
        characters.add('b');
        characters.add('c');
        characters.add('d');
        characters.add('e');
        characters.add('f');
        characters.add('g');
        characters.add('h');
        characters.add('i');
        characters.add('j');
        characters.add('k');
        characters.add('l');
        characters.add('m');
        characters.add('n');
        characters.add('o');
        characters.add('p');
        characters.add('q');
        characters.add('r');
        characters.add('s');
        characters.add('t');
        characters.add('u');
        characters.add('v');
        characters.add('w');
        characters.add('x');
        characters.add('y');
        characters.add('z');
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

    // Naively search all four squares adjacent to each newly committed tile for its neighbors, ignores diagonals
    private Set<LetterContainer> getEmptyAdjacentSquares(LetterContainer letterContainer) {
        Set<LetterContainer> adjacentSquares = new HashSet<>();
        int row = letterContainer.getLocation().getRow();
        int col = letterContainer.getLocation().getCol();
        int oneRowDown = row < 14 ? row + 1 : -1;
        int oneRowUp = row > 0 ? row - 1 : -1;
        int oneColRight = col < 14 ? col + 1 : -1;
        int oneColLeft = col > 0 ? col - 1 : -1;
        // clockwise bounds check:
        // ^
        if (oneRowUp != -1 && !board.getRefToSquareByRowColumn(oneRowUp, col).containsLetter) {
            adjacentSquares.add(board.getRefToSquareByRowColumn(oneRowUp, col));
        }
        // ->
        if (oneColRight != -1 && !board.getRefToSquareByRowColumn(row, oneColRight).containsLetter) {
            adjacentSquares.add(board.getRefToSquareByRowColumn(row, oneColRight));
        }
        // down
        if (oneRowDown != -1 && !board.getRefToSquareByRowColumn(oneRowDown, col).containsLetter) {
            adjacentSquares.add(board.getRefToSquareByRowColumn(oneRowDown, col));
        }
        // <-
        if (oneColLeft != -1 && !board.getRefToSquareByRowColumn(row, oneColLeft).containsLetter) {
            adjacentSquares.add(board.getRefToSquareByRowColumn(row, oneColLeft));
        }

        return adjacentSquares;
    }

    Map<LetterContainer, Set<Character>> updatePlayableCharsForSquaresAroundWord(Word word) {
        LetterContainer[] containersOfWord = word.getContainersOfWord();
        for (LetterContainer letterOfWord : containersOfWord) {
            Set<LetterContainer> emptyAdjacentSquares = getEmptyAdjacentSquares(letterOfWord);
            for (LetterContainer emptyAdjacentSquare : emptyAdjacentSquares) {
                playableCharsForEachAdjacentSquare.put(emptyAdjacentSquare, restrictPossibleCharacters(emptyAdjacentSquare));
                System.out.println(emptyAdjacentSquare.getLocation().getRow()+","+emptyAdjacentSquare.getLocation().getCol()+": ");
                printRestrictedSet(emptyAdjacentSquare);
                System.out.println("------------------------------------------------------------------");
            }
        }

        return playableCharsForEachAdjacentSquare;
    }

    private void printRestrictedSet(LetterContainer lc) {
        Set<Character> chars = playableCharsForEachAdjacentSquare.get(lc);
        for (Character c : chars) {
            System.out.print(c+"|");
        }
    }

    private Set<Character> restrictPossibleCharacters(LetterContainer emptySquare) {
        LetterContainer.Location currentLocation = emptySquare.getLocation();
        Set<Character> restrictedSet = new HashSet<>();
        buildAlphabetSet(restrictedSet);
        String wordAbove = getWordAbove(currentLocation);
        String wordBelow = getWordBelow(currentLocation);

        if (wordAbove != null && !wordAbove.equals("")) {
            restrictedSet = restrictByWordAbove(wordAbove, restrictedSet);
        }
        if (wordBelow != null && !wordBelow.equals("")) {
            restrictedSet = restrictByWordBelow(wordBelow, restrictedSet);
        }
        
        return restrictedSet;
    }

    private String getWordAbove(LetterContainer.Location location) {
        StringBuilder sb = new StringBuilder();
        if (location.getRow() != 0) {
            int offset = -1;
            char letterAbove = getLetterInColByOffSet(location, offset);
            if (letterAbove != ' ') {
                sb.append(letterAbove);
            }
            while (getLetterInColByOffSet(location, --offset) != ' ') {
                sb.append(getLetterInColByOffSet(location, offset));
            }
            return sb.toString();
        }
        return null;
    }

    private String getWordBelow(LetterContainer.Location location) {
        StringBuilder sb = new StringBuilder();
        if (location.getRow() != 14) {
            int offset = 1;
            char letterBelow = getLetterInColByOffSet(location, offset);
            if (letterBelow != ' ') {
                sb.append(letterBelow);
            }
            while (getLetterInColByOffSet(location, ++offset) != ' ') {
                sb.append(getLetterInColByOffSet(location, offset));
            }
            return sb.toString();
        }
        return null;
    }

    private Character getLetterInColByOffSet(LetterContainer.Location location, int offset) {
       return board.getVirtualBoard()[location.getRow() + offset][location.getCol()];
    }

    private static Set<Character> restrictByWordAbove(String wordAbove, Set<Character> restrictedSet) {
        for (Character c : ALPHABET) {
            if (!wordChecker.startsWith(wordAbove + c.toString())) {
                restrictedSet.remove(c);
            }
        }
        return restrictedSet;
    }

    private static Set<Character> restrictByWordBelow(String wordBelow, Set<Character> restrictedSet) {
        for (Character c : ALPHABET) {
            if (!wordChecker.startsWith(c.toString() + wordBelow)) {
                restrictedSet.remove(c);
            }
        }
        return restrictedSet;
    }

   private static void removeNewlyPopulatedFromFringeSet(LetterContainer letterContainer) {
        playableCharsForEachAdjacentSquare.remove(letterContainer);
    }

    static void addLetterToRowColOnBoard(char c, LetterContainer letterContainer) {
        newlyPopulatedContainers.add(letterContainer);
        GameManager.containersWithCommittedLetters.put(letterContainer, true);
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
            GameManager.containersWithCommittedLetters.put(letterContainer, true);
            letterContainer.setDisable(true);
            removeNewlyPopulatedFromFringeSet(letterContainer);
        }
    }
}
