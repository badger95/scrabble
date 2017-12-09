package scrabbleAISrcPckg;

import java.util.*;

import static scrabbleAISrcPckg.Board.*;

public class GameManager {

    static Map<LetterContainer, Boolean> containersWithCommittedLetters = new HashMap<>();
    private final Board board;
    private static final Set<Character> ALPHABET = new HashSet<>();
    static WordChecker wordChecker = new WordChecker();
    private static Map<LetterContainer, Set<Character>> acrossPlaysForEmptySquares = new HashMap<>();
    private static Map<LetterContainer, Set<Character>> downPlaysForEmptySquares = new HashMap<>();

    GameManager(Board board) {
        this.board = board;
        buildAlphabetSet(ALPHABET);
    }

    // Naively search all four squares adjacent to each newly committed tile for its neighbors, ignores diagonals
    private Set<LetterContainer> getEmptyAdjacentSquares(LetterContainer emptySquare) {
        Set<LetterContainer> adjacentSquares = new HashSet<>();
        int row = emptySquare.getLocation().getRow();
        int col = emptySquare.getLocation().getCol();
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

    private void restrictPossibleCharacters(LetterContainer emptySquare) {
        LetterContainer.Location currentLocation = emptySquare.getLocation();
        Set<Character> restrictedAcrossSet = new HashSet<>();
        buildAlphabetSet(restrictedAcrossSet);
        Set<Character> restrictedDownSet = new HashSet<>();
        buildAlphabetSet(restrictedDownSet);
        String wordAbove = getWordAbove(currentLocation);
        String wordBelow = getWordBelow(currentLocation);
        String wordRight = getWordRight(currentLocation);
        String wordLeft = getWordLeft(currentLocation);

        restrictedAcrossSet = restrictBySurroundingWords(wordAbove, wordBelow, restrictedAcrossSet);
        restrictedDownSet = restrictBySurroundingWords(wordLeft, wordRight, restrictedDownSet);

        acrossPlaysForEmptySquares.put(emptySquare, restrictedAcrossSet);
        downPlaysForEmptySquares.put(emptySquare, restrictedDownSet);
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

    private List<LetterContainer> getExistingWordLeft(LetterContainer.Location location) {
        int row = location.getRow();
        int col = location.getCol();
        List<LetterContainer> lcsOfWordToLeftAndChar = new ArrayList<>();
        if (location.getCol() != 0) {
            int offset = -1;
            char letterLeft = getLetterInRowByOffSet(location, offset);
            if (letterLeft != ' ') {
                lcsOfWordToLeftAndChar.add(board.getRefToSquareByRowColumn(row, col + offset));
                while (offset > (-1 * col) && getLetterInRowByOffSet(location, --offset) != ' ') {
                    lcsOfWordToLeftAndChar.add(board.getRefToSquareByRowColumn(row, col + offset));
                }
            }
            return lcsOfWordToLeftAndChar;
        }
        return null; // edge of board
    }

    private Character getLetterInColByOffSet(LetterContainer.Location location, int offset) {
        return board.getVirtualBoard()[location.getRow() + offset][location.getCol()];
    }

    private Character getLetterInRowByOffSet(LetterContainer.Location location, int offset) {
        return board.getVirtualBoard()[location.getRow()][location.getCol() + offset];
    }

    private static Set<Character> restrictBySurroundingWords(String wordBefore, String wordAfter, Set<Character> restrictedSet) {
        if (wordBefore == null) {
            wordBefore = "";
        }

        if (wordAfter == null) {
            wordAfter = "";
        }

        for (Character c : ALPHABET) {
            WordChecker.TrieNode result = wordChecker.search(wordBefore + c.toString() + wordAfter);
            if (null == result || !result.endOfWord ) {
                restrictedSet.remove(c);
            }
        }

        return restrictedSet;
    }

    private int getChildIndexForLetter(String letter) {
        if (letter.equalsIgnoreCase("A")) {
            return 0;
        } else if (letter.equalsIgnoreCase("B")) {
            return 1;
        } else if (letter.equalsIgnoreCase("C")) {
            return 2;
        } else if (letter.equalsIgnoreCase("D")) {
            return 3;
        } else if (letter.equalsIgnoreCase("E")) {
            return 4;
        } else if (letter.equalsIgnoreCase("F")) {
            return 5;
        } else if (letter.equalsIgnoreCase("G")) {
            return 6;
        } else if (letter.equalsIgnoreCase("H")) {
            return 7;
        } else if (letter.equalsIgnoreCase("I")) {
            return 8;
        } else if (letter.equalsIgnoreCase("j")) {
            return 9;
        } else if (letter.equalsIgnoreCase("k")) {
            return 10;
        } else if (letter.equalsIgnoreCase("l")) {
            return 11;
        } else if (letter.equalsIgnoreCase("m")) {
            return 12;
        } else if (letter.equalsIgnoreCase("n")) {
            return 13;
        } else if (letter.equalsIgnoreCase("o")) {
            return 14;
        } else if (letter.equalsIgnoreCase("p")) {
            return 15;
        } else if (letter.equalsIgnoreCase("q")) {
            return 16;
        } else if (letter.equalsIgnoreCase("r")) {
            return 17;
        } else if (letter.equalsIgnoreCase("s")) {
            return 18;
        } else if (letter.equalsIgnoreCase("t")) {
            return 19;
        } else if (letter.equalsIgnoreCase("u")) {
            return 20;
        } else if (letter.equalsIgnoreCase("v")) {
            return 21;
        } else if (letter.equalsIgnoreCase("w")) {
            return 22;
        } else if (letter.equalsIgnoreCase("x")) {
            return 23;
        } else if (letter.equalsIgnoreCase("y")) {
            return 24;
        } else if (letter.equalsIgnoreCase("z")) {
            return 25;
        }

        return -1;
    }

    private int getNumNonAnchorsInRowToLeft(LetterContainer anchor) {
        LetterContainer.Location location = anchor.getLocation();
        Map<Integer, Set<LetterContainer>> anchorsInEachRow = getAnchorsInEachRow();
        int row = location.getRow();
        int col = location.getCol();
        if (col == 0) {
            return 0;
        }

        Set<LetterContainer> anchorsInSameRow = anchorsInEachRow.get(row);
        if (anchorsInSameRow == null) {
            return 0;
        }
        int max = 0;
        for (LetterContainer a : anchorsInSameRow) {
            if (a.getLocation().getCol() < col && a.getLocation().getCol() > max) {
                max = a.getLocation().getCol();
            }
        }

        if (max == 0) {
            return col;
        }
        return col - max - 1;
    }

    private Map<Integer, Set<LetterContainer>> getAnchorsInEachRow() {
        Set<LetterContainer> anchors = findPotentialAnchors();
        Map<Integer, Set<LetterContainer>> anchorsInEachRow = new HashMap<>();
        for (int k = 0; k < 14; k++) {
            HashSet<LetterContainer> anchorsInThisRow = new HashSet<>();
            for (LetterContainer a : anchors) {
                if (a.getLocation().getRow() == k) {
                    anchorsInThisRow.add(a);
                }
            }
            anchorsInEachRow.put(k, anchorsInThisRow);
        }
        return anchorsInEachRow;
    }

    private Set<LetterContainer> findPotentialAnchors() {
        return acrossPlaysForEmptySquares.keySet();
    }

    private static void removeNewlyPopulatedFromFringeSet(LetterContainer letterContainer) {
        acrossPlaysForEmptySquares.remove(letterContainer);
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

    private static int getMoveScore(Move move) {
        int wordScore = 0;
        int numTripleWordBonuses = 0;
        int numDoubleWordBonuses = 0;
        for (TileMove tileMove : move.getTileMoves()) {
            int letterScore = LetterBag.letterScoreMappings.get(tileMove.getCharacter());
            // check move mapping to see if this letter was placed from rack, if so apply bonus
            if (tileMove.isFromRack()) {
                switch (tileMove.getDestination().getBonusText()) {
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
            }
            wordScore += letterScore;
        }
        wordScore = wordScore * ((numDoubleWordBonuses != 0) ? 2 * numDoubleWordBonuses : 1) * ((numTripleWordBonuses != 0) ? 3 * numTripleWordBonuses : 1);
        return wordScore;
    }

    private void placeHighestScoringWord(Move highestScoringMove, Player player) {
        if (highestScoringMove != null) {
            for (TileMove tileMove : highestScoringMove.getTileMoves()) {
                LetterContainer target = tileMove.getDestination();
                target.addLetter(tileMove.getCharacter().toString());
                if (tileMove.isFromRack()) {
                    player.removeLetterFromRack(tileMove.getCharacter().toString());
                }
            }
        }
    }

    void doBestPossibleMove(Player player) {
        player.clearPlayableMoves();
        Set<LetterContainer> potentialAnchors = findPotentialAnchors();
        for (LetterContainer anchor : potentialAnchors) {
            findAllPossibleMoves(player, anchor);
        }

        Move highestScoringMove = null;
        int highScore = 0;
        for (Map.Entry move : player.getPlayableMoves().entrySet()) {
            if ((Integer) move.getValue() > highScore) {
                highScore = (Integer) move.getValue();
                highestScoringMove = (Move) move.getKey();
            }
        }

        placeHighestScoringWord(highestScoringMove, player);
    }

    private void findAllPossibleMoves(Player player, LetterContainer anchor) {
        Move newMove = new Move(); //copied later to save each playable moves
        // first check for a word to the left
        String wordLeft = getWordLeft(anchor.getLocation());
        List<LetterContainer> letterContainersOfWordToLeft = getExistingWordLeft(anchor.getLocation());
        if (wordLeft != null && letterContainersOfWordToLeft != null) {
            addTileMovesOfExistingWord(newMove, letterContainersOfWordToLeft);
            extendRight(wordLeft, wordChecker.search(wordLeft), anchor, player);
        }
        // find all left parts
        else {
            int limitToTheLeft = getNumNonAnchorsInRowToLeft(anchor);
            getLeftPart(anchor, "", wordChecker.getRoot(), limitToTheLeft, player);
        }
    }


    private void addTileMovesOfExistingWord(Move newMove, List<LetterContainer> letterContainersOfWordToLeft) {
        List<TileMove> wordOnTheBoard = new ArrayList<>();
        int size = letterContainersOfWordToLeft.size();
        for (int k = size - 1; k >= 0; k--) {
            LetterContainer letterContainer = letterContainersOfWordToLeft.get(k);
            TileMove tileThatIsAlreadyOnBoard = new TileMove(false, letterContainer, letterContainer.getText().charAt(0));
            wordOnTheBoard.add(tileThatIsAlreadyOnBoard);
        }
        newMove.setTileMoves(wordOnTheBoard);
    }

    private void getLeftPart(LetterContainer anchor, String wordSoFar, WordChecker.TrieNode currentNode, int limit, Player player) {
        extendRight(wordSoFar, currentNode, anchor, player);
        if (limit > 0) {
            for (int j = 0; j < currentNode.children.length; j++) {
                WordChecker.TrieNode child = currentNode.children[j];
                if (child == null) {
                    continue;
                }
                Character characterAtChildNode = WordChecker.toChar(j);
                if (player.letterRackContainsLetter(characterAtChildNode.toString())) {
                    LetterContainer removedLettersContainer = player.removeLetterFromRack(characterAtChildNode.toString());
                    getLeftPart(anchor, wordSoFar + characterAtChildNode, child, limit - 1, player);
                    player.putLetterInRack(characterAtChildNode.toString().toUpperCase(), removedLettersContainer);
                }
            }
        }
    }

    private void extendRight(String wordSoFar, WordChecker.TrieNode node, LetterContainer currentSquare, Player player) {
        if (currentSquare != null && !currentSquare.containsLetter) {
            if (node.endOfWord) {
                recordLegalMove(wordSoFar, player, currentSquare);
            }

            for (int j = 0; j < node.children.length; j++) {
                WordChecker.TrieNode child = node.children[j];
                if (child == null) {
                    continue;
                }

                Character characterAtChildNode = WordChecker.toChar(j);
                if (player.letterRackContainsLetter(characterAtChildNode.toString()) &&
                        acrossPlaysForEmptySquares.get(currentSquare) != null &&
                        acrossPlaysForEmptySquares.get(currentSquare).contains(characterAtChildNode)) {
                    LetterContainer removedLettersContainer = player.removeLetterFromRack(characterAtChildNode.toString());
                    LetterContainer nextSquare = board.getRefToSquareByRowColumn(currentSquare.getLocation().getRow(),
                            currentSquare.getLocation().getCol() + 1);
                    extendRight(wordSoFar + characterAtChildNode, child, nextSquare, player);
                    player.putLetterInRack(characterAtChildNode.toString().toUpperCase(), removedLettersContainer);
                }
            }
        } else if (currentSquare != null) {
            extendRightCurrentOccupied(wordSoFar, node, currentSquare, player);
        }
    }

    private void recordLegalMove(String word, Player player, LetterContainer lastContainerOfWord) {
        Move move = new Move();
        List<TileMove> tileMoves = new ArrayList<>();
        int row = lastContainerOfWord.getLocation().getRow();
        int col = lastContainerOfWord.getLocation().getCol();
        for (int i = 0; i < word.length(); i++) {
            LetterContainer currentLetterInWord = board.getRefToSquareByRowColumn(row,col - i);
            boolean alreadyOnBoard = board.getRefToSquareByRowColumn(row,col - i).containsLetter;
            TileMove tileMove;
            if (alreadyOnBoard) {
                tileMove = new TileMove(false, currentLetterInWord, word.charAt(word.length() - 1 - i));
            } else {
                tileMove = new TileMove(true, currentLetterInWord, word.charAt(word.length() - 1 - i));
            }
            tileMoves.add(tileMove);
        }
        move.addTileMoves(tileMoves);
        move.setWord(word);
        player.addPlayableMove(move, getMoveScore(move));
    }

    private void extendRightCurrentOccupied(String wordSoFar, WordChecker.TrieNode node, LetterContainer currentSquare,
                                            Player player) {
        String letter = currentSquare.getText();
        if (node.children[getChildIndexForLetter(letter)] != null) {
            WordChecker.TrieNode child = node.children[getChildIndexForLetter(letter)];
            LetterContainer nextSquare = board.getRefToSquareByRowColumn(currentSquare.getLocation().getRow(),
                    currentSquare.getLocation().getCol() + 1);
            extendRight(wordSoFar + letter, child, nextSquare, player);
        }
    }


/////////// PUBLIC METHODS /////////////////////////////////////////////////////////////////////////////////////////////

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

    void updatePlayableCharsForSquaresAroundWord(Set<LetterContainer> containersOfMove) {
        for (LetterContainer letterOfWord : containersOfMove) {
            Set<LetterContainer> emptyAdjacentSquares = getEmptyAdjacentSquares(letterOfWord);
            for (LetterContainer emptyAdjacentSquare : emptyAdjacentSquares) {
                restrictPossibleCharacters(emptyAdjacentSquare);
            }
        }

    }

    Set<LetterContainer> getPlayedWord() {
        return newlyPopulatedContainers;
    }
}
