package scrabbleAISrcPckg;

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
//                System.out.println(emptyAdjacentSquare.getLocation().getRow() + "," + emptyAdjacentSquare.getLocation().getCol() + ": ");
//                printRestrictedSet(emptyAdjacentSquare);
//                System.out.println("\n------------------------------------------------------------------");
            }
        }

        return playableCharsForEachAdjacentSquare;
    }

//    private void printRestrictedSet(LetterContainer lc) {
//        Set<Character> chars = playableCharsForEachAdjacentSquare.get(lc);
//        for (Character c : chars) {
//            System.out.print(c + "|");
//        }
//    }

    private Set<Character> restrictPossibleCharacters(LetterContainer emptySquare) {
        LetterContainer.Location currentLocation = emptySquare.getLocation();
        Set<Character> restrictedSet = new HashSet<>();
        buildAlphabetSet(restrictedSet);
        String wordAbove = getWordAbove(currentLocation);
        String wordBelow = getWordBelow(currentLocation);
        String wordRight = getWordRight(currentLocation);
        String wordLeft = getWordLeft(currentLocation);

        restrictedSet = restrictBySurroundingWords(wordAbove, wordBelow, restrictedSet);
        restrictedSet = restrictBySurroundingWords(wordLeft, wordRight, restrictedSet);

        return restrictedSet;
    }

    private String getWordAbove(LetterContainer.Location location) {
        final StringBuilder sb = new StringBuilder();
        if (location.getRow() != 0) {
            int offset = -1;
            char letterAbove = getLetterInColByOffSet(location, offset);
            if (letterAbove != ' ') {
                sb.append(letterAbove);
                while (offset > (-1 * location.getRow())
                        && getLetterInColByOffSet(location, --offset) != ' ') {
                    sb.append(getLetterInColByOffSet(location, offset));
                }
            }
            return sb.toString();
        }
        return null;
    }

    private String getWordBelow(LetterContainer.Location location) {
        final StringBuilder sb = new StringBuilder();
        if (location.getRow() != 14) {
            int offset = 1;
            char letterBelow = getLetterInColByOffSet(location, offset);
            if (letterBelow != ' ') {
                sb.append(letterBelow);
                while (offset < (14 - location.getRow()) && getLetterInColByOffSet(location, ++offset) != ' ') {
                    sb.append(getLetterInColByOffSet(location, offset));
                }
                return sb.toString();
            }
        }
        return null;
    }

    private String getWordRight(LetterContainer.Location location) {
        final StringBuilder sb = new StringBuilder();
        if (location.getCol() != 14) {
            int offset = 1;
            char letterRight = getLetterInRowByOffSet(location, offset);
            if (letterRight != ' ') {
                sb.append(letterRight);
                while (offset < (14 - location.getCol()) && getLetterInRowByOffSet(location, ++offset) != ' ') {
                    sb.append(getLetterInRowByOffSet(location, offset));
                }
                return sb.toString();
            }
        }
        return null;
    }

    private String getWordLeft(LetterContainer.Location location) {
        final StringBuilder sb = new StringBuilder();
        if (location.getCol() != 0) {
            int offset = -1;
            char letterLeft = getLetterInRowByOffSet(location, offset);
            if (letterLeft != ' ') {
                sb.append(letterLeft);
                while (offset > (-1 * location.getCol()) && getLetterInRowByOffSet(location, --offset) != ' ') {
                    sb.append(getLetterInRowByOffSet(location, offset));
                }
                return sb.toString();
            }
        }
        return null;
    }

    private Character getLetterInColByOffSet(LetterContainer.Location location, int offset) {
        return board.getVirtualBoard()[location.getRow() + offset][location.getCol()];
    }

    private Character getLetterInRowByOffSet(LetterContainer.Location location, int offset) {
        return board.getVirtualBoard()[location.getRow()][location.getCol() + offset];
    }

    // words before are to the left or above, words after are under or to the left
    private static Set<Character> restrictBySurroundingWords(String wordBefore, String wordAfter, Set<Character> restrictedSet) {
        if (wordBefore == null) {
            wordBefore = "";
        }
        if (wordAfter == null) {
            wordAfter = "";
        }
        for (Character c : ALPHABET) {
            if (!wordChecker.startsWith(wordBefore + c.toString() + wordAfter)) {
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

    void doBestPossibleMove(Player player) {
        int highestScore = 0;
        Set<LetterContainer> potentialAnchors = findPotentialAnchors();
        for (LetterContainer anchor : potentialAnchors) {
            Set<LetterContainer> lcsOfWordSoFar = new HashSet<>();
            getLeftPart(anchor, "", wordChecker.getRoot(), getNumNonAnchorsInRowToLeft(anchor), player, lcsOfWordSoFar);
        }
    }

    private int getNumNonAnchorsInRowToLeft(LetterContainer anchor) {
        LetterContainer.Location location = anchor.getLocation();
        int row = location.getRow();
        int col = location.getCol();
        int numNonAnchors = 0;
        Set<LetterContainer> anchors = findPotentialAnchors();
        for (int i = 0; i < (-14 + col); i++) {
            if (!anchors.contains(board.getRefToSquareByRowColumn(row, col - i))
                    && board.getRefToSquareByRowColumn(row, col - i).getText().length() != 1) {
                numNonAnchors++;
            }
        }

        return numNonAnchors;
    }

    private Set<LetterContainer> findPotentialAnchors() {
        return playableCharsForEachAdjacentSquare.keySet();
    }

    private void getLeftPart(LetterContainer anchor, String wordSoFar, WordChecker.TrieNode currentNode, int limit,
                             Player player, Set<LetterContainer> lcsOfWordSoFar) {
        extendRight(wordSoFar, currentNode, anchor, player, lcsOfWordSoFar);
        if (limit > 0) {
            for (WordChecker.TrieNode child : currentNode.children) {
                if (player.letterRackContainsLetter(child.toString())) {
                    LetterContainer removedLettersContainer = player.removeLetterFromRack(child.toString());
                    lcsOfWordSoFar.add(removedLettersContainer);
                    getLeftPart(anchor, wordSoFar + child.toString(), child, limit - 1, player, lcsOfWordSoFar);
                    // put letter back
                    player.putLetterInRack(child.toString(), removedLettersContainer);
                    lcsOfWordSoFar.remove(removedLettersContainer);
                }
            }
        }
    }

    private void extendRight(String wordSoFar, WordChecker.TrieNode node, LetterContainer currentSquare, Player player, Set<LetterContainer> lcsOfWordSoFar) {
        if (currentSquare.getText().equals("")) {
            if (node.endOfWord) {
                Word newWord = new Word(lcsOfWordSoFar);
                player.addPlayableMove(newWord, getWordScore(newWord));
            }
            for (WordChecker.TrieNode child : node.children) {
                if (player.letterRackContainsLetter(node.toString())) {
                    Set<Character> playables = playableCharsForEachAdjacentSquare.get(currentSquare);
                    if (playables != null && playables.contains(node.toString().charAt(0))) {
                        // save a reference to original location to replace it if necessary
                        LetterContainer removedLettersContainer = player.removeLetterFromRack(node.toString());
                        lcsOfWordSoFar.add(removedLettersContainer);
                        LetterContainer nextSquare = board.getRefToSquareByRowColumn(currentSquare.getLocation().getRow() + 1,
                                currentSquare.getLocation().getCol());
                        if (nextSquare == null) {
                            break;
                        }
                        extendRight(wordSoFar + node.toString(), child, nextSquare, player, lcsOfWordSoFar);
                        player.putLetterInRack(node.toString(), removedLettersContainer);
                        lcsOfWordSoFar.remove(removedLettersContainer);
                    }
                }
            }
        } else {
            String letter = currentSquare.getText();
            for (WordChecker.TrieNode child : node.children) {
                if (child.toString().equals(letter)) {
                    LetterContainer nextSquare = board.getRefToSquareByRowColumn(currentSquare.getLocation().getRow() + 1,
                            currentSquare.getLocation().getCol());
                    if (nextSquare == null) {
                        break;
                    }
                    extendRight(wordSoFar + letter, child, nextSquare, player, lcsOfWordSoFar);
                }
            }
        }
    }

}
