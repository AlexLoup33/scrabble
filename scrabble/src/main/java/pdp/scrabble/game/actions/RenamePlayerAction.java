package pdp.scrabble.game.actions;

import pdp.scrabble.game.GameModel;
import pdp.scrabble.game.Player;

public class RenamePlayerAction extends GameAction {
  public final String newName;

  // Used by undo
  public final String oldName;

  public RenamePlayerAction(Player player, int turn, String newName) {
    super(player, turn);
    this.newName = newName;
    this.oldName = player.getName();
  }

  @Override
  public void applyGameAction(GameModel gameModel) {
    gameModel.applyGameAction(this);
  }

  @Override
  public String toSerializedString() {
    return super.toSerializedString() + " RenamePlayer " + "\"" + this.newName + "\"";
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
