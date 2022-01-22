package dungeonmodel;

/**
 * Represents monster that are present in the dungeon.
 */
public interface Monster {

  /**
   * Gets the type of monster.
   *
   * @return the type of monster.
   */
  String getType();

  /**
   * Gets the health of the monster which should be between 0 and 100 inclusive.
   *
   * @return the health of the monster.
   */
  int getHealth();

  /**
   * Gets the radius which specifies the number of location where a monster will smell, around
   * his current location.
   *
   * @return the smell radius around the monster.
   */
  int getSmellRadius();

  /**
   * Gets the damage done by the monster to the player.
   *
   * @return the damage done by the monster to the player.
   */
  int getDamage();

  /**
   * Gets a new monster with the health specified.
   *
   * @return a new monster with the health specified.
   */
  Monster getCopy(int health);


  @Override
  String toString();
}
