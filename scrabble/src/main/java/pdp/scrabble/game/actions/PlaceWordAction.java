package pdp.scrabble.game.actions;

import pdp.scrabble.dictionary.Letter;
import pdp.scrabble.game.GridWordPos;
import pdp.scrabble.game.Player;

/**
 * Play at least one tile on the board, adding the value of all words formed to the player's
 * cumulative score
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
}
