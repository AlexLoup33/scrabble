package pdp.scrabble.game.actions;

import pdp.scrabble.game.GameModel;
import pdp.scrabble.game.Player;

public class RestartGameAction extends GameAction {

    public RestartGameAction(Player player, int turn) {
        super(player, turn);
    }

    @Override
    public boolean applyGameAction(GameModel gameModel) {
        return gameModel.applyGameAction(this);
    }
}
