package dungeonmodel;

import java.util.List;

/**
 * Specifies the methods that the Rule classes of each weapon must have.
 */
interface WeaponRuleInterface {
  List<Direction> getCaveExit(Direction entryDirection)
          throws IllegalArgumentException;

  List<Direction> getTunnelExit(Direction entryDirection)
          throws IllegalArgumentException;

  int costPerTunnelTraversal();

  int costPerCaveTraversal();
}
