package dungeonmodel;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains the rules used for crooked arrow traversal in the dungeon.
 */
class CrookedArrowRule implements WeaponRuleInterface {
  @Override
  public List<Direction> getCaveExit(Direction entryDirection)
          throws IllegalArgumentException {
    if (entryDirection == null) {
      throw new IllegalArgumentException("Entry direction cannot be null");
    }
    List<Direction> exitList = new ArrayList<>();
    exitList.add(entryDirection.getOpposite());
    return exitList;
  }

  @Override
  public List<Direction> getTunnelExit(Direction entryDirection)
          throws IllegalArgumentException {
    if (entryDirection == null) {
      throw new IllegalArgumentException("Entry direction cannot be null");
    }
    List<Direction> exitList = new ArrayList<>();
    for (Direction d : Direction.values()) {
      if (d != entryDirection) {
        exitList.add(d);
      }
    }
    return exitList;
  }

  @Override
  public int costPerTunnelTraversal() {
    return 0;
  }

  @Override
  public int costPerCaveTraversal() {
    return 1;
  }
}
