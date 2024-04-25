package gameLogic;
// Simple class or struct to represent X,Y coordinates in a type-safe way.

public class Coordinate {
    private final int x;
    private final int y;

    public Coordinate(int x, int y) {
        // Add bounds checking if necessary
        if (x < 0 || x >= 10 || y < 0 || y >= 10) {
            throw new IllegalArgumentException("Coordinates out of bounds");
        }
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    // Optionally override equals and hashCode if you want to use Coordinates as keys in a map
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Coordinate)) return false;
        Coordinate that = (Coordinate) o;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return 31 * x + y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
