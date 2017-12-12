package scrabbleAISrcPckg;

import java.util.HashMap;
import java.util.Map;

abstract class Player {

    private int score;
    private LetterRack letterRack;
    private Map<Move, Integer> playableMoves;

    Player() {
        letterRack = new LetterRack();
        playableMoves = new HashMap<>();
    }

    boolean letterRackContainsLetter(String letter) {
        for (LetterContainer lc : letterRack.getLetters()) {
            if (lc.getText().equalsIgnoreCase(letter)) {
                return true;
            }
        }

        return false;
    }

    LetterRack getLetterRack() {
        return letterRack;
    }

    void dumpLetters() {
        Character[] oldLetters = new Character[7];
        int i = 0;
        for (LetterContainer letterContainer : letterRack.getLetters()) {
            String letter = letterContainer.getText();
            if (letter.equals("")) {
                return; // bag must be empty
            }
            oldLetters[i] = letter.charAt(0);
            String newLetter = LetterBag.getRandomFromBagAsString();
            if (newLetter != null && !newLetter.equals("")) {
                letterContainer.setText(newLetter);
                i++;
            } else {
                return;
            }
        }

        for (i = 0; i < oldLetters.length; i++) {
            LetterBag.addLetter(oldLetters[i]);
            i++;
        }
    }

    int getScore() {
        return score;
    }

    void updateScore(int score) {
        this.score += score;
    }

    void fillLetterRack() {
        for (LetterContainer letterContainer : letterRack.getLetters()) {
            if (letterContainer.getText().equals("")) {
                String letter = LetterBag.getRandomFromBagAsString();

                if (letter != null && !letter.equals("")) {
                    letterContainer.addLetter(letter);
                }
            }
        }
    }

    LetterContainer removeLetterFromRack(String letter) {
        for (LetterContainer lc : letterRack.getLetters()) {
            if (lc.getText().equalsIgnoreCase(letter)) {
                lc.removeLetter();
                return lc;
            }
        }

        return null;
    }

    void putLetterInRack(String string, LetterContainer lc) {
        lc.addLetter(string);
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
