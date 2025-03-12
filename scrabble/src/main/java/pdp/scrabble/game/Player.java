package pdp.scrabble.game;

import pdp.scrabble.dictionary.LettersCounter;
import pdp.scrabble.errorsHandling.CustomError;
import pdp.scrabble.errorsHandling.ErrorType;

public class Player {
  private static int nextPlayerID = 0;

  public final Integer id;
  private String name;
  public int score;
  public final LettersCounter bag;
  private boolean hasForfeited;

  public Player() {
    this("Player");
  }

  protected Player(String nameBase) {
    this.id = Player.nextPlayerID;
    Player.nextPlayerID += 1;
    this.name = nameBase + this.id;
    this.score = 0;
    this.bag = new LettersCounter();
    this.hasForfeited = false;
  }

  public void reset() {
    this.score = 0;
    this.bag.reset();
    this.hasForfeited = false;
    // don't reset the player name
  }

  public String getName() {
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
    return "Player{id=" + id + ", name='" + name + '\'' + ", score=" + score + ", bag=" + bag + '}';
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
}
