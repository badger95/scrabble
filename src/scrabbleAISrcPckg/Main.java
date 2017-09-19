package scrabbleAISrcPckg;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        // FXMLLoader.load(getClass().getResource("sample.fxml"));
        VBox root = new VBox();
        HBox hBox = new HBox();
        primaryStage.setScene(new Scene(root, 1000, 900));
        LetterBag letterBag = LetterBag.getInstance();
        AIPlayer aiPlayer = new AIPlayer();
        HumanPlayer humanPlayer = new HumanPlayer();
        Board board = new Board();
        board.setAlignment(Pos.TOP_CENTER);
        root.getChildren().add(board);
        hBox.getChildren().add(aiPlayer.getLetterRack());
        hBox.getChildren().add(humanPlayer.getLetterRack());
        root.getChildren().add(hBox);
        primaryStage.setTitle("Scrabble");
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
