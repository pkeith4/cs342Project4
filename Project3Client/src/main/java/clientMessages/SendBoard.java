package clientMessages;

public class SendBoard {
    public int[][][] ships;

    public SendBoard(int[][][] ships) {
        this.ships = ships;
    }

    /*
        Get the list of ships that we're passed by the client
        Same format as getRevealedShip from Hit class, except it's an array of ships instead
        Each item in the outer array represents a different ship
        Each item in the inner (center) array represents a different cell
        Each item the inner-inner array represents the row & col of the cell,
        where the item in the 0th index is the row and the item in the 1st index is the col
        Here's an example
        Client sending board:
        [ X | X | X ]
        [   |   |   ]
        [ X |   |   ]
        [ X |   |   ]
        Ships array:
        [
            [[0,0], [0,1], [0,2]], // 3 length ship at the top
            [[2,0], [3,0]] // 2 length ship at the bottom
        ]
    */
    public int[][][] getShips() { return this.ships; }

 }