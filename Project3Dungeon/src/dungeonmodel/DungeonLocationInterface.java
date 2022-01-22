package dungeonmodel;

import java.util.HashMap;

/**
 * A DungeonLocationInterface represents an individual location in the entire dungeon and points the
 * public method that ar usable by the user.
 */
public interface DungeonLocationInterface {

  /**
   * Returns all treasure available at the current dungeon location.
   *
   * @return the list of treasure available at the current dungeon location.
   */
  HashMap getAllTreasure();

  /**
   * Returns all weapon available at the current dungeon location.
   *
   * @return the list of weapon available at the current dungeon location.
   */
  HashMap getAllWeapon();

  /**
   * Returns true if monster is present in the present cave, false otherwise.
   *
   * @return true if monster is present in the present cave, false otherwise.
   */
  boolean isMonsterPresent();

  /**
   * Returns monster if present, null otherwise.
   *
   * @return monster if present, null otherwise.
   */
  Monster getMonster();


  /**
   * Returns the count of treasure available at the current dungeon location.
   *
   * @return the count of treasure available at the current dungeon location.
   */
  int getTotalTreasureCount();

  /**
   * Represent the count of the given treasure.
   *
   * @param treasureEnum is the treasure for which the count will be returned.
   * @return the count of the given treasure in the Dungeon location.
   */
  int getTreasureCountOf(TreasureEnum treasureEnum);


  /**
   * Returns the count of the specified weapon present at the current location.
   *
   * @param weapon the weapon to check
   * @return the count of the specified weapon present at the current location.
   */
  int getWeaponCountOf(Weapon weapon) throws IllegalArgumentException;

  /**
   * Gets the location of the location on a 2D plane.
   *
   * @return the location of the location on a 2D plane.
   */
  Coordinate2D getCoordinate();

  /**
   * Returns the id of the Dungeon location.
   *
   * @return the id of the Dungeon location.
   */
  int getDungeonId();

  @Override
  boolean equals(Object o);

  @Override
  int hashCode();

  @Override
  String toString();
}
