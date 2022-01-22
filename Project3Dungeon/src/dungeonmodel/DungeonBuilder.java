package dungeonmodel;

import random.RandomNumberGenerator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * The class generates a Dungeon object with the given parameters.
 */
class DungeonBuilder {
  private boolean wrapping;
  private int noRows;
  private int noColumns;
  private int interconnectivity;
  private DungeonLocation[][] dungeonMatrix;
  private HashMap<DungeonLocation, HashMap<Direction, DungeonLocation>> dungeonMap;
  private RandomNumberGenerator randomNumberGenerator;
  private List<DungeonPath> allPath;
  private List<DungeonPath> leftOverPath;
  private List<Integer> ancestor;
  private int percentageCaveContainingTreasure;
  private int percentOfCaveContainingWeapon;
  private static final int MIN_START_END_DISTANCE = 4;  //Omit neighbours unto length 4
  private int noMonster;

  /**
   * A builder class used to create a Dungeon Object.
   *
   * @param wrapping              true if dungeon is wrapped, false otherwise.
   * @param noRows                represents the number of rows in the dungeon.
   * @param noColumns             represents the number of columns in the dungeon.
   * @param interconnectivity     represents the degree on interconnectivity. When the
   *                              interconnectivity is greater than the maximum possible value,
   *                              you get a completely open dungeon.
   * @param randomNumberGenerator is the object used to generate randomness and pick random
   *                              numbers.
   * @throws IllegalArgumentException when the noRows is negative
   *                                  when the noColumns is negative.
   *                                  when the interconnectivity is negative
   *                                  when null is passed as randomNumberGenerator.
   */
  DungeonBuilder(boolean wrapping, int noRows, int noColumns, int interconnectivity,
                 int percentageCaveContainingTreasure, int monsterCount,
                 RandomNumberGenerator randomNumberGenerator)
          throws IllegalArgumentException {
    validateInputs(wrapping, noRows, noColumns, interconnectivity,
            percentageCaveContainingTreasure, monsterCount, randomNumberGenerator);
    this.wrapping = wrapping;
    this.noRows = noRows;
    this.noColumns = noColumns;
    this.interconnectivity = interconnectivity;
    this.percentageCaveContainingTreasure = percentageCaveContainingTreasure;
    this.percentOfCaveContainingWeapon = percentageCaveContainingTreasure;
    this.randomNumberGenerator = randomNumberGenerator;
    this.noMonster = monsterCount;
    dungeonMatrix = new DungeonLocation[noRows][noColumns];
    allPath = new ArrayList<>();
    leftOverPath = new ArrayList<>();
    ancestor = new ArrayList<>();
    ancestor.add(0);
    dungeonMap = new HashMap<>();
  }

  /**
   * Validate all inputs entered by the user.
   */
  private void validateInputs(boolean wrapping, int noRows, int noColumns, int interconnectivity,
                              int percentageCaveContainingTreasure, int monsterCount,
                              RandomNumberGenerator randomNumberGenerator)
          throws IllegalArgumentException {
    if (randomNumberGenerator == null) {
      throw new IllegalArgumentException("Random Number Generator cannot be null");
    }
    if (noColumns < 0 || noRows < 0) {
      throw new IllegalArgumentException("Rows and columns cannot be negative");
    }
    if (noColumns + noColumns < 7) {
      throw new IllegalArgumentException("The minimum size of Dungeon is 3*4 or 4*3");
    }
    if (percentageCaveContainingTreasure < 0) {
      throw new IllegalArgumentException("The treasure containing caves cannot be negative");
    }
    if (interconnectivity < 0) {
      throw new IllegalArgumentException("Interconnectivity cannot be negative");
    }
    if (monsterCount < 1) {
      throw new IllegalArgumentException("Minimum monster count is 1");
    }
  }

