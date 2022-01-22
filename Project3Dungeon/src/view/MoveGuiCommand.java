package view;

import controller.ControllerFeatures;
import dungeonmodel.Direction;

/**
 * Used to handle command when the user decides to move.
 */
public class MoveGuiCommand implements MazeGuiCommand {
  private Direction direction;
  private ControllerFeatures controller;

  /**
   * Initialize the direction to move and the controller.
   *
   * @param direction  the direction to move
   * @param controller which can be used to call features supported by the controller
   */
  public MoveGuiCommand(Direction direction, ControllerFeatures controller) {
    this.direction = direction;
    this.controller = controller;
  }

  /**
   * Execute the actual move method.
   */
  @Override
  public void execute() {
    controller.movePlayer(direction);
  }
}
