import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HelpScene {

    public static Scene getScene(Stage primaryStage, Client clientConnection) {
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(10, 10, 10, 10));

        // Title for the help scene
        Label title = new Label("Game Instructions");
        title.setStyle("-fx-font-size: 20px; -fx-text-alignment: center;");
        BorderPane.setAlignment(title, Pos.TOP_CENTER);

        // Dummy text for game instructions
        Label instructions = new Label("Place your ships on the game board and try to sink your opponent's ships before they sink all of yours.");
        instructions.setWrapText(true);

        // Back button to return to the StartScene
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> primaryStage.setScene(StartScene.getScene(primaryStage, clientConnection)));
        BorderPane.setAlignment(backButton, Pos.BOTTOM_RIGHT);

        // Organizing the layout
        VBox centerContent = new VBox(10, instructions);
        centerContent.setAlignment(Pos.TOP_CENTER);

        layout.setTop(title);
        layout.setCenter(centerContent);
        layout.setBottom(backButton);

        return new Scene(layout, 400, 300);
    }
}
