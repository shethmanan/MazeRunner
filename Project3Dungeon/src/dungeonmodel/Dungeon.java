package dungeonmodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a dungeon consisting of multiple locations. The path between different dungeon
 * location is represented by a HashMap of each location.
 */
class Dungeon implements DungeonInterface {
  private HashMap<DungeonLocation, HashMap<Direction, DungeonLocation>> dungeonMap;
  private int percentageCaveContainingTreasure;
  private DungeonLocation startLocation;
  private DungeonLocation endLocation;
  private boolean wrapping;
  private int noRows;
  private int noColumns;
  private int monsterCount;
  private List<Integer> locationVisitedByPlayer;

  /**
   * Constructs the dungeon.
   *
   * @param dungeonMap                       is a hashmap that stores connectivity between each
   *                                         DungeonLocation.
   * @param percentageCaveContainingTreasure denotes the percentage of caves that contain treasure.
   * @param startLocation                    indicates where the location where player will start
   *                                         from
   * @param endLocation                      indicates where the location where player has to
   *                                         reach to win the game
   */
  Dungeon(HashMap<DungeonLocation, HashMap<Direction, DungeonLocation>> dungeonMap,
          int percentageCaveContainingTreasure, DungeonLocation startLocation,
          DungeonLocation endLocation, boolean wrapping, int noRows, int noColumns,
          int monsterCount)
          throws IllegalArgumentException {
    if (dungeonMap == null) {
      throw new IllegalArgumentException("DungeonMap cannot be NULL");
    }
    if (startLocation == null) {
      throw new IllegalArgumentException("Dungeon start location cannot be NULL");
    }
    if (endLocation == null) {
      throw new IllegalArgumentException("Dungeon end location cannot be NULL");
    }
    this.dungeonMap = dungeonMap;
    this.wrapping = wrapping;
    this.noRows = noRows;
    this.noColumns = noColumns;
    this.monsterCount = monsterCount;
    this.percentageCaveContainingTreasure = percentageCaveContainingTreasure;
    this.startLocation = startLocation;
    this.endLocation = endLocation;
    if (locationVisitedByPlayer == null) {
      locationVisitedByPlayer = new ArrayList<>();
      setVisited(startLocation);
    }
  }

  Dungeon(HashMap<DungeonLocation, HashMap<Direction, DungeonLocation>> dungeonMap,
          int percentageCaveContainingTreasure, DungeonLocation startLocation,
          DungeonLocation endLocation, boolean wrapping, int noRows, int noColumns,
          int monsterCount, List<Integer> locationVisitedByPlayer)
          throws IllegalArgumentException {
    this(dungeonMap, percentageCaveContainingTreasure, startLocation,
            endLocation, wrapping, noRows, noColumns, monsterCount);
    if (locationVisitedByPlayer != null) {
      for (int i : locationVisitedByPlayer) {
        this.locationVisitedByPlayer.add(i);
      }
    }


  }

  @Override
  public int getNumberOfCave() {
    int noCaves = 0;
    for (DungeonLocation item : dungeonMap.keySet()) {
      if (isCave(dungeonMap.get(item))) {
        noCaves++;
      }
    }
    return noCaves;
  }

  @Override
  public int getNumberOfTunnel() {
    int noTunnel = 0;
    for (DungeonLocation item : dungeonMap.keySet()) {
      if (!isCave(dungeonMap.get(item))) {
        noTunnel++;
      }
    }
    return noTunnel;
  }

  @Override
  public DungeonLocationType getType(DungeonLocationInterface dungeon)
          throws IllegalArgumentException {
    if (dungeon == null) {
      throw new IllegalArgumentException("Dungeon location cannot be null");
    }
    return dungeonMap.get(dungeon).keySet().size() == 2 ? DungeonLocationType.TUNNEL :
            DungeonLocationType.CAVE;
  }

  int getAllTreasureCountAt(DungeonLocation location)
          throws IllegalArgumentException {
    if (location == null) {
      throw new IllegalArgumentException("Location cannot be null");
    }
    return location.getTotalTreasureCount();
  }

  @Override
  public int getNumberOfTreasuredCaves() {
    int treasuredCave = 0;

    for (DungeonLocation item : dungeonMap.keySet()) {
      if (isCave(dungeonMap.get(item))) {
        treasuredCave += item.getTotalTreasureCount() > 0 ? 1 : 0;
      }
    }
    return treasuredCave;
  }

