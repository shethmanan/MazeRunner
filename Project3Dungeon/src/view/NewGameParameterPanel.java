package view;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JOptionPane;
import javax.swing.JLabel;

import controller.ControllerFeatures;

/**
 * A panel that works as a modal to take parameter inputs for creating new dungeon game.
 */
public class NewGameParameterPanel extends JPanel {
  private ControllerFeatures controller;
  private final String[] wrappingOption = {"True", "False"};
  private JComboBox<String> wrapping;
  private JSpinner noRows;
  private JSpinner noCols;
  private JSpinner interconnectivityDegree;
  private JSpinner percentageOfCaveContainingTreasure;
  private JSpinner noMonster;

  /**
   * Creates the new Parameter panel.
   */
  public NewGameParameterPanel() {
    wrapping = new JComboBox<>(wrappingOption);
    this.noRows = new JSpinner(new SpinnerNumberModel(4, 4, 20, 1));
    this.noCols = new JSpinner(new SpinnerNumberModel(6, 6, 20, 1));
    interconnectivityDegree = new JSpinner(new SpinnerNumberModel(1, 0, 20, 1));
    percentageOfCaveContainingTreasure = new JSpinner(new SpinnerNumberModel(30, 1,
            100, 1));
    noMonster = new JSpinner(new SpinnerNumberModel(3, 1, 20, 1));

    this.setLayout(new GridLayout(0, 1));
    this.add(new JLabel("Enter the parameter for game: "));
    this.add(new JLabel("Wrapping: "));
    this.add(wrapping);
    this.add(new JLabel("Number of rows: "));
    this.add(noRows);
    this.add(new JLabel("Number of columns: "));
    this.add(noCols);
    this.add(new JLabel("Degree of interconnectivity: "));
    this.add(interconnectivityDegree);
    this.add(new JLabel("Percentage of caves containing treasure: "));
    this.add(percentageOfCaveContainingTreasure);
    this.add(new JLabel("Number of monsters: "));
    this.add(noMonster);
  }

  /**
   * Sets the controller for the class which can be used for features provided by the controller.
   *
   * @param controller the controller that will be used to call features of the controller.
   */
  public void setController(ControllerFeatures controller) {
    this.controller = controller;
  }

  /**
   * Display the current panel.
   */
  public void showPanel() {
    int result = JOptionPane.showConfirmDialog(this, this, "Game Parameters",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE);
    if (result == JOptionPane.OK_OPTION) {

      boolean isWrapped = wrapping.getSelectedIndex() == 0 ? true : false;
      controller.setNewGameParameter(isWrapped, Integer.parseInt(noRows.getValue().toString()),
              Integer.parseInt(noCols.getValue().toString()),
              Integer.parseInt(interconnectivityDegree.getValue().toString()),
              Integer.parseInt(percentageOfCaveContainingTreasure.getValue().toString()),
              Integer.parseInt(noMonster.getValue().toString()));
    }
  }

}
