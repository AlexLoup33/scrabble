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
    return "";
  }
}
