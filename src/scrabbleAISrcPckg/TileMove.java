package scrabbleAISrcPckg;

class TileMove {

    private boolean isFromRack;
    private LetterContainer destination;
    private Character character;

    TileMove(boolean source, LetterContainer destination, Character character) {
        this.isFromRack = source;
        this.destination = destination;
        this.character = character;
    }

    boolean isFromRack() {
        return isFromRack;
    }

    void setFromRack(boolean fromRack) {
        this.isFromRack = fromRack;
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
