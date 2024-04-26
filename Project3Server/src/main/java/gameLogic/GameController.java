// Interfaces with both the Board and the UI to update the game state based on user input and server messages.
package gameLogic;

public class GameController {
    private Player player1;
    private Player player2;
    private AtomicBoolean player1Initialized = new AtomicBoolean(false);
    private AtomicBoolean player2Initialized = new AtomicBoolean(false);
    private GameState gameState;

    public GameController(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.gameState = GameState.SETUP;
    }

    public synchronized void startGame() {
        gameState = GameState.IN_PROGRESS;
        player1.setTurn(true); // Randomly assign who starts or alternate
        notifyAll(); // Notify any waiting threads that game has started
    }

    public synchronized boolean initializeBoard(Player player, Board board) {
        boolean initialized = false;
        if (player == player1 && player1Initialized.compareAndSet(false, true)) {
            player1.setBoard(board);
            initialized = true;
        } else if (player == player2 && player2Initialized.compareAndSet(false, true)) {
            player2.setBoard(board);
            initialized = true;
        }

        if (!initialized) {
            throw new IllegalStateException("Player must be part of GameController");
        }

        // Check if both players are initialized to start the game
        if (player1Initialized.get() && player2Initialized.get()) {
            startGame(); // Start game automatically
            return true;
        }
        return false;
    }

    public synchronized boolean makeMove(int x, int y, Player player) {
        if (gameState != GameState.IN_PROGRESS)
            throw new IllegalStateException("The game is not in progress.");
        if (!player.isTurn())
            throw new IllegalStateException("It's not your turn, " + player.getName());

        Player opponent = (player == player1) ? player2 : player1;
        boolean result = player.makeGuess(x, y, opponent);

        if (opponent.isAllShipsSunk()) {
            gameState = GameState.FINISHED;
            notifyAll(); // Notify any waiting threads that game has finished
            System.out.println(player.getName() + " has won the game!");
        }
        return result;
    }

    public synchronized GameState getGameState() {
        return gameState;
    }

}
