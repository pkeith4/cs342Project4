package client;

public class ClientGameUpdater {
    private ClientController controller;

    public ClientGameUpdater(ClientController controller) {
        this.controller = controller;
    }

    public void update(String message) {
        // Interpret the server's message and update the board
        System.out.println("Received update from server: " + message);
        controller.board.displayBoard();
    }
}
