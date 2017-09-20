package scrabbleAISrcPckg;

/** Shared object for turn threads to decide whose turn it is */
public class Mutex {
    /** number corresponds to position in GameManagers list of players */
    private int activePlayerId;
    private int numberOfPlayers;

    public Mutex(int activePlayerId, int numberOfPlayers) {
        this.activePlayerId = activePlayerId;
        this.numberOfPlayers = numberOfPlayers;
    }

    public synchronized void switchTurns(){
        activePlayerId = (activePlayerId + 1) % numberOfPlayers;
        notifyAll();
    }

    public synchronized void waitForTurn(int id) throws InterruptedException{
        while(this.activePlayerId != id) {
            wait();
        }
    }

    public String getWhoseTurnIsIt() {
        return "Player " + activePlayerId + " 's turn.";
    }

}
