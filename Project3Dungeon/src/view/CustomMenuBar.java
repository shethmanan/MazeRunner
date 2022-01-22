package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import controller.ControllerFeatures;

/**
 * This class is used to add a custom menu bar to the game and handle event on them.
 */
public class CustomMenuBar extends JMenuBar implements ActionListener {
  private String restartGame = "Restart Game";
  private String newGame = "New Game";
  private String quitGame = "Quit Game";
  private ControllerFeatures controller;

  /**
   * Construct the CustomMenuBar.
   */
  public CustomMenuBar() {

    JMenuItem newGameButton = new JMenuItem(newGame);
    newGameButton.addActionListener(this);

    JMenuItem restartGameButton = new JMenuItem(restartGame);
    restartGameButton.setMnemonic(KeyEvent.VK_A);
    restartGameButton.addActionListener(this);

    JMenuItem quitGameButton = new JMenuItem(quitGame);
    quitGameButton.addActionListener(this);

    JMenu settingMenu = new JMenu("Menu");
    settingMenu.add(newGameButton);
    settingMenu.addSeparator();
    settingMenu.add(restartGameButton);
    settingMenu.addSeparator();
    settingMenu.add(quitGameButton);
    this.add(settingMenu);
  }

  /**
   * Set controller which can be used to call features supported by the controller.
   *
   * @param controller the controller which provides a list of feature
   */
  public void setController(ControllerFeatures controller) {
    this.controller = controller;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (controller == null) {
      return;
    }
    if (e.getActionCommand().equals(restartGame)) {
      controller.restartGame();
    } else if (e.getActionCommand().equals(newGame)) {
      controller.setNewGameParameter();
    } else if (e.getActionCommand().equals(quitGame)) {
      controller.quitGame();
    }
  }
}
