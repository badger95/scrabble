package scrabbleAISrcPckg;

abstract class Player {

    private int score;

    private LetterRack letterRack;

    Player() {
        letterRack = new LetterRack();
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    LetterRack getLetterRack() {
        return letterRack;
    }

    public void clearLetterRack() {
        for (Letter letter : letterRack.getL) {

        }
    }

}
