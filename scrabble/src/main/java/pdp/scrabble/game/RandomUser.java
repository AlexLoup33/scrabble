package pdp.scrabble.game;

public interface RandomUser {
  /**
   * generate a random long in [origin, bound[
   *
   * @param origin the lower bound (included)
   * @param bound the upper bound (exclued)
   * @return a random long
   */
  public long getlong(long origin, long bound);

  /**
   * generate a random int in [origin, bound[
   *
   * @param origin the lower bound (included)
   * @param bound the upper bound (exclued)
   * @return a random int
   */
  public int getInt(int origin, int bound);
}
