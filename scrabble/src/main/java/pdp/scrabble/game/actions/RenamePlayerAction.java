package pdp.scrabble.game.actions;

import pdp.scrabble.game.Player;

public class RenamePlayerAction extends GameAction {
  public final String newName;

  public RenamePlayerAction(Player player, int turn, String newName) {
    super(player, turn);
    this.newName = newName;
  }

  public RenamePlayerAction(Player player, int turn, String newName) {
    super(player, turn);
    this.newName = newName;
  }
}
