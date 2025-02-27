package pdp.scrabble.game.actions;

import java.util.List;

import pdp.scrabble.dictionary.Letter;
import pdp.scrabble.dictionary.LettersCounter;
import pdp.scrabble.game.Player;

/**
 * Exchange one or more tiles for an equal number from the bag, scoring zero.
 * This can only be done
 * if 7 or more tiles remain in the bag.
 */
public class ExchangeLetterAction extends GameAction {
    public final LettersCounter lettersToExchange;

    // the letters to remove from the players hand

    public ExchangeLetterAction(Player player, int turn, List<Letter> toExchange) {
        super(player, turn);
        this.lettersToExchange = new LettersCounter(toExchange);
    }
}
