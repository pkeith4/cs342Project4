package gameLogic;

import java.util.Random;

public class AIPlayer {
    private Board board;
    private Random random;
    private Player player;

    public AIPlayer() {
        this.board = new Board();
        this.random = new Random();
        this.player = new Player("AI");
        // need to implement random placement
        intializeAIShips();
//        this.player.setBoard(this.board);
        this.player.setShips(this.board.getShips());
    }

    private void intializeAIShips() {
        board.intializeShips();
        board.printBoard();
    }

    public Coordinate makeMove() {
        // Implement the logic to decide a move
        return decideMove();
    }

    private Coordinate decideMove() {
        int x, y;
        do {
            x = random.nextInt(10);
            y = random.nextInt(10);
        } while (!MoveValidator.isValidMove(x, y, board));

        return new Coordinate(x, y);
    }

    public Player getPlayer() {
        return player;
    }

    public Board getBoard() {
        return board;
    }
}