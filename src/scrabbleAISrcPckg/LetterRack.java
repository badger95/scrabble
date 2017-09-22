package scrabbleAISrcPckg;

import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;

class LetterRack extends GridPane {

    LetterRack() {
        for (int i = 0; i < 7; i++){
            LetterContainer letterContainer = new LetterContainer();
            letterContainer.addLetter(LetterBag.getRandomFromBag());
            add(letterContainer, i, 0);
        }
        setPadding(new Insets(0,15,15,15));
    }
}
