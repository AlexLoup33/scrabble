package pdp.scrabble.game;

import java.util.Random;
import pdp.scrabble.Utilities;

public class RandomAPI {
  private static final Random rngGenerator = new Random();
  private static Long baseSeed = null;
  private static Long currentState = null;

  /**
   * will set the base seed for the RandomAPI
   *
   * @param configSeed the seed to use, -1 => random seed
   */
  public static void configure(int configSeed) {
    if (configSeed == -1) {
      RandomAPI.baseSeed = RandomAPI.rngGenerator.nextLong();
      Utilities.verbose(
          "configured RandomAPI(random seed) with seed: " + RandomAPI.baseSeed + "\n");
    } else {
      RandomAPI.baseSeed = (long) configSeed;
    }
    RandomAPI.reset();
  }

  /** reset the state to the baseSeed determined when configured */
  public static void reset() {
    RandomAPI.loadState(RandomAPI.baseSeed);
  }

  /**
   * @return the current state of the RandomAPI
   */
  public static long getCurrentState() {
    return RandomAPI.currentState;
  }

  /**
   * load the internal state to rngState
   *
   * @param rngState the internal state to restore
   */
  public static void loadState(long rngState) {
    RandomAPI.rngGenerator.setSeed(rngState);
    RandomAPI.currentState = rngState;
  }

  /** update the internal current state, to be called after each call to RandomAPI.rngGenerator */
  private static void updateState() {
    RandomAPI.currentState = RandomAPI.rngGenerator.nextLong();
    RandomAPI.rngGenerator.setSeed(RandomAPI.currentState);
  }

  /**
   * generate a random long in [origin, bound[
   *
   * @param origin the lower bound (included)
   * @param bound the upper bound (exclued)
   * @return a random long
   */
  public static long getlong(long origin, long bound) {
    long num = RandomAPI.rngGenerator.nextLong(origin, bound);
    RandomAPI.updateState();
    return num;
  }

  /**
   * generate a random int in [origin, bound[
   *
   * @param origin the lower bound (included)
   * @param bound the upper bound (exclued)
   * @return a random int
   */
  public static int getInt(int origin, int bound) {
    int num = RandomAPI.rngGenerator.nextInt(origin, bound);
    RandomAPI.updateState();
    return num;
  }
}
