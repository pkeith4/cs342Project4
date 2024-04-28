package serverMessages;

import java.io.Serializable;

public class Shoot implements Serializable {
    private static final long serialVersionUID = 1;
    private boolean hit;
    private gameLogic.Coordinate[] revealedShip;
    private boolean gameOver;
    private final gameLogic.Coordinate coord;
    private boolean shotFromAI;

    public Shoot(gameLogic.Coordinate coord, boolean hit, gameLogic.Coordinate[] revealedShip, boolean gameOver, boolean shotFromAI) {
        this.coord = coord;
        this.hit = hit;
        this.revealedShip = revealedShip;
        this.gameOver = gameOver;
        this.shotFromAI = shotFromAI;
    }

    public gameLogic.Coordinate getCoordinate() { return coord; }
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
    public gameLogic.Coordinate[] getRevealedShip() { return this.revealedShip; }
    // return whether are not the game is over
    public boolean getGameOver() { return this.gameOver; }
}
