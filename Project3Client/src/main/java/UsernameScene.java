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
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class UsernameScene {

    private Client clientConnection;
    private String username;
    private TextField usernameInput;
    private Button setUsernameButton;

    public UsernameScene(Client client) {
        this.clientConnection = client;
    }

    public Scene getScene(Stage primaryStage) {
        Label errorMsg = new Label();
        errorMsg.setStyle("-fx-text-fill: red;");
        usernameInput = new TextField();
        usernameInput.setPrefWidth(150);
        usernameInput.setMinWidth(100);
        usernameInput.setMaxWidth(200);

        setUsernameButton = new Button("Set Username");
        setUsernameButton.setPrefSize(120, 20);
        setUsernameButton.setMaxSize(140, 40);
        setUsernameButton.setMinSize(100, 20);

        setUsernameButton.setOnAction(e -> {
            String username = usernameInput.getText().trim(); // Trim any white space
            if (!username.isEmpty()) {
                clientConnection.createUsername(username, data -> {
                    Platform.runLater(() -> {
                        if (data.getSuccess()) { // Assuming getSuccess() method checks response from server
                            this.username = username;
                            primaryStage.setScene(StartScene.getScene(primaryStage, clientConnection));
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

        Image battleshipImage = new Image(getClass().getResourceAsStream("/images/battleship.png"));
        ImageView battleshipImageView = new ImageView(battleshipImage);
        battleshipImageView.setFitHeight(1250);
        battleshipImageView.setFitWidth(1500);
        battleshipImageView.setPreserveRatio(true);
        battleshipImageView.setTranslateX(180);
        battleshipImageView.setTranslateY(-20);

        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(battleshipImageView);
        BorderPane.setMargin(battleshipImageView, new Insets(20, 0, 0, 0));
        mainLayout.setCenter(layout);
        mainLayout.setStyle("-fx-background-image: url('/images/background.png'); " +
                "-fx-background-size: cover; " +
                "-fx-background-repeat: no-repeat; " +
                "-fx-background-position: center center;");

        return new Scene(mainLayout, 300, 200);
    }

    private void resetErrorMessage(Label errorMsg) {
        Duration duration = Duration.seconds(3);
        KeyFrame keyFrame = new KeyFrame(duration, event -> errorMsg.setText(""));
        Timeline timeline = new Timeline(keyFrame);
        timeline.setCycleCount(1);
        timeline.play();
    }
}