  @Override
  public int getNumberOfConnection() {
    int noConnection = 0;
    for (DungeonLocation item : dungeonMap.keySet()) {
      noConnection += dungeonMap.get(item).size();
    }
    return (noConnection / 2);
  }

  @Override
  public SmellLevel getSmellLevel(DungeonLocationInterface dungeonLocation)
          throws IllegalArgumentException {
    if (dungeonLocation == null) {
      throw new IllegalArgumentException("Enter valid dungeon");
    }
    DungeonNeighbourFinder dungeonNeighbourFinder = new DungeonNeighbourFinder();
    List<DungeonLocation> neighbourList = dungeonNeighbourFinder.getNeighbouringDungeonLocation(
            (DungeonLocation) dungeonLocation, getDungeonMapCopy(), 1);
    int monsterPresent = neighbourList.stream()
            .filter(x -> x.isMonsterPresent()).collect(Collectors.toList()).size();
    if (monsterPresent > 0) {
      return SmellLevel.STRONG;
    }
    neighbourList = dungeonNeighbourFinder.getNeighbouringDungeonLocation(
            (DungeonLocation) dungeonLocation, getDungeonMapCopy(), 2);
    monsterPresent = neighbourList.stream()
            .filter(x -> x.isMonsterPresent()).collect(Collectors.toList()).size();
    if (monsterPresent > 1) {
      return SmellLevel.STRONG;
    } else if (monsterPresent > 0) {
      return SmellLevel.LIGHT;
    }
    return SmellLevel.ZERO;
  }

  @Override
  public HashMap<DungeonLocation, HashMap<Direction, DungeonLocation>> getDungeonMap() {
    return getDungeonMapCopy();

  }

  @Override
  public List<DungeonLocationInterface> getDungeonList() {
    List<DungeonLocationInterface> dungeonList = new ArrayList<>();
    for (int i = 1; i <= noColumns * noRows; i++) {
      dungeonList.add(this.findDungeonById(i).getDeepCopy());
    }
    return dungeonList;
  }

  @Override
  public int getNoOfRows() {
    return this.noRows;
  }

  @Override
  public int getNoOfColumns() {
    return this.noColumns;
  }

  ArrowTraversalModel killMonster(DungeonLocation playerLocation, Weapon weapon,
                                  Direction attackDirection, int distance)
          throws IllegalArgumentException {
    if (playerLocation == null || weapon == null || attackDirection == null) {
      throw new IllegalArgumentException("Player location, weapon and attack direction cannot be "
              + "null");
    }
    WeaponNavigator weaponNavigator = new WeaponNavigator();
    ArrowTraversalModel arrowTraversalModel = weaponNavigator.weaponNavigationEnd(playerLocation,
            weapon, attackDirection,
            distance);
    arrowTraversalModel = attackMonsterToKill(arrowTraversalModel, weapon);
    return arrowTraversalModel;
  }

  private ArrowTraversalModel attackMonsterToKill(ArrowTraversalModel arrowTraversalModel,
                                                  Weapon weapon)
          throws IllegalArgumentException {
    if (arrowTraversalModel == null || weapon == null) {
      throw new IllegalArgumentException("Arrow Traversal model and weapon cannot be null");
    }
    if (!arrowTraversalModel.isArrowTravelledCompleteDistance()) {
      return arrowTraversalModel;
    }
    DungeonLocation weaponEndLocation = (DungeonLocation)
            arrowTraversalModel.getLocationListTravelledByArrow()
                    .get(arrowTraversalModel.getLocationListTravelledByArrow().size() - 1);
    if (!weaponEndLocation.isMonsterPresent()) {
      return arrowTraversalModel;
    }
    arrowTraversalModel.setHitMonsterSuccessful(true);
    int healthBefore = weaponEndLocation.getMonster().getHealth();
    int damage = weapon.getDamage();
    int newHealth = healthBefore - damage;

    weaponEndLocation.addMonster(weaponEndLocation.getMonster().getCopy(newHealth));

    arrowTraversalModel.setKillMonsterSuccessful(!weaponEndLocation.isMonsterPresent());
    return arrowTraversalModel;
  }

