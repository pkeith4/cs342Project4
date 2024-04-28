// Methods to place ships, check for hits/misses, and keep track of the shots taken.
package gameLogic;

import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;
import java.util.Random;

public class Board implements Serializable {
  public static final long serialVersionUID = 1;
  private final int width;
  private final int height;
  private char[][] grid;
  private List<Ship> ships;
  private Random random;

  public Board() {
    this.width = 10;
    this.height = 10;
    this.grid = new char[height][width];
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        grid[i][j] = '.';
      }
    }
    this.ships = new ArrayList<>();
    this.random = new Random();
  }

  public boolean placeShip(Ship ship) {
    ships.add(ship);
    int x = ship.getStartX();
    int y = ship.getStartY();

    for (int i = 0; i < ship.getSize(); i++) {
      if (ship.isVertical()) {
        grid[y + i][x] = 'S';
      } else {
        grid[y][x + i] = 'S';
      }
    }
    return true;
  }

  public boolean canPlaceShip(Ship ship) {
    int x = ship.getStartX();
    int y = ship.getStartY();
    if (ship.isVertical()) {
      if (y + ship.getSize() > height)
        return false;
      for (int i = 0; i < ship.getSize(); i++) {
        if (grid[y + i][x] != '.')
          return false;
      }
    } else {
      if (x + ship.getSize() > width)
        return false;
      for (int i = 0; i < ship.getSize(); i++) {
        if (grid[y][x + i] != '.')
          return false;
      }
    }
    return true;
  }

  public boolean recordHit(int x, int y) {
    if (grid[y][x] == 'S') {
      grid[y][x] = 'H';
      for (Ship ship : ships) {
        if (ship.isHit(x, y)) {
          return true;
        }
      }
    } else if (grid[y][x] == '.') {
      grid[y][x] = 'M'; // mark as miss
    }
    return false;
  }

  public char[][] getGrid() {
    return grid;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public List<Ship> getShips() {
    return this.ships;
  }

  // used by the ai to randomly place ships
  public void intializeShips() {
    int[] shipSizes = { 2, 3, 3, 4, 5 };
    for (int size : shipSizes) {
      boolean placed = false;
      while (!placed) {
        int x = random.nextInt(10);
        int y = random.nextInt(10);
        boolean vertical = random.nextBoolean();
        Ship ship = new Ship(size, x, y, vertical);
        if (canPlaceShip(ship)) {
          placeShip(ship);
          placed = true;
        }
      }
    }
  }

  // debugging func
  public void printBoard() {
    // Print column headers
    System.out.print(" ");
    for (int i = 0; i < width; i++) {
      System.out.print(i + " ");
    }
    System.out.println();
    for (int i = 0; i < height; i++) {
      // Print row headers
      System.out.print(i + "  ");
      for (int j = 0; j < width; j++) {
        System.out.print(grid[i][j] + " ");
      }
      System.out.println();
    }
  }
}
