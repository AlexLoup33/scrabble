package pdp.scrabble.game;

import java.util.Random;

import pdp.scrabble.Utilities;

public class RandomAPI implements RandomUser {
    private final Random rngGenerator;
    private final Long baseSeed;
    private Long currentState;

    /**
     * will set the base seed for the RandomAPI
     *
     * @param configSeed the seed to use, -1 => random seed
     */
    public RandomAPI(int configSeed) {
        this.rngGenerator = new Random();
        if (configSeed == -1) {
            this.baseSeed = this.rngGenerator.nextLong();
            Utilities.verbose("configured RandomAPI(random seed) with seed: " + this.baseSeed + "\n");
        } else {
            this.baseSeed = (long) configSeed;
        }
        this.reset();
    }

    /**
     * reset the state to the baseSeed determined when configured
     */
    public void reset() {
        this.loadState(this.baseSeed);
    }

    /**
     * @return the current state of the RandomAPI
     */
    public long getCurrentState() {
        return this.currentState;
    }

    /**
     * load the internal state to rngState
     *
     * @param rngState the internal state to restore
     */
    public void loadState(long rngState) {
        this.rngGenerator.setSeed(rngState);
        this.currentState = rngState;
    }

    /**
     * update the internal current state, to be called after each call to RandomAPI.rngGenerator
     */
    private void updateState() {
        this.currentState = this.rngGenerator.nextLong();
        this.rngGenerator.setSeed(this.currentState);
    }

    /**
     * generate a random long in [origin, bound[
     *
     * @param origin the lower bound (included)
     * @param bound  the upper bound (exclued)
     * @return a random long
     */
    public long getlong(long origin, long bound) {
        long num = this.rngGenerator.nextLong(origin, bound);
        this.updateState();
        return num;
    }

    /**
     * generate a random int in [origin, bound[
     *
     * @param origin the lower bound (included)
     * @param bound  the upper bound (exclued)
     * @return a random int
     */
    public int getInt(int origin, int bound) {
        int num = this.rngGenerator.nextInt(origin, bound);
        this.updateState();
        return num;
    }
}
