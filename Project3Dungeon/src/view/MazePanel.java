package view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Rectangle;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import dungeonmodel.Direction;
import dungeonmodel.DungeonInterface;
import dungeonmodel.DungeonLocationInterface;
import dungeonmodel.GameReadonlyInterface;
import dungeonmodel.SmellLevel;
import dungeonmodel.TreasureEnum;
import dungeonmodel.Weapon;

/**
 * The JPanel which contains the entire dungeon to be displayed on the screen.
 */
public class MazePanel extends JPanel {
  private GameReadonlyInterface gameModel;
  private HashMap<String, String> nameOfImageFile;
  private HashMap<String, Integer> positionOfItemInsideALocation;
  private String basePathForImage = "images/";
  private String imageExtension = ".png";
  private final int Size_Per_Grid_Item = 100;
  private int prevPlayerX = 0;
  private int prevPlayerY = 0;

  /**
   * Constructs the Maze Panel and initializes the game dungeon.
   *
   * @param dungeon the game
   */
  public MazePanel(GameReadonlyInterface dungeon) {
    this.gameModel = dungeon;
    nameOfImageFile = new HashMap<>();
    positionOfItemInsideALocation = new HashMap<>();
    initializeAllFilePath(nameOfImageFile);
    initializePositionOfItemsWithinLocation(positionOfItemInsideALocation);
    this.setLayout(new GridBagLayout());
    this.setPreferredSize(new Dimension((dungeon.getDungeon().getNoOfColumns() + 0)
            * Size_Per_Grid_Item,
            (dungeon.getDungeon().getNoOfRows() + 0) * Size_Per_Grid_Item));
  }

  private void initializePositionOfItemsWithinLocation(HashMap<String, Integer>
                                                               positionOfItemOnsideLocation) {
    positionOfItemOnsideLocation.put(TreasureEnum.SAPPHIRE.name() + "X", 10);
    positionOfItemOnsideLocation.put(TreasureEnum.SAPPHIRE.name() + "Y", 10);

    positionOfItemOnsideLocation.put(TreasureEnum.RUBY.name() + "X", 20);
    positionOfItemOnsideLocation.put(TreasureEnum.RUBY.name() + "Y", 10);

    positionOfItemOnsideLocation.put(TreasureEnum.DIAMOND.name() + "X", 32);
    positionOfItemOnsideLocation.put(TreasureEnum.DIAMOND.name() + "Y", 10);

    positionOfItemOnsideLocation.put("otyughX", 25);
    positionOfItemOnsideLocation.put("otyughY", 30);

    positionOfItemOnsideLocation.put(Weapon.CROOKED_ARROW.name() + "X", 30);
    positionOfItemOnsideLocation.put(Weapon.CROOKED_ARROW.name() + "Y", 30);

    positionOfItemOnsideLocation.put("playerX", 25);
    positionOfItemOnsideLocation.put("playerY", 25);

    positionOfItemOnsideLocation.put(SmellLevel.STRONG.name() + "X", 15);
    positionOfItemOnsideLocation.put(SmellLevel.STRONG.name() + "Y", 15);

    positionOfItemOnsideLocation.put(SmellLevel.LIGHT.name() + "X", 15);
    positionOfItemOnsideLocation.put(SmellLevel.LIGHT.name() + "Y", 15);
  }

