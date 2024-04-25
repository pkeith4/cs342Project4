package client;

public class ClientController {
    private ClientBoard board;
    private ClientGameUpdater gameUpdater;

    public ClientController() {
        this.board = new ClientBoard(10, 10);  // assuming 10x10 grid
        this.gameUpdater = new ClientGameUpdater(this);
    }

    public void startClient() {
        // Setup network connections, handle initial setup
        System.out.println("Client started. Setting up the game...");
    }

    public void handleUserInput(int x, int y) {
        // Simulate sending user input to the server
        System.out.println("Handling user input at (" + x + ", " + y + ")");
    }

    public void updateGameState(String message) {
        // Update game state based on server message
        gameUpdater.update(message);
    }
}
