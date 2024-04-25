// Interfaces with both the Board and the UI to update the game state based on user input and server messages.
package gameLogic;

public class GameController {
    private Player player1;
    private Player player2;
    private boolean player1Initialized;
    private boolean player2Initialized;
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

    public boolean initializeBoard(Player player, Board board) {
        if (player == player1) {
            player1Initialized = true;
            player1.setBoard(board);
        } else if (player == player2) {
            player2Initialized = true;
            player2.setBoard(board);
        } else {
            throw new IllegalStateException("Player must be apart of GameController");
        }

        if (player1Initialized && player2Initialized) {
            startGame(); // start game automatically
            return true;
        }
        return false;
    }
    public boolean makeMove(int x, int y, Player player) {
        if (gameState != GameState.IN_PROGRESS)
            throw new IllegalStateException("The game is not in progress.");
        if (!player.isTurn())
            throw new IllegalStateException("It's not your turn, " + player.getName());
        Player opponent = (player == player1) ? player2 : player1;
        boolean result = player.makeGuess(x, y, opponent);
        if (opponent.isAllShipsSunk()) {
            System.out.println(player.getName() + " has won the game!");
            gameState = GameState.FINISHED;
        }
        return result;
    }

    public GameState getGameState() {
        return gameState;
    }

}
