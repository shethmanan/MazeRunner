package dungeonmodel;


/**
 * Represents a collection of methods the user can call throughout the game to play.
 */
public interface GameInterface extends GameReadonlyInterface {


  /**
   * Moves the player from his current location to the specified direction.
   *
   * @param direction character representing the direction the player should move. (n->North, s->
   *                  South, e->East, w->West)
   * @return true if the player was able to move in the specified direction, false otherwise
   * @throws IllegalArgumentException when input character is not IN (n,s,e,w)
   */
  boolean movePlayerToDirection(char direction) throws IllegalArgumentException;

  /**
   * Collects the specified unit of given treasure.
   *
   * @param treasure      the treasure to be collected by the user.
   * @param unitToCollect the number of units of the given treasure to collect
   * @return true if specified treasure collected successfully, false otherwise
   * @throws IllegalArgumentException when null is given in place of a treasure
   *                                  when unit to collect is more than the count that exists
   */
  boolean collectATreasure(TreasureEnum treasure, int unitToCollect)
          throws IllegalArgumentException;

  /**
   * Collects the specified unit of given weapon.
   *
   * @param weapon        the weapon to be collected by the user
   * @param unitToCollect the number of units of the given weapon to collect
   * @return true if specified weapon collected successfully, false otherwise
   * @throws IllegalArgumentException when null is given in place of a weapon
   *                                  when unit to collect is more than the count that exists at
   *                                  the location
   */
  boolean collectAWeapon(Weapon weapon, int unitToCollect)
          throws IllegalArgumentException;

  /**
   * Collect all treasure present at player's current location.
   *
   * @return true if treasure is collected, false otherwise.
   */
  boolean collectTreasure();


  /**
   * Attempts to kill the monster at player's current location with the specified weapon. The
   * weapon will be fired in the mentioned direction and distance.
   *
   * @param weapon          the weapon used to kill the monster
   * @param attackDirection the direction in which weapon will be fired
   * @param distance        the distance at which the weapon will be fired
   * @return true of monster killed, false otherwise
   * @throws IllegalArgumentException when weapon is null
   *                                  when direction is null
   *                                  when direction is not one of the permissible direction
   *                                  based on player's current location
   *                                  when distance is less than 1
   */
  ArrowTraversalModelInterface killMonster(Weapon weapon, Direction attackDirection, int distance)
          throws IllegalArgumentException;

  /**
   * Restart the game.
   */
  void restartGame();

}
