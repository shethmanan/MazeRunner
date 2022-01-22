package dungeonmodel;

/**
 * This class represents all the Treasure that may be available in the dungeon.
 */
public enum TreasureEnum {
  DIAMOND(1),
  RUBY(1),
  SAPPHIRE(1);
  private int point;

  /**
   * Constructs the enum with given value.
   *
   * @param point represent the number of point the treasure provides.
   */
  TreasureEnum(int point) {
    this.point = point;
  }

  /**
   * Gets the point each treasure provides.
   *
   * @return the point each treasure provides.
   */
  public int getSequence() {
    return point;
  }
}
