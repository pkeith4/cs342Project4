import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class HelpScene {

    public static Scene getScene(Stage primaryStage, Client clientConnection) {
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(10, 20, 10, 20));

        // Back button to return to the StartScene
        Button backButton = new Button();
        backButton.setPrefSize(140, 40);
        backButton.getStyleClass().addAll("bubble", "image", "back-btn");
        backButton.setOnAction(e -> {
            primaryStage.setScene(StartScene.getScene(primaryStage, clientConnection));
            primaryStage.setFullScreen(true);
        });


        // Title for the help scene
        Text titleText = new Text("Game Instructions\n");
        titleText.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        VBox titleBox = new VBox(titleText);
        titleBox.setAlignment(Pos.CENTER);
        // Detailed instructions with bold headers using TextFlow
        TextFlow instructions = new TextFlow(
                titleText,
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
        instructions.setStyle("-fx-text-alignment: center;");

        // Applying bold style to headers
        ((Text)instructions.getChildren().get(2)).setStyle("-fx-font-weight: bold;");
        ((Text)instructions.getChildren().get(8)).setStyle("-fx-font-weight: bold;");
        ((Text)instructions.getChildren().get(10)).setStyle("-fx-font-weight: bold;");

        layout.setTop(Helper.getTitle());
        layout.setBottom(backButton);
        layout.setCenter(instructions);
        layout.getStyleClass().add("background");

        Scene scene = new Scene(layout);
        scene.getStylesheets().add(InviteScene.class.getResource("style.css").toExternalForm());
        return scene;
    }
}
