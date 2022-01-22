package dungeonmodel;

import java.util.List;

/**
 * Contains a set of read-only methods on the model which can be exposed to the view.
 */
public interface GameReadonlyInterface {
  /**
   * Get the dungeon object.
   *
   * @return the dungeon object
   */
  DungeonInterface getDungeon();

  /**
   * Get the location from where the player should start playing.
   *
   * @return the location from where the player should start playing.
   */
  DungeonLocationInterface getStartLocation();

  /**
   * Get the location from where the player has to reach finally. Once the player reaches this,
   * he is said to have completed the game.
   *
   * @return the location from where the player should reach to win.
   */
  DungeonLocationInterface getEndLocation();

  /**
   * Get the player object who is currently playing the game.
   *
   * @return the player object who is currently playing the game.
   */

  PlayerInterface getPlayer();

  /**
   * Returns the possible directions that the player can move from his current location.
   *
   * @return the List of string representing the directions where the player can move from his
   *        current location.
   */
  List<String> getPlayerPossibleMoves();

  /**
   * Gets the treasure count at the player's current location.
   *
   * @return the treasure count at the player's current location.
   */
  int getTreasureCountAtPlayerLocation();

  /**
   * Determines if the game is completed or not.
   *
   * @return true when game is completed, false otherwise.
   */
  boolean isGameOver();
}
