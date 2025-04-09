package pdp.scrabble.game.actions;

import pdp.scrabble.game.GameModel;
import pdp.scrabble.game.Player;

/** forfeiting the turn and scoring zero */
public class PassTurnAction extends GameAction {
  public PassTurnAction(Player player, int turn) {
    super(player, turn);
  }

  @Override
  public void applyGameAction(GameModel gameModel) {
    gameModel.applyGameAction(this);
  }

  @Override
  public String toSerializedString() {
    return this.player.id + " PassTurn";
  }

  @Override
  public void undo(GameModel gameModel) {
    gameModel.undoGameAction(this);
  }
}
