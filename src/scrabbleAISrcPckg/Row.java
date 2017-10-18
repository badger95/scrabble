package scrabbleAISrcPckg;

class Row {
    char elements[] = new char[15];
    boolean isTransposed;

    Row(char[] elements, boolean isTransposed) {
        this.elements = elements;
        isTransposed = isTransposed;
    }

    boolean isTransposed() {
        return isTransposed;
    }
}
