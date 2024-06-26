import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class UsernameScene {

    private Client clientConnection;
    private TextField usernameInput;
    private Button setUsernameButton;

    public UsernameScene(Client client) {
        this.clientConnection = client;
    }

    public Scene getScene(Stage primaryStage) {
        Label errorMsg = new Label();
        errorMsg.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
        usernameInput = new TextField();
        usernameInput.setPrefWidth(150);
        usernameInput.setMinWidth(100);
        usernameInput.setMaxWidth(200);
        usernameInput.getStyleClass().add("bubble");

        setUsernameButton = new Button("Set Username");
        setUsernameButton.setPrefSize(120, 30);
        setUsernameButton.setMaxSize(140, 30);
        setUsernameButton.setMinSize(100, 30);
        setUsernameButton.getStyleClass().add("bubble");

        setUsernameButton.setOnAction(e -> {
            String username = usernameInput.getText().trim(); // Trim any white space
            if (!username.isEmpty()) {
                clientConnection.createUsername(username, data -> {
                    Platform.runLater(() -> {
                        if (data.getSuccess()) { // Assuming getSuccess() method checks response from server
                            primaryStage.setScene(StartScene.getScene(primaryStage, clientConnection));
                            primaryStage.setFullScreen(true);
                        } else {
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
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(10));

        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(Helper.getTitle());
        mainLayout.setCenter(layout);
        mainLayout.getStyleClass().add("background");

        Scene scene = new Scene(mainLayout);
        scene.getStylesheets().add(InviteScene.class.getResource("style.css").toExternalForm());
        return scene;
    }

    private void resetErrorMessage(Label errorMsg) {
        Duration duration = Duration.seconds(3);
        KeyFrame keyFrame = new KeyFrame(duration, event -> errorMsg.setText(""));
        Timeline timeline = new Timeline(keyFrame);
        timeline.setCycleCount(1);
        timeline.play();
    }
}
