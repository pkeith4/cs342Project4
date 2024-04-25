import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class PlacementScene {
    private static final int GRID_SIZE = 10;
    private Button[][] grid = new Button[GRID_SIZE][GRID_SIZE];
    private boolean isVertical = true; // Assume default orientation is vertical
    private int currentShipSize = 5; // Starting with the largest ship
    private int[] shipSizes = {5, 4, 3, 3, 2}; // Ship sizes to be placed
    private int shipIndex = 0; // To track which ship size is currently being placed
    private Image shipBlock;
    private Image shipEnd;
    private Image background;

    public PlacementScene() {
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
        GridPane gridPane = createGrid();
        root.setCenter(gridPane);

        BackgroundImage bgImage = new BackgroundImage(background, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
        root.setBackground(new Background(bgImage));

        Scene scene = new Scene(root, 600, 600);
        setupKeyBindings(scene);
        return scene;
    }

    private GridPane createGrid() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(5);
        gridPane.setVgap(5);

        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                Button button = new Button();
                button.setPrefSize(30, 30);
                button.setStyle("-fx-background-color: transparent;");
                final int x = i, y = j;
                button.setOnMouseEntered(e -> showShip(x, y));
                button.setOnMouseExited(e -> clearShip());
                button.setOnMouseClicked(e -> placeShip(x, y));
                grid[i][j] = button;
                gridPane.add(button, i, j);
            }
        }
        return gridPane;
    }

    private void showShip(int x, int y) {
        try {
            clearShip(); // Clear previously shown ships
            for (int i = 0; i < currentShipSize; i++) {
                int dx = isVertical ? 0 : i;
                int dy = isVertical ? i : 0;
                if (x + dx < GRID_SIZE && y + dy < GRID_SIZE) {
                    Button btn = grid[x + dx][y + dy];
                    btn.setBackground(new Background(new BackgroundImage(shipBlock,
                            BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                            BackgroundSize.COVER)));
                }
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {
            // Handle out of bounds
        }
    }

    private void clearShip() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                grid[i][j].setStyle("-fx-background-color: transparent;");
            }
        }
    }

    private void placeShip(int x, int y) {
        if (shipIndex >= shipSizes.length) return; // Check if all ships are placed
        try {
            for (int i = 0; i < currentShipSize; i++) {
                int dx = isVertical ? 0 : i;
                int dy = isVertical ? i : 0;
                if (x + dx < GRID_SIZE && y + dy < GRID_SIZE) {
                    Button btn = grid[x + dx][y + dy];
                    btn.setBackground(new Background(new BackgroundImage(shipEnd,
                            BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                            BackgroundSize.COVER)));
                }
            }
            shipIndex++;
            if (shipIndex < shipSizes.length) {
                currentShipSize = shipSizes[shipIndex]; // Update to next ship size
            } else {
                System.out.println("All ships placed");
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {
            // Handle out of bounds
        }
    }

    private void setupKeyBindings(Scene scene) {
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.SPACE) {
                isVertical = !isVertical; // Toggle orientation
                System.out.println("Orientation changed to " + (isVertical ? "Vertical" : "Horizontal"));
            }
        });
    }
}
