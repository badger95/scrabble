package scrabbleAISrcPckg;

import javafx.scene.paint.Color;

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

    private Map<LetterContainer, String> getExistingWordLeft(LetterContainer.Location location) {
        int row = location.getRow();
        int col = location.getCol();
        Map<LetterContainer, String> lcsOfWordToLeftAndChar = new HashMap<>();
        if (location.getCol() != 0) {
            int offset = -1;
            char letterLeft = getLetterInRowByOffSet(location, offset);
            if (letterLeft != ' ') {
                lcsOfWordToLeftAndChar.put(board.getRefToSquareByRowColumn(row, col + offset), String.valueOf(virtualBoard[row][col+offset]));
                while (offset > (-1 * col) && getLetterInRowByOffSet(location, --offset) != ' ') {
                    lcsOfWordToLeftAndChar.put(board.getRefToSquareByRowColumn(row, col + offset), String.valueOf(virtualBoard[row][col+offset]));
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
            if (!wordChecker.startsWith(wordBefore + c.toString() + wordAfter)) {
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

    private LetterContainer getLetterContainerToTheLeft(LetterContainer anchor) {
        return board.getRefToSquareByRowColumn(anchor.getLocation().getRow(), anchor.getLocation().getCol() - 1);
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
        return playableCharsForEachAdjacentSquare.keySet();
    }

    private static void removeNewlyPopulatedFromFringeSet(LetterContainer letterContainer) {
        playableCharsForEachAdjacentSquare.remove(letterContainer);
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

    private static int getWordScore(Move move) {
        int wordScore = 0;
        int numTripleWordBonuses = 0;
        int numDoubleWordBonuses = 0;
        for (TileMove tileMove : move.getTileMoves()) {
            int letterScore = LetterBag.letterScoreMappings.get(tileMove.getCharacter());
            // check move mapping to see if this letter was placed from rack, if so apply bonus
            if (tileMove.getDestination() != tileMove.getSource()) {
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

    private void placeHighestScoringWord(Move highestScoringMove) {

        if (highestScoringMove != null) {
            for (TileMove tileMove : highestScoringMove.getTileMoves()) {
                LetterContainer source = tileMove.getSource();
                LetterContainer target = tileMove.getDestination();
                target.addLetter(tileMove.getCharacter().toString());
                source.removeLetter();
            }
        }
    }

    void doBestPossibleMove(Player player) {
        player.clearPlayableMoves();
        Set<LetterContainer> potentialAnchors = findPotentialAnchors();
        for (LetterContainer anchor : potentialAnchors) {
            int limitToTheLeft = getNumNonAnchorsInRowToLeft(anchor);
            if (limitToTheLeft == 0) {
                handleNoEmptysLeftOfAnchor(player, anchor, limitToTheLeft);
            } else  {
                getLeftPart(anchor, "", wordChecker.getRoot(), limitToTheLeft, player, new Move());
            }
        }

        Move highestScoringMove = null;
        int highScore = 0;
        for (Map.Entry move : player.getPlayableMoves().entrySet()) {
            if ((Integer) move.getValue() > highScore) {
                highScore = (Integer) move.getValue();
                highestScoringMove = (Move) move.getKey();
            }
        }

        placeHighestScoringWord(highestScoringMove);
    }

    private void handleNoEmptysLeftOfAnchor(Player player, LetterContainer anchor, int limitToTheLeft) {
        Move newMove = new Move();
        Map<LetterContainer, String> letterContainersOfWordToLeft = getExistingWordLeft(anchor.getLocation());
        // edge of the board
        if (letterContainersOfWordToLeft == null || letterContainersOfWordToLeft.isEmpty()) {
            getLeftPart(anchor, "", wordChecker.getRoot(), limitToTheLeft, player, newMove);
        }
        // word (or single letter) to the left
        else {
            addTileMovesOfExistingWord(newMove, letterContainersOfWordToLeft.keySet());
            String wordSoFar = getWordLeft(anchor.getLocation());
            WordChecker.TrieNode child = wordChecker.search(wordSoFar);
            getLeftPart(anchor, wordSoFar, child, limitToTheLeft, player, newMove);
        }
    }

    private void addTileMovesOfExistingWord(Move newMove, Set<LetterContainer> letterContainersOfWordToLeft) {
        List<TileMove> wordOnTheBoard = new ArrayList<>();
        for (LetterContainer letterContainer : letterContainersOfWordToLeft) {
            TileMove tileThatIsAlreadyOnBoard = new TileMove(letterContainer, letterContainer, letterContainer.getText().charAt(0));
            wordOnTheBoard.add(tileThatIsAlreadyOnBoard);
        }
        newMove.setTileMoves(wordOnTheBoard);
    }

    private void getLeftPart(LetterContainer anchor, String wordSoFar, WordChecker.TrieNode currentNode, int limit, Player player, Move newMove) {
        extendRight(wordSoFar, currentNode, anchor, player, newMove);
        if (limit > 0) {
            for (int j = 0; j < currentNode.children.length; j++) {
                WordChecker.TrieNode child = currentNode.children[j];
                if (child == null) {
                    continue;
                }
                String characterAtChildNode = String.valueOf(WordChecker.toChar(j));
                placeLetterAndExtendLeft(anchor, wordSoFar, limit, player, child, characterAtChildNode, newMove);
            }
        }
    }

    private void extendRight(String wordSoFar, WordChecker.TrieNode node, LetterContainer currentSquare, Player player, Move newMove) {
        if (currentSquare != null && (currentSquare.getText().equals("") || currentSquare.getText().length() > 1)) {
            extendRightEmptyLeft(wordSoFar, node, currentSquare, player, newMove);
        } else if (currentSquare != null) {
            extendRightLeftOccupied(wordSoFar, node, currentSquare, player, newMove);
        }
    }

    private void extendRightEmptyLeft(String wordSoFar, WordChecker.TrieNode node, LetterContainer currentSquare,
                                      Player player, Move newMove) {
        if (node.endOfWord) {
            newMove.setWord(wordSoFar);
            Move copyOfCurrentPlayableMove = new Move(newMove.getWord(), newMove.getTileMoves()); // copies values from current playable move
            player.addPlayableMove(copyOfCurrentPlayableMove, getWordScore(copyOfCurrentPlayableMove));
        }
        for (int j = 0; j < node.children.length; j++) {
            WordChecker.TrieNode child = node.children[j];
            if (child == null) {
                continue;
            }
            Character characterAtChildNode = WordChecker.toChar(j);
            if (player.letterRackContainsLetter(characterAtChildNode.toString())) {
                placeLetterAndExtendRight(wordSoFar, currentSquare, player, child, characterAtChildNode, newMove);
            }
        }
    }

    private void extendRightLeftOccupied(String wordSoFar, WordChecker.TrieNode node, LetterContainer currentSquare,
                                         Player player, Move newMove) {
        String letter = currentSquare.getText();
        for (int k = 0; k < node.children.length; k++) {
            Character characterAtChildNode = WordChecker.toChar(k);
            WordChecker.TrieNode child = node.children[k];
            if (child == null) {
                continue;
            }
            if (characterAtChildNode == letter.charAt(0)) {
                LetterContainer nextSquare = board.getRefToSquareByRowColumn(currentSquare.getLocation().getRow(),
                        currentSquare.getLocation().getCol() + 1);
                TileMove newTileMove = new TileMove(currentSquare, currentSquare, currentSquare.getText().charAt(0));
                newMove.addTileMove(newTileMove);
                extendRight(wordSoFar + letter, child, nextSquare, player, newMove);
            }
        }
    }

    private void placeLetterAndExtendLeft(LetterContainer anchor, String wordSoFar, int limit, Player player,
                                          WordChecker.TrieNode child, String characterAtChildNode, Move newMove) {
        if (player.letterRackContainsLetter(characterAtChildNode)) {
            LetterContainer removedLettersContainer = player.removeLetterFromRack(characterAtChildNode);
            TileMove newTileMove = new TileMove(removedLettersContainer, anchor, characterAtChildNode.charAt(0));
            newMove.addTileMove(newTileMove);
            getLeftPart(anchor, wordSoFar + characterAtChildNode, child, limit - 1, player, newMove);
            // put letter back
            player.putLetterInRack(characterAtChildNode.toUpperCase(), removedLettersContainer);
            newMove.removeTileMove(newTileMove);
        }
    }

    private void placeLetterAndExtendRight(String wordSoFar, LetterContainer currentSquare, Player player,
                                           WordChecker.TrieNode child, Character characterAtChildNode, Move newMove) {
        Set<Character> restrictedCharacterSet = playableCharsForEachAdjacentSquare.get(currentSquare);
        if (restrictedCharacterSet == null || restrictedCharacterSet.contains(characterAtChildNode)) {
            // save a reference to original location to replace it if necessary
            LetterContainer removedLettersContainer = player.removeLetterFromRack(characterAtChildNode.toString());
            TileMove newTileMove = new TileMove(removedLettersContainer, currentSquare, characterAtChildNode);
            newMove.addTileMove(newTileMove);
            LetterContainer nextSquare = board.getRefToSquareByRowColumn(currentSquare.getLocation().getRow(),
                    currentSquare.getLocation().getCol() + 1);
            extendRight(wordSoFar + characterAtChildNode, child, nextSquare, player, newMove);
            player.putLetterInRack(characterAtChildNode.toString().toUpperCase(), removedLettersContainer);
            newMove.removeTileMove(newTileMove);
        }
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

    void updatePlayableCharsForSquaresAroundWord(Set<LetterContainer> containersOfMove) {
        for (LetterContainer letterOfWord : containersOfMove) {
            Set<LetterContainer> emptyAdjacentSquares = getEmptyAdjacentSquares(letterOfWord);
            for (LetterContainer emptyAdjacentSquare : emptyAdjacentSquares) {
                playableCharsForEachAdjacentSquare.put(emptyAdjacentSquare, restrictPossibleCharacters(emptyAdjacentSquare));
            }
        }

    }

    Set<LetterContainer> getPlayedWord() {
        return newlyPopulatedContainers;
    }
}
