package scrabbleAISrcPckg;

import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;

class LetterRack extends GridPane {

    RackLetterContainer[] letters = new RackLetterContainer[7];

    LetterRack() {
        for (int i = 0; i < 7; i++){
            RackLetterContainer newLetter = new RackLetterContainer(LetterBag.getRandomFromBagAsString(), 0, i);
            add(newLetter, i, 0);
            letters[i] = newLetter;
        }
        setPadding(new Insets(0,15,15,15));
    }
}
