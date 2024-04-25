// Interfaces with both the Board and the UI to update the game state based on user input and server messages.
package gameLogic;

public class GameController {
    private Player player1;
    private Player player2;
    private GameState gameState;

    public GameController(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.gameState = GameState.SETUP;
    }

    public void startGame() {
        gameState = GameState.IN_PROGRESS;
        player1.setTurn(true); // Randomly assign who starts or alternate
    }

    public boolean makeMove(int x, int y, Player player) {
        if (gameState != GameState.IN_PROGRESS) {
            System.out.println("The game is not in progress.");
            return false;
        }

        if (player.isTurn()) {
            Player opponent = (player == player1) ? player2 : player1;
            boolean result = player.makeGuess(x, y, opponent);
            if (opponent.isAllShipsSunk()) {
                System.out.println(player.getName() + " has won the game!");
                gameState = GameState.FINISHED;
            }
            return result;
        } else {
            System.out.println("It's not your turn, " + player.getName());
            return false;
        }
    }

    public GameState getGameState() {
        return gameState;
    }
}
