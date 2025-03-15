package pdp.scrabble.game;

import java.util.Random;

public class RandomAPI {
    private static final Random rngGenerator = new Random();
    private static Long baseSeed = null;

    /**
     * will set the base seed for the RandomAPI
     * @param configSeed the seed to use, -1 => random seed
     */
    public static void configure(int configSeed) {
        if (configSeed == -1) {
            RandomAPI.baseSeed = RandomAPI.rngGenerator.nextLong();
        } else {
            RandomAPI.baseSeed = (long)configSeed;
        }
        RandomAPI.reset();
    }

    /**
     * reset the state to the baseSeed determined when configured
     */
    public static void reset() {
        RandomAPI.rngGenerator.setSeed(RandomAPI.baseSeed);
    }

    public static Random get() {
        return RandomAPI.rngGenerator;
    }
}
