package pdp.scrabble.game;

import pdp.scrabble.dictionary.LettersCounter;
import pdp.scrabble.errorsHandling.CustomError;
import pdp.scrabble.errorsHandling.ErrorType;
import pdp.scrabble.game.actions.GameAction;

public class Player {

  public final Integer id;
  private String name;
  private int score;
  public final LettersCounter bag;
  private boolean hasForfeited;
  private boolean hasPassed;
  private long remainingTime; // in sec
  private final int timePerPlayer;

  public Player(int nextPlayerID, int timePerPlayer) {
    this("Player", nextPlayerID, timePerPlayer);
  }

  protected Player(String nameBase, int nextPlayerID, int timePerPlayer) {
    this.id = nextPlayerID;
    this.name = nameBase + this.id;
    this.score = 0;
    this.bag = new LettersCounter();
    this.hasForfeited = false;
    this.hasPassed = false;
    this.timePerPlayer = timePerPlayer;
    this.remainingTime = this.timePerPlayer * 60L;
  }

  public void reset() {
    this.score = 0;
    this.bag.clear();
    this.hasForfeited = false;
    this.hasPassed = false;
    this.remainingTime = this.timePerPlayer * 60L;
    // don't reset the player name
  }

  /** tell the player he passed his turn */
  public void playerPassedTurn() {
    this.hasPassed = true;
  }

  /**
   * @return whether the player passed his turn
   */
  public boolean hasPassed() {
    return this.hasPassed;
  }

  /** to be called when the game turn (for all players) is finished */
  public void endGameTurn() {
    this.hasPassed = false;
  }

  public String getName() {
    if (this.hasForfeited) {
      return this.name + "(forfeited)";
    }
    return this.name;
  }

  public boolean getHasForfeited() {
    return this.hasForfeited;
  }

  public void forfeit() {
    if (this.hasForfeited) {
      throw new CustomError(
          ErrorType.ForfeitTwiceError,
          "can't forfeit twice",
          this + " has already forfeited, can't forfeit another time");
    }
    // => hasn't forfeited yet
    this.hasForfeited = true;
  }

  public void rename(String newName) {
    this.name = newName;
  }

  @Override
  public String toString() {
    return "Player{"
        + "id="
        + id
        + ", name='"
        + name
        + '\''
        + ", score="
        + score
        + ", bag="
        + bag
        + ", hasForfeited="
        + hasForfeited
        + ", hasPassed="
        + hasPassed
        + ", remainingTime="
        + remainingTime
        + '}';
  }

  @Override
  protected Object clone() throws CloneNotSupportedException {
    return super.clone();
  }

  public int getScore() {
    return this.score;
  }

  public void addScore(int points) {
    this.score = score + points;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null || obj.getClass() != this.getClass()) return false;
    Player compared = (Player) obj;
    return this.id == compared.id;
  }

  public float getRemainingTime() {
    return this.remainingTime;
  }

  public String getRemainingTimeAsString() {
    int minutes = (int) this.remainingTime / 60;
    int seconds = (int) this.remainingTime % 60;

    return String.format("%d'%02d\"", minutes, seconds);
  }

  public void addTurnPlayedTime(float duration) {
    this.remainingTime -= (long) duration;
  }

  public void undoTurnPlayedTime(GameAction toUndo) {
    this.remainingTime += (long) toUndo.duration;
  }

  public void undoForfeit() {
    if (!this.hasForfeited) {
      throw new Error("[BUG] can't undo forfeit, the player (id=" + this.id + ") hasn't forfeited");
    }
    this.hasForfeited = false;
  }

  /**
   * undo the score of the player to the given score
   *
   * @param scoreBefore score of the player before the action
   */
  public void undoScoreTo(int scoreBefore) {
    this.score = scoreBefore;
  }
}
