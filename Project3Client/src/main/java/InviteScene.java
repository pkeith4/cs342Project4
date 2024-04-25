import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class InviteScene {
    public static Scene getScene(Stage primaryStage, Client clientConnection) {
        // Create the BorderPane as the root layout
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        VBox inviteBox = new VBox();


        HBox inviteRow = new HBox(20);
        inviteRow.getStyleClass().add("box");
        Label label = new Label("Text"); // Create a label
        label.getStyleClass().add("invite-text");
        Button button = new Button("Invite"); // Create a button
        button.getStyleClass().add("invite-btn");

        inviteRow.getChildren().addAll(label, button);

        HBox inviteRow2 = new HBox();
        inviteRow2.getStyleClass().add("box");
        Label label2 = new Label("Text"); // Create a label
        label2.getStyleClass().add("invite-text");
        Button button2 = new Button("Invite"); // Create a button
        button2.getStyleClass().add("invite-btn");

        inviteRow2.getChildren().addAll(label2, button2);

        inviteBox.getChildren().addAll(inviteRow, inviteRow2);

        HBox listBoxes = new HBox(20); // spacing between list views
        listBoxes.setAlignment(Pos.CENTER);

        listBoxes.getChildren().addAll(inviteBox);

        root.setCenter(listBoxes);

        Scene scene = new Scene(root, 600, 400);
        scene.getStylesheets().add(InviteScene.class.getResource("style.css").toExternalForm());
        return scene;
    }
}
