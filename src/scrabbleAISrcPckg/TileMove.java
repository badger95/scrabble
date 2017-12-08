package scrabbleAISrcPckg;

public class TileMove {

    private LetterContainer source;
    private LetterContainer destination;
    private Character character;

    TileMove(LetterContainer source, LetterContainer destination, Character character) {
        this.source = source;
        this.destination = destination;
        this.character = character;
    }

    LetterContainer getSource() {
        return source;
    }

    void setSource(LetterContainer source) {
        this.source = source;
    }

    LetterContainer getDestination() {
        return destination;
    }

    void setDestination(LetterContainer destination) {
        this.destination = destination;
    }

    Character getCharacter() {
        return character;
    }

    void setCharacter(Character character) {
        this.character = character;
    }
}
