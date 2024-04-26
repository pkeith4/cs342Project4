import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.function.Consumer;

public class Helper {
    public static Button getBackButton(Stage primaryStage, Client clientConnection, Consumer<String> callback) {
        // Back button to return to the StartScene
        Button backButton = new Button();
        backButton.setPrefSize(140, 40);
        backButton.getStyleClass().addAll("bubble", "image", "back-btn");
        backButton.setOnAction(e -> {
            callback.accept(null);
            primaryStage.setScene(StartScene.getScene(primaryStage, clientConnection));
            primaryStage.setFullScreen(true);
        });
        return backButton;
    }
    public static Button getBackButton(Stage primaryStage, Client clientConnection) {
        // Back button to return to the StartScene
        Button backButton = new Button();
        backButton.setPrefSize(140, 40);
        backButton.getStyleClass().addAll("bubble", "image", "back-btn");
        backButton.setOnAction(e -> {
            primaryStage.setScene(StartScene.getScene(primaryStage, clientConnection));
            primaryStage.setFullScreen(true);
        });
        return backButton;
    }
    public static StackPane getTitle() {
        Image battleshipImage = new Image(Helper.class.getResourceAsStream("/images/battleship.png"));
        ImageView battleshipImageView = new ImageView(battleshipImage);
        battleshipImageView.setFitHeight(1250);
        battleshipImageView.setFitWidth(1500);
        battleshipImageView.setPreserveRatio(true);
        battleshipImageView.setTranslateX(180);
        battleshipImageView.setTranslateY(-20);


        StackPane centeredImageView = new StackPane(battleshipImageView);
        centeredImageView.setAlignment(Pos.CENTER); // This will center the image

        battleshipImageView.setFitHeight(1250);
        battleshipImageView.setFitWidth(1500);
        battleshipImageView.setPreserveRatio(true);
        battleshipImageView.setTranslateX(180);
        battleshipImageView.setTranslateY(-20);

        return centeredImageView;
    }
}
