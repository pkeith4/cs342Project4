// Takes server messages and updates the local Board and GameState accordingly.
package gameLogic;

public class GameUpdater {
    private GameController gameController;

    public GameUpdater(GameController gameController) {
        this.gameController = gameController;
    }

    public void updateGame(int x, int y, Player player) {
        if (gameController.getGameState() == GameState.IN_PROGRESS) {
            if (MoveValidator.isValidMove(x, y, player.getBoard())) {
                boolean hit = gameController.makeMove(x, y, player);
                if (hit) {
                    System.out.println("Hit at " + x + ", " + y);
                } else {
                    System.out.println("Miss at " + x + ", " + y);
                }
            } else {
                System.out.println("Invalid move by " + player.getName());
            }
        }
    }
}