  DungeonLocation getStartLocation() {
    return startLocation;
  }

  DungeonLocation getEndLocation() {
    return endLocation;
  }

  HashMap collectTreasure(DungeonLocation location)
          throws IllegalArgumentException {
    if (location == null) {
      throw new IllegalArgumentException("Location cannot be null");
    }
    HashMap<TreasureEnum, Integer> treasure = location.collectAllTreasure();
    return treasure;
  }

  boolean collectATreasure(DungeonLocation location, TreasureEnum treasure, int unitToCollect)
          throws IllegalArgumentException {
    if (location == null) {
      throw new IllegalArgumentException("Location cannot be null");
    }
    if (treasure == null) {
      throw new IllegalArgumentException("Treasure cannot be null");
    }
    location.collectATreasure(treasure, unitToCollect);
    return true;
  }

  boolean collectAWeapon(DungeonLocation location, Weapon weapon, int unitToCollect)
          throws IllegalArgumentException {
    if (location == null) {
      throw new IllegalArgumentException("Location cannot be null");
    }
    if (weapon == null) {
      throw new IllegalArgumentException("Weapon cannot be null");
    }
    location.collectAWeapon(weapon, unitToCollect);
    return true;
  }

  private boolean isCave(HashMap<Direction, DungeonLocation> directionDungeonItemHashMap)
          throws IllegalArgumentException {
    if (directionDungeonItemHashMap == null) {
      throw new IllegalArgumentException("The direction map cannot be null");
    }
    int count = 0;
    for (Direction d : directionDungeonItemHashMap.keySet()) {
      count++;
    }
    return count == 2 ? false : true;
  }

  private DungeonLocation findDungeonById(int id) {
    DungeonLocation dungeonLocation = null;
    for (DungeonLocation d :
            dungeonMap.keySet()) {
      if (d.getDungeonId() == id) {
        dungeonLocation = d;
        break;
      }
    }
    return dungeonLocation;
  }

  List<String> getPossibleDirection(DungeonLocation currentLocation)
          throws IllegalArgumentException {
    if (currentLocation == null) {
      throw new IllegalArgumentException("Location cannot be null");
    }
    List<String> possibleDirectionList = new ArrayList<>();
    for (Direction direction :
            dungeonMap.get(currentLocation).keySet()) {
      possibleDirectionList.add(direction.toString());
    }
    return possibleDirectionList;
  }

  void setVisited(DungeonLocationInterface dungeonLocationInterface) {
    int id = dungeonLocationInterface.getDungeonId();
    if (!locationVisitedByPlayer.contains(id)) {
      locationVisitedByPlayer.add(id);
    }
  }

  public List<Direction> getPossibleDirectionFrom(DungeonLocationInterface currentLocation)
          throws IllegalArgumentException {
    if (currentLocation == null) {
      throw new IllegalArgumentException("Location cannot be null");
    }
    List<Direction> possibleDirectionList = new ArrayList<>();
    for (Direction direction :
            dungeonMap.get(currentLocation).keySet()) {
      possibleDirectionList.add(direction);
    }
    return possibleDirectionList;
  }

  @Override
  public List<Integer> getLocationVisitedByPlayer() {
    List<Integer> copy = new ArrayList<>(locationVisitedByPlayer.size());
    for (int i : locationVisitedByPlayer) {
      copy.add(i);
    }
    return copy;
  }

  boolean isValidMove(DungeonLocation source, Direction direction)
          throws IllegalArgumentException {
    if (source == null || direction == null) {
      throw new IllegalArgumentException("Source or direction cannot be null");
    }
    return dungeonMap.get(source).containsKey(direction);
  }

  DungeonLocation getLocation(DungeonLocation source, Direction direction)
          throws IllegalArgumentException {
    if (source == null || direction == null) {
      throw new IllegalArgumentException("Source or direction cannot be null");
    }
    try {
      return findDungeonById(dungeonMap.get(source).get(direction).getDungeonId());
    } catch (NullPointerException e) {
      throw new IllegalArgumentException("Invalid direction on location");
    }
  }

