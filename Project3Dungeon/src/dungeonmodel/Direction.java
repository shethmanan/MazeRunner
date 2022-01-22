package dungeonmodel;

/**
 * Represents the directions a Player can travel in a dungeon.
 */
public enum Direction {
  NORTH,
  SOUTH,
  EAST,
  WEST;

  /**
   * Returns the opposite direction of the direction object on which it is called.
   *
   * @return the opposite direction of the direction object on which it is called.
   */
  public Direction getOpposite() {
    if (this == NORTH) {
      return SOUTH;
    } else if (this == SOUTH) {
      return NORTH;
    } else if (this == EAST) {
      return WEST;
    }
    return EAST;
  }
}
