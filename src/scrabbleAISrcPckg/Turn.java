package scrabbleAISrcPckg;

public abstract class Turn implements Runnable {

    private final int turnId;
    private final Mutex mutex;
    private Player player;

    Turn(Mutex mutex, int turnId, Player player) {
        this.turnId = turnId;
        this.mutex = mutex;
        this.player = player;
    }

    @Override
    public void run(){
        while(true){
            try {
                mutex.waitForTurn(turnId, player);
                player.getLetterRack().setDisable(false);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
