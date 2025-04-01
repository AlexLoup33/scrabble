package pdp.scrabble.game.actions;

import pdp.scrabble.game.GameModel;
import pdp.scrabble.game.Player;

/** forfeiting the turn and scoring zero */
public class PassTurnAction extends GameAction {
  public PassTurnAction(Player player, int turn) {
    super(player, turn);
  }

  public static PassTurnAction create(Player player, int turn) {
    return new PassTurnAction(player, turn);
  }

  @Override
  public void applyGameAction(GameModel gameModel) {
    gameModel.applyGameAction(this);
  }

  @Override
  public String toSerializedString() {
    return super.toSerializedString() + " PassTurn";
  }

  @Override
  public void undo(GameModel gameModel) {
    gameModel.undoGameAction(this);
  }

  @Override
  public void redo(GameModel gameModel) {
    gameModel.redoGameAction(this);
  }
}
