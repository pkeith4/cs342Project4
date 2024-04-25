import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class EndScene {
//
//    public Scene createEndScene(boolean won, Stage primaryStage, Client clientConnection) {
//        BorderPane layout = new BorderPane();
//        layout.setStyle("-fx-background-image: url('/images/background.png'); " +
//                "-fx-background-size: cover; " +
//                "-fx-background-repeat: no-repeat; " +
//                "-fx-background-position: center center;");
//
//        // Image display based on win or loss
//        Image resultImage = new Image(won ? getClass().getResourceAsStream("/images/You Won.png")
//                : getClass().getResourceAsStream("/images/You Lost.png"));
//        ImageView resultImageView = new ImageView(resultImage);
//        resultImageView.setFitHeight(200); // Set height and let width preserve ratio
//        resultImageView.setPreserveRatio(true);
//
//        // Play Again Button
//        Button playAgainButton = new Button();
//        playAgainButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/Play Again.png"))));
//        playAgainButton.setOnAction(e -> primaryStage.setScene(StartScene.getScene(primaryStage, clientConnection))); // Assuming StartScene.getScene() is a static method that returns a Scene
//        playAgainButton.setStyle("-fx-background-color: transparent;");
//
//        // Quit Button
//        Button quitButton = new Button();
//        quitButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/Quit.png"))));
//        quitButton.setOnAction(e -> {
//            Platform.exit();
//            System.exit(0);
//        });
//        quitButton.setStyle("-fx-background-color: transparent;");
//
//        VBox buttonBox = new VBox(10, playAgainButton, quitButton);
//        buttonBox.setAlignment(Pos.CENTER);
//
//        layout.setTop(resultImageView);
//        layout.setCenter(buttonBox);
//        BorderPane.setAlignment(resultImageView, Pos.TOP_CENTER);
//        BorderPane.setMargin(resultImageView, new Insets(20, 0, 0, 0));
//
//        return new Scene(layout, 600, 400);
//    }
}
