import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
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
import javafx.util.Duration;

import java.util.ArrayList;

public class InviteScene {
    private Stage primaryStage;
    private Client clientConnection;
    private VBox inviteBox;
    InviteScene(Stage primaryStage, Client clientConnection) {
        this.primaryStage = primaryStage;
        this.clientConnection = clientConnection;
        inviteBox = new VBox();
    }
    public Scene getScene() {
        // send add to queue request to server
        clientConnection.addToQueue();
        // initialize on accept invite
        clientConnection.onAcceptInvite(data -> {
            System.out.println(data.getSuccess());
            System.out.println(data.getUsername());
        });

        // Create the BorderPane as the root layout
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        HBox listBoxes = new HBox(75); // spacing between list views
        listBoxes.setAlignment(Pos.CENTER);
        listBoxes.setStyle("-fx-pref-height: 300;-fx-pref-width: 300;");

        listBoxes.getChildren().add(inviteBox);

        setInviteBox();
        inviteRefresher();

        addAcceptBox(listBoxes);

        root.setTop(Helper.getTitle());
        root.getStyleClass().add("background");
        root.setCenter(listBoxes);

        Scene scene = new Scene(root, 600, 400);
        scene.getStylesheets().add(InviteScene.class.getResource("style.css").toExternalForm());
        return scene;
    }
    public void inviteRefresher() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    setInviteBox();
                })
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
    public void setInviteBox() {
        inviteBox.getChildren().clear(); // clear previous instance
        inviteBox.setStyle("""
                -fx-pref-width: 250px;
                -fx-pref-height: 500px;
            """);
        inviteBox.getStyleClass().addAll("general-box", "large-bubble");

        HBox titleRow = new HBox();
        titleRow.setStyle("-fx-alignment: center;-fx-pref-height: 50px");
        Label title = new Label("Queue List");
        title.setStyle("-fx-font-size: 15px");
        titleRow.getChildren().add(title);
        inviteBox.getChildren().add(titleRow);

        this.clientConnection.getQueue(data -> {
            Platform.runLater(() -> {
                System.out.println(data.getQueue());
                for (String username : data.getQueue()) {
                    if (username.equals(clientConnection.getUsername())) // if it's our username
                        continue;
                    HBox inviteRow = new HBox(20);
                    inviteRow.setStyle("""
                        -fx-padding: 3;
                        -fx-pref-width: 150px;
                    """);
                    Label label = new Label(username); // Create a label
                    label.getStyleClass().add("invite-text");
                    Button button = new Button("Invite"); // Create a button
                    button.setOnAction(e -> { // when invite button is pressed
                        // send invite request to other client
                        clientConnection.inviteUser(username);
                    });
                    button.getStyleClass().add("invite-btn");

                    inviteRow.getChildren().addAll(label, button);
                    inviteBox.getChildren().add(inviteRow);
                }
            });
        });
    }
    public void addAcceptBox(HBox box) {
        VBox acceptBox = new VBox();
        acceptBox.setStyle("""
            -fx-pref-width: 250px;
            -fx-pref-height: 500px;
        """);
        acceptBox.getStyleClass().addAll("general-box", "large-bubble");

        HBox titleRow = new HBox();
        titleRow.setStyle("-fx-alignment: center;-fx-pref-height: 50;");
        Label title = new Label("Invites");
        title.setStyle("-fx-font-size: 15px");
        titleRow.getChildren().add(title);
        acceptBox.getChildren().add(titleRow);
        box.getChildren().addAll(acceptBox);
        clientConnection.onInviteRequest(data -> {
            Platform.runLater(() -> {
                HBox acceptRow = new HBox();
                acceptRow.setStyle("""
                    -fx-padding: 10 3;
                    -fx-pref-width: 150px;
                                        """);
                Label label = new Label(data.getUsername()); // Create a label
                label.getStyleClass().add("accept-text");
                Button button = new Button("Accept"); // Create a button
                button.getStyleClass().add("accept-btn");

                acceptRow.getChildren().addAll(label, button);
                acceptBox.getChildren().add(acceptRow);
            });
        });
    }
}
