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

    LetterContainer getDestination() {
        return destination;
    }

    Character getCharacter() {
        return character;
    }
}
