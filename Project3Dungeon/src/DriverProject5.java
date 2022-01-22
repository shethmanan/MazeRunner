
import java.io.InputStreamReader;

import controller.ConsoleDungeonController;
import controller.DungeonController;
import controller.DungeonGuiController;
import dungeonmodel.Game;
import dungeonmodel.GameInterface;
import random.RandomNumberGenerator;
import view.GameViewSwingUi;

/**
 * Driver for Project 5 deliverable. Run with arguments for text based game and without
 * parameters for GUI game.
 */
public class DriverProject5 {
  /**
   * The main method to start the program.
   *
   * @param arg contains command line arguments.
   */
  public static void main(String[] arg) {
    //1 4 6 0 30 3
    if (arg.length == 0) {
      GameInterface model = new Game(true, 4, 6, 1,
              30, 3, new RandomNumberGenerator());
      GameViewSwingUi view = new GameViewSwingUi(model);
      DungeonGuiController controller = new DungeonGuiController(model, view);
      controller.playGame();
    } else {
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

      randomObject = 2;
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
}
