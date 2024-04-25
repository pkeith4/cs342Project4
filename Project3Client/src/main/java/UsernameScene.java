import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class UsernameScene {

    private Client clientConnection;
    private String username;
    private TextField usernameInput;

    public UsernameScene(Client client) {
        this.clientConnection = client;
    }

    public Scene getScene(Stage primaryStage) {
        Label errorMsg = new Label();
        errorMsg.setStyle("-fx-text-fill: red;");
        usernameInput = new TextField();
        usernameInput.setMaxWidth(150);
        Button setUsernameButton = new Button("Set Username");

        setUsernameButton.setOnAction(e -> {
            String username = usernameInput.getText();
            if (!username.isEmpty()) {
                clientConnection.createUsername(username, data -> {
                    Platform.runLater(() -> {
                        if (data.getSuccess()) { // username is verified
                            this.username = username;
                            primaryStage.setScene(StartScene.getScene(primaryStage, clientConnection)); // Switch to StartScene
                        } else { // Username is not verified
                            errorMsg.setText("Username is already taken!");
                            resetErrorMessage(errorMsg);
                        }
                    });
                });

            } else {
                errorMsg.setText("Username field must be populated!");
                resetErrorMessage(errorMsg);
            }
        });

        VBox layout = new VBox(10, errorMsg, usernameInput, setUsernameButton);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(10));
        return new Scene(layout, 300, 200);
    }

    private void resetErrorMessage(Label errorMsg) {
        Duration duration = Duration.seconds(3);
        KeyFrame keyFrame = new KeyFrame(duration, event -> errorMsg.setText(""));
        Timeline timeline = new Timeline(keyFrame);
        timeline.setCycleCount(1);
        timeline.play();
    }
}
