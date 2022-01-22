package dungeonmodel;

import java.util.HashMap;

/**
 * Represent the player playing the Dungeon game. He has will have properties such as his current
 * location and treasure collected so-far.
 */
class Player implements PlayerInterface {
  private DungeonLocation currentLocation;
  private HashMap<TreasureEnum, Integer> treasureMap;
  private HashMap<Weapon, Integer> weaponMap;
  private int health;
  private static final int CROOKED_ARROW_COUNT_AT_START = 3;

  /**
   * Construct the player object and initializes required class fields.
   */
  Player() {
    treasureMap = new HashMap<>();
    weaponMap = new HashMap<>();
    weaponMap.put(Weapon.CROOKED_ARROW, CROOKED_ARROW_COUNT_AT_START);
    health = 100;
  }

  /**
   * Update the current location of the player.
   *
   * @param currentLocation is the DungeonLocation where the player is currently residing.
   */
  void setCurrentLocation(DungeonLocation currentLocation)
          throws IllegalArgumentException {
    if (currentLocation == null) {
      throw new IllegalArgumentException("Location cannot be null");
    }
    this.currentLocation = currentLocation;
  }

  /**
   * The current location of the player.
   *
   * @return the current location of the player.
   * @throws NullPointerException when the player has not yet started playing and is not in the
   *                              dungeon.
   */
  DungeonLocation getCurrentLocationObject() {
    return currentLocation;
  }

  @Override
  public DungeonLocation getCurrentLocationOfPlayer() {
    return currentLocation.getDeepCopy();
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
    HashMap<Weapon, Integer> weaponNewMap = new HashMap<>();
    for (Weapon weapon : weaponMap.keySet()) {
      weaponNewMap.put(weapon, weaponMap.get(weapon));
    }
    return weaponNewMap;
  }

  void addTreasure(HashMap<TreasureEnum, Integer> tempTreasureMap)
          throws IllegalArgumentException {
    if (tempTreasureMap == null) {
      throw new IllegalArgumentException("Treasure map cannot be null");
    }
    for (TreasureEnum treasureEnum : tempTreasureMap.keySet()) {
      if (treasureMap.get(treasureEnum) == null) {
        treasureMap.put(treasureEnum, tempTreasureMap.get(treasureEnum));
      } else {
        treasureMap.put(treasureEnum,
                treasureMap.get(treasureEnum) + tempTreasureMap.get(treasureEnum));
      }
    }
  }

  void addRemoveWeapon(Weapon weapon, int count)
          throws IllegalArgumentException {
    if (weapon == null) {
      throw new IllegalArgumentException("Weapon cannot be null");
    }
    if (weaponMap.get(weapon) == null) {
      weaponMap.put(weapon, count);
    } else {
      weaponMap.put(weapon,
              weaponMap.get(weapon) + count);
    }
  }

  @Override
  public String printCollectedTreasure() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Treasure Collected: ");
    for (TreasureEnum treasureEnum : treasureMap.keySet()) {
      stringBuilder.append(treasureEnum + "-" + treasureMap.get(treasureEnum) + ",");
    }
    if (treasureMap.isEmpty()) {
      stringBuilder.append("None ");
    }
    return stringBuilder.substring(0, stringBuilder.length() - 1);
  }

  @Override
  public String getPlayerDetail() {
    return this.getCurrentLocationOfPlayer().toString() + "\n" + this.printCollectedTreasure();
  }

  @Override
  public int getPlayerHealth() {
    return this.health;
  }

  void deductHealth(int healthDamage)
          throws IllegalArgumentException {
    if (healthDamage < 0) {
      throw new IllegalArgumentException("The damage should be expressed as positive value");
    }
    this.health = this.health > healthDamage ? this.health - healthDamage : 0;
  }
}
