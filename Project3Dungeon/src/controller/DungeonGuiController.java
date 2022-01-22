package controller;

import dungeonmodel.ArrowTraversalModelInterface;
import dungeonmodel.Direction;
import dungeonmodel.Game;
import dungeonmodel.GameInterface;
import dungeonmodel.TreasureEnum;
import dungeonmodel.Weapon;
import view.GuiView;
import view.GameViewSwingUi;
import dungeonmodel.SoundType;

/**
 * GUI controller uses the model to play the dungeon game as a GUI enabled game.
 */
public class DungeonGuiController implements ControllerFeatures {
  private GameInterface model;
  private GuiView view;

  /**
   * Initialize the Controller with view and model.
   *
   * @param model the model for dungeon game
   * @param view  the GUI view for dungeon game
   */
  public DungeonGuiController(GameInterface model, GuiView view) {
    if (model == null || view == null) {
      throw new IllegalArgumentException("Model and view cannot be null");
    }
    this.model = model;
    this.view = view;
  }

  @Override
  public void playGame() {
    view.setListener(this);
    view.setVisible();
  }

  @Override
  public void movePlayer(Direction direction) {
    if (isGameOver()) {
      return;
    }
    char d = 'n';
    switch (direction) {
      case NORTH:
        d = 'n';
        break;
      case SOUTH:
        d = 's';
        break;
      case EAST:
        d = 'e';
        break;
      case WEST:
        d = 'w';
        break;
      default:
        d = 'a';
        break;
    }
    if (model.movePlayerToDirection(d)) {
      view.playSound(SoundType.PLAYER_MOVE);
      view.repaint();
      isGameOver();
    } else {
      System.out.println("Cannot mode player as invalid move entered");
    }
  }

  @Override
  public void pickTreasure(TreasureEnum treasureToPick) {
    if (isGameOver()) {
      return;
    }
    try {
      if (model.collectATreasure(treasureToPick, 1)) {
        view.playSound(SoundType.PICK);
        view.repaint();
      }
    } catch (IllegalArgumentException e) {
      System.out.println("Could not pick treasure");
    }
  }

  @Override
  public void pickWeapon(Weapon weaponToPick) {
    if (isGameOver()) {
      return;
    }
    try {
      if (model.collectAWeapon(weaponToPick, 1)) {
        view.playSound(SoundType.PICK);
        view.repaint();
      }
    } catch (IllegalArgumentException e) {
      System.out.println("Could not pick weapon");
    }
  }

  @Override
  public void shootArrow(Direction attackDirection, int distance) {
    if (isGameOver()) {
      return;
    }
    try {
      ArrowTraversalModelInterface arrow = model.killMonster(Weapon.CROOKED_ARROW, attackDirection,
              distance);

      view.repaint();
      if (arrow.isKillMonsterSuccessful()) {
        view.playSound(SoundType.MONSTER_DEAD);
        view.showPopupMessage("Bravo! You killed an otyugh.");
      }
    } catch (IllegalArgumentException e) {
      System.out.println("Exception when firing crooked arrow");
    }
  }

  @Override
  public void restartGame() {
    model.restartGame();
    view.repaint();
  }

  @Override
  public void setNewGameParameter() {
    view.openGameParameterInputPanel();
  }

  @Override
  public void setNewGameParameter(boolean isWrapping, int noRows, int noColumns,
                                  int interconnectivity,
                                  int percentageCaveContainingTreasure, int noOfMonster) {
    this.model = new Game(isWrapping, noRows, noColumns, interconnectivity,
            percentageCaveContainingTreasure, noOfMonster);
    view.dispose();
    this.view = new GameViewSwingUi(model);

    this.playGame();
  }

  @Override
  public void quitGame() {
    System.exit(0);
  }

  /**
   * Checks if the game is over.
   *
   * @return true when game is over, false otherwise
   */
  private boolean isGameOver() {
    if (model.isGameOver()) {
      if (model.getPlayer().getPlayerHealth() == 100) {
        view.playSound(SoundType.PLAYER_WON);
        view.showPopupMessage("Congratulations! You won the game.");
      } else {
        view.playSound(SoundType.PLAYER_LOSE);
        view.showPopupMessage("Nom, nom, nomm..Game Over. An Otyugh ate you!");
      }
      return true;
    }
    return false;
  }

}
