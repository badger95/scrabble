package scrabbleAISrcPckg;

import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;

public class LetterRack extends GridPane {

    public LetterRack() {
        for (int i = 0; i < 7; i++){
            LetterContainer letterContainer = new LetterContainer();
            letterContainer.addLetter(LetterBag.getRandomFromBag());
            add(letterContainer, i, 0);
        }
        setPadding(new Insets(25,25,25,25));
    }
}
