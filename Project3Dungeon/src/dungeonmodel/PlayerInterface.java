package dungeonmodel;

import java.util.HashMap;

/**
 * Represent the player playing the Dungeon game and lists methods that can be used by the player.
 */
public interface PlayerInterface {
  /**
   * Gets the current location of the player on the dungeon.
   *
   * @return the current location of the player on the dungeon.
   */
  DungeonLocationInterface getCurrentLocationOfPlayer();

  /**
   * Returns the list of treasure collected by the player.
   *
   * @return the list of treasure collected by the player.
   */
  HashMap getAllTreasure();

  /**
   * Returns all the weapon currently held by the player.
   *
   * @return a map indicating the weapon name and it's count.
   */
  HashMap getAllWeapon();


  /**
   * Prints a description of treasure collected by the player so far.
   *
   * @return a string containing the type and count of treasure collected by the player.
   */
  String printCollectedTreasure();

  /**
   * Gets the player current location and the treasure collected so far.
   *
   * @return string representation of the player location and treasure collected.
   */
  String getPlayerDetail();

  /**
   * Gets the player's health.
   *
   * @return the player's health.
   */
  int getPlayerHealth();
}
