package pdp.scrabble.game.actions;

import java.util.Arrays;
import pdp.scrabble.dictionary.Letter;
import pdp.scrabble.dictionary.LettersCounter;
import pdp.scrabble.errorsHandling.CustomError;
import pdp.scrabble.errorsHandling.ErrorType;
import pdp.scrabble.game.GameModel;
import pdp.scrabble.game.GridWordPos;
import pdp.scrabble.game.LettersDrawer;
import pdp.scrabble.game.Player;

/**
 * Play at least one tile on the board, adding the value of all words formed to
 * the player's
 * cumulative score
 */
public class PlaceWordAction extends GameAction {
  public final Letter[] toPlace;
  // the letters of the player to place from
  public final GridWordPos startPosition;

  // Used by undo
  public long rngStateBefore = 0L; // to set by model
  public final LettersCounter drawedAfter; // Empty until model calls drawLetters()
  public final int scoreBefore;

  public PlaceWordAction(Player player, int turn, Letter[] toPlace, GridWordPos startPos) {
    super(player, turn);
    this.toPlace = toPlace.clone();
    this.startPosition = startPos;
    this.scoreBefore = player.getScore();
    this.drawedAfter = new LettersCounter();
  }

  @Override
  public void applyGameAction(GameModel gameModel) {
    gameModel.applyGameAction(this);
  }

  /**
   * convert the letters toPlace to a LetterCounter
   *
   * @return
   */
  private LettersCounter getToPlaceAsCounter() {
    LettersCounter result = new LettersCounter();
    for (Letter l : this.toPlace) {
      result.addLetter(l);
    }
    return result;
  }

  private LettersCounter getMissingLetters() {
    LettersCounter missing = new LettersCounter();
    LettersCounter toPlace = this.getToPlaceAsCounter();

    for (Letter letter : toPlace.getLettersSet()) {
      int nbToPlace = toPlace.getLetterCount(letter);
      int nbInBag = this.player.bag.getLetterCount(letter);
      if (nbToPlace > nbInBag) {
        // => the letter isn't in the player's bag
        // add the nb of missing letters
        missing.addLetter(letter, nbToPlace - nbInBag);
      } // else -> the count is good
    }
    return missing;
  }

  /** assert the player has the required letters in his bag */
  public void assertHasLettersToPlace() {
    LettersCounter missing = this.getMissingLetters();

    // check if the jokers can fill the missing letters
    int nbJokerInBag = this.player.bag.getLetterCount(Letter.joker());
    int nbMissingLetters = missing.size();
    if (nbMissingLetters > nbJokerInBag) {
      throw new CustomError(
          ErrorType.PlayerDoesntHaveLettersInIsBag,
          "the following letters: " + missing.toString() + " are missing (before jokers)",
          this.player.getName()
              + " doesn't have the following letters in his bag: "
              + missing.toString()
              + " in is bag (before appling jokers)");
    }
  }

  /**
   * remove from the bag the letters placed you must check if the player has the
   * letters before !
   * (it will be asserted before removing the letters)
   */
  public void removeLettersPlaced() {
    this.assertHasLettersToPlace(); // avoid breaking anything

    LettersCounter toPlace = this.getToPlaceAsCounter();
    for (Letter letter : toPlace.getLettersSet()) {
      int nbToPlace = toPlace.getLetterCount(letter);
      int nbInBag = this.player.bag.getLetterCount(letter);
      if (nbToPlace <= nbInBag) {
        // => there are enougth letters in the bag
        this.player.bag.removeLetter(letter, nbToPlace);
      } else {
        // => not enougth letters in the bag, use jokers
        int nbMissing = (nbToPlace - nbInBag);
        this.player.bag.removeLetter(letter, nbInBag);
        this.player.bag.removeLetter(Letter.joker(), nbMissing);
      }
    }
  }

  public void drawLetters(LettersDrawer draw) {
    if (draw.getCurrentSize() < 7) {
      throw new Error("[BUG] draw letter count is strictly under 7");
    }
    while (this.player.bag.size() < 7) {
      Letter letterDrawed = draw.popLetter();
      this.player.bag.addLetter(letterDrawed);
      this.drawedAfter.addLetter(letterDrawed);
    }
  }

  @Override
  public String toString() {
    String lettersList = Arrays.stream(this.toPlace)
        .map(arg0 -> "" + arg0.chr)
        .reduce("", (arg0, arg1) -> arg0 + arg1 + ", ");
    return this.getClass().getSimpleName()
        + "{"
        + "toPlace=["
        + lettersList
        + "], startPosition="
        + this.startPosition
        + ", playerID:"
        + this.player.id
        + ", rngStateBefore="
        + this.rngStateBefore
        + ", drawedAfter=["
        + this.drawedAfter.toString()
        + "], scoreBefore="
        + this.scoreBefore
        + '}';
  }

  @Override
  public String toSerializedString() {
    // ascii 97 = a
    char row = (char) (97 + this.startPosition.row());
    String col = String.valueOf(this.startPosition.col() + 1);
    char direction = switch (this.startPosition.direction()) {
      case Right -> 'h';
      case Down -> 'v';
    };
    String word = Letter.concatenate(this.toPlace);
    return this.player.id + " PlaceWord " + row + col + direction + " " + word;
  }

  @Override
  public void undo(GameModel gameModel) {
    gameModel.undoGameAction(this);
  }
}
