// Properties like length, position, hit status; methods to check if it's sunk.
package gameLogic;

import java.io.Serializable;

public class Ship implements Serializable {
  public static final long serialVersionUID = 1;
  private int size;
  private int startX;
  private int startY;
  private boolean isVertical;
  private boolean[] hits;

  public Ship(int size, int startX, int startY, boolean isVertical) {
    this.size = size;
    this.startX = startX;
    this.startY = startY;
    this.isVertical = isVertical;
    this.hits = new boolean[size];
  }

  public boolean isHit(int x, int y) {
    if (isVertical) {
      if (x == startX && y >= startY && y < startY + size) {
        hits[y - startY] = true;
        return true;
      }
    } else {
      if (y == startY && x >= startX && x < startX + size) {
        hits[x - startX] = true;
        return true;
      }
    }
    return false;
  }

  public boolean isSunk() {
    for (boolean hit : hits) {
      if (!hit)
        return false;
    }
    return true;
  }

  public int getSize() {
    return size;
  }

  public int getStartX() {
    return startX;
  }

  public int getStartY() {
    return startY;
  }

  public boolean isVertical() {
    return isVertical;
  }
}
