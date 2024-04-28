package clientMessages;

import java.io.Serializable;

public class Shoot implements Serializable {
    private static final long serialVersionUID = 1;
    private final gameLogic.Coordinate coord;
    private boolean shotFromAI;

    public Shoot(gameLogic.Coordinate coord, boolean shotFromAI) {

        this.coord = coord;
        this.shotFromAI = shotFromAI;
    }

    public gameLogic.Coordinate getCoord() { return coord; }
    public boolean getShotFromAI() {
        return shotFromAI;
    }
}
