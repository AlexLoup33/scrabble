package pdp.scrabble.view;

import pdp.scrabble.dictionary.LettersCounter;
import pdp.scrabble.game.actions.GameAction;
import pdp.scrabble.game.Grid;

public interface IView {
    public GameAction askAction();

    public void displayGrid(Grid grid);

    public void displayPlayerLetters(LettersCounter letters);
}
