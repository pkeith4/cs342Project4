
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class GuiServer extends Application{
	HashMap<String, Scene> sceneMap;
	Server serverConnection;
	ListView<String> listItems;
	ListView<String> activeUsers;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		serverConnection = new Server(data -> {
			LocalDateTime now = LocalDateTime.now(); // get local datetime

			// define format pattern
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd HH:mm:ss");

			String formattedTime = now.format(formatter);
			Platform.runLater(() -> {
				listItems.getItems().add(formattedTime + ": " + data.toString());
			});
		});
		serverConnection.onClientChange(data -> {
			Platform.runLater(() -> {
				activeUsers.getItems().clear(); // clear old user logs
				activeUsers.getItems().add("User Log:");
				for (int i = 0; i < data.size(); i++) {
					String username = data.get(i);
					if (username == null)
						username = "Not Set";
					activeUsers.getItems().add("Client #" + i + " (" + username + ")");
				}
			});
		});

		listItems = new ListView<>();
		activeUsers = new ListView<>();
		activeUsers.getItems().add("User Log:");
		sceneMap = new HashMap<>();
		sceneMap.put("server",  createServerGui());

		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent t) {
				Platform.exit();
				System.exit(0);
			}
		});

		primaryStage.setScene(sceneMap.get("server"));
		primaryStage.setTitle("Server Logger");
		primaryStage.show();
	}

	public Scene createServerGui() {
		BorderPane layout = new BorderPane();

		BorderPane logPane = new BorderPane();
		logPane.setPrefWidth(400);
		logPane.setCenter(listItems);
		logPane.setStyle("-fx-border-color: orange; -fx-border-width: 2; -fx-font-family: 'serif'");

		BorderPane userPane = new BorderPane();
		userPane.setPrefWidth(200);
		userPane.setCenter(activeUsers);
		userPane.setStyle("-fx-border-color: orange; -fx-border-width: 2; -fx-font-family: 'serif'");

		layout.setLeft(logPane);
		layout.setRight(userPane);
		return new Scene(layout, 650, 400);
	}


}
