package dungeontest;

import java.util.List;

import dungeonmodel.ArrowTraversalModelInterface;
import dungeonmodel.Direction;
import dungeonmodel.DungeonInterface;
import dungeonmodel.DungeonLocationInterface;
import dungeonmodel.Game;
import dungeonmodel.GameInterface;
import dungeonmodel.PlayerInterface;
import dungeonmodel.TreasureEnum;
import dungeonmodel.Weapon;
import random.RandomNumberGenerator;

/**
 * A mock model to test the controller.
 */
public class MockGameModel implements GameInterface {
  StringBuilder gameLog;

  /**
   * Construct the mock model.
   */
  public MockGameModel(Appendable gameLog) {
    this.gameLog = (StringBuilder) gameLog;
  }

  @Override
  public DungeonInterface getDungeon() {
    gameLog.append("getDungeon() called");
    return null;
  }

  @Override
  public DungeonLocationInterface getStartLocation() {
    gameLog.append("getStartLocation() called");
    return null;
  }

  @Override
  public DungeonLocationInterface getEndLocation() {
    gameLog.append("getEndLocation() called");
    return null;
  }

  @Override
  public PlayerInterface getPlayer() {
    gameLog.append("getPlayer() called");
    return new Game(false, 4, 6,
            1, 30, 3,
            new RandomNumberGenerator()).getPlayer();
  }

  @Override
  public List<String> getPlayerPossibleMoves() {
    gameLog.append("getPlayerPossibleMoves() called");
    return null;
  }

  @Override
  public boolean movePlayerToDirection(char direction) throws IllegalArgumentException {
    gameLog.append("movePlayerToDirection() called with: " + direction);
    return false;
  }

  @Override
  public int getTreasureCountAtPlayerLocation() {
    gameLog.append("getTreasureCountAtPlayerLocation() called");
    return 0;
  }

  @Override
  public boolean collectATreasure(TreasureEnum treasure, int unitToCollect)
          throws IllegalArgumentException {
    gameLog.append("collectATreasure() called with treasure: " + treasure
            + " ,units: " + unitToCollect);
    return false;
  }

  @Override
  public boolean collectAWeapon(Weapon weapon, int unitToCollect)
          throws IllegalArgumentException {
    gameLog.append("collectAWeapon() called with weapon: " + weapon
            + " ,units: " + unitToCollect);
    return true;
  }

  @Override
  public boolean collectTreasure() {
    gameLog.append("collectTreasure() called");
    return false;
  }

  @Override
  public boolean isGameOver() {
    gameLog.append("isGameOver() called");
    return false;
  }

  @Override
  public ArrowTraversalModelInterface killMonster(Weapon weapon, Direction attackDirection,
                                                  int distance) throws IllegalArgumentException {
    gameLog.append(String.format(" killMonster() called with weapon: %s, direction: %s, distance: "
            + "%d", weapon, attackDirection, distance));
    return new Game(false, 4, 6,
            1, 30, 3,
            new RandomNumberGenerator()).killMonster(Weapon.CROOKED_ARROW, Direction.WEST, 3);
  }

  @Override
  public void restartGame() {
    gameLog.append("restartGame() called");
  }

  @Override
  public String toString() {
    return gameLog.toString();
  }
}
