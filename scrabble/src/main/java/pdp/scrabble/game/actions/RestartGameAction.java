package pdp.scrabble.game.actions;

import java.util.List;
import pdp.scrabble.game.GameModel;
import pdp.scrabble.game.Player;

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
    return super.toSerializedString() + " RestartGame";
  }
}
