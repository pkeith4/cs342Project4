import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class HelpScene {

    public static Scene getScene(Stage primaryStage, Client clientConnection) {
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(10, 20, 10, 20));

        // Title for the help scene
        Text titleText = new Text("Game Instructions");
        titleText.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        VBox titleBox = new VBox(titleText);
        titleBox.setAlignment(Pos.CENTER);

        // Detailed instructions with bold headers using TextFlow
        TextFlow instructions = new TextFlow(
                new Text("Welcome to Battleship!\n\n"),
                new Text("Board Setup:\n"),
                new Text("Place your fleet of ships on a 10x10 grid. Your fleet includes ships of the following sizes:\n"),
                new Text("- One 2-cell ship\n"),
                new Text("- Two 3-cell ships\n"),
                new Text("- One 4-cell ship\n"),
                new Text("- One 5-cell ship\n\n"),
                new Text("Gameplay:\n"),
                new Text("After placing your ships, you will take turns with your opponent, either another player or the AI, " +
                        "to fire at chosen coordinates on the opponent's grid. The goal is to sink all of your opponent's ships " +
                        "before they sink all of yours.\n\n"),
                new Text("Turns:\n"),
                new Text("To play, select a grid cell to target during your turn and hit 'Fire'. The game updates both grids " +
                        "to show hits and misses, with hits marked as red and misses as white.")
        );

        // Applying bold style to headers
        ((Text)instructions.getChildren().get(1)).setStyle("-fx-font-weight: bold;");
        ((Text)instructions.getChildren().get(7)).setStyle("-fx-font-weight: bold;");
        ((Text)instructions.getChildren().get(9)).setStyle("-fx-font-weight: bold;");

        // Back button to return to the StartScene
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> primaryStage.setScene(StartScene.getScene(primaryStage, clientConnection)));
        backButton.setStyle("-fx-background-color: #fafafa; -fx-font-weight: bold;");
        BorderPane.setAlignment(backButton, Pos.BOTTOM_RIGHT);

        // Organizing the layout
        VBox centerContent = new VBox(10, instructions);
        centerContent.setAlignment(Pos.TOP_CENTER);

        layout.setTop(titleBox);
        layout.setCenter(centerContent);
        layout.setBottom(backButton);

        return new Scene(layout, 400, 300);
    }
}
