package dungeontest;

import org.junit.Before;
import org.junit.Test;

import controller.DungeonGuiController;
import dungeonmodel.Direction;
import dungeonmodel.Game;
import dungeonmodel.GameInterface;
import dungeonmodel.TreasureEnum;
import dungeonmodel.Weapon;
import random.RandomNumberGenerator;
import view.GameViewSwingUi;
import view.GuiView;

import static org.junit.Assert.assertEquals;

/**
 * Used to test the UI controller.
 */
public class UIControllerTest {
  private Appendable log;
  private StringBuilder gameLog;
  private DungeonGuiController controller;
  private GameInterface model;

  @Before
  public void setUp() throws Exception {
    log = new StringBuffer();
    model = new Game(true, 4, 6, 1,
            30, 3, new RandomNumberGenerator());
    GuiView view = new MockGameViewSwingUi(log);
    controller = new DungeonGuiController(model, view);
  }

  //Tests the controller using Mock view
  @Test(expected = IllegalArgumentException.class)
  public void testNullViewToController() {
    GameInterface model = new Game(true, 4, 6, 1,
            30, 3, new RandomNumberGenerator());
    DungeonGuiController controller = new DungeonGuiController(model, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullModelToController() {
    GameInterface model = new Game(true, 4, 6, 1,
            30, 3, new RandomNumberGenerator());
    GuiView view = new GameViewSwingUi(model);
    new DungeonGuiController(null, view);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullModelToView() {
    new GameViewSwingUi(null);
  }

  @Test
  public void testPlayGameCallMockView() {
    controller.playGame();
    assertEquals(true,
            log.toString().contains("setListener() called with controller features"));
    assertEquals(true,
            log.toString().contains("setVisible() called"));
  }

  @Test
  public void testMovePlayerMockView() {
    controller.playGame();
    if (model.getPlayerPossibleMoves().contains(Direction.WEST.name())) {
      controller.movePlayer(Direction.WEST);
    } else if (model.getPlayerPossibleMoves().contains(Direction.EAST.name())) {
      controller.movePlayer(Direction.EAST);
    } else if (model.getPlayerPossibleMoves().contains(Direction.NORTH.name())) {
      controller.movePlayer(Direction.NORTH);
    } else {
      controller.movePlayer(Direction.SOUTH);
    }
    //repaint only called if the player moves
    assertEquals(true,
            log.toString().contains("repaint() called"));
  }

  @Test
  public void testShootArrowMockView() {
    controller.playGame();
    int arrowCountBefore = (int) model.getPlayer().getAllWeapon()
            .getOrDefault(Weapon.CROOKED_ARROW, 0);
    controller.shootArrow(Direction.WEST, 1);
    controller.shootArrow(Direction.EAST, 1);
    controller.shootArrow(Direction.NORTH, 1);
    controller.shootArrow(Direction.SOUTH, 1);
    int arrowCountAfter = (int) model.getPlayer().getAllWeapon()
            .getOrDefault(Weapon.CROOKED_ARROW, 0);
    assertEquals(true, arrowCountBefore > arrowCountAfter);
    assertEquals(true, log.toString().contains("repaint() called"));
  }

  @Test
  public void testSetNewGameParamForNewGameMockView() {
    controller.playGame();
    controller.setNewGameParameter(
            true, 6, 6, 1,
            30, 3);
    assertEquals(true, log.toString().contains("dispose() called"));
  }

  @Test
  public void testSetNewGameParameterMockView() {
    controller.playGame();
    controller.setNewGameParameter();
    assertEquals(true, log.toString().contains("openGameParameterInputPanel() called"));
  }

  @Test
  public void testRestartGameMockView() {
    controller.playGame();
    controller.restartGame();
    assertEquals(true, log.toString().contains("repaint() called"));
  }

  @Test
  public void testPickWeaponMockView() {
    controller.playGame();
    if ((int) model.getPlayer().getCurrentLocationOfPlayer()
            .getAllWeapon().getOrDefault(Weapon.CROOKED_ARROW, 0) > 0) {
      controller.pickWeapon(Weapon.CROOKED_ARROW);
      assertEquals(true,
              log.toString().contains("repaint() called"));
      assertEquals(true,
              log.toString().contains("playSound() called"));
    } else {
      assertEquals(false,
              log.toString().contains("repaint() called"));
    }
  }

  @Test
  public void testPickTreasureMockView() {
    controller.playGame();
    if (model.getTreasureCountAtPlayerLocation() > 0) {
      controller.pickTreasure(TreasureEnum.RUBY);
      controller.pickTreasure(TreasureEnum.DIAMOND);
      controller.pickTreasure(TreasureEnum.SAPPHIRE);
      assertEquals(true,
              log.toString().contains("repaint() called"));
    } else {
      assertEquals(false,
              log.toString().contains("repaint() called"));
    }
  }

  //Tests the controller using Mock view and mock model
  private void getMockViewMockModel() {
    gameLog = new StringBuilder();
    model = new MockGameModel(gameLog);
    GuiView view = new MockGameViewSwingUi(log);
    controller = new DungeonGuiController(model, view);
  }

  @Test
  public void testMovePlayerMockViewMockModel() {
    getMockViewMockModel();
    controller.playGame();
    controller.movePlayer(Direction.WEST);
    //repaint only called if the player moves
    assertEquals(true,
            gameLog.toString().contains("movePlayerToDirection() called with: w"));
  }

  @Test
  public void testShootArrowMockViewMockModel() {
    getMockViewMockModel();
    controller.playGame();
    controller.shootArrow(Direction.WEST, 1);
    assertEquals(true, gameLog.toString().contains("called killMonster() called "
            + "with weapon: "
            + "CROOKED_ARROW, direction: WEST, distance: 1"));
  }

  @Test
  public void testRestartGameMockViewMockModel() {
    getMockViewMockModel();
    controller.playGame();
    controller.restartGame();
    assertEquals(true, gameLog.toString().contains("restartGame() called"));
  }

  @Test
  public void testPickWeaponMockViewMockModel() {
    getMockViewMockModel();
    controller.playGame();
    controller.pickWeapon(Weapon.CROOKED_ARROW);
    assertEquals(true, gameLog.toString().contains("collectAWeapon() called with weapon: "
            + "CROOKED_ARROW ,units: 1"));
  }

  @Test
  public void testPickTreasureMockViewMockModel() {
    getMockViewMockModel();
    controller.playGame();
    controller.pickTreasure(TreasureEnum.RUBY);
    assertEquals(true,
            gameLog.toString().contains("collectATreasure() called with treasure: RUBY ,units: 1"));

  }
}
