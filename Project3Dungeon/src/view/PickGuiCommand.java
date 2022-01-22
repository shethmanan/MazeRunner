package view;

import controller.ControllerFeatures;
import dungeonmodel.TreasureEnum;
import dungeonmodel.Weapon;

/**
 * Used to handle command when the user decides to pick.
 */
public class PickGuiCommand implements MazeGuiCommand {
  private final char Char_To_Select_Arrow_Pick = 'a';
  private final char Char_To_Select_Diamond_Pick = 'd';
  private final char Char_To_Select_Ruby_Pick = 'r';
  private final char Char_To_Select_Sapphire_Pick = 's';
  private char currentChar;
  private ControllerFeatures controller;

  /**
   * Initialize the direction to item to pick and  the controller.
   *
   * @param currentChar the character representing what to pick
   * @param controller  which can be used to call features supported by the controller
   */
  public PickGuiCommand(char currentChar, ControllerFeatures controller) {
    this.currentChar = currentChar;
    this.controller = controller;
  }

  @Override
  public void execute() {
    if (currentChar == Char_To_Select_Arrow_Pick) {
      controller.pickWeapon(Weapon.CROOKED_ARROW);
    } else if (currentChar == Char_To_Select_Ruby_Pick) {
      controller.pickTreasure(TreasureEnum.RUBY);
    } else if (currentChar == Char_To_Select_Diamond_Pick) {
      controller.pickTreasure(TreasureEnum.DIAMOND);
    } else if (currentChar == Char_To_Select_Sapphire_Pick) {
      controller.pickTreasure(TreasureEnum.SAPPHIRE);
    }
  }
}
