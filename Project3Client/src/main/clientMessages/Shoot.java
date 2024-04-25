package clientMessages;

import java.io.Serializable;

public class Shoot implements Serializable {
    private static final long serialVersionUID = 1;
    private final gameLogic.Coordinate coord;

    public Shoot(gameLogic.Coordinate coord) {
        this.coord = coord;
    }

    public gameLogic.Coordinate getCoord() { return coord; }
}
