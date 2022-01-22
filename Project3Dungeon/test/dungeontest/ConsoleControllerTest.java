package dungeontest;

import org.junit.Test;

import java.io.StringReader;

import controller.ConsoleDungeonController;
import controller.DungeonController;
import dungeonmodel.Game;
import dungeonmodel.GameInterface;
import random.RandomNumberGenerator;

import static org.junit.Assert.assertNotEquals;

/**
 * Used to test the Console Controller.
 */
public class ConsoleControllerTest {

  @Test(expected = IllegalArgumentException.class)
  public void testNullReader() {
    Appendable gameLog = new FailingAppendable();
    DungeonController c = new ConsoleDungeonController(null, gameLog);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullAppendable() {
    StringReader input = new StringReader("2 2 1 1 3 3 1 2 1 3 2 3 2 1 3 1 3 2");
    DungeonController c = new ConsoleDungeonController(input, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullAppendableAndReader() {
    StringReader input = new StringReader("2 2 1 1 3 3 1 2 1 3 2 3 2 1 3 1 3 2");
    DungeonController c = new ConsoleDungeonController(null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullModel() {
    StringReader input = new StringReader("2 2 1 1 3 3 1 2 1 3 2 3 2 1 3 1 3 2");
    Appendable gameLog = new FailingAppendable();
    DungeonController c = new ConsoleDungeonController(input, gameLog);
    c.playGame(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailingAppendable() {
    // Testench when something goes wrong with the Appendable
    // Here we are passing it a mock Appendable that always fails
    StringReader input = new StringReader("2 2 1 1 3 3 1 2 1 3 2 3 2 1 3 1 3 2");
    Appendable gameLog = new FailingAppendable();
    DungeonController c = new ConsoleDungeonController(input, gameLog);
    c.playGame(null);
  }

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
            0, 1, 1           // decide to kill player or not when monster injured. 0 = Don't kill
    ));
  }

  /**
   * In this test case we test all the invalid inputs in all scenarios:
   * 1. The player starts from a start location
   * 2. When prompted to select whether to move, shoot or pick the player inputs invalid input.
   * This is handled by appropriate message by the controller.
   * 3. When selecting to Move -> A. the player inputs invalid direction shortcut. B. Direction
   * not traversable from the current location. Both are handled by the controller.
   * 4. When selecting to pick -> A. User selects invalid shortcut to pick. B. An item which is
   * not present at the current location. Both of these are handled bt the controller.
   * 5. When selecting to see player description -> controller shows correct description.
   * 6. When selecting to shoot -> A. Player enter invalid distance. B. Player enters invalid
   * shortcut for a direction. C. Player enters valid direction but the direction is not
   * navigable from current location.
   */
  @Test
  public void testPlay1() {
    StringReader input = new StringReader("w m x n p n s d s 0 2 q n m e m e m e");
    Appendable gameLog = new StringBuffer();
    GameInterface model = getGameObject();
    DungeonController c = new ConsoleDungeonController(input, gameLog);
    c.playGame(model);

    String log = gameLog.toString();
    int currentIndex;
    currentIndex = log.indexOf("You are in a CAVE");
    assertNotEquals(-1, currentIndex); //Point 1
    log = log.substring(currentIndex);
    // menu option to select move type
    currentIndex = log.indexOf("Move, Pickup, Shoot, Player Details (M-P-S-D)?");
    assertNotEquals(-1, currentIndex);
    log = log.substring(currentIndex);
    // Menu displayed when the invalid shortcut entered by user in the previous menu
    currentIndex = log.indexOf("Enter M - Move, P - Pickup, S - Shoot, D - Player Details");
    assertNotEquals(-1, currentIndex); //Point 2
    log = log.substring(currentIndex);

    // Menu displayed when M or Move selected
    currentIndex = log.indexOf("Where? (N,S,E,W)");
    assertNotEquals(-1, currentIndex);
    log = log.substring(currentIndex);

    // Menu displayed when user types invalid shortcut in the Move menu
    currentIndex = log.indexOf("(N - NORTH, S - SOUTH, E - EAST, W - WEST) Where?");
    assertNotEquals(-1, currentIndex); //Point 3.A
    log = log.substring(currentIndex);

    // Menu displayed when user enters a direction which cannot be traversed from current location
    currentIndex = log.indexOf("enter a valid move.");
    assertNotEquals(-1, currentIndex); //Point 3.B
    log = log.substring(currentIndex);

    // Menu displayed when user select Pick or P
    currentIndex = log.indexOf("What? (Ruby - r, Diamond - d, Sapphire - s, Arrow - a");
    assertNotEquals(-1, currentIndex);
    log = log.substring(currentIndex);

    // Menu displayed when user selects invalid shortcut to pick item
    currentIndex = log.indexOf("Kindly enter valid input. Pick what?"); //Point 4.A
    assertNotEquals(-1, currentIndex);
    log = log.substring(currentIndex);

    // Displayed when user selects an item to pick which is not present at the current location
    currentIndex = log.indexOf("You can only pick treasure at current location"); //Point 4.B
    assertNotEquals(-1, currentIndex);
    log = log.substring(currentIndex);


    // Print player description when 'd' selected
    currentIndex = log.indexOf("Treasure collected: NONE\n"
            + "Available weapons: 3 CROOKED_ARROW");
    assertNotEquals(-1, currentIndex); //Point 5
    log = log.substring(currentIndex);

    // Displayed when user selects to shoot 's'
    currentIndex = log.indexOf("No of caves (1-5):");
    assertNotEquals(-1, currentIndex);
    log = log.substring(currentIndex);

    // Displayed when user enters an invalid distance
    currentIndex = log.indexOf("Enter valid distance.");
    assertNotEquals(-1, currentIndex); //Point 6.A
    log = log.substring(currentIndex);

    // Displayed when user enters valid distance but invalid direction
    currentIndex = log.indexOf("Enter valid direction"); //Point 6.B
    assertNotEquals(-1, currentIndex);
    log = log.substring(currentIndex);

    // Displayed when user enter a direction which is not accessible from player's current location
    currentIndex = log.indexOf("Kindly select available weapon and direction");
    assertNotEquals(-1, currentIndex); //Point 6.C
    log = log.substring(currentIndex);
  }

  /**
   * In this test case:
   * 1. The player starts from a start location
   * 2. The player finds treasure and arrow both at a location but does not pick any
   * 3. The player moves around in the dungeon and find a MILD stench.
   * 4. The player shoots arrows - (4.A) All which he currently has.
   * (4.B)He also tries to shoot arrows in locations which are not valid based on his
   * current location. He gets appropriate error messages for this.
   * 5. On trying to shoot more he gets appropriate error message.
   * 6. The arrows don't kill any Otyugh.
   * 7. The player checks his profile of collected weapons and treasure.
   * 8. The player moves ahead and gets a STRONG smell.
   * 9. The player moves and ends in a location where an Otyugh is present.
   * An otyugh kills the player and the game is over.
   */
  @Test
  public void testPlay2() {
    StringReader input = new StringReader("m e s 2 n s 1 e s 1 e s 1 e s 2 e d m e m e");
    Appendable gameLog = new StringBuffer();
    GameInterface model = getGameObject();
    DungeonController c = new ConsoleDungeonController(input, gameLog);
    c.playGame(model);
    String log = gameLog.toString();

    int currentIndex;
    currentIndex = log.indexOf("You are in a CAVE");
    assertNotEquals(-1, currentIndex); //Point 1
    log = log.substring(currentIndex);

    currentIndex = Math.max(log.indexOf("You find 1 RUBY,1 DIAMOND"),
            log.indexOf("You find 1 DIAMOND,1 RUBY"));

    assertNotEquals(-1, currentIndex); //Point 2
    log = log.substring(currentIndex);

    currentIndex = log.indexOf("You find 1 CROOKED_ARROW");
    assertNotEquals(-1, currentIndex); //Point 2
    log = log.substring(currentIndex);

    currentIndex = log.indexOf("You smell a mild stench nearby");
    assertNotEquals(-1, currentIndex); //Point 3
    log = log.substring(currentIndex);

    currentIndex = log.indexOf("Kindly select available weapon and direction");
    assertNotEquals(-1, currentIndex); //Point 4.B Invalid direction
    log = log.substring(currentIndex);

    currentIndex = log.lastIndexOf("You shot an arrow!");
    assertNotEquals(-1, currentIndex); //Point 4.A Shoot all arrow
    log = log.substring(currentIndex);


    currentIndex = log.lastIndexOf("Kindly select available weapon and direction");
    //Point 5. Trying to shoot when no arrow exist
    assertNotEquals(-1, currentIndex);
    log = log.substring(currentIndex);

    currentIndex = log.lastIndexOf("Treasure collected: NONE\n"
            + "Available weapons: 0 CROOKED_ARROW");
    //Point 7.
    assertNotEquals(-1, currentIndex);
    log = log.substring(currentIndex);

    currentIndex = log.lastIndexOf("You smell something terrible nearby");
    //Point 8.
    assertNotEquals(-1, currentIndex);
    log = log.substring(currentIndex);

    currentIndex = log.lastIndexOf("Nom, nom, nomm.. An Otyugh ate you!");
    //Point 9.
    assertNotEquals(-1, currentIndex);
    log = log.substring(currentIndex);
  }

  /**
   * In this test case:
   * 1. The player starts from a start location
   * 2. (A) The player finds multiple treasures and (B)arrow both at a location.
   * (C) - The player picks some treasure and leaves the remaining.
   * (D) - The player then also picks an arrow.
   * 3. The player checks his collected weapons and arrows.
   * 4. The player then moves ahead and tries to pick treasure at a location where no treasure
   * exists. An appropriate message is displayed in this case.
   * 5. The player also tries to pick an arrow at a location where no arrows exist. An
   * appropriate message is displayed.
   * 6. The player moves around in the dungeon and find a MILD stench.
   * 7. The player moves around in the dungeon and find a STRONG stench.
   * 8. The player (A)shoots 2 arrows and (B)kills a monster.
   * 9. The player smells another stench while moving ahead.
   * 10. The player moves ahead and gets a STRONG stink.
   * 11. The player fires 2 arrow and kills the Otyugh.
   * 12. The player moves ahead and ends up in the final cave. The game is over.
   */
  @Test
  public void testPlay3() {
    StringReader input = new StringReader("p r p a d m e p r m w p a m e m e s 1 e s 1 e m "
            + "s m s s 1 s s 1 s m s");
    Appendable gameLog = new StringBuffer();
    GameInterface model = getGameObject();
    DungeonController c = new ConsoleDungeonController(input, gameLog);
    c.playGame(model);
    String log = gameLog.toString();
    int currentIndex;
    currentIndex = log.indexOf("You are in a CAVE");
    assertNotEquals(-1, currentIndex); //Point 1
    log = log.substring(currentIndex);

    currentIndex = Math.max(log.indexOf("You find 1 RUBY,1 DIAMOND"),
            log.indexOf("You find 1 DIAMOND,1 RUBY"));

    assertNotEquals(-1, currentIndex); //Point 2.A
    log = log.substring(currentIndex);

    currentIndex = log.indexOf("You find 1 CROOKED_ARROW");
    assertNotEquals(-1, currentIndex); //Point 2.B
    log = log.substring(currentIndex);

    currentIndex = log.indexOf("You picked a treasure");
    assertNotEquals(-1, currentIndex); //Point 2.C
    log = log.substring(currentIndex);

    currentIndex = log.indexOf("You picked an arrow");
    assertNotEquals(-1, currentIndex); //Point 2.D
    log = log.substring(currentIndex);

    currentIndex = log.indexOf("Treasure collected: 1 RUBY\n"
            + "Available weapons: 4 CROOKED_ARROW");
    assertNotEquals(-1, currentIndex); //Point 3
    log = log.substring(currentIndex);

    currentIndex = log.indexOf("You can only pick treasure at current location");
    assertNotEquals(-1, currentIndex); //Point 4
    log = log.substring(currentIndex);

    currentIndex = log.indexOf("You can only pick weapon at current location");
    assertNotEquals(-1, currentIndex); //Point 5
    log = log.substring(currentIndex);

    currentIndex = log.indexOf("You smell a mild stench nearby");
    assertNotEquals(-1, currentIndex); //Point 6
    log = log.substring(currentIndex);

    currentIndex = log.indexOf("You smell something terrible nearby");
    assertNotEquals(-1, currentIndex); //Point 7
    log = log.substring(currentIndex);

    currentIndex = log.indexOf("You shot an arrow!");
    assertNotEquals(-1, currentIndex); //Point 8(A), 1st arrow
    log = log.substring(currentIndex);

    currentIndex = log.indexOf("You shot an arrow!");
    assertNotEquals(-1, currentIndex); //Point 8(A), 2nd arrow
    log = log.substring(currentIndex);

    currentIndex = log.indexOf("Bravo! You killed a monster");
    assertNotEquals(-1, currentIndex); //Point 8(B)
    log = log.substring(currentIndex);

    currentIndex = log.indexOf("You smell a mild stench nearby");
    assertNotEquals(-1, currentIndex); //Point 9
    log = log.substring(currentIndex);

    currentIndex = log.indexOf("You smell something terrible nearby");
    assertNotEquals(-1, currentIndex); //Point 10
    log = log.substring(currentIndex);

    currentIndex = log.indexOf("Bravo! You killed a monster");
    assertNotEquals(-1, currentIndex); //Point 11
    log = log.substring(currentIndex);

    currentIndex = log.indexOf("Congratulations! You won the game.");
    assertNotEquals(-1, currentIndex); //Point 12
    log = log.substring(currentIndex);
  }

  /**
   * In this test case:
   * 1. The player starts from a start location
   * 2. He smells a mild stench.
   * 3. He fires 1 arrow.
   * 4. He travels to a location which has an injured Otyugh.
   * 5. The player moves back to another position without getting killed by the Otyugh.
   * 6. The player again moves to the location which had the monster.
   * 7. This time the Otyugh kills the player.
   */
  @Test
  public void testPlay4() {
    StringReader input = new StringReader("m e s 2 e m e m e m w m e");
    Appendable gameLog = new StringBuffer();
    GameInterface model = getGameObject();
    DungeonController c = new ConsoleDungeonController(input, gameLog);
    c.playGame(model);
    String log = gameLog.toString();
    int currentIndex;
    currentIndex = log.indexOf("You are in a CAVE");
    assertNotEquals(-1, currentIndex); //Point 1
    log = log.substring(currentIndex);

    currentIndex = log.indexOf("You smell a mild stench nearby");
    assertNotEquals(-1, currentIndex); //Point 2
    log = log.substring(currentIndex);

    currentIndex = log.indexOf("You shot an arrow!");
    assertNotEquals(-1, currentIndex); //Point 3
    log = log.substring(currentIndex);

    currentIndex = log.indexOf("You smell something terrible nearby");
    assertNotEquals(-1, currentIndex); //Point 4
    log = log.substring(currentIndex);

    currentIndex = log.indexOf("You smell something terrible nearby");
    assertNotEquals(-1, currentIndex); //Point 5
    log = log.substring(currentIndex);

    currentIndex = log.indexOf("You smell something terrible nearby");
    assertNotEquals(-1, currentIndex); //Point 6
    log = log.substring(currentIndex);


    currentIndex = log.lastIndexOf("Nom, nom, nomm.. An Otyugh ate you!");
    assertNotEquals(-1, currentIndex);  //Point 7
    log = log.substring(currentIndex);

  }


}
