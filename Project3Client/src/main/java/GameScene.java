import javafx.application.Platform;
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
  private final int GRID_SIZE = 10;
  private final String BACKGROUND_URL = "file:resources/images/background.png";
  private final String FIRE_BUTTON_URL = "file:resources/images/Fire.png";
  private final String HIT_IMAGE_URL = "file:resources/images/hitfinal.png";
  private final String MISS_IMAGE_URL = "file:resources/images/missfinal.png";
  private final String SHIP_IMAGE_URL = "file:resources/images/shipblockfinal.png";

  private Stage primaryStage;
  private Client clientConnection;

  // Placeholder for game logic connections
  private gameLogic.Player player;
  private gameLogic.Player opponent;
  private gameLogic.Board playerBoard;
  private gameLogic.Board opponentBoard;

  GameScene(Stage primaryStage, Client clientConnection) {
    this.primaryStage = primaryStage;
    this.clientConnection = clientConnection;
  }

  public Scene getScene() {
    BorderPane layout = new BorderPane();

    // Set background image to cover the entire background
    BackgroundImage bgImage = new BackgroundImage(new Image(BACKGROUND_URL),
        BackgroundRepeat.NO_REPEAT,
        BackgroundRepeat.NO_REPEAT,
        BackgroundPosition.CENTER,
        new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true));
    layout.setBackground(new Background(bgImage));

    // Left side - Player's grid
    GridPane playerGrid = createGrid(true);
    // Right side - Opponent's grid
    GridPane opponentGrid = createGrid(false);

    // Fire button in the center bottom
    Button fireButton = new Button("Fire!");
    fireButton.setGraphic(new ImageView(new Image(FIRE_BUTTON_URL)));
    fireButton.setOnAction(e -> endUserTurn(primaryStage, player, opponent));

    HBox fireBox = new HBox(fireButton);
    fireBox.setAlignment(Pos.CENTER);

    layout.setLeft(playerGrid);
    layout.setRight(opponentGrid);
    layout.setBottom(fireBox);

    Scene scene = new Scene(layout);
    scene.getStylesheets().add(GameScene.class.getResource("style.css").toExternalForm());
    return scene;
  }

  public GridPane createGrid(boolean isPlayer) {
    GridPane grid = new GridPane();
    grid.setPadding(new Insets(5));
    grid.setHgap(5);
    grid.setVgap(5);

    for (int i = 0; i < GRID_SIZE; i++) {
      for (int j = 0; j < GRID_SIZE; j++) {
        Button button = new Button();
        button.setPrefSize(30, 30);
        button.setStyle("-fx-background-color: transparent;");
        int x = i, y = j;

        if (isPlayer) {
          updateButtonBackground(button, x, y, playerBoard);
          button.setOnAction(e -> {
          }); // No action on player's own grid
        } else {
          button.setOnAction(e -> handleOpponentAction(button, x, y));
        }
        grid.add(button, i, j);
      }
    }
    return grid;
  }

  private void updateButtonBackground(Button button, int x, int y, gameLogic.Board board) {
    if (board.getGrid()[y][x] == 'S') {
      button.setBackground(new Background(new BackgroundImage(new Image(SHIP_IMAGE_URL),
          BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
          BackgroundSize.DEFAULT)));
    }
  }

  private void handleOpponentAction(Button button, int x, int y) {
    // This should trigger a shoot event in your game logic
    boolean hit = opponentBoard.recordHit(x, y);
    Image img = new Image(hit ? HIT_IMAGE_URL : MISS_IMAGE_URL);
    button.setGraphic(new ImageView(img));
  }

  private void endUserTurn(Stage primaryStage, gameLogic.Player currentPlayer, gameLogic.Player currentOpponent) {
    System.out.println("Fire! Button pressed - End of turn.");
    // Check game state and proceed accordingly
    if (currentPlayer.isAllShipsSunk() || currentOpponent.isAllShipsSunk()) {
      boolean won = currentOpponent.isAllShipsSunk();
      // Proceed to end game scene
      EndScene scene = new EndScene(won, primaryStage, null); // Assuming Client connection if needed
      primaryStage.setScene(scene.getScene());
    }
    // Otherwise, toggle player turn
    currentPlayer.setTurn(false);
    currentOpponent.setTurn(true);
  }
}
