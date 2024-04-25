// Checks if a move is valid given the current state of the Board and the GameState.
package gameLogic;

public class MoveValidator {

    public static boolean isValidMove(int x, int y, Board board) {
        // Check if the coordinates are within the board limits
        return x >= 0 && x < board.getWidth() && y >= 0 && y < board.getHeight() && board.getGrid()[y][x] == '.';
    }

    public static boolean isValidPlacement(Ship ship, Board board) {
        // This can include more complex rules, checking for overlapping ships or out of bounds placement
        return board.canPlaceShip(ship);
    }
}