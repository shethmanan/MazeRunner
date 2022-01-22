package dungeonmodel;

import java.util.Objects;

/**
 * Represents the location inside a dungeon in a 2D Co-ordinate System.
 */
public class Coordinate2D {
  private int xCord;
  private int yCord;

  /**
   * Initialize the coordinate of a location inside the dungeon.
   *
   * @param xCord the co-ordinate on x-axis
   * @param yCord the co-ordinate on y-axis
   */
  public Coordinate2D(int xCord, int yCord) {
    this.xCord = xCord;
    this.yCord = yCord;
  }

  /**
   * Gets x-coordinate of a location inside a dungeon.
   *
   * @return x-coordinate of a location inside a dungeon.
   */
  public int getX() {
    return xCord;
  }

  /**
   * y-coordinate of a location inside a dungeon.
   *
   * @return x-coordinate of a location inside a dungeon.
   */
  public int getY() {
    return yCord;
  }

  void setX(int xCord) {
    this.xCord = xCord;
  }

  void setY(int yCord) {
    this.yCord = yCord;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Coordinate2D that = (Coordinate2D) o;
    return xCord == that.xCord && yCord == that.yCord;
  }

  @Override
  public int hashCode() {
    return Objects.hash(xCord, yCord);
  }
}
