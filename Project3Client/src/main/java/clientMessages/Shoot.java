package clientMessages;

import java.io.Serializable;

public class Shoot implements Serializable {
    private static final long serialVersionUID = 1;
    private final int row;
    private final int col;

    public Shoot(int row, int col) {
        this.row = row;
        this.col = col;
    }

    // get row index that user wants to hit
    public int getRow() { return this.row; }
    // get column index that user wants to hit
    public int getCol() { return this.col; }
}
