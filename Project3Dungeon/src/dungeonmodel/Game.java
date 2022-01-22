package dungeonmodel;

import random.RandomNumberGenerator;

import java.util.HashMap;
import java.util.List;


/**
 * Provides the implementation of the Game interface required to play the 'Dungeon' game.
 */
public class Game implements GameInterface {
  private Dungeon dungeonGame;
  private Dungeon dungeonGameBackUp;
  private Player playerBackUp;
  private Player player;
  private RandomNumberGenerator randomNumberGenerator;

  /**
   * Constructor that takes the required parameters, including an option to provide a random
   * number generator to build a game.
   *
   * @param wrapping                         true if dungeon is wrapped, false otherwise.
   * @param noRows                           represents the number of rows in the dungeon.
   * @param noColumns                        represents the number of columns in the dungeon.
   * @param interconnectivity                represents the degree on interconnectivity.
   * @param percentageCaveContainingTreasure represents the percentage of caves that should
   *                                         contain treasure in the dungeon.
   * @param monsterCount                     represents the number of monsters in the dungeon.
   * @param randomNumberGenerator            is the object used to generate randomness and pick
   *                                         random
   *                                         numbers.
   * @throws IllegalArgumentException when the noRows is negative
   *                                  when the noColumns is negative.
   *                                  when the interconnectivity is negative
   *                                  when null is passed as randomNumberGenerator.
   */
  public Game(boolean wrapping, int noRows, int noColumns, int interconnectivity,
              int percentageCaveContainingTreasure,
              int monsterCount, RandomNumberGenerator randomNumberGenerator)
          throws IllegalArgumentException {
    dungeonGame = new DungeonBuilder(wrapping, noRows, noColumns, interconnectivity,
            percentageCaveContainingTreasure, monsterCount,
            randomNumberGenerator).getDungeon();
    player = new Player();
    player.setCurrentLocation(dungeonGame.getStartLocation());
    this.randomNumberGenerator = randomNumberGenerator;


    dungeonGameBackUp = dungeonGame.getDeepCopy();
    playerBackUp = new Player();
    playerBackUp.setCurrentLocation(dungeonGameBackUp.getStartLocation());
  }

  /**
   * Constructor that takes the required parameter to build a game.
   *
   * @param wrapping                         true if dungeon is wrapped, false otherwise.
   * @param noRows                           represents the number of rows in the dungeon.
   * @param noColumns                        represents the number of columns in the dungeon.
   * @param interconnectivity                represents the degree on interconnectivity.
   * @param percentageCaveContainingTreasure represents the percentage
   *                                         of caves that should
   *                                         contain treasure in the dungeon.
   * @throws IllegalArgumentException when the noRows is negative
   *                                  when the noColumns is negative.
   *                                  when the interconnectivity is negative
   *                                  when null is passed as randomNumberGenerator.
   */
  public Game(boolean wrapping, int noRows, int noColumns, int interconnectivity,
              int percentageCaveContainingTreasure, int monsterCount)
          throws IllegalArgumentException {
    this(wrapping, noRows, noColumns, interconnectivity, percentageCaveContainingTreasure,
            monsterCount, new RandomNumberGenerator());
  }

  @Override
  public DungeonInterface getDungeon() {
    return dungeonGame.getDeepCopy();
  }

  @Override
  public DungeonLocationInterface getStartLocation() {
    return dungeonGame.getStartLocation();
  }

  @Override
  public DungeonLocationInterface getEndLocation() {
    return dungeonGame.getEndLocation();
  }

  @Override
  public PlayerInterface getPlayer() {
    return player;
  }

  @Override
  public List<String> getPlayerPossibleMoves() {
    return dungeonGame.getPossibleDirection(player.getCurrentLocationObject());
  }

