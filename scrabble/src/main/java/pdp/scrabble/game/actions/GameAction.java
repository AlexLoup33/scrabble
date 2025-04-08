package pdp.scrabble.game.actions;

import pdp.scrabble.game.GameModel;
import pdp.scrabble.game.Player;

public abstract class GameAction {
  public final Player player;
  public final int turn;

  /** whether the action is from a redo */
  public boolean fromRedo;

  /** to be setted durring the dispatch of the game action (in seconds) */
  public long duration;

  protected GameAction(Player player, int turn) {
    this.player = player;
    this.turn = turn;
    this.duration = 0;
    this.fromRedo = false;
  }

  public String toString() {
    return this.getClass().getSimpleName() + "{" + "player=" + player + "," + "turn=" + turn;
  }

  public void undoTurnPlayedTime() {
    this.player.undoTurnPlayedTime(this);
  }

  public abstract void applyGameAction(GameModel gameModel);

  public abstract String toSerializedString();

  public abstract void undo(GameModel gameModel);
}
