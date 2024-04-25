// Player-specific data such as their board and any flags indicating their status in the game.
package gameLogic;

import java.util.List;
import java.util.ArrayList;

public class Player {
    private String name;
    private Board board;
    private List<Ship> ships;
    private boolean isTurn;

    public Player(String name, int boardWidth, int boardHeight) {
        this.name = name;
        this.board = new Board(boardWidth, boardHeight);
        this.ships = new ArrayList<>();
        this.isTurn = false;
    }

    public boolean placeShip(Ship ship) {
        boolean placed = board.placeShip(ship);
        if (placed) {
            ships.add(ship);
        }
        return placed;
    }

    public boolean makeGuess(int x, int y, Player opponent) {
        if (isTurn) {
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
        } else {
            System.out.println(name + ", it's not your turn!");
            return false;
        }
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