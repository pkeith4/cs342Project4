// Player-specific data such as their board and any flags indicating their status in the game.
package gameLogic;

import java.util.List;
import java.util.ArrayList;

public class Player {
    private String name;
    private Board board;
    private List<Ship> ships;
    private boolean isTurn;

    public Player(String name) {
        this.name = name;
        this.board = new Board();
        this.ships = new ArrayList<>();
        this.isTurn = false;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public boolean makeGuess(int x, int y, Player opponent) {
        if (!isTurn)
            throw new IllegalStateException(name + ", it's not your turn!");
        boolean hit = opponent.receiveAttack(x, y);
        if (hit) {
            // temporary logic need to figure out how to represent
            System.out.println(name + " hit a ship at (" + x + ", " + y + ")!");
        } else {
            // temprorary logic
            System.out.println(name + " missed at (" + x + ", " + y + ").");
        }
        isTurn = false;
        opponent.setTurn(true);
        return hit;
    }

    public boolean receiveAttack(int x, int y) {
        return board.recordHit(x, y);
    }

    public void setTurn(boolean isTurn) {
        this.isTurn = isTurn;
    }

    public boolean isAllShipsSunk() {
        for (Ship ship : ships) {
            if (!ship.isSunk()) {
                return false;
            }
        }
        return true;
    }

    public Board getBoard() {
        return board;
    }

    public String getName() {
        return name;
    }

    public boolean isTurn() {
        return this.isTurn;
    }
}