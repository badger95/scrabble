package scrabbleAISrcPckg;

import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;

public class LetterRack extends GridPane {
    private Letter[] letters;

    public LetterRack() {
        letters = new Letter[7];
        for (int i = 0; i < 7; i++){
            letters[i] = LetterBag.getRandomFromBag();
            LetterContainer letterContainer = new LetterContainer();
            letterContainer.setLetter(letters[i]);
            add(letterContainer, i, 0);
        }
        setPadding(new Insets(25,25,25,25));
    }

    public Letter[] getLetters() {
        return letters;
    }

    public void setLetters(Letter[] letters) {
        this.letters = letters;
    }
}
