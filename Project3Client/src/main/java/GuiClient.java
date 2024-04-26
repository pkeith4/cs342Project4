import javafx.application.Application;
import javafx.scene.Scene;
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
		primaryStage.setTitle("Battleship");

		// Initialize the network client
		clientConnection = new Client(); // Assuming Client is your custom class for handling network
		clientConnection.start(); // Assuming Client extends Thread

		setupUsernameScene();
		primaryStage.show();
	}

	private void setupUsernameScene() {
		UsernameScene usernameScene = new UsernameScene(clientConnection); // Passing the actual client object
		primaryStage.setScene(usernameScene.getScene(primaryStage)); // Ensuring getScene takes Stage as a parameter if needed
		primaryStage.setFullScreen(true);
	}

	public void setupStartScene() {
		primaryStage.setScene(StartScene.getScene(primaryStage, clientConnection));
		primaryStage.setFullScreen(true);
	}

	public void setupHelpScene() {
		primaryStage.setScene(HelpScene.getScene(primaryStage, clientConnection));
		primaryStage.setFullScreen(true);
	}
	public void setupInviteScene() {
		InviteScene scene = new InviteScene(primaryStage, clientConnection);
		primaryStage.setScene(scene.getScene());
		primaryStage.setFullScreen(true);
	}

	public void setupPlacementScene() {
		// Assuming PlacementScene is refactored to not extend Application
		PlacementScene scene = new PlacementScene(primaryStage, true, clientConnection);
		primaryStage.setScene(scene.getScene());
		primaryStage.setFullScreen(true);
	}
	public void setupGameScene() {
		// Assuming GameScene has a static method to create and return a Scene
		Scene gameScene = GameScene.createGameScene(primaryStage);
		primaryStage.setScene(gameScene);
		primaryStage.setFullScreen(true);
	}
	public void setupEndScene(boolean won) {
		Scene endScene = EndScene.createEndScene(won, primaryStage, clientConnection);
		primaryStage.setScene(endScene);
		primaryStage.setFullScreen(true);
	}
}
