package pdp.scrabble.game.actions;

import java.util.List;
import java.util.Map;
import pdp.scrabble.dictionary.Letter;
import pdp.scrabble.dictionary.LettersCounter;
import pdp.scrabble.errorsHandling.CustomError;
import pdp.scrabble.errorsHandling.ErrorType;
import pdp.scrabble.game.GameModel;
import pdp.scrabble.game.LettersDrawer;
import pdp.scrabble.game.Player;

/**
 * Exchange one or more tiles for an equal number from the bag, scoring zero. This can only be done
 * if 7 or more tiles remain in the bag.
 */
public class ExchangeLetterAction extends GameAction {
  // the letters to remove from the players hand
  public final LettersCounter lettersToExchange;

  // Used by undo
  public long rngStateBefore = 0L;
  public LettersCounter lettersDrawed = null; // Empty until model calls drawLetters()

  public ExchangeLetterAction(Player player, int turn, List<Letter> toExchange) {
    super(player, turn);
    this.lettersToExchange = new LettersCounter(toExchange);
  }

  @Override
  public void applyGameAction(GameModel gameModel) {
    gameModel.applyGameAction(this);
  }

  public void clearUndoInfos() {
    this.rngStateBefore = 0L;
    this.lettersDrawed = null;
  }

  /** assert the player has the required letters in his bag */
  public void assertHasLettersToPlace() {
    LettersCounter missing = new LettersCounter();

    for (Letter letter : this.lettersToExchange.getLettersSet()) {
      int nbToExchange = this.lettersToExchange.getLetterCount(letter);
      int nbInBag = this.player.bag.getLetterCount(letter);
      if (nbToExchange > nbInBag) {
        // => not enougth letters to exchange
        missing.addLetter(letter, nbToExchange - nbInBag);
      }
    }
    if (missing.size() != 0) {
      throw new CustomError(
          ErrorType.PlayerDoesntHaveLettersInIsBag,
          "the following letters: " + missing + " are missing",
          this.player.getName()
              + " doesn't have the following letters in his bag: "
              + missing
              + " in is bag");
    }
  }

  /**
   * remove from the bag the letters exchnaged and draw the new letters, you must check if the
   * player has the letters before ! (it will be asserted before removing the letters)
   *
   * @param draw the drawer of the GameModel
   */
  public void exchangeLetters(LettersDrawer draw) {
    this.assertHasLettersToPlace(); // avoid breaking anything
    // remove the letters from the player's bag and put them in the draw
    for (Letter letter : this.lettersToExchange.getLettersSet()) {
      int nbToExchange = this.lettersToExchange.getLetterCount(letter);
      this.player.bag.removeLetter(letter, nbToExchange);
      draw.pushLetter(letter, nbToExchange);
    }
    // draw the new letters and put them in the player's bag
    LettersCounter drawedLetters = draw.popLetter(this.lettersToExchange.size());
    this.lettersDrawed = drawedLetters.copy();

    for (Letter letter : drawedLetters.getLettersSet()) {
      int nbDrawed = drawedLetters.getLetterCount(letter);
      this.player.bag.addLetter(letter, nbDrawed);
    }
  }

  @Override
  public String toSerializedString() {
    StringBuilder toExchange = new StringBuilder();
    for (Map.Entry<Letter, Integer> l : this.lettersToExchange.getTable().entrySet()) {
      for (int i = 0; i < l.getValue(); i++) {
        toExchange.append(l.getKey() + ",");
      }
    }
    if (toExchange.length() > 0) toExchange.deleteCharAt(toExchange.length() - 1);
    return this.player.id + " ExchangeLetter [" + toExchange.toString() + "]";
  }

  @Override
  public void undo(GameModel gameModel) {
    gameModel.undoGameAction(this);
  }
}
