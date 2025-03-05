package pdp.scrabble.game.actions;

import pdp.scrabble.game.GameModel;
import pdp.scrabble.game.Player;

public abstract class GameAction {
    public final Player player;
    public final int turn;

    protected GameAction(Player player, int turn) {
        this.player = player;
        this.turn = turn;
    }

    public abstract boolean applyGameAction(GameModel gameModel);
}
