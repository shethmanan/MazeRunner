package dungeonmodel;

import java.util.HashMap;
import java.util.Objects;

/**
 * A DungeonLocation represents an individual location in the entire dungeon. The location can
 * have hold multiple treasure if acting as a Cave, NONE otherwise.
 */
class DungeonLocation implements DungeonLocationInterface {
  private HashMap<TreasureEnum, Integer> treasureMap;
  private int dungeonId;
  private Coordinate2D coordinate;
  private Monster monster;
  private HashMap<Weapon, Integer> weaponMap;

  /**
   * The id represents an identifier for a dungeon location.
   *
   * @param dungeonId   represents a unique id for the dungeon.
   * @param xCoordinate the location of location on x-axis
   * @param yCoordinate the location of location on x-axis
   */
  DungeonLocation(int dungeonId, int xCoordinate, int yCoordinate) {
    this.dungeonId = dungeonId;
    treasureMap = new HashMap<>();
    coordinate = new Coordinate2D(xCoordinate, yCoordinate);
    weaponMap = new HashMap<>();
  }

  @Override
  public HashMap getAllTreasure() {
    HashMap<TreasureEnum, Integer> treasureMapNew = new HashMap<>();
    for (TreasureEnum treasureEnum : treasureMap.keySet()) {
      treasureMapNew.put(treasureEnum, treasureMap.get(treasureEnum));
    }
    return treasureMapNew;
  }

  @Override
  public HashMap getAllWeapon() {
    HashMap<Weapon, Integer> tempWeaponMap = new HashMap<>();
    for (Weapon weapon : weaponMap.keySet()) {
      tempWeaponMap.put(weapon, weaponMap.get(weapon));
    }
    return tempWeaponMap;
  }

  @Override
  public boolean isMonsterPresent() {
    return !(this.monster == null || (this.monster != null && this.monster.getHealth() <= 0));

  }

  @Override
  public Monster getMonster() {
    return this.monster == null ? null : this.monster.getCopy(this.monster.getHealth());
  }


  @Override
  public int getTotalTreasureCount() {
    int count = 0;
    for (TreasureEnum treasureEnum : treasureMap.keySet()) {
      count += this.getTreasureCountOf(treasureEnum);
    }
    return count;
  }

  @Override
  public int getTreasureCountOf(TreasureEnum treasureEnum) {
    if (treasureEnum == null) {
      throw new IllegalArgumentException("Treasure enum cannot be null");
    }
    return treasureMap.get(treasureEnum) == null ? 0 : treasureMap.get(treasureEnum);
  }

  @Override
  public int getWeaponCountOf(Weapon weapon)
          throws IllegalArgumentException {
    if (weapon == null) {
      throw new IllegalArgumentException("Weapon name cannot be null");
    }
    return weaponMap.get(weapon) == null ? 0 : treasureMap.get(weapon);
  }

  @Override
  public Coordinate2D getCoordinate() {
    return coordinate;
  }

  @Override
  public int getDungeonId() {
    return dungeonId;
  }

  //Returns the given type of treasure available at the current dungeon location and removes it
  // from the current location.
  boolean collectATreasure(TreasureEnum treasureType, int unitToCollect)
          throws IllegalArgumentException {
    if (treasureType == null) {
      throw new IllegalArgumentException("Treasure type cannot be null");
    }

    if (treasureMap.get(treasureType) != null) {
      int unitPresent = treasureMap.get(treasureType);
      if (unitPresent < unitToCollect) {
        throw new IllegalArgumentException("Count to be collected cannot be more than available "
                + "count");
      }
      if (unitPresent == unitToCollect) {
        treasureMap.remove(treasureType);
      } else {
        treasureMap.put(treasureType, treasureMap.get(treasureType) - unitToCollect);
      }
    } else {
      throw new IllegalArgumentException("Treasure type not found at the location.");
    }
    return true;
  }

  //Returns all treasure available at the current dungeon location and removes it from the current
  //location.
  HashMap collectAllTreasure() {
    HashMap<TreasureEnum, Integer> tempTreasureMap;
    tempTreasureMap = treasureMap;
    treasureMap = new HashMap<>();
    return tempTreasureMap;
  }

  /**
   * Adds a unit of the given treasure in the dungeon.
   *
   * @param treasureEnum the treasure location for which a count is to be added.
   */
  void addTreasure(TreasureEnum treasureEnum)
          throws IllegalArgumentException {
    if (treasureEnum == null) {
      throw new IllegalArgumentException("Treasure enum cannot be null");
    }
    treasureMap.put(treasureEnum, treasureMap.get(treasureEnum) == null ? 1 :
            treasureMap.get(treasureEnum) + 1);
  }

  boolean collectAWeapon(Weapon weapon, int countToPick)
          throws IllegalArgumentException {
    if (weaponMap.get(weapon) == null) {
      throw new IllegalArgumentException("Specified weapon does not exits at current location");
    } else if (weaponMap.get(weapon) < countToPick) {
      throw new IllegalArgumentException("Cannot pick more weapon than what is present");
    } else if (weaponMap.get(weapon) == countToPick) {
      weaponMap.remove(weapon);
    } else {
      weaponMap.put(weapon, weaponMap.get(weapon) - countToPick);
    }
    return true;
  }

  void addAWeapon(Weapon weapon) throws IllegalArgumentException {
    if (weapon == null) {
      throw new IllegalArgumentException("Weapon cannot be null");
    }
    weaponMap.put(weapon,
            weaponMap.getOrDefault(weapon, 0) + 1);
  }

  void addMonster(Monster monster) {

    if (monster == null || monster.getHealth() == 0) {
      this.monster = null;
    } else {
      this.monster = monster;
    }
  }

  DungeonLocation getDeepCopy() {
    HashMap<TreasureEnum, Integer> treasureMapNew = new HashMap<>();
    DungeonLocation location = new DungeonLocation(dungeonId, coordinate.getX(), coordinate.getY());
    for (TreasureEnum treasureEnum : treasureMap.keySet()) {
      treasureMapNew.put(treasureEnum, treasureMap.get(treasureEnum));
    }
    location.treasureMap = treasureMapNew;
    HashMap<Weapon, Integer> newWeaponMap = new HashMap<>();
    for (Weapon weapon : this.weaponMap.keySet()) {
      newWeaponMap.put(weapon, this.weaponMap.get(weapon));
    }
    location.weaponMap = newWeaponMap;
    location.addMonster(this.monster == null ? null :
            this.monster.getCopy(this.monster.getHealth()));
    return location;
  }

  //Overloads of Object class
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DungeonLocation that = (DungeonLocation) o;
    return dungeonId == that.dungeonId && coordinate.equals(that.coordinate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(dungeonId, coordinate);
  }

  @Override
  public String toString() {
    return "Dungeon" + dungeonId
            + "- Treasure: " + treasureMap.toString()
            + "- Weapon: " + weaponMap.toString()
            + "- Monster: " + monster;
  }
}
