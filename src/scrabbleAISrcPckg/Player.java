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

}
