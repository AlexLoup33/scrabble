package pdp.scrabble.game.actions;

import pdp.scrabble.game.GameModel;
import pdp.scrabble.game.Player;

public class RenamePlayerAction extends GameAction {
  public final String newName;

  public RenamePlayerAction(Player player, int turn, String newName) {
    super(player, turn);
    this.newName = newName;
  }
  @Override
  public boolean applyGameAction(GameModel gameModel) {
    return gameModel.applyGameAction(this);
  }
}
