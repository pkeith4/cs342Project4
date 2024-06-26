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

public class GameScene {
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
  private GridPane leftGridPane;
  private GridPane rightGridPane;
  private boolean currentTurn;
  private ArrayList<ArrayList<Button>> leftCells;
  private ArrayList<ArrayList<Button>> rightCells;

  public GameScene(Stage primaryStage, boolean isAI, String opponent, Client clientConnection, GridPane leftGridPane,
      boolean goesFirst, ArrayList<ArrayList<Button>> leftCells) {
    this.primaryStage = primaryStage;
    this.clientConnection = clientConnection;
    this.isAI = isAI;
    this.opponent = opponent;
    this.cells = new ArrayList<>();
    this.isVertical = false;
    this.currHoverY = -1;
    this.currHoverX = -1;
    this.leftGridPane = leftGridPane;
    this.currentTurn = goesFirst;
    this.leftCells = leftCells;
    this.rightCells = new ArrayList<>();
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
    HBox boxes = new HBox(10); // Adding a spacing of 10 pixels between children
    boxes.setAlignment(Pos.CENTER); // Centering children within the HBox

    direction = new Label("");
    direction.getStyleClass().add("message");
    VBox leftBox = new VBox();
    leftBox.getChildren().addAll(direction, new Label("('r' to rotate)"), this.leftGridPane);

    leftBox.setPrefWidth(400);
    leftBox.setAlignment(Pos.CENTER);

    this.rightGridPane = createRightGrid();
    VBox rightBox = new VBox();

    Label fakeDirection = new Label("");
    fakeDirection.getStyleClass().add("message");
    rightBox.getChildren().addAll(fakeDirection, new Label("('r' to rotate)"), rightGridPane);
    rightBox.setPrefWidth(400);
    rightBox.setAlignment(Pos.CENTER);

    boxes.getChildren().addAll(leftBox, rightBox);

    root.setCenter(boxes);
    root.setTop(Helper.getTitle());

    root.getStyleClass().add("background");

    Scene scene = new Scene(root);
    scene.getStylesheets().add(GameScene.class.getResource("style.css").toExternalForm());
    startMoves();
    initRightListeners();
    return scene;
  }

  private boolean canHitCell(Button cell) {
    return !cell.getStyleClass().contains("miss") && !cell.getStyleClass().contains("hit")
        && !cell.getStyleClass().contains("ship-center") && !cell.getStyleClass().contains("ship-edge");
  }

  private void initRightListeners() {
    for (int rowI = 0; rowI < this.rightCells.size(); rowI++) {
      ArrayList<Button> row = this.rightCells.get(rowI);
      for (int colI = 0; colI < row.size(); colI++) {
        Button cell = row.get(colI);
        int finalColI = colI;
        int finalRowI = rowI;
        cell.setOnMouseEntered(e -> {
          clearHovers(this.rightCells);
          if (!this.currentTurn)
            return;
          if (!canHitCell(cell))
            return;
          cell.getStyleClass().add("hover");
        });
        cell.setOnMouseClicked(e -> {
          clearHovers(this.rightCells);
          if (!this.currentTurn)
            return;
          if (!canHitCell(cell)) {
            setDirection("You cannot shoot on that occupied cell.");
            return;
          }
          System.out.println("Shooting!!");
          // code to shoot the cell
          clientConnection.shoot(finalRowI, finalColI);
          // wait for server to respond and trigger the onOpponentShoot callback function
        });
      }
    }
  }

  private void clearHovers(ArrayList<ArrayList<Button>> cells) {
    for (ArrayList<Button> row : cells) {
      for (Button cell : row) {
        if (cell.getStyleClass().contains("hover"))
          cell.getStyleClass().remove("hover");
      }
    }
  }

  private void startMoves() {
    if (this.currentTurn) {
      setDirection("Your turn! Take and aim and shoot!");
      // wait for mouse click event from already initialized events from cells
    } else {
      setDirection("Waiting for opponent to shoot...");
    }

    // initialize shoot callback function
    clientConnection.onOpponentShoot(data -> {
      Platform.runLater(() -> {
        System.out.println("RECEIVED SHOOT");
        System.out.println(data.getCoordinate());
        System.out.println(data.getHit());
        System.out.println(data.getRevealedShip());
        if (this.currentTurn) {
          applyOurHit(data.getHit(), data.getCoordinate(), data.getRevealedShip());
        } else {
          // reflect enemy shot
          applyOpponentHit(data.getHit(), data.getCoordinate());
        }
        if (data.getGameOver()) {
          EndScene scene = new EndScene(this.currentTurn, primaryStage, clientConnection);
          primaryStage.setScene(scene.getScene());
          primaryStage.setFullScreen(true);
        }
        swapTurn();
      });
    });
  }

  private void swapTurn() {
    this.currentTurn = !this.currentTurn;
    if (this.currentTurn) {
      setDirection("Your turn! Take and aim and shoot!");
      // wait for mouse click event from already initialized events from cells
    } else {
      setDirection("Waiting for opponent to shoot...");
    }
  }

  private void applyOurHit(boolean hit, gameLogic.Coordinate coord, gameLogic.Coordinate[] revealedShip) {
    if (revealedShip == null) {
      String className;
      if (hit)
        className = "hit";
      else
        className = "miss";
      this.rightCells.get(coord.getX()).get(coord.getY()).getStyleClass().add(className);
    } else {
      for (int i = 0; i < revealedShip.length; i++) {
        gameLogic.Coordinate cellCoord = revealedShip[i];
        String className;
        if (i == 0 || revealedShip.length - 1 == i)
          className = "ship-edge";
        else
          className = "ship-center";
        Button cell = this.rightCells.get(cellCoord.getX()).get(cellCoord.getY());
        if (cell.getStyleClass().contains("hit"))
          cell.getStyleClass().remove("hit");
        if (cell.getStyleClass().contains("miss"))
          cell.getStyleClass().remove("miss");
        cell.getStyleClass().add(className);
      }
    }
  }

  private void applyOpponentHit(boolean hit, gameLogic.Coordinate coord) {
    String className;
    if (hit)
      className = "hit";
    else
      className = "miss";
    Button cell = this.leftCells.get(coord.getX()).get(coord.getY());
    if (cell.getStyleClass().contains("ship-center"))
      cell.getStyleClass().remove("ship-center");
    if (cell.getStyleClass().contains("ship-edge"))
      cell.getStyleClass().remove("ship-edge");
    cell.getStyleClass().add(className);
  }

  private void setDirection(String text) {
    this.direction.setText(text);
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

  private GridPane createRightGrid() {
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
      rightCells.add(row);
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
        if (!button.getStyleClass().contains("empty"))
          return false;
      }
    }
    return true;
  }

  private void placeShip(int x, int y, int size, boolean permanent) {
    for (int i = 0; i < size; i++) {
      Button button;
      if (isVertical)
        button = this.cells.get(y + i).get(x);
      else
        button = this.cells.get(y).get(x + i);
      button.getStyleClass().add("ship-center");
      if (permanent) {// if you are permanently placing the ship
        button.getStyleClass().remove("empty"); // remove the empty signaller
        for (int j = 0; j < 5; j++) { // iterate through ships
          if (ships[j] == null) { // if ship hasn't been assigned yet
            ships[j] = new Ship(size, x, y, isVertical); // add Ship to finished ships
            continue;
          }
        }
      }
    }
  }
}
