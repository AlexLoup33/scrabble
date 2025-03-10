package pdp.scrabble.game.actions;

import pdp.scrabble.dictionary.Letter;
import pdp.scrabble.dictionary.LettersCounter;
import pdp.scrabble.errorsHandling.CustomError;
import pdp.scrabble.errorsHandling.ErrorType;
import pdp.scrabble.game.GameModel;
import pdp.scrabble.game.GridWordPos;
import pdp.scrabble.game.Player;

/**
 * Play at least one tile on the board, adding the value of all words formed to
 * the player's cumulative score
 */
public class PlaceWordAction extends GameAction {
  public final Letter[] toPlace;
  // the letters of the player to place from
  public final GridWordPos startPosition;

  public PlaceWordAction(Player player, int turn, Letter[] toPlace, GridWordPos startPos) {
    super(player, turn);
    this.toPlace = toPlace.clone();
    this.startPosition = startPos;
  }

  @Override
  public boolean applyGameAction(GameModel gameModel) {
    return gameModel.applyGameAction(this);
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

  /**
   * assert the player has the required letters in his bag
   */
  public void assertHasLettersToPlace() {
    LettersCounter missing = this.getMissingLetters();

    // check if the jokers can fill the missing letters
    int nbJokerInBag = this.player.bag.getLetterCount(Letter.joker());
    int nbMissingLetters = missing.size();
    if (nbMissingLetters > nbJokerInBag) {
      throw new CustomError(
          ErrorType.PlayerDoesntHaveLettersInIsBag,
          "the following letters: " + missing.toString() + " are missing (before jokers)",
          this.player.getName() + " doesn't have the following letters in his bag: "
              + missing.toString() + " in is bag (before appling jokers)");
    }
  }

  /**
   * remove from the bag the letters placed
   * you must check if the player has the letters before !
   * (it will be asserted before removing the letters)
   * 
   * @param pwa the action played
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
}
