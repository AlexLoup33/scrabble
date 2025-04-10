package pdp.scrabble.game;

import pdp.scrabble.game.actions.GameAction;
import pdp.scrabble.game.actions.PassTurnAction;

public class AI extends Player {

  public AI(int nextPlayerID, int timePerPlayer) {
    super("Player[AI]", nextPlayerID, timePerPlayer, true);
  }

  public GameAction getAction(GameModel gm) {
    return new PassTurnAction(this, gm.getTurn());
  }
}
