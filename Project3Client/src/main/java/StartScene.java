import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class StartScene {
    public static Scene getScene(Stage primaryStage, Client clientConnection) {
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(10, 10, 10, 10));

        // Help Button on the top-left
        Button helpButton = new Button("Help (?)");
        helpButton.setOnAction(e -> primaryStage.setScene(HelpScene.getScene(primaryStage, clientConnection)));

        // Quit Button on the top-right
        Button quitButton = new Button("Quit");
        quitButton.setOnAction(e -> System.exit(0)); // Properly exit the application

        // Top bar with Help and Quit buttons
        HBox topBar = new HBox(10);
        topBar.getChildren().addAll(helpButton, quitButton);
        HBox.setHgrow(helpButton, javafx.scene.layout.Priority.ALWAYS);
        HBox.setHgrow(quitButton, javafx.scene.layout.Priority.ALWAYS);
        helpButton.setMaxWidth(Double.MAX_VALUE);
        quitButton.setMaxWidth(Double.MAX_VALUE);
        helpButton.setAlignment(Pos.CENTER_LEFT);
        quitButton.setAlignment(Pos.CENTER_RIGHT);
        layout.setTop(topBar);

        // Game Mode label and buttons
        Label gameModeLabel = new Label("Choose Game Mode:");
        Button aiButton = new Button("AI");
        aiButton.setOnAction(e -> primaryStage.setScene(PlacementScene.getScene(primaryStage, true, clientConnection)));  // Assuming PlacementScene takes Client
        Button pvpButton = new Button("PvP");
        pvpButton.setOnAction(e -> primaryStage.setScene(PlacementScene.getScene(primaryStage, false, clientConnection)));

        VBox centerBox = new VBox(10);
        centerBox.getChildren().addAll(gameModeLabel, aiButton, pvpButton);
        centerBox.setAlignment(Pos.CENTER);
        layout.setCenter(centerBox);

        return new Scene(layout, 400, 300);
    }
}
