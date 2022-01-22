package random;

import java.util.Random;

/**
 * Can be used in both modes, to generate or emulate random numbers.
 */
public class RandomNumberGenerator extends Random {
  int[] ints;
  final boolean trueRandomMode;
  int pointer;

  /**
   * Called when we want to use class in pseudo-random mode.
   *
   * @param ints is the integer array that will be returned when {@code trueRandomMode = false}
   */
  public RandomNumberGenerator(int... ints) {
    this.ints = ints;
    trueRandomMode = false;
    pointer = 0;
  }

  /**
   * Called when we want to use class in Random mode.
   */
  public RandomNumberGenerator() {
    trueRandomMode = true;
  }

  /**
   * Gets a random Integer.
   *
   * @return a random Integer value.
   * @throws ArrayIndexOutOfBoundsException when class is in pseudo-random mode and more values
   *                                        are fetched then the ones that exist.
   */
  public int getRandomInt() throws ArrayIndexOutOfBoundsException {
    if (trueRandomMode) {
      return this.nextInt();
    } else {
      return ints[pointer++];
    }
  }

  /**
   * Gets a random Integer between Min and Max.
   *
   * @param min the min value of the return Int.
   * @param max the max value of the return Int.
   * @return a random Integer between min and max.
   * @throws ArrayIndexOutOfBoundsException when class is in pseudo-random mode and more values
   *                                        * are fetched then the ones that exist.
   */
  public int getRandomInt(int min, int max) throws ArrayIndexOutOfBoundsException {
    if (trueRandomMode) {
      max++;
      return this.nextInt(max - min) + min;

    } else {
      return ints[pointer++];
    }
  }
}