  /**
   * Generates a dungeon and returns it as a HashMap of HashMap. Every key in the hashmap is an
   * DungeonLocation and its values is another HashMap whose keys are the directions and
   * value is the corresponding DungeonLocation in that direction.
   *
   * @return the entire dungeon as a HashMap.
   */
  Dungeon getDungeon() {
    initializeDungeon();
    createAllPossiblePath();
    createMinimumSpanningTree();
    addInterconnectivity();
    //Now our dungeon is ready

    addTreasure();
    List<DungeonLocation> startAndEndCave = initializeStartAndEndLocation();
    addWeapon(startAndEndCave);
    addMonster(startAndEndCave);
    return new Dungeon(dungeonMap, percentageCaveContainingTreasure, startAndEndCave.get(0),
            startAndEndCave.get(1),wrapping,noRows,noColumns,noMonster);
  }

  private void addMonster(List<DungeonLocation> startAndEndCave) {
    if (startAndEndCave == null) {
      throw new IllegalArgumentException("Start and end cave cannot be null");
    }
    List<DungeonLocation> allCave = getCaveList(dungeonMap);
    allCave.remove(startAndEndCave.get(0)); //Monster can never appear at the start location

    //1 monster always at the end cave
    startAndEndCave.get(1)
            .addMonster(new Otyugh(100));

    //Add monsters to the remaining cave
    if (noMonster >= getCaveList(this.dungeonMap).size()) {
      // 1st cave will never have a monster, hence -1
      noMonster = getCaveList(this.dungeonMap).size() - 1;
    }
    int noMonsterToAdd = this.noMonster - 1; //Since we added 1 monster at end cave
    while (noMonsterToAdd > 0) {
      int randomNumber = randomNumberGenerator.getRandomInt(0,
              allCave.size() - 1);
      if (!allCave.get(randomNumber).isMonsterPresent()) {
        allCave.get(randomNumber).addMonster(new Otyugh(100));
        noMonsterToAdd--;
      }
    }
  }

  private void addWeapon(List<DungeonLocation> startAndEnd)
          throws IllegalArgumentException {
    if (startAndEnd == null) {
      throw new IllegalArgumentException("Start and end location cannot be null");
    }
    int noLocationToAddWeapon =
            (int) (getCaveList(this.dungeonMap).size() * (percentOfCaveContainingWeapon / 100.0));
    ArrayList<DungeonLocation> allLocation =
            new ArrayList<>(dungeonMap.keySet());
    allLocation.remove(startAndEnd.get(1)); //remove the end cave
    Set<Integer> locationWithWeaponCounter = new HashSet<>();
    while (locationWithWeaponCounter.size() < noLocationToAddWeapon) {
      int randomNumber = randomNumberGenerator.getRandomInt(0,
              allLocation.size() - 1);
      allLocation.get(randomNumber)
              .addAWeapon(Weapon.CROOKED_ARROW);
      locationWithWeaponCounter.add(randomNumber);
    }

  }