  private HashMap<DungeonLocation, HashMap<Direction, DungeonLocation>> getDungeonMapCopy() {
    HashMap<DungeonLocation, HashMap<Direction, DungeonLocation>> dungeonMap = new HashMap<>();
    for (DungeonLocation dungeonLocation : this.dungeonMap.keySet()) {
      DungeonLocation dungeonLocationTemp = dungeonLocation.getDeepCopy();
      dungeonMap.put(dungeonLocationTemp, new HashMap<>());
      for (Direction direction : this.dungeonMap.get(dungeonLocation).keySet()) {
        dungeonMap.get(dungeonLocationTemp).put(direction,
                findDungeonById(this.dungeonMap.get(dungeonLocation).get(direction).getDungeonId())
                        .getDeepCopy());
      }
    }
    return dungeonMap;
  }

  Dungeon getDeepCopy() {
    HashMap<DungeonLocation, HashMap<Direction, DungeonLocation>> dungeonMap =
            getDungeonMapCopy();
    return new Dungeon(dungeonMap, percentageCaveContainingTreasure, startLocation.getDeepCopy(),
            endLocation.getDeepCopy(), wrapping, noRows, noColumns, monsterCount,
            this.locationVisitedByPlayer);
  }

  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    for (DungeonLocation dungeonLocation : this.dungeonMap.keySet()) {
      stringBuilder.append(dungeonLocation.getDungeonId()).append(" -> [");
      for (Direction direction : this.dungeonMap.get(dungeonLocation).keySet()) {
        stringBuilder.append(this.dungeonMap.get(dungeonLocation).get(direction)
                .getDungeonId() + ", ");
      }
      stringBuilder.append("]");
    }
    return String.format("Dungeon = %s", stringBuilder.toString()
            .replace(", ]", "] "));
  }

  class WeaponNavigator {

    ArrowTraversalModel weaponNavigationEnd(DungeonLocation playerLocation, Weapon weapon,
                                            Direction attackDirection,
                                            int distance)
            throws IllegalArgumentException {
      if (playerLocation == null || weapon == null || attackDirection == null) {
        throw new IllegalArgumentException("Player location, weapon and attack direction cannot "
                + "be null");
      }
      WeaponRuleInterface weaponRule = weapon.getRuleClass();
      ArrowTraversalModel arrowTraversalModel = new ArrowTraversalModel();
      List<DungeonLocationInterface> locationListTravelledByArrow = new ArrayList<>();
      locationListTravelledByArrow.add(playerLocation.getDeepCopy());
      DungeonLocationType location;
      DungeonLocation weaponLocation = playerLocation;
      Direction weaponEntryDirection = attackDirection.getOpposite();
      List<Direction> weaponPossibleDirection;
      while (distance > 0) {
        int costOfTraversal;
        boolean nextLocationTraversable;
        location = getType(weaponLocation);
        if (location.equals(DungeonLocationType.CAVE)) {
          weaponPossibleDirection = weaponRule.getCaveExit(weaponEntryDirection);
        } else {  //Tunnel found
          weaponPossibleDirection = weaponRule.getTunnelExit(weaponEntryDirection);
        }
        nextLocationTraversable = false;
        for (Direction d : weaponPossibleDirection) {
          if (dungeonMap.get(weaponLocation).get(d) != null) {
            //A direction where the weapon can travel, and a same path also exist in the dungeon

            weaponLocation = findDungeonById(dungeonMap.get(weaponLocation).get(d).getDungeonId());
            locationListTravelledByArrow.add(weaponLocation);
            if (getType(weaponLocation) == DungeonLocationType.CAVE) {
              costOfTraversal = weaponRule.costPerCaveTraversal();
            } else {
              costOfTraversal = weaponRule.costPerTunnelTraversal();
            }
            distance -= costOfTraversal;
            if (location.equals(DungeonLocationType.TUNNEL)
                    && weaponEntryDirection != d.getOpposite()) {
              weaponEntryDirection = d.getOpposite();
            }
            nextLocationTraversable = true;
            break;
          }

        }
        if (!nextLocationTraversable) {
          arrowTraversalModel.setArrowTravelledCompleteDistance(false);
          arrowTraversalModel.setLocationListTravelledByArrow(locationListTravelledByArrow);
          return arrowTraversalModel;
        }
      }
      arrowTraversalModel.setLocationListTravelledByArrow(locationListTravelledByArrow);
      return arrowTraversalModel;
    }
  }

}
