import java.io.InputStreamReader;
import java.util.Scanner;

import controller.ConsoleDungeonController;
import controller.DungeonController;
import dungeonmodel.Game;
import dungeonmodel.GameInterface;
import random.RandomNumberGenerator;

/**
 * The driver class which will mock the entire Dungeon game.
 */
public class DriverProject4 {

  /**
   * The method which starts the program execution.
   *
   * @param arg represents command line arguments.
   */
  public static void main(String[] arg) {
    boolean wrapping;

    if (Integer.parseInt(arg[0]) == 2) {
      wrapping = true;
    } else {
      wrapping = false;
    }
    int noRows;
    noRows = Integer.parseInt(arg[1]);
    int noColumns;
    noColumns = Integer.parseInt(arg[2]);
    int interconnectivity;
    interconnectivity = Integer.parseInt(arg[3]);
    int percentageCaveContainingTreasure;
    percentageCaveContainingTreasure = Integer.parseInt(arg[4]);
    int noOfMonster;
    noOfMonster = Integer.parseInt(arg[5]);
    int randomObject;
    Scanner sc = new Scanner(System.in);
    System.out.print("Enter 1 for pseudo-random object and 2 for random object: ");
    randomObject = sc.nextInt();
    RandomNumberGenerator randomNumberGenerator;

    if (randomObject == 2) {
      randomNumberGenerator = new RandomNumberGenerator();
    } else {
      System.out.println("NOTE: (Random numbers have been passed for a maximum of 4*6 matrix in "
              + "the pseudo random constructor)");
      if (wrapping) {
        randomNumberGenerator = new RandomNumberGenerator(
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
        );
      } else {
        randomNumberGenerator = new RandomNumberGenerator(
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
        );
      }
    }

    GameInterface game = new Game(wrapping, noRows, noColumns, interconnectivity,
            percentageCaveContainingTreasure, noOfMonster, randomNumberGenerator);
    Readable input = new InputStreamReader(System.in);
    Appendable output = System.out;


    DungeonController controller = new ConsoleDungeonController(input, output);
    controller.playGame(game);
  }
}