  private List<DungeonLocation> initializeStartAndEndLocation() {
    DungeonNeighbourFinder d1 = new DungeonNeighbourFinder();
    List<DungeonLocation> neighbourList;
    DungeonLocation startCave = null;
    DungeonLocation endCave = null;
    boolean startEndFound = false;

    while (!startEndFound) {
      List<DungeonLocation> onlyCaves = getCaveList(this.dungeonMap);
      startCave = onlyCaves.get(randomNumberGenerator.getRandomInt(0,
              onlyCaves.size() - 1));
      neighbourList = d1.getNeighbouringDungeonLocation(startCave, dungeonMap,
              MIN_START_END_DISTANCE);

      //Once we get the list, remove all these locations to get the caves that are at a distance
      // of 5 or more from the START cave.

      onlyCaves.removeAll(neighbourList);

      if (onlyCaves.size() > 0) {
        //If we found an end cave corresponding to the start, then we will select any one end cave
        // and proceed
        startEndFound = true;

        endCave = onlyCaves.get(randomNumberGenerator.getRandomInt(0,
                onlyCaves.size() - 1));

      }
    }
    List<DungeonLocation> startEndLocation = new ArrayList<>();
    startEndLocation.add(startCave);
    startEndLocation.add(endCave);
    return startEndLocation;
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

  private void addTreasure() {
    List<DungeonLocation> list = getCaveList(dungeonMap);
    int countTreasureLadenCaveToAdd =
            (int) (list.size() * (percentageCaveContainingTreasure / 100.0));
    Set<Integer> cavesWithTreasureCounter = new HashSet<>();
    while (cavesWithTreasureCounter.size() < countTreasureLadenCaveToAdd) {
      int randomNumber = randomNumberGenerator.getRandomInt(0,
              list.size() - 1);
      allocateTreasureToDungeonItem(list.get(randomNumber));
      cavesWithTreasureCounter.add(randomNumber);
    }
  }

  private void allocateTreasureToDungeonItem(DungeonLocation dungeonLocation)
          throws IllegalArgumentException {
    if (dungeonLocation == null) {
      throw new IllegalArgumentException("Dungeon Location cannot be null");
    }
    dungeonLocation.addTreasure(TreasureEnum.values()[randomNumberGenerator.getRandomInt(0,
            TreasureEnum.values().length - 1)]);
  }

  private List<DungeonLocation> getCaveList(HashMap<DungeonLocation, HashMap<Direction,
          DungeonLocation>> dungeonMap) {
    if (dungeonMap == null) {
      throw new IllegalArgumentException("Dungeon map cannot be null");
    }
    List<DungeonLocation> list = new ArrayList();
    for (DungeonLocation item : dungeonMap.keySet()) {
      if (isCave(dungeonMap.get(item))) {
        list.add(item);
      }
    }
    list.stream().sorted(Comparator.comparingInt(DungeonLocation::getDungeonId));
    return list;
  }

  private boolean isCave(HashMap<Direction, DungeonLocation> directionDungeonItemHashMap)
          throws IllegalArgumentException {
    if (directionDungeonItemHashMap == null) {
      throw new IllegalArgumentException("Dungeon item map cannot be  null");
    }
    int count = 0;
    for (Direction d : directionDungeonItemHashMap.keySet()) {
      count++;
    }
    return count == 2 ? false : true;
  }

  private void createAllPossiblePath() {
    for (int i = 0; i < noRows; i++) {
      for (int j = 0; j < noColumns; j++) {
        if (j != noColumns - 1) {
          allPath.add(new DungeonPath(dungeonMatrix[i][j], dungeonMatrix[i][j + 1],
                  Direction.EAST));
        }
        if (i != noRows - 1) {
          allPath.add(new DungeonPath(dungeonMatrix[i][j], dungeonMatrix[i + 1][j],
                  Direction.SOUTH));
        }
      }
    }
    if (wrapping) {
      addWrappingPath();
    }
  }

  private void addWrappingPath() {
    //node wraps to another node in same Column
    for (int i = 0; i < noColumns; i++) {
      allPath.add(new DungeonPath(dungeonMatrix[0][i], dungeonMatrix[noRows - 1][i],
              Direction.NORTH));
    }
    //node wraps to another node in same Row
    for (int i = 0; i < noRows; i++) {
      allPath.add(new DungeonPath(dungeonMatrix[i][0], dungeonMatrix[i][noColumns - 1],
              Direction.WEST));
    }
  }

  private void createMinimumSpanningTree() {
    createAncestor();
    int count = allPath.size();
    DungeonPath dungeonPath;
    for (int i = 0; i < count; i++) {

      int tempRandomNumber = randomNumberGenerator.getRandomInt(0, allPath.size() - 1);
      dungeonPath = allPath.get(tempRandomNumber);
      processPath(dungeonPath);
      allPath.remove(tempRandomNumber);
    }
  }

  private void processPath(DungeonPath dungeonPath)
          throws IllegalArgumentException {
    if (dungeonPath == null) {
      throw new IllegalArgumentException("Dungeon path cannot be null");
    }
    int item1Ancestor = findAncestor(dungeonPath.getLocation1().getDungeonId());
    int item2Ancestor = findAncestor(dungeonPath.getLocation2().getDungeonId());
    if (item1Ancestor == item2Ancestor) {
      addToLeftOver(dungeonPath);
    } else if (item1Ancestor == dungeonPath.getLocation1().getDungeonId()
            && item2Ancestor == dungeonPath.getLocation2().getDungeonId()) {
      unite(dungeonPath, dungeonPath.getLocation1().getDungeonId(),
              dungeonPath.getLocation2().getDungeonId());
    } else if (item1Ancestor == dungeonPath.getLocation1().getDungeonId()
    ) {

      unite(dungeonPath, dungeonPath.getLocation2().getDungeonId(),
              dungeonPath.getLocation1().getDungeonId());

    } else {
      if (item1Ancestor < item2Ancestor) {
        unite(dungeonPath, item1Ancestor, item2Ancestor);
      } else {
        unite(dungeonPath, item2Ancestor, item1Ancestor);
      }

    }
  }

  private void addToLeftOver(DungeonPath dungeonPath)
          throws IllegalArgumentException {
    if (dungeonPath == null) {
      throw new IllegalArgumentException("Dungeon path cannot be null");
    }
    leftOverPath.add(dungeonPath);
  }

  private void unite(DungeonPath dungeonPath, int id1, int id2)
          throws IllegalArgumentException {
    if (dungeonPath == null) {
      throw new IllegalArgumentException("Dungeon path cannot be null");
    }
    ancestor.set(id2, id1);
    addPathToDungeonMap(dungeonPath);
  }

  private void addPathToDungeonMap(DungeonPath dungeonPath)
          throws IllegalArgumentException {
    if (dungeonPath == null) {
      throw new IllegalArgumentException("Dungeon path cannot be null");
    }
    if (dungeonMap.get(dungeonPath.getLocation1()) == null) {
      dungeonMap.put(dungeonPath.getLocation1(), new HashMap<>());
    }
    if (dungeonMap.get(dungeonPath.getLocation2()) == null) {
      dungeonMap.put(dungeonPath.getLocation2(), new HashMap<>());
    }
    dungeonMap.get(dungeonPath.getLocation1()).put(dungeonPath.getDirection(),
            dungeonPath.getLocation2());
    dungeonMap.get(dungeonPath.getLocation2()).put(dungeonPath.getDirection().getOpposite(),
            dungeonPath.getLocation1());

  }

  private int findAncestor(int dungeonKey) {
    if (ancestor.get(dungeonKey) == -1) {
      return dungeonKey;
    } else {
      return findAncestor(ancestor.get(dungeonKey));
    }
  }

  private void createAncestor() {
    for (int i = 1; i <= noRows * noColumns; i++) {
      ancestor.add(-1);
    }
  }

  private void addInterconnectivity() {
    if (this.interconnectivity == 0) {
      return;
    }
    int tempInterconnectivity = this.interconnectivity;
    while (tempInterconnectivity-- > 0) {
      int tempRandom = randomNumberGenerator.getRandomInt(0, leftOverPath.size() - 1);
      addPathToDungeonMap(leftOverPath.get(tempRandom));
      leftOverPath.remove(tempRandom);
    }
  }

  /**
   * Initialize all the units inside the dungeon with an id.
   */
  private void initializeDungeon() {
    int dungeonId = 1;
    for (int i = 0; i < noRows; i++) {
      for (int j = 0; j < noColumns; j++) {
        dungeonMatrix[i][j] = new DungeonLocation(dungeonId++, i, j);
      }
    }
  }

  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    for (DungeonLocation dungeonLocation : this.dungeonMap.keySet()) {
      stringBuilder.append(dungeonLocation.getDungeonId() + " -> ");
      for (Direction direction : this.dungeonMap.get(dungeonLocation).keySet()) {
        stringBuilder.append(this.dungeonMap.get(dungeonLocation)
                .get(direction).getDungeonId() + ", ");
      }
    }
    return String.format("Dungeon = %s", stringBuilder);
  }
}
