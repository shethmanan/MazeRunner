package controller;

import dungeonmodel.Direction;
import dungeonmodel.TreasureEnum;
import dungeonmodel.Weapon;

/**
 * Dictates list of features provided by the controller that can be used by other modules for
 * interacting with controller. Each feature is exposed as a function in this interface.
 * This function is used suitably as a callback by the view, to pass control to the controller.
 */
public interface ControllerFeatures {

  /**
   * Feature used when the user selects to play a new game.
   */
  void playGame();

  /**
   * Feature used when the user decides to move the player.
   *
   * @param direction the direction to move
   */
  void movePlayer(Direction direction);

  /**
   * Feature to use when the user decides to pick treasure.
   *
   * @param treasureToPick the treasure to pick.
   */
  void pickTreasure(TreasureEnum treasureToPick);

  /**
   * Feature to use when the used decides to pick a weapon.
   *
   * @param weaponToPick the weapon to pick.
   */
  void pickWeapon(Weapon weaponToPick);

  /**
   * Feature to use when the user decides to shoot an arrow.
   *
   * @param attackDirection the direction in which the arrow will be fired.
   * @param distance        the distance which the arrow should cover.
   */
  void shootArrow(Direction attackDirection, int distance);

  /**
   * Used to restart the game with the same parameters.
   */
  void restartGame();

  /**
   * Used to start a new game.
   */
  void setNewGameParameter();

  /**
   * Used to select the parameters that will be used to start the new game.
   *
   * @param isWrapping                       dungeon is wrapped when true, false otherwise
   * @param noRows                           number of rows in the dungeon
   * @param noColumns                        number of columns in the dungeon
   * @param interconnectivity                degree of interconnectivity in the dungeon
   * @param percentageCaveContainingTreasure the percentage of caves that will contain treasure
   * @param noOfMonster                      the number of monsters in the dungeon
   */
  void setNewGameParameter(boolean isWrapping, int noRows, int noColumns, int interconnectivity,
                           int percentageCaveContainingTreasure, int noOfMonster);

  /**
   * Exit the game.
   */
  void quitGame();
}
