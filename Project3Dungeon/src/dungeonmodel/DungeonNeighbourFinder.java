package dungeonmodel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * This class exposes a method to find the nearest neighbours of a given source location using
 * the depth limited search algorithm.
 */
class DungeonNeighbourFinder {
  /**
   * Uses depth limited search algorithm to find the neighbours of a given location of the
   * dungeon unto the given depth.
   *
   * @param source     the start location whose neighbours are to be found.
   * @param dungeonMap the map representing the entire dungeon.
   * @param depthLimit the limit till which the neighbours are to be found.
   * @return a list of traversable neighbours from the source, unto the given depth, null if none.
   * @throws IllegalArgumentException when the source is null
   *                                  when the dungeon is null
   *                                  when the limit is negative
   */
  List<DungeonLocation> getNeighbouringDungeonLocation(
          DungeonLocation source,
          HashMap<DungeonLocation, HashMap<Direction, DungeonLocation>> dungeonMap, int depthLimit)
          throws IllegalArgumentException {
    if (source == null) {
      throw new IllegalArgumentException("Dungeon Source Location cannot be NULL");
    }
    if (dungeonMap == null) {
      throw new IllegalArgumentException("Dungeon cannot be NULL");
    }
    if (depthLimit < 1) {
      throw new IllegalArgumentException("Depth cannot be less than 1");
    }

    Queue<DungeonLocation> queue = new LinkedList<>();
    List<Boolean> visitedNodes = new ArrayList<>(Collections.nCopies(dungeonMap.size(),
            false));
    List<DungeonLocation> neighbours = new ArrayList<>();

    addNeighboursRecursively(neighbours, dungeonMap, source, depthLimit);

    return neighbours;
  }

  private void addNeighboursRecursively(List<DungeonLocation> neighbours,
                                        HashMap<DungeonLocation,
                                                HashMap<Direction, DungeonLocation>> dungeonMap,
                                        DungeonLocation source, int depthLimit) {
    if (neighbours.contains(source)) {
      return;
    }
    if (depthLimit < 0) {
      return;
    }
    neighbours.add(source);
    depthLimit--;
    for (Direction d :
            dungeonMap.get(source).keySet()) {
      addNeighboursRecursively(neighbours, dungeonMap, dungeonMap.get(source).get(d), depthLimit);
    }
  }


}
