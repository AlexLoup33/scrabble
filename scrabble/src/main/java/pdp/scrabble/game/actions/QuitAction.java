package pdp.scrabble.game.actions;

import pdp.scrabble.game.GameModel;
import pdp.scrabble.game.Player;

public class QuitAction extends GameAction {

  public final boolean isSaveWanted;

  public QuitAction(Player player, int turn, boolean isSaveWanted) {
    super(player, turn);
    this.isSaveWanted = isSaveWanted;
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
