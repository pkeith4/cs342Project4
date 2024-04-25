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
}
