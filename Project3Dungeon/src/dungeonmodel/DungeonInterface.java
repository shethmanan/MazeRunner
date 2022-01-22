package dungeonmodel;

import java.util.HashMap;
import java.util.List;

/**
 * Represent a dungeon which will contain a set of locations and the possible paths from those
 * locations.
 */
public interface DungeonInterface {
  /**
   * Get the number of caves in the dungeon.
   *
   * @return the number of caves in the dungeon.
   */
  int getNumberOfCave();

  /**
   * Get the number of tunnel in the dungeon.
   *
   * @return the number of tunnel in the dungeon.
   */
  int getNumberOfTunnel();

  /**
   * Gets the type of the given dungeon location.
   *
   * @param dungeon the location of dungeon whose type will be determined.
   * @return the location type of given dungeon location.
   */
  DungeonLocationType getType(DungeonLocationInterface dungeon) throws IllegalArgumentException;

  /**
   * Gets the number of caves that contain treasure.
   *
   * @return the number of caves that contain treasure.
   */
  int getNumberOfTreasuredCaves();

  /**
   * Gets the number of connection there exists in a dungeon, where a connection is a path
   * between 2 different item(caves/tunnels) in the dungeon.
   *
   * @return the number of connection there exists in a dungeon
   */
  int getNumberOfConnection();

  /**
   * Gets the smell perceived at the given location.
   *
   * @param dungeonLocation the location where the smell level has to be checked.
   * @return the smell level.
   * @throws IllegalArgumentException when dungeon location is not given.
   */
  SmellLevel getSmellLevel(DungeonLocationInterface dungeonLocation)
          throws IllegalArgumentException;

  /**
   * Returns an adjacency list of entire dungeon in the form of hashmap of hashmaps. The key of
   * outer hashmap is each location in the dungeon, and its value is another hashmap, where the
   * keys of this inner hashmap are @code{Direction} and its values are the dungeon location
   * than can be reached by traversing from the source to specified direction.
   *
   * @return hashmap of hashmaps representing entire dungeon.
   */
  HashMap<DungeonLocation, HashMap<Direction, DungeonLocation>> getDungeonMap();

  /**
   * Returns an arraylist list of entire dungeon in the form of hashmap of hashmaps. The key of
   * outer hashmap is each location in the dungeon, and its value is another hashmap, where the
   * keys of this inner hashmap are @code{Direction} and its values are the dungeon location
   * than can be reached by traversing from the source to specified direction.
   *
   * @return hashmap of hashmaps representing entire dungeon.
   */
  List<DungeonLocationInterface> getDungeonList();


  /**
   * Gets the number of rows in the dungeon.
   *
   * @return the number of rows in the dungeon.
   */
  int getNoOfRows();

  /**
   * Gets the number of columns in the dungeon.
   *
   * @return the number of columns in the dungeon.
   */
  int getNoOfColumns();

  /**
   * Returns the possible direction from the given location.
   *
   * @param currentLocation the current location from where the possible direction will be
   *                        calculated.
   * @return a list of possible directions.
   */
  List<Direction> getPossibleDirectionFrom(DungeonLocationInterface currentLocation);

  /**
   * A list of ids of location visited by the player.
   *
   * @return list of ids of location visited by the player.
   */
  List<Integer> getLocationVisitedByPlayer();

}
