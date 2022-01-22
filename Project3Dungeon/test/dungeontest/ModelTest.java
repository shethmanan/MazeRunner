package dungeontest;

import org.junit.Before;
import org.junit.Test;

import dungeonmodel.ArrowTraversalModelInterface;
import dungeonmodel.Direction;
import dungeonmodel.DungeonInterface;
import dungeonmodel.DungeonLocationInterface;
import dungeonmodel.DungeonLocationType;
import dungeonmodel.Game;
import dungeonmodel.GameInterface;
import random.RandomNumberGenerator;
import dungeonmodel.SmellLevel;
import dungeonmodel.TreasureEnum;
import dungeonmodel.Weapon;

import java.util.HashMap;
import java.util.List;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Used to test the entire dungeon game.
 */
public class ModelTest {
  GameInterface game;

  @Before
  public void setUp() throws Exception {
    game = new Game(false, 4, 6, 0, 30,
            1, new RandomNumberGenerator(
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,   //38 random ints for 4*6 matrix as it has 38 paths
            // 0, 0,     //  2 random ints for interconnectivity of leftovers
            0, 1,     //   cave2 at location 0 followed by ruby treasure
            0, 0,     //  cave2 at location 0 followed by diamond treasure
            2, 0,     //  cave4 at location 2 followed by diamond treasure
            6, 1,
            0, 0,     //for start and end cave
            0, 1, 2      // add 3 arrows to these cave numbers

    ));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidRowNoNegativeConstructor1() {
    new Game(false, -4, 6, 2, 10, 1,
            new RandomNumberGenerator(0, 0, 0, 0, 0, 0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidRowNoNegativeConstructor2() {
    new Game(false, -4, 6, 2, 10, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidColumnNoNegativeConstructor1() {
    new Game(false, 4, -6, 2, 10, 1,
            new RandomNumberGenerator(0, 0, 0, 0, 0, 0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidColumnNoNegativeConstructor2() {
    new Game(false, 4, -6, 2, 10, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidRowColumnNoNegative() {
    new Game(false, -4, -6, 2, 10, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testExtremelySmallDungeon() {
    new Game(false, 2, 1, 2, 10, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidInterconnectivityConstructor1() {
    new Game(false, 4, 6, -2, 10, 1,
            new RandomNumberGenerator(0, 0, 0, 0, 0, 0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidInterconnectivityConstructor2() {
    new Game(false, 4, 6, -22, 10, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidTreasurePercentConstructor1() {
    new Game(false, 4, 6, 21, -10, 1,
            new RandomNumberGenerator(0, 0, 0, 0, 0, 0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidTreasurePercentConstructor2() {
    new Game(false, 4, 6, 22, -10, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullRandomObject() {
    new Game(false, 4, 6, 21, -10, 1,
            null);
  }

  //Validation of user input done


  //Testing the created dungeon

  @Test
  public void testIfNodesCreatedAsExpectedForRandomNonWrappingDungeon() {
    GameInterface gameInterface = new Game(false,
            5, 5, 0, 20, 1);
    int expectedValue = 25;   //since it is a 5*5 dungeon
    DungeonInterface dungeon1 = gameInterface.getDungeon();
    assertEquals(expectedValue, dungeon1.getNumberOfCave() + dungeon1.getNumberOfTunnel());
  }

  @Test
  public void testIfNodesCreatedAsExpectedForRandomWrappingDungeon() {
    GameInterface gameInterface = new Game(true,
            5, 5, 0, 20, 1);
    int expectedValue = 25;   //since it is a 5*5 dungeon
    DungeonInterface dungeon1 = gameInterface.getDungeon();
    assertEquals(expectedValue, dungeon1.getNumberOfCave() + dungeon1.getNumberOfTunnel());
  }

  @Test
  public void testIfNodesCreatedAsExpectedForDeterministicDungeon() {
    int expectedValue = 24;   //since it is a 4*6 dungeon
    DungeonInterface dungeon = game.getDungeon();
    assertEquals(expectedValue, dungeon.getNumberOfTunnel() + dungeon.getNumberOfCave());
  }

  @Test
  public void testIfCavesCreatedAsExpectedForDeterministicDungeon() {
    int expectedValue = 10;   //since it is a 4*6 dungeon, it will always have 10 cave for the
    // given random number in setup. To understand how, you can check the dungeon that will be
    // constructed @ line 592 (bottom of this page)
    DungeonInterface dungeon = game.getDungeon();

    assertEquals(expectedValue, dungeon.getNumberOfCave());
  }

  @Test
  public void testIfTunnelsCreatedAsExpectedForDeterministicDungeon() {
    int expectedValue = 14;   //since it is a 4*6 dungeon, it will always have 14 tunnels for the
    // given random number in setup. To understand how, you can check the dungeon that will be
    // constructed @ line 151
    DungeonInterface dungeon = game.getDungeon();
    assertEquals(expectedValue, dungeon.getNumberOfTunnel());
  }

  @Test
  public void testNumberOfTreasuredCave() {
    DungeonInterface dungeon = game.getDungeon();
    //30% is passed in constructor for treasured cave, hence 0.30.
    int expectedValue = (int) (0.30 * dungeon.getNumberOfCave());
    assertEquals(expectedValue, dungeon.getNumberOfTreasuredCaves());
  }

  @Test
  public void testNumberOfTreasureCaveWhenZero() {
    DungeonInterface dungeon = new Game(false, 4,
            6, 0, 0, 1).getDungeon();

    //0% is passed in constructor for treasured cave, hence 0.
    int expectedValue = 0;
    assertEquals(expectedValue, dungeon.getNumberOfTreasuredCaves());
  }

  @Test
  public void testNumberOfEdgeInDungeonWithNoInterconnectivity() {
    //a 6*4 dungeon non interconnecting dungeon will have 23 edges
    GameInterface gameInterface = new Game(false, 4, 6,
            0, 30, 1);
    assertEquals(23, gameInterface.getDungeon().getNumberOfConnection());
  }

  @Test
  public void testNumberOfEdgeInDungeonWithInterconnectivity() {
    //a 6*4 dungeon non interconnecting dungeon will have 23 edges
    // adding interconnectivity, we get: total edges = edges in Min Span tree + interconnectivity
    GameInterface gameInterface = new Game(false, 4, 6,
            1, 30, 1);
    assertEquals(24, gameInterface.getDungeon().getNumberOfConnection());

    gameInterface = new Game(false, 4, 6,
            2, 30, 1);
    assertEquals(25, gameInterface.getDungeon().getNumberOfConnection());
  }

  @Test
  public void testWrappingDungeonColumnWise() {

    GameInterface gameInterface = new Game(true, 4, 6,
            0, 30, 1,
            new RandomNumberGenerator(
                    38, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0, 0,   //38 random ints for 4*6 matrix as it has 38
                    // paths + 10 for wrapping
                    0, 0, 0, 0, 0, 0, 0, 0,
                    0, 1,
                    0, 0,
                    2, 0,
                    0, 0,     //for start and end cave
                    0, 0,
                    0, 0,
                    0, 0,     //for start and end cave
                    0, 0,
                    0, 0,
                    2, 0,
                    1, 1,     //for start and end cave
                    0, 0,
                    0, 0,
                    0, 0,     //for start and end cave
                    0, 0,
                    0, 0,
                    1, 2
            ));
    //Player is currently at Location 1
    assertEquals(true,
            gameInterface.getPlayer().getCurrentLocationOfPlayer().toString().contains("Dungeon1"));

    //A player can move NORTH of Dungeon 1 -> which means there is a wrapping path
    assertEquals(true,
            gameInterface.getPlayerPossibleMoves().toString().contains("NORTH"));
    gameInterface.movePlayerToDirection('n');

    //Player directly reached Dungeon 19, which means he was able to explore the wrapping path
    // correctly
    assertEquals(true,
            gameInterface.getPlayer().getCurrentLocationOfPlayer().toString().contains("Dungeon19"
            ));
    gameInterface.movePlayerToDirection('s');
    assertEquals(true,
            gameInterface.getPlayer().getCurrentLocationOfPlayer().toString().contains("Dungeon1"));

  }

  @Test
  public void testWrappingDungeonRowWise() {

    GameInterface gameInterface = new Game(true, 4, 6,
            0, 30, 1,
            new RandomNumberGenerator(
                    44, 38, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0, 0,   //38 random ints for 4*6 matrix as it has 38
                    // paths + 10 for wrapping
                    0, 0, 0, 0, 0, 0, 0, 0,
                    0, 1,
                    0, 0,
                    2, 0,
                    6, 1,
                    0, 0,     //for start and end cave
                    1, 2,
                    1, 2, 3
            ));
    //Player is currently at Location 1
    assertEquals(true,
            gameInterface.getPlayer().getCurrentLocationOfPlayer().toString().contains("Dungeon1"));

    //A player can move WEST, NORTH of Dungeon 1 -> which means there is a wrapping path on both
    // sides
    assertEquals(true,
            gameInterface.getPlayerPossibleMoves().toString().contains("WEST"));
    assertEquals(true,
            gameInterface.getPlayerPossibleMoves().toString().contains("NORTH"));
    gameInterface.movePlayerToDirection('w');

    //Player directly reached Dungeon 19, which means he was able to explore the wrapping path
    // correctly
    assertEquals(true,
            gameInterface.getPlayer().getCurrentLocationOfPlayer().toString().contains("Dungeon6"
            ));

    gameInterface.movePlayerToDirection('e');
    //Player can also move back on the same wrapped path
    assertEquals(true,
            gameInterface.getPlayer().getCurrentLocationOfPlayer().toString().contains("Dungeon1"));

  }

  @Test
  public void testMinimumDistanceOfFiveBetweenStartEnd() {
    //The dungeon generated by the Setup() method is given at the end of this file

    int playerStartPosition = game.getPlayer().getCurrentLocationOfPlayer().getDungeonId();
    int destinationPosition = game.getEndLocation().getDungeonId();
    assertEquals(2, playerStartPosition);
    assertEquals(22, destinationPosition);
    //the start and end location is 2 and 22. We traverse on the shortest path between these
    // nodes to prove the minimum distance constraint
    int distanceTravelledByPlayer = 0;
    game.movePlayerToDirection('e');
    distanceTravelledByPlayer++;
    game.movePlayerToDirection('e');
    distanceTravelledByPlayer++;
    game.movePlayerToDirection('s');
    distanceTravelledByPlayer++;
    game.movePlayerToDirection('s');
    distanceTravelledByPlayer++;
    game.movePlayerToDirection('s');
    distanceTravelledByPlayer++;
    assertEquals(true, distanceTravelledByPlayer >= 5);
  }

  @Test
  public void testTreasureAppearsOnlyInCave() {
    //Here we will visit each and all 24 location in the dungeon, if the possible direction from
    // that node is 2, it is a tunnel. Treasure should not be at this place. Other places can have
    // treasure.

    game.movePlayerToDirection('w');  //location 1
    checkNoTreasureIfTunnel();
    game.movePlayerToDirection('s');  //location 7
    checkNoTreasureIfTunnel();
    game.movePlayerToDirection('s');  //location 13
    checkNoTreasureIfTunnel();
    game.movePlayerToDirection('s');  //location 19
    checkNoTreasureIfTunnel();
    game.movePlayerToDirection('n');  //location 13 backtrack
    game.movePlayerToDirection('n');  //location 7 backtrack
    game.movePlayerToDirection('n');  //location 1 backtrack
    game.movePlayerToDirection('e');  //location 2
    checkNoTreasureIfTunnel();
    game.movePlayerToDirection('s');  //location 8
    checkNoTreasureIfTunnel();
    game.movePlayerToDirection('s');  //location 14
    checkNoTreasureIfTunnel();
    game.movePlayerToDirection('s');  //location 20
    checkNoTreasureIfTunnel();
    game.movePlayerToDirection('n');  //location 14 backtrack
    game.movePlayerToDirection('n');  //location 8 backtrack
    game.movePlayerToDirection('n');  //location 2 backtrack
    game.movePlayerToDirection('e');  //location 3
    checkNoTreasureIfTunnel();
    game.movePlayerToDirection('s');  //location 9
    checkNoTreasureIfTunnel();
    game.movePlayerToDirection('s');  //location 15
    checkNoTreasureIfTunnel();
    game.movePlayerToDirection('s');  //location 21
    checkNoTreasureIfTunnel();
    game.movePlayerToDirection('n');  //location 15 backtrack
    game.movePlayerToDirection('n');  //location 9 backtrack
    game.movePlayerToDirection('n');  //location 3 backtrack
    game.movePlayerToDirection('e');  //location 4 will be checked ahead
    game.movePlayerToDirection('e');  //location 5
    checkNoTreasureIfTunnel();
    game.movePlayerToDirection('s');  //location 11
    checkNoTreasureIfTunnel();
    game.movePlayerToDirection('s');  //location 17
    checkNoTreasureIfTunnel();
    game.movePlayerToDirection('s');  //location 23
    checkNoTreasureIfTunnel();
    game.movePlayerToDirection('n');  //location 17 backtrack
    game.movePlayerToDirection('n');  //location 11 backtrack
    game.movePlayerToDirection('n');  //location 5 backtrack
    game.movePlayerToDirection('e');  //location 6
    checkNoTreasureIfTunnel();
    game.movePlayerToDirection('s');  //location 12
    checkNoTreasureIfTunnel();
    game.movePlayerToDirection('s');  //location 18
    checkNoTreasureIfTunnel();
    game.movePlayerToDirection('s');  //location 24
    checkNoTreasureIfTunnel();
    game.movePlayerToDirection('n');  //location 18 backtrack
    game.movePlayerToDirection('n');  //location 12 backtrack
    game.movePlayerToDirection('n');  //location 6 backtrack
    game.movePlayerToDirection('w');  //location 5 backtrack
    game.movePlayerToDirection('w');  //location 4
    checkNoTreasureIfTunnel();
    game.movePlayerToDirection('s');  //location 10
    checkNoTreasureIfTunnel();
    game.movePlayerToDirection('s');  //location 16
    checkNoTreasureIfTunnel();
    game.movePlayerToDirection('s');  //location 22 - the destination node
    assertEquals(true, checkNoTreasureIfTunnel());
    //If no assert failure till now, we checked each node to successfully

  }

  private boolean checkNoTreasureIfTunnel() {
    if (game.getPlayerPossibleMoves().size() == 2) {
      assertEquals(0, game.getTreasureCountAtPlayerLocation());
    }
    return true;
  }

  @Test
  public void testDungeonToString() {
    //Dungeon String shows how each node is connected to its neighbours. Here we test that each
    // node has its adjacency list pointing to his neighbours that can be travelled from
    String dungeonString = game.getDungeon().toString();
    for (int i = 1; i <= 24; i++) {
      assertEquals(true, dungeonString.contains(i + " -> ["));
    }
  }

  //Testing player
  @Test
  public void testPlayerLocationAtGameStart() {
    assertEquals(game.getStartLocation(), game.getPlayer().getCurrentLocationOfPlayer());
  }

  @Test
  public void testPlayerDetail() {
    String playerDetails = game.getStartLocation() + "\n"
            + game.getPlayer().printCollectedTreasure();
    assertEquals(true, playerDetails.contains("Dungeon2-")
            && playerDetails.contains("Treasure Collected: None")
            && playerDetails.contains("RUBY=1")
            && playerDetails.contains("DIAMOND=1"));
  }

  @Test
  public void testPlayerLocationAtGameEnd() {
    assertEquals(true, game.getEndLocation().toString().contains("Dungeon22"));
  }

  @Test
  public void testPlayerReachEndDestinationPositive() {
    assertEquals(game.getStartLocation(), game.getPlayer().getCurrentLocationOfPlayer());
    game.movePlayerToDirection('e');
    game.movePlayerToDirection('e');
    game.movePlayerToDirection('s');
    game.movePlayerToDirection('s');
    game.movePlayerToDirection('s');
    assertEquals(true, game.isGameOver());
  }

  @Test
  public void testPlayerReachEndDestinationNegative() {
    assertEquals(game.getStartLocation(), game.getPlayer().getCurrentLocationOfPlayer());
    game.movePlayerToDirection('e');
    game.movePlayerToDirection('e');
    game.movePlayerToDirection('e');
    game.movePlayerToDirection('s');
    assertEquals(false, game.isGameOver());
  }

  @Test(expected = IllegalArgumentException.class)
  public void denyPlayerMovementAfterEndLocationReached() {
    assertEquals(game.getStartLocation(), game.getPlayer().getCurrentLocationOfPlayer());
    game.movePlayerToDirection('e');
    game.movePlayerToDirection('e');
    game.movePlayerToDirection('s');
    game.movePlayerToDirection('s');
    game.movePlayerToDirection('s');
    game.movePlayerToDirection('s');
  }

  @Test
  public void testPlayerVisitEachNode() {
    StringBuilder stringBuilder = new StringBuilder();
    game.movePlayerToDirection('w');
    stringBuilder.append(game.getPlayer().getCurrentLocationOfPlayer()); //location 1
    game.movePlayerToDirection('s');  //location 7
    stringBuilder.append(game.getPlayer().getCurrentLocationOfPlayer());
    game.movePlayerToDirection('s');  //location 13
    stringBuilder.append(game.getPlayer().getCurrentLocationOfPlayer());
    game.movePlayerToDirection('s');  //location 19
    stringBuilder.append(game.getPlayer().getCurrentLocationOfPlayer());
    game.movePlayerToDirection('n');  //location 13
    game.movePlayerToDirection('n');  //location 7
    game.movePlayerToDirection('n');  //location 1
    game.movePlayerToDirection('e');  //location 2
    stringBuilder.append(game.getPlayer().getCurrentLocationOfPlayer());
    game.movePlayerToDirection('s');  //location 8
    stringBuilder.append(game.getPlayer().getCurrentLocationOfPlayer());
    game.movePlayerToDirection('s');  //location 14
    stringBuilder.append(game.getPlayer().getCurrentLocationOfPlayer());
    game.movePlayerToDirection('s');  //location 20
    stringBuilder.append(game.getPlayer().getCurrentLocationOfPlayer());
    game.movePlayerToDirection('n');  //location 14
    game.movePlayerToDirection('n');  //location 8
    game.movePlayerToDirection('n');  //location 2
    game.movePlayerToDirection('e');  //location 3
    stringBuilder.append(game.getPlayer().getCurrentLocationOfPlayer());
    game.movePlayerToDirection('s');  //location 9
    stringBuilder.append(game.getPlayer().getCurrentLocationOfPlayer());
    game.movePlayerToDirection('s');  //location 15
    stringBuilder.append(game.getPlayer().getCurrentLocationOfPlayer());
    game.movePlayerToDirection('s');  //location 21
    stringBuilder.append(game.getPlayer().getCurrentLocationOfPlayer());
    game.movePlayerToDirection('n');  //location 15
    game.movePlayerToDirection('n');  //location 9
    game.movePlayerToDirection('n');  //location 3
    game.movePlayerToDirection('e');  //location 4
    game.movePlayerToDirection('e');  //location 5
    stringBuilder.append(game.getPlayer().getCurrentLocationOfPlayer());
    game.movePlayerToDirection('s');  //location 11
    stringBuilder.append(game.getPlayer().getCurrentLocationOfPlayer());
    game.movePlayerToDirection('s');  //location 17
    stringBuilder.append(game.getPlayer().getCurrentLocationOfPlayer());
    game.movePlayerToDirection('s');  //location 23
    stringBuilder.append(game.getPlayer().getCurrentLocationOfPlayer());
    game.movePlayerToDirection('n');  //location 17
    game.movePlayerToDirection('n');  //location 11
    game.movePlayerToDirection('n');  //location 5
    game.movePlayerToDirection('e');  //location 6
    stringBuilder.append(game.getPlayer().getCurrentLocationOfPlayer());
    game.movePlayerToDirection('s');  //location 12
    stringBuilder.append(game.getPlayer().getCurrentLocationOfPlayer());
    game.movePlayerToDirection('s');  //location 18
    stringBuilder.append(game.getPlayer().getCurrentLocationOfPlayer());
    game.movePlayerToDirection('s');  //location 24
    stringBuilder.append(game.getPlayer().getCurrentLocationOfPlayer());
    game.movePlayerToDirection('n');  //location 18
    game.movePlayerToDirection('n');  //location 12
    game.movePlayerToDirection('n');  //location 6
    game.movePlayerToDirection('w');  //location 5
    game.movePlayerToDirection('w');  //location 4
    stringBuilder.append(game.getPlayer().getCurrentLocationOfPlayer());
    game.movePlayerToDirection('s');  //location 10
    stringBuilder.append(game.getPlayer().getCurrentLocationOfPlayer());
    game.movePlayerToDirection('s');  //location 16
    stringBuilder.append(game.getPlayer().getCurrentLocationOfPlayer());
    game.movePlayerToDirection('s');  //location 22 - the destination node
    stringBuilder.append(game.getPlayer().getCurrentLocationOfPlayer());
    String allNodeVisitedByPlayer = stringBuilder.toString();
    for (int i = 1; i <= 24; i++) {
      assertEquals(true, allNodeVisitedByPlayer.contains("Dungeon" + i));
    }

  }

  @Test
  public void testPlayerPossibleMove() {
    assertEquals(true,
            game.getPlayerPossibleMoves().size() == 3
                    && game.getPlayerPossibleMoves().contains("SOUTH")
                    && game.getPlayerPossibleMoves().contains("EAST")
                    && game.getPlayerPossibleMoves().contains("WEST"));
    assertEquals(true, game.movePlayerToDirection('s'));  // Move to south
    assertEquals(true, game.movePlayerToDirection('n'));  // return to original location

    assertEquals(true, game.movePlayerToDirection('e'));  // Move to EAST
    assertEquals(true, game.movePlayerToDirection('w'));  // return to original location

    assertEquals(true, game.movePlayerToDirection('w'));  // Move to WEST
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPlayerInvalidInputForMove() {
    game.movePlayerToDirection('z');
  }

  @Test
  public void testPlayerInvalidMove() {
    assertEquals(true,
            game.getPlayerPossibleMoves().size() == 3
                    && game.getPlayerPossibleMoves().contains("SOUTH")
                    && game.getPlayerPossibleMoves().contains("WEST")
                    && game.getPlayerPossibleMoves().contains("EAST"));
    assertEquals(false, game.movePlayerToDirection('n'));   // Not allowed traveling NORTH
    assertEquals(game.getStartLocation(), game.getPlayer().getCurrentLocationOfPlayer());
  }

  @Test
  public void testPlayerValidMove() {
    assertEquals(game.getStartLocation(), game.getPlayer().getCurrentLocationOfPlayer());
    assertEquals(true, game.movePlayerToDirection('s'));
    assertNotEquals(game.getStartLocation(), game.getPlayer().getCurrentLocationOfPlayer());
  }

  @Test
  public void testPickUpTreasureWhenNotExist() {
    int expectedCount = 0;
    game.movePlayerToDirection('w');
    assertEquals(expectedCount, game.getTreasureCountAtPlayerLocation());
    HashMap<TreasureEnum, Integer> map = game.getPlayer().getAllTreasure();
    game.collectTreasure();
    assertEquals(map, game.getPlayer().getAllTreasure());
  }

  @Test
  public void testPickUpTreasureWhenExist() {
    assertEquals("Treasure Collected: None", game.getPlayer().printCollectedTreasure());
    game.movePlayerToDirection('e');
    game.movePlayerToDirection('e');
    int expectedCount = 1;
    assertEquals(expectedCount, game.getTreasureCountAtPlayerLocation());
    game.collectTreasure();
    assertEquals("Treasure Collected: DIAMOND-1", game.getPlayer().printCollectedTreasure());
  }

  @Test
  public void testDoublePickUpTreasure() {
    assertEquals("Treasure Collected: None", game.getPlayer().printCollectedTreasure());
    game.movePlayerToDirection('e');
    game.movePlayerToDirection('e');
    int expectedCount = 1;
    assertEquals(expectedCount, game.getTreasureCountAtPlayerLocation());
    game.collectTreasure();
    assertEquals("Treasure Collected: DIAMOND-1", game.getPlayer().printCollectedTreasure());
    assertEquals(0, game.getTreasureCountAtPlayerLocation()); // No treasure, since it is collected

    //Even after collectTreasure() again, the count still remains the same. This means that the
    // treasure is correctly cleared from the dungeon after picking it up.
    game.collectTreasure();
    assertEquals("Treasure Collected: DIAMOND-1", game.getPlayer().printCollectedTreasure());

  }

  @Test
  public void testPickTreasureWhenPlayerAlreadyHasTreasure() {
    assertEquals("Treasure Collected: None", game.getPlayer().printCollectedTreasure());
    game.collectTreasure();
    assertEquals(true, (game.getPlayer().printCollectedTreasure()
            .equals("Treasure Collected: DIAMOND-1,RUBY-1")
            || game.getPlayer().printCollectedTreasure()
            .equals("Treasure Collected: RUBY-1,DIAMOND-1")));
    game.movePlayerToDirection('e');
    game.movePlayerToDirection('e');
    game.collectTreasure();
    assertEquals(true, (game.getPlayer().printCollectedTreasure()
            .equals("Treasure Collected: DIAMOND-2,RUBY-1")
            || game.getPlayer().printCollectedTreasure()
            .equals("Treasure Collected: RUBY-1,DIAMOND-2")));

  }

  //Testcases added for Project 4 modification

  private GameInterface getGameObject() {
    return new Game(false, 4, 6, 0, 30,
            3, new RandomNumberGenerator(
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,   //38 random ints for 4*6 matrix as it has 38 paths
            // 0, 0,     //  2 random ints for interconnectivity of leftovers
            0, 1,     //   cave2 at location 0 followed by ruby treasure
            0, 0,     //  cave2 at location 0 followed by diamond treasure
            2, 0,     //  cave4 at location 2 followed by diamond treasure
            6, 1,
            0, 0,     //for start and end cave
            0, 1, 2,      // add 3 arrows to these cave numbers
            2, 3,       // 1 default at end, 2 other location for total of 3 monster
            0           // decide to kill player or not when monster injured
    ));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWrongNumberOfMonster1() {
    GameInterface tempGame = new Game(false, 4, 6, 0, 30,
            0, new RandomNumberGenerator(2, 3, 4, 5));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWrongNumberOfMonster2() {
    GameInterface tempGame = new Game(false, 4, 6, 0, 30,
            -2, new RandomNumberGenerator(2, 3, 4, 5));
  }

  @Test
  public void testCorrectNumberOfArrowAdded() {
    // The frequency of finding treasure and weapon should be the same
    int expectedArrowCount = this.game.getDungeon().getNumberOfTreasuredCaves();
    int numberOfArrowFound = 0;
    //Check the number of arrow at each location in the dungeon
    for (DungeonLocationInterface dungeonLocationInterface :
            game.getDungeon().getDungeonMap().keySet()) {
      numberOfArrowFound += weaponCountOfArrow(dungeonLocationInterface);
    }
    assertEquals(expectedArrowCount, numberOfArrowFound);

  }

  private int weaponCountOfArrow(DungeonLocationInterface currentLocationOfPlayer) {
    return (int) currentLocationOfPlayer.getAllWeapon().getOrDefault(Weapon.CROOKED_ARROW, 0);
  }

  @Test
  public void testNoMonsterAtStart() {
    assertEquals(null, this.game.getStartLocation().getMonster());
  }

  @Test
  public void testMonsterAlwaysAtEnd() {
    assertNotEquals(null, this.game.getEndLocation().getMonster());
  }

  @Test
  public void testMonsterAlwaysAtEndForRandomObject() {
    GameInterface game;
    for (int i = 0; i < 50; i++) {
      game = new Game(false, 4,
              6, 0, 30, 1);
      assertNotEquals(null, game.getEndLocation().getMonster());
    }

  }

  @Test
  public void testNoMonsterForRandomObject() {
    GameInterface game;
    for (int i = 0; i < 50; i++) {
      game = new Game(false, 4,
              6, 0, 30, 1);
      assertEquals(null, game.getStartLocation().getMonster());
    }

  }

  @Test
  public void testMonsterHasFullHealthWhenGameBegin() {
    assertEquals(100, this.game.getEndLocation().getMonster().getHealth());
  }

  @Test
  public void testSmellRadiusOfOtyugh() {
    GameInterface game1 = getGameObject();
    assertEquals(2, game1.getEndLocation().getMonster().getSmellRadius());
  }

  @Test
  public void testCorrectNumberOfMonsterAdded() {
    GameInterface game1 = getGameObject();
    int expectedMonsterCount = 3;     //3 monsters to be created is passed in getGameObject()
    int numberOfMonsterFound = 0;
    for (DungeonLocationInterface dungeonLocationInterface :
            game1.getDungeon().getDungeonMap().keySet()) {
      numberOfMonsterFound += dungeonLocationInterface.getMonster() == null ? 0 :
              dungeonLocationInterface.getMonster().getType().equals("otyugh") ? 1 : 0;
    }
    assertEquals(expectedMonsterCount, numberOfMonsterFound);
  }

  @Test
  public void testCorrectNumberOfMonsterAddedWhenMonsterCountMoreThanCave() {
    GameInterface game1 = new Game(false, 4, 6,
            0, 30, 30,
            new RandomNumberGenerator(
                    0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0,   //38 random ints for 4*6 matrix as it has 38 paths
                    // 0, 0,     //  2 random ints for interconnectivity of leftovers
                    0, 1,     //   cave2 at location 0 followed by ruby treasure
                    0, 0,     //  cave2 at location 0 followed by diamond treasure
                    2, 0,     //  cave4 at location 2 followed by diamond treasure
                    6, 1,
                    0, 0,     //for start and end cave
                    0, 1, 2,      // add 3 arrows to these cave numbers
                    2, 3, 1, 3, 4, 0, 5, 6, 7, 8, 9, 10, 11, 12,
                    0           // decide to kill player or not when monster injured
            ));
    int expectedMonsterCount = 9;
    int numberOfMonsterFound = 0;
    for (DungeonLocationInterface dungeonLocationInterface :
            game1.getDungeon().getDungeonMap().keySet()) {
      numberOfMonsterFound += dungeonLocationInterface.getMonster() == null ? 0 :
              dungeonLocationInterface.getMonster().getType().equals("otyugh") ? 1 : 0;
    }
    assertEquals(expectedMonsterCount, numberOfMonsterFound);
  }

  @Test
  public void testOtyughAlwaysInCaveAndNeverInTunnel() {
    GameInterface game1 = getGameObject();
    for (DungeonLocationInterface dungeonLocationInterface :
            game1.getDungeon().getDungeonMap().keySet()) {
      if ((dungeonLocationInterface.getMonster() == null ? 0 :
              dungeonLocationInterface.getMonster().getType().equals("otyugh") ? 1 : 0) > 0) {
        assertEquals(DungeonLocationType.CAVE,
                game1.getDungeon().getType(dungeonLocationInterface));
      }

    }
  }

  @Test
  public void testCaveContainingOtyughCanAlsoContainOtherItem() {
    GameInterface game1 = new Game(false, 4, 6, 0, 30,
            3, new RandomNumberGenerator(
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,   //38 random ints for 4*6 matrix as it has 38 paths
            // 0, 0,     //  2 random ints for interconnectivity of leftovers
            2, 1,     //   cave2 at location 0 followed by ruby treasure
            0, 0,     //  cave2 at location 0 followed by diamond treasure
            3, 0,     //  cave4 at location 2 followed by diamond treasure
            6, 1,
            0, 0,     //for start and end cave
            0, 3, 4,      // add 3 arrows to these location numbers
            2, 3,       // 1 default at end, 2 other location for total of 3 monster
            0           // decide to kill player or not when monster injured
    ));
    for (DungeonLocationInterface dungeonLocationInterface :
            game1.getDungeon().getDungeonMap().keySet()) {
      if (dungeonLocationInterface.getDungeonId() == 4) {
        //CAVE has monster
        assertEquals(true,
                dungeonLocationInterface.toString().contains("Otyugh: health=100"));
        //CAVE has treasure
        assertEquals(true,
                dungeonLocationInterface.toString().contains("RUBY=1"));
        //CAVE has an arrow
        assertEquals(true,
                dungeonLocationInterface.toString().contains(Weapon.CROOKED_ARROW + "=1"));
      }

    }
  }

  @Test
  public void testPickingIndividualTreasureWhenPresent() {
    assertEquals(true, game.getPlayer().printCollectedTreasure().toString()
            .contains("Treasure Collected: None"));
    String detail = game.getPlayer().getCurrentLocationOfPlayer().toString();
    assertEquals(true, detail.contains("DIAMOND=1") && detail.contains("RUBY=1"));
    game.collectATreasure(TreasureEnum.RUBY, 1);
    assertEquals(true, game.getPlayer().printCollectedTreasure()
            .contains("Treasure Collected: RUBY-1"));

  }

  @Test
  public void testPickingSingleTreasureWhenMultiplePresent() {
    GameInterface gameTemp = new Game(false, 4, 6, 0, 30,
            3, new RandomNumberGenerator(
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,   //38 random ints for 4*6 matrix as it has 38 paths
            // 0, 0,     //  2 random ints for interconnectivity of leftovers
            0, 1,     //   cave2 at location 0 followed by ruby treasure
            0, 1,     //  cave2 at location 0 followed by diamond treasure
            2, 0,     //  cave4 at location 2 followed by diamond treasure
            6, 1,
            0, 0,     //for start and end cave
            0, 1, 2,      // add 3 arrows to these cave numbers
            2, 3       // 1 default at end, 2 other location for total of 3 monster
    ));
    assertEquals(true, gameTemp.getPlayer().printCollectedTreasure().toString()
            .contains("Treasure Collected: None"));
    String detail = gameTemp.getPlayer().getCurrentLocationOfPlayer().toString();
    assertEquals(true, detail.contains("RUBY=2"));    //2 ruby present
    gameTemp.collectATreasure(TreasureEnum.RUBY, 1);  //Player collects 1
    assertEquals(true, gameTemp.getPlayer().printCollectedTreasure()
            .contains("Treasure Collected: RUBY-1"));       //Player now has 1 ruby
    detail = gameTemp.getPlayer().getCurrentLocationOfPlayer().toString();
    assertEquals(true, detail.contains("RUBY=1"));    //location now has only 1 ruby left

  }

  @Test(expected = IllegalArgumentException.class)
  public void testPickingTreasureMoreThanExist() {
    String detail = game.getPlayer().getCurrentLocationOfPlayer().toString();
    assertEquals(true, detail.contains("DIAMOND=1") && detail.contains("RUBY=1"));
    game.collectATreasure(TreasureEnum.RUBY, 3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPickingTreasureWhichDoesNotExist() {
    String detail = game.getPlayer().getCurrentLocationOfPlayer().toString();
    assertEquals(true, detail.contains("DIAMOND=1") && detail.contains("RUBY=1"));
    game.collectATreasure(TreasureEnum.SAPPHIRE, 1);
  }

  @Test
  public void testPlayerHasThreeArrowAtStart() {
    assertEquals(3, game.getPlayer().getAllWeapon().get(Weapon.CROOKED_ARROW));
  }

  @Test
  public void testPlayerPickArrowWhenExist() {
    //3 arrows already present by default
    assertEquals(3, game.getPlayer().getAllWeapon().get(Weapon.CROOKED_ARROW));
    //1 new arrow present at current location
    assertEquals(1, game.getStartLocation()
            .getAllWeapon().get(Weapon.CROOKED_ARROW));
    //player decides to pick the arrow
    game.collectAWeapon(Weapon.CROOKED_ARROW, 1);
    //arrow collected by the player
    assertEquals(4, game.getPlayer().getAllWeapon().get(Weapon.CROOKED_ARROW));
    //arrow removed from the current location
    assertEquals(0, game.getPlayer().getCurrentLocationOfPlayer()
            .getAllWeapon().getOrDefault(Weapon.CROOKED_ARROW, 0));
  }

  @Test
  public void testPlayerPickArrowFoundInCaveAndTunnel() {
    assertEquals(DungeonLocationType.CAVE, game.getDungeon().getType(game
            .getPlayer().getCurrentLocationOfPlayer()));
    //1 new arrow present at CAVE
    assertEquals(1, game.getPlayer().getCurrentLocationOfPlayer()
            .getAllWeapon().get(Weapon.CROOKED_ARROW));
    game.movePlayerToDirection('w');
    assertEquals(DungeonLocationType.TUNNEL, game.getDungeon().getType(game
            .getPlayer().getCurrentLocationOfPlayer()));
    //1 new arrow present at TUNNEL
    assertEquals(1, game.getPlayer().getCurrentLocationOfPlayer()
            .getAllWeapon().get(Weapon.CROOKED_ARROW));

  }

  @Test(expected = IllegalArgumentException.class)
  public void testPlayerPickingWrongWeapon() {
    game.collectAWeapon(null, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPlayerAttemptingToPickMoreArrowThenExist() {
    //1 new arrow present at current location
    assertEquals(1, game.getStartLocation()
            .getAllWeapon().get(Weapon.CROOKED_ARROW));
    //player decides to pick the arrow
    game.collectAWeapon(Weapon.CROOKED_ARROW, 2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPlayerShootingInDirectionNotAllowedInCurrentLocationTunnel() {
    game.movePlayerToDirection('w');
    assertEquals(DungeonLocationType.TUNNEL, game.getDungeon()
            .getType(game.getPlayer().getCurrentLocationOfPlayer()));
    assertEquals(false, game.getPlayerPossibleMoves().contains("WEST"));
    game.killMonster(Weapon.CROOKED_ARROW, Direction.WEST, 2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPlayerShootingInDirectionNotAllowedInCurrentLocationCave() {
    assertEquals(DungeonLocationType.CAVE, game.getDungeon()
            .getType(game.getPlayer().getCurrentLocationOfPlayer()));
    assertEquals(false, game.getPlayerPossibleMoves().contains("NORTH"));
    game.killMonster(Weapon.CROOKED_ARROW, Direction.NORTH, 2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSmellOnWrongLocation() {
    GameInterface game = getGameObject();
    assertEquals(SmellLevel.ZERO, game.getDungeon().getSmellLevel(null));
  }

  @Test
  public void testNoSmell() {
    GameInterface game = getGameObject();
    assertEquals(SmellLevel.ZERO, game.getDungeon().getSmellLevel(game.getStartLocation()));
  }

  @Test
  public void testLightSmellWhenMonsterTwoDistanceApart() {
    //We know the monster is at Dungeon 5. Check the last line of this file to view how the
    // dungeon looks
    GameInterface game = getGameObject();
    assertEquals(SmellLevel.ZERO, game.getDungeon().getSmellLevel(
            game.getPlayer().getCurrentLocationOfPlayer()));
    game.movePlayerToDirection('e');
    assertEquals(SmellLevel.LIGHT, game.getDungeon().getSmellLevel(
            game.getPlayer().getCurrentLocationOfPlayer()));
  }

  @Test
  public void testStrongSmellWhenMonsterOneDistanceApart() {
    //We know the monster is at Dungeon location 5. Check the last line of this file to view
    // how the dungeon looks.
    GameInterface game = getGameObject();
    assertEquals(SmellLevel.ZERO, game.getDungeon().getSmellLevel(
            game.getPlayer().getCurrentLocationOfPlayer()));
    game.movePlayerToDirection('e');
    assertEquals(SmellLevel.LIGHT, game.getDungeon().getSmellLevel(
            game.getPlayer().getCurrentLocationOfPlayer()));
    game.movePlayerToDirection('e');
    assertEquals(SmellLevel.STRONG, game.getDungeon().getSmellLevel(
            game.getPlayer().getCurrentLocationOfPlayer()));
  }

  @Test
  public void testStrongSmellWhenMultipleMonstersTwoDistanceApart() {
    // We know the monster is at Dungeon location 5 and location 22.
    // Check the last line of this file to view how the dungeon looks.
    GameInterface game = getGameObject();
    for (DungeonLocationInterface dungeonLocationInterface :
            game.getDungeon().getDungeonMap().keySet()) {
      if (dungeonLocationInterface.getDungeonId() == 5
              || dungeonLocationInterface.getDungeonId() == 22) {

        // 2 Monster at 1 distance from location 10
        assertEquals(true,
                dungeonLocationInterface.toString().contains("Otyugh"));
      }
      if (dungeonLocationInterface.getDungeonId() == 4
              || dungeonLocationInterface.getDungeonId() == 14) {
        // No Monster at 1 distance from location 10
        assertEquals(false,
                dungeonLocationInterface.toString().contains("Otyugh"));
      }

    }
    game.movePlayerToDirection('e');
    game.movePlayerToDirection('e');
    game.movePlayerToDirection('s');
    //Location 10 will have a STRONG smell because 2 Monster(s) at 5 and 22 are location within 2
    // distance of it
    assertEquals(10, game.getPlayer().getCurrentLocationOfPlayer().getDungeonId());
    assertEquals(SmellLevel.STRONG, game.getDungeon().getSmellLevel(
            game.getPlayer().getCurrentLocationOfPlayer()));
  }

  @Test
  public void testOverlappingSmell() {
    //We know the monster is at Dungeon location 2 and location 5. Hence, there should be a light
    // smell at location 3 because of monster at location 2, but a strong smell at same location
    // 3 because of the monster at location 5

    //Part of dungeon looks like. To check the full dungeon, check the last line of this file.
    //    1 - 2 - 3 - 4 - 5 - 6
    //    |   |   |   |   |   |
    //    7   8   9   10  11  12
    GameInterface game = new Game(false, 4, 6, 0, 30,
            3, new RandomNumberGenerator(
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,   //38 random ints for 4*6 matrix as it has 38 paths
            // 0, 0,     //  2 random ints for interconnectivity of leftovers
            0, 1,     //   cave2 at location 0 followed by ruby treasure
            0, 0,     //  cave2 at location 0 followed by diamond treasure
            2, 0,     //  cave4 at location 2 followed by diamond treasure
            6, 1,
            1, 0,     //for start and end cave
            0, 1, 2,      // add 3 arrows to these cave numbers
            0, 2,       // 1 default at end, 2 other location for total of 3 monster
            0, 1           // decide to kill player or not when monster injured
    ));
    for (DungeonLocationInterface dungeon : game.getDungeon().getDungeonMap().keySet()) {
      if (dungeon.getDungeonId() == 2 || dungeon.getDungeonId() == 5) {
        assertEquals(true, dungeon.isMonsterPresent());
      }
    }
    assertEquals(3, game.getPlayer().getCurrentLocationOfPlayer().getDungeonId());
    assertEquals(SmellLevel.STRONG, game.getDungeon().getSmellLevel(
            game.getPlayer().getCurrentLocationOfPlayer()));
  }

  @Test
  public void testPlayerHealth() {
    assertEquals(100, game.getPlayer().getPlayerHealth());
  }

  @Test
  public void testPlayerKilledByMonster() {
    GameInterface game = getGameObject();
    game.movePlayerToDirection('e');
    game.movePlayerToDirection('e');
    assertEquals(SmellLevel.STRONG, game.getDungeon().getSmellLevel(
            game.getPlayer().getCurrentLocationOfPlayer()));
    game.movePlayerToDirection('e');
    assertEquals(true, game.isGameOver());
    assertEquals(0, game.getPlayer().getPlayerHealth());
  }

  @Test
  public void testGetType() {
    assertEquals(DungeonLocationType.CAVE, game.getDungeon().getType(game.getStartLocation()));
    assertEquals(DungeonLocationType.CAVE, game.getDungeon().getType(game.getEndLocation()));
    game.movePlayerToDirection('w');
    assertEquals(DungeonLocationType.TUNNEL, game.getDungeon().getType(game
            .getPlayer().getCurrentLocationOfPlayer()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testExceptionWhenPlayerUsingInvalidWeaponForAttack() {
    game.killMonster(null, Direction.EAST, 2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testExceptionWhenPlayerUsingInvalidDirectionForAttack() {
    game.killMonster(Weapon.CROOKED_ARROW, null, 2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testExceptionWhenPlayerUsingInvalidDistanceForAttack() {
    game.killMonster(Weapon.CROOKED_ARROW, Direction.EAST, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testExceptionWhenPlayerUsingMoreArrowThanPresent() {
    assertEquals(3, game.getPlayer().getAllWeapon().getOrDefault(
            Weapon.CROOKED_ARROW, 0));
    game.killMonster(Weapon.CROOKED_ARROW, Direction.EAST, 2);
    game.killMonster(Weapon.CROOKED_ARROW, Direction.EAST, 2);
    game.killMonster(Weapon.CROOKED_ARROW, Direction.EAST, 2);
    game.killMonster(Weapon.CROOKED_ARROW, Direction.EAST, 2);
  }

  /**
   * Tests that a player can kill a monster.
   * Tests that 2 arrows are required to kill a monster.
   * Tests that the smell disappears after a monster is killed.
   * Tests that the player's arrow decreases after using them.
   */
  @Test
  public void testPlayerAttackKillMonster() {
    GameInterface game = getGameObject(); // dungeon with a monster at location 5
    game.movePlayerToDirection('e');  // move to location 3
    assertEquals(SmellLevel.LIGHT, game.getDungeon().getSmellLevel(
            game.getPlayer().getCurrentLocationOfPlayer()));
    assertEquals(3, game.getPlayer().getAllWeapon().getOrDefault(
            Weapon.CROOKED_ARROW, 0));
    assertEquals(false, game.killMonster(Weapon.CROOKED_ARROW,
            Direction.EAST, 2).isKillMonsterSuccessful());
    assertEquals(SmellLevel.LIGHT, game.getDungeon().getSmellLevel(   //Monster still present
            game.getPlayer().getCurrentLocationOfPlayer()));
    assertEquals(2, game.getPlayer().getAllWeapon().getOrDefault(
            Weapon.CROOKED_ARROW, 0));
    assertEquals(true, game.killMonster(Weapon.CROOKED_ARROW, Direction.EAST, 2)
            .isKillMonsterSuccessful());

    //Monster killed, hence smell disappeared
    assertEquals(SmellLevel.ZERO, game.getDungeon().getSmellLevel(
            game.getPlayer().getCurrentLocationOfPlayer()));
    game.movePlayerToDirection('e');
    game.movePlayerToDirection('e');
    assertEquals(5, game.getPlayer().getCurrentLocationOfPlayer().getDungeonId());
    assertEquals(false, game.getPlayer().getCurrentLocationOfPlayer().isMonsterPresent());

    assertEquals(1, game.getPlayer().getAllWeapon().getOrDefault(
            Weapon.CROOKED_ARROW, 0));    //1 arrow left
  }

  /**
   * Here we test if the arrow travels correctly.
   * We try to kill a monster from a distance of 2,
   * 1. by giving distance of 1 -> which does not kill the monster
   * 2. by giving distance of 3 -> which does not kill the monster
   * 3. by giving distance of 2 -> which finally kills the monster
   */
  @Test
  public void testArrowTraversal() {
    GameInterface game = getGameObject(); // dungeon with a monster at location 5
    DungeonLocationInterface dungeonWithMonster = null;
    for (DungeonLocationInterface dungeon : game.getDungeon().getDungeonMap().keySet()) {
      if (dungeon.getDungeonId() == 5) {
        dungeonWithMonster = dungeon;
        break;
      }
    }
    game.movePlayerToDirection('w');
    game.collectAWeapon(Weapon.CROOKED_ARROW, 1);
    game.movePlayerToDirection('e');
    game.collectAWeapon(Weapon.CROOKED_ARROW, 1);
    game.movePlayerToDirection('e');
    game.collectAWeapon(Weapon.CROOKED_ARROW, 1);

    assertEquals(6, game.getPlayer().getAllWeapon().get(Weapon.CROOKED_ARROW));

    //monster is at location 5 which is 2 distance apart from player's current location to the east
    assertEquals(true, dungeonWithMonster.isMonsterPresent());
    assertEquals(3, game.getPlayer().getCurrentLocationOfPlayer().getDungeonId());

    game.killMonster(Weapon.CROOKED_ARROW, Direction.EAST, 1);
    game.killMonster(Weapon.CROOKED_ARROW, Direction.EAST, 1);
    for (DungeonLocationInterface dungeon : game.getDungeon().getDungeonMap().keySet()) {
      if (dungeon.getDungeonId() == 5) {
        dungeonWithMonster = dungeon;
        break;
      }
    }
    assertEquals(true, dungeonWithMonster.isMonsterPresent());

    game.killMonster(Weapon.CROOKED_ARROW, Direction.EAST, 3);
    game.killMonster(Weapon.CROOKED_ARROW, Direction.EAST, 3);
    for (DungeonLocationInterface dungeon : game.getDungeon().getDungeonMap().keySet()) {
      if (dungeon.getDungeonId() == 5) {
        dungeonWithMonster = dungeon;
        break;
      }
    }
    assertEquals(true, dungeonWithMonster.isMonsterPresent());

    game.killMonster(Weapon.CROOKED_ARROW, Direction.EAST, 2);
    game.killMonster(Weapon.CROOKED_ARROW, Direction.EAST, 2);
    for (DungeonLocationInterface dungeon : game.getDungeon().getDungeonMap().keySet()) {
      if (dungeon.getDungeonId() == 5) {
        dungeonWithMonster = dungeon;
        break;
      }
    }
    assertEquals(false, dungeonWithMonster.isMonsterPresent());

  }

  @Test
  public void testArrowTraversalByAssertingPath() {
    GameInterface game = getGameObject(); // dungeon with a monster at location 5
    game.movePlayerToDirection('e');
    ArrowTraversalModelInterface arrowTraversalModel = game
            .killMonster(Weapon.CROOKED_ARROW, Direction.EAST, 3);

    //Even when distance is 3, the location travelled is 7 due to Tunnels
    assertEquals(7, arrowTraversalModel.getLocationListTravelledByArrow()
            .size());
    //1st location travelled
    assertEquals(4, arrowTraversalModel.getLocationListTravelledByArrow()
            .get(1).getDungeonId());
    assertEquals(5, arrowTraversalModel.getLocationListTravelledByArrow()
            .get(2).getDungeonId());
    assertEquals(6, arrowTraversalModel.getLocationListTravelledByArrow()
            .get(3).getDungeonId());
    //arrow changed direction to south
    assertEquals(12, arrowTraversalModel.getLocationListTravelledByArrow()
            .get(4).getDungeonId());
    assertEquals(18, arrowTraversalModel.getLocationListTravelledByArrow()
            .get(5).getDungeonId());
    //reached cave 24
    assertEquals(24, arrowTraversalModel.getLocationListTravelledByArrow()
            .get(arrowTraversalModel.getLocationListTravelledByArrow().size() - 1).getDungeonId());


  }


  @Test
  public void testArrowTraversalByAssertingPathOnWrappingDungeon() {

    GameInterface gameInterface = new Game(true, 4, 6,
            0, 30, 3,
            new RandomNumberGenerator(
                    44, 38, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0,
                    0, 1,
                    0, 0,
                    2, 0,
                    6, 1,
                    0, 0,     //for start and end cave
                    1, 2,
                    0, 3, 2, //arrow
                    0, 1    //monster
            ));
    //Player is currently at Location 1
    assertEquals(true,
            gameInterface.getPlayer().getCurrentLocationOfPlayer().toString().contains("Dungeon1"));
    //There is a monster at Dungeon 19 which is wrapped to dungeon 1
    for (DungeonLocationInterface dungeon : gameInterface.getDungeon().getDungeonMap().keySet()) {
      if (dungeon.getDungeonId() == 19) {
        assertEquals(true, dungeon.isMonsterPresent());
      }
      if (dungeon.getDungeonId() == 2 || dungeon.getDungeonId() == 6
              || dungeon.getDungeonId() == 12) {
        assertEquals(false, dungeon.isMonsterPresent());
      }
    }

    //We get a Smell from dungeon 19, hence smell is wrapped
    assertEquals(SmellLevel.STRONG,
            gameInterface.getDungeon().getSmellLevel(gameInterface.getPlayer()
                    .getCurrentLocationOfPlayer()));

    //we fire the arrow to NORTH
    ArrowTraversalModelInterface arrowTraversalModel =
            gameInterface.killMonster(Weapon.CROOKED_ARROW, Direction.NORTH, 1);

    //Weapon hit the monster, which means arrow wrapped
    assertEquals(true, arrowTraversalModel.isHitMonsterSuccessful());

    //check arrow path
    //start form location 1

    assertEquals(1, arrowTraversalModel.getLocationListTravelledByArrow()
            .get(0).getDungeonId());
    //arrow reached location 19
    assertEquals(19, arrowTraversalModel.getLocationListTravelledByArrow()
            .get(1).getDungeonId());


    //Weapon hit the monster again, which should kill him
    arrowTraversalModel =
            gameInterface.killMonster(Weapon.CROOKED_ARROW, Direction.NORTH, 1);
    assertEquals(true, arrowTraversalModel.isHitMonsterSuccessful());
    //check is arrow killed monster
    assertEquals(true, arrowTraversalModel.isKillMonsterSuccessful());

  }


  /**
   * Tests that when a monster is injured, it can kill and leave a player alive.
   * 1st case we shoot the monster with arrow and assert that player can remain alive, visiting the
   * cave where there is monster.
   * 2nd case we shoot the monster with arrow and assert that player dies, visiting the
   * cave where there is monster.
   */
  @Test
  public void testPlayerKilledOrNotWhenMonsterInjured() {
    GameInterface game = new Game(false, 4, 6, 0, 30,
            3, new RandomNumberGenerator(
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 1, 0, 0, 2, 0, 6, 1, 0, 0, 0, 1, 2, 2, 3,
            // decide to kill player or not when monster injured.
            0   // 0 MEANS PLAYER NOT KILLED
    ));
    game.movePlayerToDirection('e');
    game.movePlayerToDirection('e');
    assertEquals(SmellLevel.STRONG, game.getDungeon().getSmellLevel(
            game.getPlayer().getCurrentLocationOfPlayer()));
    game.killMonster(Weapon.CROOKED_ARROW, Direction.EAST, 1);
    game.movePlayerToDirection('e');
    assertEquals(true, game.getPlayer().getCurrentLocationOfPlayer().isMonsterPresent());
    assertEquals(false, game.isGameOver());
    assertEquals(100, game.getPlayer().getPlayerHealth());    //PLAYER left alive
    game.movePlayerToDirection('w');


    game = new Game(false, 4, 6, 0, 30,
            3, new RandomNumberGenerator(
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 1, 0, 0, 2, 0, 6, 1, 0, 0, 0, 1, 2, 2, 3,
            // decide to kill player or not when monster injured.
            1   // 1 MEANS PLAYER  KILLED
    ));
    game.movePlayerToDirection('e');
    game.movePlayerToDirection('e');
    assertEquals(SmellLevel.STRONG, game.getDungeon().getSmellLevel(
            game.getPlayer().getCurrentLocationOfPlayer()));
    game.killMonster(Weapon.CROOKED_ARROW, Direction.EAST, 1);
    game.movePlayerToDirection('e');
    assertEquals(true, game.getPlayer().getCurrentLocationOfPlayer().isMonsterPresent());
    assertEquals(true, game.isGameOver());
    assertEquals(0, game.getPlayer().getPlayerHealth());  //PLAYER killed

  }


  //Test added for project 5 deliverable
  @Test
  public void testNoOfRow() {
    assertEquals(4, game.getDungeon().getNoOfRows());
  }

  @Test
  public void testNoOfColumn() {
    assertEquals(6, game.getDungeon().getNoOfColumns());
  }

  @Test
  public void testRestartGame() {
    //The following parameters should remain same after game restart
    DungeonLocationInterface playerLocationBefore = game.getPlayer().getCurrentLocationOfPlayer();
    DungeonLocationInterface startLocationBefore = game.getStartLocation();
    DungeonLocationInterface endLocationBefore = game.getEndLocation();
    DungeonInterface dungeonInterface = game.getDungeon();
    int noRowsBefore = dungeonInterface.getNoOfRows();
    int noColsBefore = dungeonInterface.getNoOfColumns();
    int noCaveBefore = dungeonInterface.getNumberOfCave();
    String playerSpecBefore = game.getPlayer().printCollectedTreasure();
    int noTreasuredCaveBefore = dungeonInterface.getNumberOfTreasuredCaves();
    HashMap<TreasureEnum, Integer> weaponBefore = game.getPlayer().getAllWeapon();
    game.collectAWeapon(Weapon.CROOKED_ARROW, 1);
    game.movePlayerToDirection('e');
    game.movePlayerToDirection('e');
    game.collectTreasure();
    String treasureCollectedBefore = game.getPlayer().printCollectedTreasure();

    int arrowCountBefore = (int) game.getPlayer().getAllWeapon()
            .getOrDefault(Weapon.CROOKED_ARROW, 0);


    game.restartGame();

    DungeonLocationInterface playerLocationAfter = game.getPlayer().getCurrentLocationOfPlayer();
    DungeonLocationInterface startLocationAfter = game.getStartLocation();
    DungeonLocationInterface endLocationAfter = game.getEndLocation();
    DungeonInterface dungeonInterface1 = game.getDungeon();
    int noRowsAfter = dungeonInterface1.getNoOfRows();
    int noColsAfter = dungeonInterface1.getNoOfColumns();
    int noCaveAfter = dungeonInterface1.getNumberOfCave();
    int noTreasuredCaveAfter = dungeonInterface1.getNumberOfTreasuredCaves();
    String playerSpecAfter = game.getPlayer().printCollectedTreasure();
    String treasureCollectedAfter = game.getPlayer().printCollectedTreasure();
    HashMap<TreasureEnum, Integer> weaponAfter = game.getPlayer().getAllWeapon();
    int arrowCountAfter = (int) game.getPlayer().getAllWeapon()
            .getOrDefault(Weapon.CROOKED_ARROW, 0);


    assertEquals(noColsBefore, noColsAfter);
    assertEquals(noRowsBefore, noRowsAfter);
    assertEquals(noCaveBefore, noCaveAfter);
    assertEquals(noTreasuredCaveBefore, noTreasuredCaveAfter);
    assertEquals(playerLocationBefore, playerLocationAfter);
    assertEquals(startLocationBefore, startLocationAfter);
    assertEquals(endLocationBefore, endLocationAfter);
    assertEquals(playerSpecBefore, playerSpecAfter);
    assertEquals(weaponBefore, weaponAfter);

    //Not same as we collected treasure and arrow before restarting the game
    assertNotEquals(treasureCollectedBefore, treasureCollectedAfter);
    assertNotEquals(arrowCountBefore, arrowCountAfter);

  }

  @Test
  public void testLocationsVisitedByPlayer() {
    List<Integer> locationVisited = game.getDungeon().getLocationVisitedByPlayer();
    assertEquals(true, locationVisited.contains(2));
    game.movePlayerToDirection('e');
    locationVisited = game.getDungeon().getLocationVisitedByPlayer();
    assertEquals(true, locationVisited.contains(2) && locationVisited.contains(3));
    game.movePlayerToDirection('e');
    locationVisited = game.getDungeon().getLocationVisitedByPlayer();
    assertEquals(true,
            locationVisited.contains(2) && locationVisited.contains(3)
                    && locationVisited.contains(4));

  }

  @Test
  public void testLocationsVisitedResetOnGameRestart() {
    List<Integer> locationVisited = game.getDungeon().getLocationVisitedByPlayer();
    assertEquals(true, locationVisited.contains(2));
    game.movePlayerToDirection('e');
    locationVisited = game.getDungeon().getLocationVisitedByPlayer();
    assertEquals(true, locationVisited.contains(3));
    game.restartGame();
    locationVisited = game.getDungeon().getLocationVisitedByPlayer();
    assertEquals(false, locationVisited.contains(3));

  }

  @Test
  public void testEveryLocationsVisitedByPlayer() {
    testPlayerVisitEachNode();  //Method that visits all locations
    for (int i = 1; i <= 24; i++) {
      assertEquals(true,
              game.getDungeon().getLocationVisitedByPlayer().contains(i));
    }
  }


  @Test
  public void testPossibleDirectionFrom() {
    List<Direction> l1 = game.getDungeon().getPossibleDirectionFrom(game.getStartLocation());
    assertEquals(true, l1.contains(Direction.EAST)
            && l1.contains(Direction.SOUTH) && l1.contains(Direction.WEST));

  }


  // Tests added for Project 5

  //  Dungeon that will be constructed by the setup method
  //  {
  //    1 - 2 - 3 - 4 - 5 - 6
  //    |   |   |   |   |   |
  //    7   8   9   10  11  12
  //    |   |   |   |   |   |
  //    13  14  15  16  17  18
  //    |   |   |   |   |   |
  //    19  20  21  22  23  24
  //   }
  //   Thus caves include -> {2,3,4,5,19,20,21,22,23,24} = 10
  //   Remaining are 14 tunnels
}