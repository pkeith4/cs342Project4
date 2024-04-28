//import javafx.geometry.Insets;
//import javafx.geometry.Pos;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
//import javafx.scene.layout.BorderPane;
//import javafx.scene.layout.HBox;
//import javafx.scene.layout.VBox;
//import javafx.scene.text.Text;
//import javafx.scene.text.TextFlow;
//import javafx.stage.Stage;
//
//public class HelpScene {
//
//    public static Scene getScene(Stage primaryStage, Client clientConnection) {
//        BorderPane layout = new BorderPane();
//        layout.setPadding(new Insets(10, 20, 10, 20));
//
//        layout.setTop(Helper.getTitle());
//        layout.setBottom(Helper.getBackButton(primaryStage, clientConnection));
//        layout.setCenter(instructions);
//        layout.getStyleClass().add("background");
//
//        Scene scene = new Scene(layout);
//        scene.getStylesheets().add(InviteScene.class.getResource("style.css").toExternalForm());
//        return scene;
//    }
//}


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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class HelpScene {

    public static Scene getScene(Stage primaryStage, Client clientConnection) {
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(10, 20, 10, 20));

        // Title for the help scene
        Text titleText = new Text("Game Instructions\n");
        titleText.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        VBox titleBox = new VBox(titleText);
        titleBox.setAlignment(Pos.CENTER);


        // Detailed instructions with bold headers using TextFlow
        Image instructionsImage = new Image("images/battleshipinstructions.png");
        ImageView imageView = new ImageView(instructionsImage);
        imageView.setFitWidth(1400); // Set the width of the image (adjust as necessary)
        imageView.setFitHeight(1200); // Set the height of the image (adjust as necessary)
        imageView.setPreserveRatio(true);


        layout.setTop(Helper.getBackButton(primaryStage, clientConnection));
        layout.setCenter(imageView);
        layout.getStyleClass().add("background");

        Scene scene = new Scene(layout);
        scene.getStylesheets().add(InviteScene.class.getResource("style.css").toExternalForm());
        return scene;
    }
}

