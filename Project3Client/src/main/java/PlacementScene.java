import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;

public class PlacementScene extends Application {
    private static final int GRID_SIZE = 10;
    private Button[][] grid = new Button[GRID_SIZE][GRID_SIZE];
    private boolean isVertical = false;
    private int currentShipSize = 5;
    private int[] shipSizes = {5, 4, 3, 3, 2}; // Sizes of the ships to be placed
    private int shipIndex = 0; // Index to track which ship is being placed
    private Image shipBlock = new Image("file:shipblockfinal.png");
    private Image shipEnd = new Image("file:shipendfinal.png");
    private Image background = new Image("file:background.png"); // Transparent background for buttons

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        GridPane gridPane = createGrid();
        root.setCenter(gridPane);
        root.setBackground(new Background(new BackgroundImage(new Image("file:background.png"),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT)));

        Scene scene = new Scene(root, 600, 600);
        setupKeyBindings(scene);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Place Your Ships");
        primaryStage.show();
    }

    private GridPane createGrid() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(5));
        gridPane.setHgap(5);
        gridPane.setVgap(5);

        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                Button button = new Button();
                button.setPrefSize(30, 30);
                button.setBackground(new Background(new BackgroundImage(background,
                        BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                        BackgroundSize.DEFAULT)));
                final int x = i, y = j;
                button.setOnMouseEntered(e -> showShip(x, y));
                button.setOnMouseExited(e -> clearShip());
                button.setOnMouseClicked(e -> placeShip(x, y));
                grid[x][y] = button;
                gridPane.add(button, x, y);
            }
        }
        return gridPane;
    }

    private void showShip(int x, int y) {
        clearShip();
        try {
            int length = currentShipSize;
            for (int i = 0; i < length; i++) {
                int dx = isVertical ? 0 : i;
                int dy = isVertical ? i : 0;
                if (x + dx < GRID_SIZE && y + dy < GRID_SIZE) {
                    Button btn = grid[x + dx][y + dy];
                    btn.setBackground(new Background(new BackgroundImage(shipBlock,
                            BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                            BackgroundSize.DEFAULT)));
                }
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
    }

    private void clearShip() {
        for (Button[] row : grid) {
            for (Button btn : row) {
                btn.setBackground(new Background(new BackgroundImage(background,
                        BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                        BackgroundSize.DEFAULT)));
            }
        }
    }

    private void placeShip(int x, int y) {
        if (shipIndex >= shipSizes.length) return; // No more ships to place
        try {
            int length = currentShipSize;
            for (int i = 0; i < length; i++) {
                int dx = isVertical ? 0 : i;
                int dy = isVertical ? i : 0;
                if (x + dx < GRID_SIZE && y + dy < GRID_SIZE) {
                    Button btn = grid[x + dx][y + dy];
                    btn.setBackground(new Background(new BackgroundImage(shipBlock,
                            BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                            BackgroundSize.DEFAULT)));
                }
            }
            shipIndex++; // Move to the next ship
            if (shipIndex < shipSizes.length) {
                currentShipSize = shipSizes[shipIndex];
            } else {
                System.out.println("All ships placed");
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
    }

    private void setupKeyBindings(Scene scene) {
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.SPACE) {
                isVertical = !isVertical; // Toggle ship orientation
                System.out.println("Orientation changed to " + (isVertical ? "Vertical" : "Horizontal"));
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
