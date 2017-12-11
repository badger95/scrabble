package scrabbleAISrcPckg;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // FXMLLoader.load(getClass().getResource("sample.fxml"));
        buildDictionaryTrie(GameManager.wordChecker);
        ScrollPane root = new ScrollPane(); // highest level container
        root.setPrefSize(1200, 800);
        HBox horizontalOutermostContainer = new HBox(); // holds board, and sideBar
        VBox sideBar = new VBox(); // holds tileRacks, player scores, and action buttons
        sideBar.setAlignment(Pos.CENTER_RIGHT);
        primaryStage.setScene(new Scene(root, 1200, 800));
        LetterBag.createInstance();
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
        GameManager gameManager = new GameManager(board);
        board.setAlignment(Pos.TOP_CENTER);
        horizontalOutermostContainer.getChildren().add(board);
        sideBar.getChildren().addAll(new Label("Player 0"), aiPlayer.getLetterRack(),
                new Label("Player 1"), humanPlayer.getLetterRack());
        HBox turnBar = new HBox(); // holds buttons and whoseTurn label
        Button dumpButton = new Button("Dump Letters");
        dumpButton.setOnMouseClicked(e -> {
            humanPlayer.dumpLetters();
            mutex.switchTurns();
        });
        Button endTurnButton = new Button("End Turn");
        Label whoseTurn = new Label(mutex.getWhoseTurnLabel());
        endTurnButton.setOnMouseClicked((MouseEvent event) -> {
            gameManager.commitAllNewlyPopulatedContainers();
            mutex.switchTurns();
            whoseTurn.setText(mutex.getWhoseTurnLabel());
            gameManager.doBestPossibleMove(humanPlayer);
            humanPlayer.fillLetterRack();
        });
        turnBar.getChildren().addAll(endTurnButton, dumpButton, whoseTurn);
        turnBar.setSpacing(10);
        sideBar.getChildren().add(turnBar);
        horizontalOutermostContainer.getChildren().add(sideBar);
        root.setContent(horizontalOutermostContainer);
        primaryStage.setTitle("Scrabble");
        primaryStage.show();
    }

    private void buildDictionaryTrie(WordChecker wordChecker) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader("src/scrabbleAISrcPckg/wordList"));
        String line = bufferedReader.readLine();
        while (line != null) {
            wordChecker.insert(line);
            line = bufferedReader.readLine();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
