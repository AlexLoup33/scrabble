package pdp.scrabble.view;

import pdp.scrabble.dictionary.LettersCounter;
import pdp.scrabble.game.Grid;
import pdp.scrabble.game.actions.GameAction;

public interface IView {
  public GameAction askAction();

  public void displayGrid(Grid grid, Boolean isSuperScrabble);

  public void displayPlayerLetters(LettersCounter letters);
}
