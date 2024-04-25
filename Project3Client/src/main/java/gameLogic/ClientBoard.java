package client;

public class ClientBoard {
    private int width;
    private int height;
    private char[][] grid;

    public ClientBoard(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new char[height][width];
        initializeBoard();
    }

    private void initializeBoard() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                grid[i][j] = '.';
            }
        }
    }

    public void displayBoard() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
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
}
