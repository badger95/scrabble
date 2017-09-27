package scrabbleAISrcPckg;

/** Shared object for turn threads to decide whose turn it is */
class Mutex {
    /** number corresponds to position in GameManagers list of players */
    private int activePlayerId;
    private int numberOfPlayers;

    Mutex(int activePlayerId, int numberOfPlayers) {
        this.activePlayerId = activePlayerId;
        this.numberOfPlayers = numberOfPlayers;
    }

    synchronized void switchTurns(){
        Board.commitAllNewlyPopulatedContainers();
        activePlayerId = (activePlayerId + 1) % numberOfPlayers;
        notifyAll();
    }

    synchronized void waitForTurn(int id, Player player) throws InterruptedException{
        while(this.activePlayerId != id) {
            player.getLetterRack().setDisable(true);
            wait();
        }

    }

    String getWhoseTurnIsIt() {
        return "Player " + activePlayerId + " 's turn.";
    }

}
