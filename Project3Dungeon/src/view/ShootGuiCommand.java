package view;

import controller.ControllerFeatures;
import dungeonmodel.Direction;

/**
 * Used to handle command when the user decides to shoot.
 */
public class ShootGuiCommand implements MazeGuiCommand {
  private Direction direction;
  private int distanceToShoot;
  private ControllerFeatures controller;

  /**
   * Initialize the required parameters.
   *
   * @param direction       the direction to shoot
   * @param distanceToShoot the distance to shoot to
   * @param controller      which can be used to call features supported by the controller
   */
  public ShootGuiCommand(Direction direction, int distanceToShoot, ControllerFeatures controller) {
    this.controller = controller;
    this.distanceToShoot = distanceToShoot;
    this.direction = direction;
  }

  @Override
  public void execute() {
    controller.shootArrow(direction, distanceToShoot);
  }
}
