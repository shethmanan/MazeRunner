import java.util.Scanner;

import dungeonmodel.Game;
import dungeonmodel.GameInterface;
import random.RandomNumberGenerator;


/**
 * The driver class which will mock the entire Dungeon game.
 */
public class DriverProject3 {
  /**
   * The method which starts the program execution.
   *
   * @param arg represents command line arguments.
   */
  public static void main(String[] arg) {
    boolean wrapping;
    char playerMove;
    int pickTreasure;


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
                2, 2,
                2, 2,
                0, 0,     //for start and end cave
                0, 0,
                0, 0
        );
      } else {
        randomNumberGenerator = new RandomNumberGenerator(
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,   //38 random ints for 4*6 matrix as it has
                // 38 paths
                // 0, 0,     //  2 random ints for interconnectivity of leftovers
                0, 1,     //   cave2 at location 0 followed by ruby treasure
                0, 0,     //  cave2 at location 0 followed by diamond treasure
                2, 0,     //  cave4 at location 2 followed by diamond treasure
                6, 1,
                0, 0,     //for start and end cave
                0, 1, 2      // add 3 arrows to these cave numbers
        );
      }
    }

    GameInterface game = new Game(wrapping, noRows, noColumns, interconnectivity,
            percentageCaveContainingTreasure, 1, randomNumberGenerator);
    System.out.println("Dungeon Created. \nStart Location: " + game.getStartLocation()
            + "\t\t\tEnd Location: " + game.getEndLocation()
            .toString());

    while (!game.isGameOver()) {
      System.out.println("\nPlayer Currently at: "
              + game.getPlayer().getPlayerDetail());
      if (game.getTreasureCountAtPlayerLocation() > 0) {
        System.out.print("Enter 1 to pick treasure, 2 otherwise: ");
        pickTreasure = sc.nextInt();
        if (pickTreasure == 1) {
          game.collectTreasure();
          System.out.print("Player updated status: ");
          System.out.println(game.getPlayer().printCollectedTreasure());
        }
      }

      System.out.print("Possible moves: " + game.getPlayerPossibleMoves() + ". Enter your move: ");
      playerMove = sc.next().charAt(0);
      while (!game.movePlayerToDirection(playerMove)) {
        System.out.print("Kindly enter correct move, ");
        System.out.print("possible moves: " + game.getPlayerPossibleMoves() + ": ");
        playerMove = sc.next().charAt(0);

      }
    }
    System.out.println("Game Completed successfully by the player by reaching destination location "
            + game.getEndLocation().getDungeonId());
  }


}
