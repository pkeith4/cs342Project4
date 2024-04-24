package serverMessages;

import java.io.Serializable;

public class Shoot implements Serializable {
    private static final long serialVersionUID = 1;
    private boolean hit;
    private int[][] revealedShip;
    private boolean gameOver;

    public Shoot(boolean hit, int[][] revealedShip, boolean gameOver) {
       this.hit = hit;
       this.revealedShip = revealedShip;
       this.gameOver = gameOver;
    }

    // Get boolean whether a ship was hit or not
    public boolean getHit() { return hit; }
    /*
        Get array of rows & cols of the revealed ship
        The outer array will have the length of the ship that's being revealed
        the inner arrays will always be a length of 2, where the 0th index represents the row
        of the cell, and the 1st index represents the col of the cell to be revealed
        Given the following board: [ , , ]
                                   [ ,X,X]
                                   [ , , ]
        If X represents the ship that was revealed
        getRevealedShip will return [ [1,1], [1,2] ]

        If no ship is being revealed, getRevealedShip will return null
     */
    public int[][] getRevealedShip() { return this.revealedShip; }
    // return whether are not the game is over
    public boolean getGameOver() { return this.gameOver; }
}
