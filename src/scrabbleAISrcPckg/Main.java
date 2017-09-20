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
        VBox sideBar = new VBox();
        primaryStage.setScene(new Scene(root, 1200, 800));
        LetterBag letterBag = LetterBag.getInstance();
        AIPlayer aiPlayer = new AIPlayer();
        HumanPlayer humanPlayer = new HumanPlayer();
        Board board = new Board();
        board.setAlignment(Pos.TOP_CENTER);
        hBox.getChildren().add(board);
        sideBar.getChildren().add(aiPlayer.getLetterRack());
        sideBar.getChildren().add(humanPlayer.getLetterRack());
        hBox.getChildren().add(sideBar);
        root.getChildren().addAll(hBox);
        primaryStage.setTitle("Scrabble");
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