  private void initializeAllFilePath(HashMap<String, String> imageToFileName) {
    imageToFileName.put(Direction.EAST.name(), "E");
    imageToFileName.put(Direction.NORTH.name(), "N");
    imageToFileName.put(Direction.SOUTH.name(), "S");
    imageToFileName.put(Direction.WEST.name(), "W");
    imageToFileName.put(TreasureEnum.RUBY.name(), "ruby");
    imageToFileName.put(TreasureEnum.DIAMOND.toString(), "diamond");
    imageToFileName.put(TreasureEnum.SAPPHIRE.toString(), "emerald");
    imageToFileName.put(SmellLevel.STRONG.name(), "stench_strong");
    imageToFileName.put(SmellLevel.LIGHT.name(), "stench_light");
    imageToFileName.put("otyugh50", "injured_otyugh");
    imageToFileName.put("otyugh100", "healthy_otyugh");
    imageToFileName.put(Weapon.CROOKED_ARROW.toString(), "arrow-black");
    imageToFileName.put("player", "player");

  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    List<Integer> locationVisitedByPlayer = gameModel.getDungeon().getLocationVisitedByPlayer();
    BufferedImage img = null;
    try {
      DungeonInterface dungeonInterface = gameModel.getDungeon();
      int rows = dungeonInterface.getNoOfRows();
      int columns = dungeonInterface.getNoOfColumns();
      DungeonLocationInterface playerLocation = gameModel.getPlayer().getCurrentLocationOfPlayer();
      int xIndex = 0;
      int yIndex = 0;
      List<DungeonLocationInterface> dungeonList = dungeonInterface.getDungeonList();
      int currentDungeonLocationIndex = 0;
      for (int i = 0; i < rows; i++) {
        for (int j = 0; j < columns; j++) {
          boolean isPlayerLocation = false;
          SmellLevel smellLevel = SmellLevel.ZERO;
          if (playerLocation.equals(dungeonList.get(currentDungeonLocationIndex))) {
            isPlayerLocation = true;
            smellLevel = dungeonInterface
                    .getSmellLevel(dungeonList.get(currentDungeonLocationIndex));
            if (prevPlayerX != xIndex || prevPlayerY != yIndex) {
              this.scrollRectToVisible(new Rectangle(xIndex, yIndex, 300, 300));
              prevPlayerX = xIndex;
              prevPlayerY = yIndex;
            }

          }
          boolean isLocationVisited = locationVisitedByPlayer
                  .contains(dungeonList.get(currentDungeonLocationIndex)
                          .getDungeonId());

          img = getFinalDungeonLocationImage(dungeonList.get(currentDungeonLocationIndex++),
                  isPlayerLocation, smellLevel, !isLocationVisited); //!isLocationVisited

          g.drawImage(img, xIndex, yIndex, Size_Per_Grid_Item, Size_Per_Grid_Item, null);
          xIndex += Size_Per_Grid_Item;
        }
        yIndex += Size_Per_Grid_Item;
        xIndex = 0;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  //Get the type of image to be displayed based on the number of openings.
  private String getLocationImageNameFromPossibleOpening(List<Direction>
                                                                 directionsFromGivenLocation) {
    char[] arrayForImageName = new char[directionsFromGivenLocation.size()];
    int directionIndex = 0;
    for (Direction d : directionsFromGivenLocation) {
      arrayForImageName[directionIndex++] = nameOfImageFile.get(d.toString()).charAt(0);
    }
    Arrays.sort(arrayForImageName);
    return new String(arrayForImageName);
  }

  private BufferedImage getFinalDungeonLocationImage(DungeonLocationInterface
                                                             dungeonLocationInterface,
                                                     boolean isPlayerCurrentLocation,
                                                     SmellLevel smellLevel,
                                                     boolean isLocationHidden)
          throws IOException {
    if (isLocationHidden) {
      BufferedImage baseLocationImage =
              ImageIO.read(Objects.requireNonNull(ClassLoader.getSystemResource(
                      basePathForImage + "black" + imageExtension)));
      return baseLocationImage;
    }
    String imageName = getLocationImageNameFromPossibleOpening(
            gameModel.getDungeon().getPossibleDirectionFrom(dungeonLocationInterface));
    //Got the base image
    BufferedImage baseLocationImage =
            ImageIO.read(Objects.requireNonNull(ClassLoader.getSystemResource(basePathForImage
                    + imageName + imageExtension)));

    //Add stench if needed
    if (smellLevel != SmellLevel.ZERO) {
      baseLocationImage = addSmellToLocation(baseLocationImage,
              smellLevel);
    }
    //Add treasure image
    if (dungeonLocationInterface.getTotalTreasureCount() > 0) {
      baseLocationImage = addTreasureImageToLocation(baseLocationImage,
              dungeonLocationInterface.getAllTreasure());
    }

    //Add monster
    if (dungeonLocationInterface.isMonsterPresent()) {
      baseLocationImage = addMonsterImageToLocation(baseLocationImage, dungeonLocationInterface);
    }

    //Add weapon image
    HashMap<Weapon, Integer> weaponMap = dungeonLocationInterface.getAllWeapon();
    if (weaponMap != null && weaponMap.size() > 0) {
      baseLocationImage = addWeaponImageToLocation(baseLocationImage, dungeonLocationInterface);
    }

    //Add Player
    if (isPlayerCurrentLocation) {
      baseLocationImage = addPlayerImageToLocation(baseLocationImage);
    }
    return baseLocationImage;
  }

  private BufferedImage addSmellToLocation(BufferedImage baseLocationImage,
                                           SmellLevel smellLevel) throws IOException {
    return overlay(baseLocationImage, nameOfImageFile.get(smellLevel.name()),
            positionOfItemInsideALocation.get(smellLevel + "X"),
            positionOfItemInsideALocation.get(smellLevel + "Y"), 30, 30);
  }

  private BufferedImage addWeaponImageToLocation(BufferedImage baseLocationImage,
                                                 DungeonLocationInterface dungeonLocationInterface)
          throws IOException {
    HashMap<Weapon, Integer> weaponMap = dungeonLocationInterface.getAllWeapon();
    for (Weapon w : Weapon.values()) {
      if (weaponMap.getOrDefault(w, 0) > 0) {
        baseLocationImage = overlay(baseLocationImage, nameOfImageFile.get(w.name()),
                positionOfItemInsideALocation.get(w.name() + "X"),
                positionOfItemInsideALocation.get(w.name() + "Y"), 15, 5);
      }
    }
    return baseLocationImage;
  }

  private BufferedImage addPlayerImageToLocation(BufferedImage baseLocationImage)
          throws IOException {
    return overlay(baseLocationImage, nameOfImageFile.get("player"),
            positionOfItemInsideALocation.get("playerX"),
            positionOfItemInsideALocation.get("playerY"), 30, 30);
  }

  private BufferedImage addMonsterImageToLocation(BufferedImage baseLocationImage,
                                                  DungeonLocationInterface
                                                          dungeonLocationInterface)
          throws IOException {
    baseLocationImage = overlay(baseLocationImage,
            nameOfImageFile.get(dungeonLocationInterface.getMonster().getType()
                    + dungeonLocationInterface.getMonster().getHealth()),
            positionOfItemInsideALocation.get(dungeonLocationInterface.getMonster()
                    .getType() + "X"), positionOfItemInsideALocation
                    .get(dungeonLocationInterface.getMonster().getType() + "Y"),
            25, 25);
    return baseLocationImage;
  }

  private BufferedImage addTreasureImageToLocation(BufferedImage baseLocationImage,
                                                   HashMap<TreasureEnum, Integer> map)
          throws IOException {
    BufferedImage processedImage = baseLocationImage;
    for (TreasureEnum t : TreasureEnum.values()) {
      if (map.getOrDefault(t, 0) > 0) {

        processedImage = overlay(processedImage, nameOfImageFile.get(t.name()),
                positionOfItemInsideALocation.get(t.name() + "X"),
                positionOfItemInsideALocation.get(t.name() + "Y"), 15, 15);
      }
    }
    return processedImage;
  }

  private BufferedImage resize(BufferedImage originalImage, int newW, int newH) {
    Image tmp = originalImage.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
    BufferedImage resizedImage = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

    Graphics2D g2d = resizedImage.createGraphics();
    g2d.drawImage(tmp, 0, 0, null);
    g2d.dispose();

    return resizedImage;
  }

  //Combine 2 images
  private BufferedImage overlay(BufferedImage baseImage,
                                String overlayImageFileName, int offsetX,
                                int offsetY, int width, int height)
          throws IOException {

    BufferedImage overlay =
            ImageIO.read(Objects.requireNonNull(ClassLoader.getSystemResource(basePathForImage
                    + overlayImageFileName + imageExtension)));
    overlay = resize(overlay, width, height);

    int w = Math.max(baseImage.getWidth(), overlay.getWidth());
    int h = Math.max(baseImage.getHeight(), overlay.getHeight());
    BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
    Graphics g = combined.getGraphics();
    g.drawImage(baseImage, 0, 0, null);
    g.drawImage(overlay, offsetX, offsetY, null);
    return combined;
  }
}
