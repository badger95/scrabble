package scrabbleAISrcPckg;

import java.util.ArrayList;
import java.util.List;

class Move {

    private List<TileMove> tileMoves = new ArrayList<>();

    Move(List<TileMove> tileMoves) {
        List<TileMove> newTileMoves = new ArrayList<>();
        for (TileMove tileMove : tileMoves) {
            TileMove newTileMove = new TileMove(tileMove.isFromRack(), tileMove.getDestination(), tileMove.getCharacter());
            newTileMoves.add(newTileMove);
        }
        this.tileMoves = newTileMoves;
    }

    Move() {
    }

    List<TileMove> getTileMoves() {
        return tileMoves;
    }

    void setTileMoves(List<TileMove> tileMoves) {
        this.tileMoves = tileMoves;
    }
}
