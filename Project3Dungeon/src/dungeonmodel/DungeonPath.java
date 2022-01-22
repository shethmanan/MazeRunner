package dungeonmodel;

/**
 * Represent a possible path within the dungeon, which contains point1, point2 and direction from
 * point1 to point2. The points will always be stored such that point1 <= point2 in the dungeon.
 */
class DungeonPath {
  private DungeonLocation location1;
  private DungeonLocation location2;
  private Direction direction;

  /**
   * Constructs the path with the given fields.
   *
   * @param location1     represents a point on the dungeon.
   * @param location2     represents another point to which location1  connects.
   * @param direction the direction from point1 to point2.
   */
  DungeonPath(DungeonLocation location1, DungeonLocation location2, Direction direction)
          throws IllegalArgumentException {
    if (location1 == null || location2 == null) {
      throw new IllegalArgumentException("Dungeon passed as parameters cannot be null");
    }
    if (direction == null) {
      throw new IllegalArgumentException("Direction cannot be null");
    }
    if (location1.getDungeonId() <= location2.getDungeonId()) {
      this.location1 = location1;
      this.location2 = location2;
      this.direction = direction;
    } else {
      this.location1 = location1;
      this.location2 = location2;
      this.direction = direction.getOpposite();
    }
  }

  /**
   * Returns the first dungeon location.
   *
   * @return the first dungeon location.
   */
  public DungeonLocation getLocation1() {
    return location1;
  }

  /**
   * Returns the second dungeon location.
   *
   * @return the second dungeon location.
   */
  public DungeonLocation getLocation2() {
    return location2;
  }

  /**
   * Gets the direction from location1 to location2.
   *
   * @return the direction from location1 to location2.
   */
  public Direction getDirection() {
    return direction;
  }

  DungeonPath getDeepCopy() {
    return new DungeonPath(location1.getDeepCopy(), location2.getDeepCopy(), direction);
  }

  @Override
  public String toString() {
    return "DungeonPath{"
            + "location1=" + location1.getDungeonId()
            + ", location2=" + location2.getDungeonId()
            + ", direction=" + direction
            + '}';
  }
}
