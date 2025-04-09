package pdp.scrabble.game.actions;

import pdp.scrabble.game.GameModel;
import pdp.scrabble.game.Player;

/*With undo to manipulate the history of a game. go to the next action */
public class RedoAction extends GameAction {

  public RedoAction(Player player, int turn) {
    super(player, turn);
  }

  @Override
  public void applyGameAction(GameModel gameModel) {
    gameModel.applyGameAction(this);
  }

  @Override
  public String toSerializedString() {
    throw new UnsupportedOperationException(
        "there is no serialization possible for " + this.getClass().getSimpleName());
  }

  @Override
  public void undo(GameModel gameModel) {
    throw new UnsupportedOperationException(
        "there is no undo possible for " + this.getClass().getSimpleName());
  }
}
