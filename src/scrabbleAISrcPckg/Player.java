package scrabbleAISrcPckg;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

abstract class Player {

    private int score;

    private LetterRack letterRack;
    private Set<String> lettersInRack;
    private Map<Move, Integer> playableMoves;

    Player() {
        letterRack = new LetterRack();
        lettersInRack = new HashSet<>();
        playableMoves = new HashMap<>();
        for (LetterContainer lc : letterRack.getLetters()) {
            lettersInRack.add(lc.getText());
        }
    }

    public int getScore() {
        return score;
    }

    boolean letterRackContainsLetter(String letter) {
        return lettersInRack.contains(letter);
    }

    public void setScore(int score) {
        this.score = score;
    }

    LetterRack getLetterRack() {
        return letterRack;
    }

    void dumpLetters() {
        Character[] oldLetters = new Character[7];
        int i = 0;
        for (LetterContainer letterContainer : letterRack.getLetters()) {
            oldLetters[i] = letterContainer.getText().charAt(0);
            letterContainer.setText(LetterBag.getRandomFromBagAsString());
            i++;
        }

        i = 0;
        for (Character letter : oldLetters) {
            LetterBag.addLetter(oldLetters[i]);
            i++;
        }
    }

    void fillLetterRack() {
        for (LetterContainer letterContainer : letterRack.getLetters()) {
            if (letterContainer.getText().equals("")) {
                String letter = LetterBag.getRandomFromBagAsString();
                letterContainer.setText(letter);
                letterContainer.addLetter(letter);
            }
        }
    }

    LetterContainer removeLetterFromRack(String letter) {
        for (LetterContainer lc : letterRack.getLetters()) {
            if (lc.getText().equals(letter)) {
                lc.setText("");
                lettersInRack.remove(letter);
                return lc;
            }
        }

        return null;
    }

    void putLetterInRack(String string, LetterContainer lc) {
        lc.setText(string);
        lettersInRack.add(string);
    }

    void addPlayableMove(Move move, int score) {
        playableMoves.put(move, score);
    }

    Map<Move, Integer> getPlayableMoves() {
        return playableMoves;
    }

    void clearPlayableMoves() {
        playableMoves.clear();
    }
}
