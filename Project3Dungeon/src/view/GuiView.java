package view;


import controller.ControllerFeatures;
import dungeonmodel.SoundType;

/**
 * Contains the features that should be provided by the view for the dungeon game.
 */
public interface GuiView {
  /**
   * Make the view visible.
   */
  void setVisible();

  /**
   * Add listeners to the view.
   *
   * @param controller the module which provides the set of features which can be used by the view
   */
  void setListener(ControllerFeatures controller);

  /**
   * Show a popup message in the view.
   *
   * @param message the message to display in the popup
   */
  void showPopupMessage(String message);

  /**
   * Used to accept new game parameters as input.
   */
  void openGameParameterInputPanel();

  /**
   * Used to repaint the view.
   */
  void repaint();

  /**
   * Used to dispose the view.
   */
  void dispose();

  void playSound(SoundType soundType);
}
