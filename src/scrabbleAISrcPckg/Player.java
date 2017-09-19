package scrabbleAISrcPckg;

abstract class Player {

    private int score;

    private LetterRack letterRack;

    public Player() {
        letterRack = new LetterRack();
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public LetterRack getLetterRack() {
        return letterRack;
    }

    public void setLetterRack(LetterRack letterRack) {
        this.letterRack = letterRack;
    }

}
