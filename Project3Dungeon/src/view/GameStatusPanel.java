package view;

import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Font;
import java.awt.Image;
import java.awt.Graphics;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.BorderFactory;
import javax.swing.UIManager;
import javax.swing.ImageIcon;
import javax.swing.Icon;
import javax.swing.border.Border;


import dungeonmodel.GameReadonlyInterface;
import dungeonmodel.SmellLevel;
import dungeonmodel.TreasureEnum;
import dungeonmodel.Weapon;

/**
 * Panel used to display the game and player details and instructions.
 */
public class GameStatusPanel extends JPanel {
  private GameReadonlyInterface dungeon;
  //  private final JLabel scoreBoardTitle;
  private String basePathForImage = "images/";
  private String imageExtension = ".png";
  private String imageForRuby = "ruby";
  private String imageForDiamond = "diamond";
  private String imageForSapphire = "emerald";
  private String imageForArrow = "arrow-black";
  private JLabel rubyCount;
  private JLabel diamondCount;
  private JLabel sapphireCount;
  private JLabel arrowCount;
  private JTextArea currentLocationDetail;
  private int gridRowNumber = 0;


  /**
   * Construct the game panel.
   *
   * @param dungeon the model used to fetch data to create view
   */
  public GameStatusPanel(GameReadonlyInterface dungeon) {
    this.dungeon = dungeon;
    this.setLayout(new GridBagLayout());
    Border border = BorderFactory.createLineBorder(Color.BLACK);
    this.setBorder(BorderFactory.createCompoundBorder(border,
            BorderFactory.createEmptyBorder(10, 10, 10, 5)));
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.NORTH;

    InstructionPanel instructionPanel = new InstructionPanel();
    gbc.gridy = gridRowNumber;
    gbc.gridx = 0;
    this.add(instructionPanel);


    gbc.gridy = ++gridRowNumber;
    JLabel desc = new JLabel("Player Stats: ");
    desc.setFont(new Font(desc.getFont().getFontName(), Font.BOLD, desc.getFont().getSize()));
    this.add(desc, gbc);
    gbc.gridx = 0;
    gbc.gridy = ++gridRowNumber;
    this.add(new JLabel("Treasure Collected: "), gbc);
    gbc.gridy = ++gridRowNumber;
    rubyCount = new JLabel("0");
    addRow(new JLabel("Ruby: ", getIconWithName(imageForRuby), JLabel.HORIZONTAL), rubyCount, gbc);
    gbc.gridy = ++gridRowNumber;
    diamondCount = new JLabel("0");
    addRow(new JLabel("Diamond: ", getIconWithName(imageForDiamond), JLabel.HORIZONTAL),
            diamondCount,
            gbc);
    gbc.gridy = ++gridRowNumber;
    sapphireCount = new JLabel("0");
    addRow(new JLabel("Sapphire: ", getIconWithName(imageForSapphire), JLabel.LEFT),
            sapphireCount, gbc);
    gbc.gridx = 0;
    gbc.gridy = ++gridRowNumber;
    this.add(new JLabel("Weapons "), gbc);
    gbc.gridy = ++gridRowNumber;
    arrowCount = new JLabel("0");
    addRow(new JLabel("Crooked Arrow: ", getIconWithName(imageForArrow), JLabel.RIGHT),
            arrowCount, gbc);
    gbc.gridy = ++gridRowNumber;
    gbc.gridx = 0;
    JLabel locDesc = new JLabel("Current Location Details: ");

    locDesc.setFont(new Font(locDesc.getFont().getFontName(), Font.BOLD,
            locDesc.getFont().getSize()));
    this.add(locDesc, gbc);
    gbc.gridy = ++gridRowNumber;
    gbc.gridx = 0;
    currentLocationDetail = new JTextArea(printCurrentLocationDetails(), 3, 15);
    currentLocationDetail.setWrapStyleWord(true);
    currentLocationDetail.setLineWrap(true);
    currentLocationDetail.setEditable(false);
    currentLocationDetail.setFocusable(false);
    currentLocationDetail.setBackground(UIManager.getColor("Label.background"));
    this.add(currentLocationDetail, gbc);
  }

  private String printCurrentLocationDetails() {
    String result;
    StringBuilder sb = new StringBuilder();
    boolean prevItemFound = false;
    //show treasure
    if (dungeon.getTreasureCountAtPlayerLocation() > 0) {
      sb.append(printHashMap(dungeon.getPlayer().getCurrentLocationOfPlayer()
              .getAllTreasure()));
      prevItemFound = true;
    }

    if (dungeon.getPlayer().getCurrentLocationOfPlayer().getAllWeapon().size() > 0) {
      if (prevItemFound) {
        sb.append(", ");
      }
      sb.append(printHashMap(dungeon.getPlayer().getCurrentLocationOfPlayer()
              .getAllWeapon()));
    }
    if (sb.length() > 0) {
      sb.append(" found.");
    }
    SmellLevel currentSmellLevel =
            dungeon.getDungeon().getSmellLevel(dungeon.getPlayer().getCurrentLocationOfPlayer());
    if (currentSmellLevel.equals(SmellLevel.STRONG)) {

      sb.append("You smell something terrible nearby.\n");
    } else if (currentSmellLevel.equals(SmellLevel.LIGHT)) {
      sb.append("You smell a mild stench nearby.\n");
    }

    return sb.length() > 0 ? sb.toString() : "-";
  }

  private void addRow(JLabel jLabel, JLabel rubyCount, GridBagConstraints gbc) {
    gbc.gridx = 0;
    this.add(jLabel, gbc);
    gbc.gridx = 1;
    this.add(rubyCount, gbc);
  }

  private Icon getIconWithName(String iconName) {
    ImageIcon icon = new ImageIcon(ClassLoader.getSystemResource(basePathForImage
            + iconName + imageExtension));
    Image i = icon.getImage();
    i = i.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
    return new ImageIcon(i);
  }

  private String printHashMap(HashMap hashMap) {
    StringBuilder sb = new StringBuilder();
    Iterator hmIterator = hashMap.entrySet().iterator();
    while (hmIterator.hasNext()) {
      Map.Entry mapElement = (Map.Entry) hmIterator.next();
      sb.append("," + mapElement.getValue() + " " + mapElement.getKey());
    }
    if (sb.length() < 1) {
      return "NONE";
    } else {
      return sb.substring(1);
    }
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    arrowCount.setText(printArrowDetails());
    rubyCount.setText(printTreasureDetails(TreasureEnum.RUBY));
    diamondCount.setText(printTreasureDetails(TreasureEnum.DIAMOND));
    sapphireCount.setText(printTreasureDetails(TreasureEnum.SAPPHIRE));

    currentLocationDetail.setText(printCurrentLocationDetails());
  }

  private String printTreasureDetails(TreasureEnum treasure) {

    HashMap<TreasureEnum, Integer> map = dungeon.getPlayer().getAllTreasure();
    String result;
    if (map == null || map.size() < 1 || map.get(treasure) == null) {
      result = "0";
    } else {
      result = Integer.toString(map.get(treasure));
    }
    return result;
  }

  private String printArrowDetails() {
    HashMap<Weapon, Integer> map = dungeon.getPlayer().getAllWeapon();
    String result;
    if (map == null || map.size() < 1) {
      result = " - ";
    } else {
      result = Integer.toString(map.get(Weapon.CROOKED_ARROW));
    }
    return result;
  }
}
