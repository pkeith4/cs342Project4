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
        Button helpButton = new Button();
        helpButton.setPrefSize(140, 40);
        helpButton.getStyleClass().addAll("bubble", "help-btn", "image");
        helpButton.setOnAction(e -> {
            primaryStage.setScene(HelpScene.getScene(primaryStage, clientConnection));
            primaryStage.setFullScreen(true);
        });

        // Quit Button on the top-right
        Button quitButton = new Button();
        quitButton.setPrefSize(140, 40);
        quitButton.getStyleClass().addAll("bubble", "quit-btn", "image");
        quitButton.setOnAction(e -> System.exit(0)); // Properly exit the application

        // Top bar with Help and Quit buttons
        HBox bottomBar = new HBox(30);
        bottomBar.getChildren().addAll(helpButton, quitButton);
        layout.setBottom(bottomBar);

        // Game Mode label and buttons
        Label gameModeLabel = new Label("Choose Game Mode:");
        gameModeLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        Button aiButton = new Button();
        aiButton.getStyleClass().addAll("bubble", "image", "ai-btn");
        aiButton.setPrefSize(140, 40);
//        aiButton.setOnAction(e -> primaryStage.setScene(PlacementScene.getScene(primaryStage, true, clientConnection)));  // Assuming PlacementScene takes Client
        Button pvpButton = new Button();
        pvpButton.setPrefSize(140, 40);
        pvpButton.getStyleClass().addAll("bubble", "image", "pvp-btn");
        pvpButton.setOnAction(e -> {
            InviteScene scene = new InviteScene(primaryStage, clientConnection);
            primaryStage.setScene(scene.getScene());
            primaryStage.setFullScreen(true);
        });

        VBox centerBox = new VBox(10);
        centerBox.getChildren().addAll(gameModeLabel, aiButton, pvpButton);
        centerBox.setAlignment(Pos.CENTER);
        layout.setCenter(centerBox);
        layout.getStyleClass().add("background");

        layout.setTop(Helper.getTitle());

        Scene scene = new Scene(layout);
        scene.getStylesheets().add(InviteScene.class.getResource("style.css").toExternalForm());
        return scene;
    }
}
