package pdp.scrabble.game.actions;

import java.util.List;
import pdp.scrabble.game.GameModel;
import pdp.scrabble.game.Player;

/*to restart a game */
public class RestartGameAction extends GameAction {

  // Used by undo
  public List<GameAction> precedingGame; // set by model

  public RestartGameAction(Player player, int turn) {
    super(player, turn);
    this.precedingGame = null;
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
