package scrabbleAISrcPckg;

import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;

class LetterRack extends GridPane {

    Letter[] letters = new Letter[7];

    LetterRack() {
        for (int i = 0; i < 7; i++){
            LetterContainer letterContainer = new LetterContainer();
            Letter newLetter = LetterBag.getRandomFromBag();
            letterContainer.addLetter(newLetter);
            add(letterContainer, i, 0);
            letters[i] = newLetter;
        }
        setPadding(new Insets(0,15,15,15));
    }
}
