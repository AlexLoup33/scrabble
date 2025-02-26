package pdp.scrabble.view;

import pdp.scrabble.dictionary.LettersCounter;
import pdp.scrabble.game.Grid;
import pdp.scrabble.game.Player;
import pdp.scrabble.game.actions.GameAction;

public interface IView {
    public GameAction askAction(Player player) throws Exception;

    public void displayGrid(Grid grid, Boolean isSuperScrabble);

<<<<<<< HEAD
  public void displayPlayerLetters(LettersCounter letters);

  public String getLangPack(String identifiant);
=======
    public void displayPlayerLetters(LettersCounter letters);
>>>>>>> cde428357562aeef88c7265a2cb258d5a7a41ed1
}
