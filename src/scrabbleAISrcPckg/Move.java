package scrabbleAISrcPckg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Move {

    private String word;
    private List<TileMove> tileMoves = new ArrayList<>();

    Move(String word, List<TileMove> tileMoves) {
        List<TileMove> newTileMoves = new ArrayList<>();
        for (TileMove tileMove : tileMoves) {
            TileMove newTileMove = new TileMove(tileMove.isFromRack(), tileMove.getDestination(), tileMove.getCharacter());
            newTileMoves.add(newTileMove);
        }
        this.tileMoves = newTileMoves;
        this.word = word;
    }

    Move() {
    }


    void addTileMoves(List<TileMove> tileMovesToBeAdded) {
        tileMoves.addAll(tileMovesToBeAdded);
    }

    List<TileMove> getTileMoves() {
        return tileMoves;
    }

    void setTileMoves(List<TileMove> tileMoves) {
        this.tileMoves = tileMoves;
    }

    void setWord(String word) {
        this.word = word;
    }

    String getWord() {
        return word;
    }

    void addTileMove(TileMove newTileMove) {
        tileMoves.add(newTileMove);
    }

    boolean removeTileMove(TileMove newTileMove) {
        return tileMoves.remove(newTileMove);
    }

    Map<Boolean, LetterContainer> getTileMoveMapping() {
        Map<Boolean, LetterContainer> moveMapping = new HashMap<>();
        for (TileMove tileMove : tileMoves) {
            moveMapping.put(tileMove.isFromRack(), tileMove.getDestination());
        }

        return moveMapping;
    }
}
