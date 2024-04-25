import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class GameScene {

    public static Scene createGameScene(Stage primaryStage) {
        BorderPane layout = new BorderPane();
        layout.setBackground(new Background(new BackgroundImage(new Image("/images/background.png"),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT)));

        // Left side - Player's grid
        GridPane playerGrid = new GridPane();
        playerGrid.setPadding(new Insets(5));
        playerGrid.setHgap(5);
        playerGrid.setVgap(5);

        // Right side - Opponent's grid
        GridPane opponentGrid = new GridPane();
        opponentGrid.setPadding(new Insets(5));
        opponentGrid.setHgap(5);
        opponentGrid.setVgap(5);

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Button playerButton = createTransparentButton();
                Button opponentButton = createTransparentButton();
                playerButton.setOnAction(e -> handlePlayerAction(playerButton));
                opponentButton.setOnAction(e -> handleOpponentAction(opponentButton));

                playerGrid.add(playerButton, i, j);
                opponentGrid.add(opponentButton, i, j);
            }
        }

        // Fire button
        Button fireButton = new Button();
        fireButton.setGraphic(new ImageView(new Image("/images/Fire.png")));
        fireButton.setOnAction(e -> endUserTurn());

        HBox fireBox = new HBox(fireButton);
        fireBox.setAlignment(Pos.CENTER);

        // Layout positioning
        layout.setLeft(playerGrid);
        layout.setRight(opponentGrid);
        layout.setBottom(fireBox);

        return new Scene(layout, 800, 600);
    }

    private static Button createTransparentButton() {
        Button button = new Button();
        button.setPrefSize(30, 30);
        button.setStyle("-fx-background-color: transparent;");
        return button;
    }

    private static void handlePlayerAction(Button button) {
        // Example action for player's grid
        ImageView hitView = new ImageView(new Image("/images/hitfinal.png"));
        hitView.setFitHeight(30);
        hitView.setFitWidth(30);
        button.setGraphic(hitView);
    }

    private static void handleOpponentAction(Button button) {
        // Example action for opponent's grid
        ImageView missView = new ImageView(new Image("/images/missfinal.png"));
        missView.setFitHeight(30);
        missView.setFitWidth(30);
        button.setGraphic(missView);
    }

    private static void endUserTurn() {
        System.out.println("Fire! Button pressed - End of turn.");
        // Logic to handle the end of the user's turn
    }
}
