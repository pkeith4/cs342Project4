// Player-specific data such as their board and any flags indicating their status in the game.
package gameLogic;

import java.util.List;
import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable {
  public static final long serialVersionUID = 1;
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

  public void setShips(List<Ship> ships) {
    this.ships = ships;
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
    System.out.println("this my ships");
    System.out.println(this.ships);
    for (Ship ship : ships) {
      System.out.println(String.valueOf(ship.getStartX()) + ", " + String.valueOf(ship.getStartY()) + ") "
          + String.valueOf(ship.isVertical()) + " " + String.valueOf(ship.getSize()));
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
