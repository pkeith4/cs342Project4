// Methods to place ships, check for hits/misses, and keep track of the shots taken.
package gameLogic;

import java.util.List;
import java.util.ArrayList;

public class Board {
    private final int width;
    private final int height;
    private char[][] grid;
    private List<Ship> ships;

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
    }

    public boolean placeShip(Ship ship) {
        if (canPlaceShip(ship)) {
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
        return false;
    }

    public boolean canPlaceShip(Ship ship) {
        int x = ship.getStartX();
        int y = ship.getStartY();
        if (ship.isVertical()) {
            if (y + ship.getSize() > height) return false;
            for (int i = 0; i < ship.getSize(); i++) {
                if (grid[y + i][x] != '.') return false;
            }
        } else {
            if (x + ship.getSize() > width) return false;
            for (int i = 0; i < ship.getSize(); i++) {
                if (grid[y][x + i] != '.') return false;
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

    public List<Ship> getShips() { return this.ships; }
}
