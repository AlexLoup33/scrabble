package pdp.scrabble.game.actions;

import pdp.scrabble.game.Player;

/**
 * forfeiting the turn and scoring zero
 */
public class PassTurnAction extends GameAction {

    protected PassTurnAction(Player player, int turn) {
        super(player, turn);
    }

}
