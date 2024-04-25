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

import java.util.ArrayList;

public class InviteScene {
    private Stage primaryStage;
    private Client clientConnection;
    InviteScene(Stage primaryStage, Client clientConnection) {
        this.primaryStage = primaryStage;
        this.clientConnection = clientConnection;
    }
    public Scene getScene() {
        // Create the BorderPane as the root layout
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        HBox listBoxes = new HBox(75); // spacing between list views
        listBoxes.setAlignment(Pos.CENTER);
        listBoxes.setStyle("-fx-pref-height: 300;-fx-pref-width: 300;");

        addInviteBox(listBoxes);
        addAcceptBox(listBoxes);

        root.setCenter(listBoxes);

        Scene scene = new Scene(root, 600, 400);
        scene.getStylesheets().add(InviteScene.class.getResource("style.css").toExternalForm());
        return scene;
    }
    public void addInviteBox(HBox box) {
        VBox inviteBox = new VBox();
        inviteBox.setStyle("""
                -fx-border-color: black;
                -fx-border-style: solid;
                -fx-pref-width: 250px;
            """);

        HBox titleRow = new HBox();
        titleRow.setStyle("-fx-alignment: center;-fx-pref-height: 50");
        Label title = new Label("Queue List");
        title.setStyle("-fx-font-size: 15px");
        titleRow.getChildren().add(title);
        inviteBox.getChildren().add(titleRow);

        box.getChildren().addAll(inviteBox);
        this.clientConnection.getQueue(data -> {
            ArrayList<String> queue = data.getQueue();
            for (int i = queue.size() - 1; i >= 0; i--) {
                String username = data.getQueue().get(i);
                if (!username.equals(clientConnection.getUsername())) { // if it's not our username to the queue
                    HBox inviteRow = new HBox(20);
                    inviteRow.setStyle("""
                        -fx-padding: 3;
                        -fx-pref-width: 150px;
                        -fx-border-color: black;
                        -fx-border-style: solid;
                    """);
                    Label label = new Label(username); // Create a label
                    label.getStyleClass().add("invite-text");
                    Button button = new Button("Invite"); // Create a button
                    button.getStyleClass().add("invite-btn");

                    inviteRow.getChildren().addAll(label, button);
                    inviteBox.getChildren().add(inviteRow);
                }
            }
        });
        HBox inviteRow = new HBox(20);
        inviteRow.setStyle("""
                        -fx-padding: 3;
                        -fx-pref-width: 150px;
                        -fx-border-color: black;
                        -fx-border-style: solid;
                    """);
        Label label = new Label("dummy"); // Create a label
        label.getStyleClass().add("invite-text");
        Button button = new Button("Invite"); // Create a button
        button.getStyleClass().add("invite-btn");

        inviteRow.getChildren().addAll(label, button);
        inviteBox.getChildren().add(inviteRow);
    }
    public void addAcceptBox(HBox box) {
        VBox acceptBox = new VBox();
        acceptBox.setStyle("""
            -fx-border-color: black;
            -fx-border-style: solid;
            -fx-pref-width: 250px;
        """);
        acceptBox.getStyleClass().add("general-box");

        HBox titleRow = new HBox();
        titleRow.setStyle("-fx-alignment: center;-fx-pref-height: 50");
        Label title = new Label("Invites");
        title.setStyle("-fx-font-size: 15px");
        titleRow.getChildren().add(title);
        acceptBox.getChildren().add(titleRow);

        box.getChildren().addAll(acceptBox);
        clientConnection.onInviteRequest(data -> {
            HBox acceptRow = new HBox();
            acceptRow.setStyle("""
                        -fx-padding: 3;
                        -fx-pref-width: 150px;
                        -fx-border-color: black;
                        -fx-border-style: solid;
                    """);
            Label label = new Label(data.getUsername()); // Create a label
            label.getStyleClass().add("accept-text");
            Button button = new Button("Accept"); // Create a button
            button.getStyleClass().add("accept-btn");


            acceptRow.getChildren().addAll(label, button);
            acceptBox.getChildren().add(acceptRow);
        });


        HBox acceptRow = new HBox(20);
        acceptRow.setStyle("""
                        -fx-padding: 3;
                        -fx-pref-width: 150px;
                        -fx-border-color: black;
                        -fx-border-style: solid;
                    """);
        Label label = new Label("dummy");
        label.getStyleClass().add("accept-text");
        Button button = new Button("Accept");
        button.getStyleClass().add("accept-btn");

        acceptRow.getChildren().addAll(label, button);
        acceptBox.getChildren().add(acceptRow);
    }
}