  @Override
  public boolean movePlayerToDirection(char direction) throws IllegalArgumentException {
    boolean status = false;
    if (isGameOver()) {
      throw new IllegalArgumentException("Game is over.");
    }
    if (direction != 'e' && direction != 'w' && direction != 'n' && direction != 's') {
      throw new IllegalArgumentException("Kindly provide valid direction");
    }
    String dString = Character.toString(direction).toLowerCase();
    Direction d;
    switch (dString) {
      case "e":
        d = Direction.EAST;
        break;
      case "w":
        d = Direction.WEST;
        break;
      case "n":
        d = Direction.NORTH;
        break;
      default:
        d = Direction.SOUTH;
    }
    if (dungeonGame.isValidMove(player.getCurrentLocationObject(), d)) {
      player.setCurrentLocation(dungeonGame.getLocation(player.getCurrentLocationObject(), d));
      dungeonGame.setVisited(player.getCurrentLocationObject());
      status = true;
    }
    checkPlayerKilledByMonster();
    return status;
  }

  private boolean checkPlayerKilledByMonster() {
    if (player.getCurrentLocationObject().isMonsterPresent()) {
      if (player.getCurrentLocationObject().getMonster().getHealth() == 100) {
        //Kill player
        player.deductHealth(player.getCurrentLocationObject().getMonster().getDamage());
      } else {
        int kill = randomNumberGenerator.getRandomInt(0, 1);
        if (kill == 1) {
          player.deductHealth(player.getCurrentLocationObject().getMonster().getDamage());
        }
      }
    }
    return false;
  }

  @Override
  public int getTreasureCountAtPlayerLocation() {
    return dungeonGame.getAllTreasureCountAt(player.getCurrentLocationObject());
  }

  @Override
  public boolean collectATreasure(TreasureEnum treasure, int unitToCollect)
          throws IllegalArgumentException {
    if (dungeonGame.collectATreasure(player.getCurrentLocationObject(), treasure, unitToCollect)) {
      HashMap<TreasureEnum, Integer> treasureMap = new HashMap<>();
      treasureMap.put(treasure, unitToCollect);
      player.addTreasure(treasureMap);
      return true;
    }
    return false;
  }

  @Override
  public boolean collectAWeapon(Weapon weapon, int unitToCollect)
          throws IllegalArgumentException {
    if (dungeonGame.collectAWeapon(player.getCurrentLocationObject(), weapon, unitToCollect)) {
      HashMap<Weapon, Integer> weaponMap = new HashMap<>();
      weaponMap.put(weapon, unitToCollect);
      player.addRemoveWeapon(weapon, unitToCollect);
      return true;
    }
    return false;
  }

  @Override
  public boolean collectTreasure() {
    HashMap<TreasureEnum, Integer> treasureMap =
            dungeonGame.collectTreasure(player.getCurrentLocationObject());
    player.addTreasure(treasureMap);
    return true;
  }

  @Override
  public boolean isGameOver() {
    return ((player.getCurrentLocationObject().equals(dungeonGame.getEndLocation())
            && !player.getCurrentLocationObject().isMonsterPresent())
            || player.getPlayerHealth() <= 0);
  }

  @Override
  public ArrowTraversalModelInterface killMonster(Weapon weapon, Direction attackDirection,
                                                  int distance) {
    if (weapon == null) {
      throw new IllegalArgumentException("Weapon cannot be null");
    }
    if (attackDirection == null) {
      throw new IllegalArgumentException("Weapon cannot be null");
    }
    if (distance < 1) {
      throw new IllegalArgumentException("Distance cannot be less than 1");
    }
    if (!getPlayerPossibleMoves().contains(attackDirection.toString())) {
      throw new IllegalArgumentException("Enter a valid direction to shoot");
    }
    int weaponCountOfPlayer = (int) player.getAllWeapon().getOrDefault(weapon, 0);
    if (weaponCountOfPlayer <= 0) {
      throw new IllegalArgumentException("Player does not have specified weapon");
    }
    ArrowTraversalModel arrowTraversalModel =
            dungeonGame.killMonster(player.getCurrentLocationObject(), weapon,
                    attackDirection, distance);
    player.addRemoveWeapon(weapon, -1);
    return arrowTraversalModel.getDeepCopy();
  }

  @Override
  public void restartGame() {
    this.dungeonGame = dungeonGameBackUp.getDeepCopy();
    this.player = new Player();
    player.setCurrentLocation(dungeonGame.getStartLocation());
  }
}
