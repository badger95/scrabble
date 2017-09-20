package scrabbleAISrcPckg;

public abstract class Turn implements Runnable {

    private final int turnId;
    private final Mutex mutex;

    public Turn(Mutex mutex, int turnId) {
        this.turnId = turnId;
        this.mutex = mutex;
    }

    @Override
    public void run(){
        takeTurn();
    }

    private void takeTurn(){
        while(true){
            try {
                mutex.waitForTurn(turnId);
                mutex.switchTurns();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
