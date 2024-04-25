import javafx.application.Application;
import javafx.stage.Stage;

public class GuiClient extends Application {
	private Stage primaryStage;
	private Client clientConnection; // Assuming Client handles network interactions

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		primaryStage.setTitle("Messaging App");

		// Initialize the network client
		clientConnection = new Client(); // Assuming Client is your custom class for handling network
		clientConnection.start(); // Assuming Client extends Thread

		setupUsernameScene();
		primaryStage.show();
	}

	private void setupUsernameScene() {
		UsernameScene usernameScene = new UsernameScene(clientConnection); // Passing the actual client object
		primaryStage.setScene(usernameScene.getScene(primaryStage)); // Ensuring getScene takes Stage as a parameter if needed
	}

	public void setupStartScene() {
		primaryStage.setScene(StartScene.getScene(primaryStage, clientConnection));
	}

	public void setupHelpScene() {
		primaryStage.setScene(HelpScene.getScene(primaryStage, clientConnection));
	}
}
