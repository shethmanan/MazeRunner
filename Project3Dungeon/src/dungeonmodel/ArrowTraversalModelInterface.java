package dungeonmodel;

import java.util.List;

/**
 * Gives a set of methods which help to know the status of firing an arrow.
 */
public interface ArrowTraversalModelInterface {
  /**
   * Gets the locations that the arrow travelled.
   *
   * @return the locations that the arrow travelled.
   */
  List<DungeonLocationInterface> getLocationListTravelledByArrow();

  /**
   * Determines if the arrow hit a monster.
   *
   * @return true if the arrow hit the monster, false otherwise.
   */
  boolean isHitMonsterSuccessful();

  /**
   * Determines if the arrow killed a monster.
   *
   * @return true if the arrow killed a monster, false otherwise.
   */
  boolean isKillMonsterSuccessful();

  /**
   * Determines if the arrow could travel to the specified distance.
   *
   * @return true if the arrow could travel to the specified distance, false otherwise.
   */
  boolean isArrowTravelledCompleteDistance();
}
