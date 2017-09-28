package scrabbleAISrcPckg;

import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

class LetterRack extends GridPane {

    LetterContainer[] letters = new LetterContainer[7];

    LetterRack() {
        for (int i = 0; i < 7; i++){
            String letter = LetterBag.getRandomFromBagAsString();
            LetterContainer newLetterContainer = new LetterContainer("", Color.TAN,0, i);
            newLetterContainer.addLetter(letter);
            add(newLetterContainer, i, 0);
            letters[i] = newLetterContainer;
        }
        setPadding(new Insets(0,15,15,15));
    }
}
