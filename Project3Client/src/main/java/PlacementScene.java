import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.util.ArrayList;

import gameLogic.Board;
import gameLogic.Ship;

public class PlacementScene {
  private static final int GRID_SIZE = 10;
  private Button[][] grid = new Button[GRID_SIZE][GRID_SIZE];
  private boolean isVertical;
  private Image shipBlock;
  private Image shipEnd;
  private Image background;
  private Client clientConnection;
  private Stage primaryStage;
  private boolean isAI;
  private String opponent;
  private Label direction;
  private ArrayList<ArrayList<Button>> cells;
  private Scene scene;
  private int currHoverX;
  private int currHoverY;
  private int[] shipSizes = { 2, 3, 3, 4, 5 };
  private int currShipI;
  private gameLogic.Ship[] ships = new gameLogic.Ship[5];
  private GridPane gridPane;

  public PlacementScene(Stage primaryStage, boolean isAI, String opponent, Client clientConnection) {
    this.primaryStage = primaryStage;
    this.clientConnection = clientConnection;
    this.isAI = isAI;
    this.opponent = opponent;
    this.cells = new ArrayList<>();
    this.isVertical = false;
    this.currHoverY = -1;
    this.currHoverX = -1;
    loadImages();
  }

  private void loadImages() {
    try {
      shipBlock = new Image("/images/shipblockfinal.png");
      shipEnd = new Image("/images/shipendfinal.png");
      background = new Image("/images/background.png");
      if (shipBlock.isError() || shipEnd.isError() || background.isError()) {
        throw new RuntimeException("Error loading images");
      }
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  public Scene getScene() {
    BorderPane root = new BorderPane();
    this.gridPane = createGrid();
    direction = new Label("");
    direction.getStyleClass().add("message");
    VBox centerBox = new VBox();
    centerBox.getChildren().addAll(direction, gridPane);
    centerBox.setPrefWidth(400);
    centerBox.setAlignment(Pos.CENTER);
    root.setCenter(centerBox);
    root.setTop(Helper.getTitle());
    initPlacement();

    root.getStyleClass().add("background");

    scene = new Scene(root);
    scene.getStylesheets().add(PlacementScene.class.getResource("style.css").toExternalForm());
    initKeybinds(); // init keybinds function depends on the scene to be set
    return scene;
  }

  private void setDirection(String text) {
    this.direction.setText(text);
  }

  private void initPlacement() {
    currShipI = 0;
    setDirection("Place a " + String.valueOf(shipSizes[currShipI]) + " length ship");
    for (int rowI = 0; rowI < GRID_SIZE; rowI++) {
      ArrayList<Button> row = cells.get(rowI);
      for (int colI = 0; colI < GRID_SIZE; colI++) {
        Button cell = row.get(colI);
        int finalColI = colI;
        int finalRowI = rowI;
        cell.setOnMouseEntered(e -> {
          if (currShipI >= 5)
            return;
          hoverCell(finalColI, finalRowI, shipSizes[currShipI]);
        });
        cell.setOnMouseClicked(e -> {
          if (currShipI >= 5)
            return;
          clearHoverImgs();
          if (canPlaceShip(finalColI, finalRowI, shipSizes[currShipI])) {
            placeShip(finalColI, finalRowI, shipSizes[currShipI], true);
            currShipI++;

            // check if all shipSizes have been placed
            if (currShipI == 5) { // not shipSizes.length - 1 to adjust for the recently incremented currShipI
              // notify user they're waiting
              setDirection("Waiting for " + this.opponent + " to finish placing ships...");
              clientConnection.onGameReady(data -> { // when game is ready, callback function will be triggered
                Platform.runLater(() -> {
                  // switch to game scene
                  GameScene scene = new GameScene(primaryStage, isAI, opponent, clientConnection, gridPane,
                      data.goesFirst(), this.cells);
                  primaryStage.setScene(scene.getScene());
                  primaryStage.setFullScreen(true);
                });
              });
              /*
               * only after the game ready callback is initialized,
               * send the board to the server. If the board is the last
               * board, the server will respond with a game ready object
               * near instantly. We don't want a race condition
               */
              clientConnection.sendBoard(getBoard());
            } else {
              setDirection("Place a " + String.valueOf(shipSizes[currShipI]) + " length ship");
            }
          }
        });
      }
    }
  }

  private Board getBoard() {
    Board board = new Board();
    for (gameLogic.Ship ship : ships) {
      board.placeShip(ship);
    }
    return board;
  }

  private void hoverCell(int colI, int rowI, int size) {
    // update hover X & Y values
    currHoverY = rowI;
    currHoverX = colI;

    clearHoverImgs();
    if (canPlaceShip(colI, rowI, size)) {
      placeShip(colI, rowI, size, false);
    } else {
      showCross(colI, rowI);
    }
  }

  private void initKeybinds() {
    scene.setOnKeyPressed(event -> {
      if (event.getCode() == KeyCode.R) {
        isVertical = !isVertical;
        if (currHoverX != -1 && currHoverY != -1) { // if there is a curr hover cell
          hoverCell(currHoverX, currHoverY, shipSizes[currShipI]);
        }
      }
    });
  }

  private void showCross(int x, int y) {
    this.cells.get(y).get(x).getStyleClass().add("cross");
  }

  private void clearHoverImgs() {
    for (ArrayList<Button> row : this.cells) {
      for (Button cell : row) {
        if (cell.getStyleClass().contains("empty")) {
          if (cell.getStyleClass().contains("ship-center"))
            cell.getStyleClass().remove("ship-center");
          if (cell.getStyleClass().contains("ship-edge"))
            cell.getStyleClass().remove("ship-edge");
        }
        if (cell.getStyleClass().contains("cross"))
          cell.getStyleClass().remove("cross");
      }
    }
  }

  private GridPane createGrid() {
    GridPane gridPane = new GridPane();
    BorderPane.setAlignment(gridPane, Pos.CENTER);
    gridPane.setHgap(5);
    gridPane.setVgap(5);
    gridPane.setPrefWidth(400);
    gridPane.setMaxWidth(400);

    for (int i = 0; i < GRID_SIZE; i++) {
      ArrayList<Button> row = new ArrayList<>();
      for (int j = 0; j < GRID_SIZE; j++) {
        Button button = new Button();
        button.getStyleClass().addAll("cell", "empty");
        button.setPrefSize(35, 35);
        final int x = i, y = j;
        grid[i][j] = button;
        gridPane.add(button, i, j);
        row.add(button);
      }
      cells.add(row);
    }
    return gridPane;
  }

  private boolean canPlaceShip(int x, int y, int size) {
    if (isVertical && y + size - 1 > 9)
      return false;
    if (!isVertical && x + size - 1 > 9)
      return false;
    for (int i = 0; i < size; i++) { // loop through every to-be occupied cell
      if (isVertical) {
        Button button = this.cells.get(y + i).get(x);
        if (!button.getStyleClass().contains("empty"))
          return false;
      } else {
        Button button = this.cells.get(y).get(x + i);
        System.out.println(button.getStyleClass().contains("empty"));
        if (!button.getStyleClass().contains("empty"))
          return false;
      }
    }
    return true;
  }

  private void placeShip(int x, int y, int size, boolean permanent) {
    Button button;
    for (int i = 0; i < size; i++) {
      if (isVertical)
        button = this.cells.get(y + i).get(x);
      else
        button = this.cells.get(y).get(x + i);
      button.getStyleClass().add("ship-center");
      if (permanent)
        button.getStyleClass().remove("empty"); // remove the empty signaller
    }

    if (permanent) {// if you are permanently placing the ship
      if (isVertical)
        button = this.cells.get(y).get(x);
      else
        button = this.cells.get(y).get(x);
      for (int j = 0; j < 5; j++) { // iterate through ships
        if (ships[j] == null) { // if ship hasn't been assigned yet
          ships[j] = new Ship(size, y, x, !isVertical); // add Ship to finished ships
          return;
        }
      }
    }
  }
}
