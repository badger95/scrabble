package scrabbleAISrcPckg;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // FXMLLoader.load(getClass().getResource("sample.fxml"));
        VBox root = new VBox(); // highest level container
        HBox horizontalOutermostContainer = new HBox(); // holds board, and sideBar
        VBox sideBar = new VBox(); // holds tileRacks, player scores, and action buttons
        primaryStage.setScene(new Scene(root, 1200, 800));
        LetterBag.getInstance();
        Mutex mutex = new Mutex(0, 2);
        AIPlayer aiPlayer = new AIPlayer();
        AITurn aiTurn = new AITurn(mutex, 0, aiPlayer);
        HumanPlayer humanPlayer = new HumanPlayer();
        HumanTurn humanTurn = new HumanTurn(mutex, 1, humanPlayer);
        Thread aiThread = new Thread(aiTurn);
        Thread humanThread = new Thread(humanTurn);
        aiThread.start();
        humanThread.start();
        Board board = new Board();
        board.setAlignment(Pos.TOP_CENTER);
        horizontalOutermostContainer.getChildren().add(board);
        sideBar.getChildren().add(aiPlayer.getLetterRack());
        sideBar.getChildren().add(humanPlayer.getLetterRack());
        HBox turnBar = new HBox();
        Button dumpButton = new Button("Dump Letters");
        Button endTurnButton = new Button("End Turn");
        Label whoseTurn = new Label(mutex.getWhoseTurnIsIt());
        endTurnButton.setOnMouseClicked(event -> {
            mutex.switchTurns();
            whoseTurn.setText(mutex.getWhoseTurnIsIt());
        });
        turnBar.getChildren().addAll(endTurnButton, dumpButton, whoseTurn);
        turnBar.setSpacing(10);
        sideBar.getChildren().add(turnBar);
        horizontalOutermostContainer.getChildren().add(sideBar);
        root.getChildren().addAll(horizontalOutermostContainer);
        primaryStage.setTitle("Scrabble");
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
