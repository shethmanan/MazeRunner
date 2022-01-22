package view;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import controller.ControllerFeatures;
import dungeonmodel.Direction;
import dungeonmodel.GameReadonlyInterface;


/**
 * Responsible for handling keyboard and click events on the Maze.
 */
public class MazeListener extends KeyAdapter implements MouseListener {
  private char prevKey = '.';
  private int distanceToShoot = 0;
  private ControllerFeatures controller;
  private final char Char_To_Pick_Option = 'p';
  private final char Char_To_Select_Shoot_Option = 's';
  private final char Char_For_No_Action = '.';
  private GameReadonlyInterface model;
  private int sizeOfEachDungeonLocation = 100;

  /**
   * Initialize the controller and the model.
   *
   * @param controller the controller which provides a set of feature that can be used by the view
   * @param model      the model that contains the state of the game
   */
  public MazeListener(ControllerFeatures controller, GameReadonlyInterface model) {
    this.controller = controller;
    this.model = model;
  }


  @Override
  public void keyReleased(KeyEvent e) {
    super.keyReleased(e);
    Direction d = getDirectionFromKeyEvent(e.getKeyCode());
    MazeGuiCommand command = null;
    char currentChar = e.getKeyChar();
    if (prevKey == Char_To_Pick_Option) {
      command = new PickGuiCommand(currentChar, controller);
      prevKey = Char_For_No_Action;
    } else if (prevKey == Char_To_Select_Shoot_Option && distanceToShoot == -1) {
      if (Character.isDigit(currentChar)) {
        distanceToShoot = Integer.parseInt("" + currentChar);
      } else {
        prevKey = Char_For_No_Action;
        distanceToShoot = -1;
      }
    } else if (prevKey == Char_To_Select_Shoot_Option && distanceToShoot != -1 && d != null) {
      command = new ShootGuiCommand(d, distanceToShoot, controller);
      prevKey = Char_For_No_Action;
      distanceToShoot = -1;
    } else if (d != null) {
      command = new MoveGuiCommand(d, controller);
      prevKey = currentChar;
      distanceToShoot = -1;
    } else {
      prevKey = currentChar;
    }
    if (command != null) {
      command.execute();
    }

  }


  /**
   * Returns the Direction corresponding to the Arrow key.
   *
   * @param vkUp the int code of the key
   * @return the direction corresponding to each arrow key, null otherwise
   */
  private Direction getDirectionFromKeyEvent(int vkUp) {
    if (vkUp == KeyEvent.VK_UP) {
      return Direction.NORTH;
    } else if (vkUp == KeyEvent.VK_DOWN) {
      return Direction.SOUTH;
    } else if (vkUp == KeyEvent.VK_LEFT) {
      return Direction.WEST;
    } else if (vkUp == KeyEvent.VK_RIGHT) {
      return Direction.EAST;
    }
    return null;
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    Direction d = getDirectionFromClickEvent(e.getX(), e.getY());
    if (d != null) {
      new MoveGuiCommand(d, controller).execute();
    }
  }

  private Direction getDirectionFromClickEvent(int clickX, int clickY) {
    Direction direction = null;
    int possibleCol = clickX / sizeOfEachDungeonLocation;
    int possibleRow = clickY / sizeOfEachDungeonLocation;
    int locationIdOfClick = possibleRow * model.getDungeon().getNoOfColumns() + possibleCol + 1;
    int locationIdOfPlayer = model.getPlayer().getCurrentLocationOfPlayer().getDungeonId();
    if (locationIdOfClick == locationIdOfPlayer - 1) {
      direction = Direction.WEST;
    } else if (locationIdOfClick == locationIdOfPlayer + 1) {
      direction = Direction.EAST;
    } else if (locationIdOfClick == locationIdOfPlayer - model.getDungeon().getNoOfColumns()) {
      direction = Direction.NORTH;
    } else if (locationIdOfClick == locationIdOfPlayer + model.getDungeon().getNoOfColumns()) {
      direction = Direction.SOUTH;
    }
    return direction;
  }


  @Override
  public void mousePressed(MouseEvent e) {
    // Does not need overriding.
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    // Does not need overriding.
  }

  @Override
  public void mouseEntered(MouseEvent e) {
    // Does not need overriding.
  }

  @Override
  public void mouseExited(MouseEvent e){
    // Does not need overriding.
  }
}
