import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.shape.Rectangle;

import java.util.function.Consumer;

public class InviteScene {
    private Stage primaryStage;
    private Client clientConnection;
    InviteScene(Stage primaryStage, Client clientConnection) {
        this.primaryStage = primaryStage;
        this.clientConnection = clientConnection;
    }
    public Scene getScene() {
        // send add to queue request to server
        clientConnection.addToQueue();
        // initialize on accept invite
        clientConnection.onAcceptInvite(data -> {
            Platform.runLater(() -> {
                // open placement scene
                PlacementScene scene = new PlacementScene(this.primaryStage, false, data.getUsername(), this.clientConnection);
                this.primaryStage.setScene(scene.getScene());
                this.primaryStage.setFullScreen(true);
            });
        });

        // Create the BorderPane as the root layout
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        HBox listBoxes = new HBox(75); // spacing between list views
        listBoxes.setAlignment(Pos.CENTER);
        listBoxes.setStyle("-fx-pref-height: 300;-fx-pref-width: 300;");

        addInviteBox(listBoxes);
        addAcceptBox(listBoxes);

        root.setTop(Helper.getTitle());
        root.getStyleClass().add("background");
        root.setCenter(listBoxes);
        root.setBottom(Helper.getBackButton(primaryStage, clientConnection, data -> {
            this.clientConnection.leaveQueue();
        }));

        Scene scene = new Scene(root, 600, 400);
        scene.getStylesheets().add(InviteScene.class.getResource("style.css").toExternalForm());
        return scene;
    }
    private void removeRow(VBox box, String username) {
        box.getChildren().removeIf(node -> {
            if (node instanceof HBox) {
                HBox hbox = (HBox) node;
                return hbox.getChildren().stream()
                    .filter(child -> child instanceof Label)
                    .map(child -> (Label) child)
                    .anyMatch(label -> username.equals(label.getText()));
            }
            return false;
        });
    }
    public void addAcceptRow(VBox box, String username, Client clientConnection) {
        HBox row = new HBox(20);
        row.setStyle("""
            -fx-padding: 3;
            -fx-pref-width: 150px;
        """);
        Label label = new Label(username); // Create a label
        label.getStyleClass().add("general-text");
        Button button = new Button("Accept");
        button.setOnAction(e -> {
            clientConnection.acceptInvite(username);
        });
        button.getStyleClass().addAll("general-btn", "small-round", "bubble");

        row.getChildren().addAll(label, button);
        box.getChildren().add(row);
    }
    public void addInviteRow(VBox box, String username, Client clientConnection) {
        HBox row = new HBox(20);
        row.setStyle("""
                        -fx-padding: 3;
                        -fx-pref-width: 150px;
                    """);
        Label label = new Label(username); // Create a label
        label.getStyleClass().add("general-text");
        Button button = new Button("Invite"); // Create a button
        button.setOnAction(e -> {
            clientConnection.inviteUser(username);
            button.setDisable(true);
            button.setText("Invited!");
        });
        button.getStyleClass().addAll("general-btn", "small-round", "bubble");

        row.getChildren().addAll(label, button);
        box.getChildren().add(row);
    }
    public void addRow(VBox box, String username, String btnText, EventHandler btnEvent) {
    }
    public void addInviteBox(HBox box) {
        VBox inviteBox = new VBox();
        inviteBox.setPrefWidth(275);
        inviteBox.setPrefHeight(Region.USE_COMPUTED_SIZE);
        inviteBox.getStyleClass().add("box");

        HBox titleRow = new HBox();
        titleRow.setStyle("-fx-alignment: center;-fx-pref-height: 50px");
        Label title = new Label("Queue List");
        title.setStyle("-fx-font-size: 15px");
        titleRow.getChildren().add(title);
        inviteBox.getChildren().add(titleRow);
        inviteBox.setPadding(new Insets(0, 0, 0, 10));

        ScrollPane inviteScroll = new ScrollPane();
        inviteScroll.getStyleClass().add("large-bubble");
        inviteScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        inviteScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        inviteScroll.setContent(inviteBox);
        inviteScroll.setPrefWidth(275);
        inviteScroll.setMaxHeight(350);
        box.getChildren().add(inviteScroll);

        // initialize on queue change callback
        this.clientConnection.getQueue(data -> {
            Platform.runLater(() -> {
                for (String username : data.getQueue()) {
                    if (username.equals(clientConnection.getUsername())) // if it's our username
                        continue;
                    addInviteRow(inviteBox, username, this.clientConnection);

                }
            });

            // initialize on queue change callback
            this.clientConnection.onQueueChange(queueChange -> {
                Platform.runLater(() -> {
                    if (queueChange.getAddition()) {
                        addInviteRow(inviteBox, queueChange.getChange(), this.clientConnection);
                    } else {
                        removeRow(inviteBox, queueChange.getChange());
                    }
                });
            });
        });
    }
    public void addAcceptBox(HBox box) {
        VBox acceptBox = new VBox();
        acceptBox.setPrefWidth(275);
        acceptBox.setPrefHeight(Region.USE_COMPUTED_SIZE);
        acceptBox.getStyleClass().add("accept-box");

        HBox titleRow = new HBox();
        titleRow.setStyle("-fx-alignment: center;-fx-pref-height: 50px");
        Label title = new Label("Invite List");
        title.setStyle("-fx-font-size: 15px");
        titleRow.getChildren().add(title);
        acceptBox.getChildren().add(titleRow);
        acceptBox.setPadding(new Insets(0, 0, 0, 10));

        ScrollPane acceptScroll = new ScrollPane();
        acceptScroll.getStyleClass().add("large-bubble");
        acceptScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        acceptScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        acceptScroll.setContent(acceptBox);
        acceptScroll.setPrefWidth(275);
        acceptScroll.setMaxHeight(350);
        box.getChildren().add(acceptScroll);
        // initialize on invite request callback
        this.clientConnection.onInviteRequest(inviteRequest -> {
            Platform.runLater(() -> {
                addAcceptRow(acceptBox, inviteRequest.getUsername(), this.clientConnection);
            });
        });
    }
}
