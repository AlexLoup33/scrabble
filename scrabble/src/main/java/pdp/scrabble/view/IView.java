package pdp.scrabble.view;

import java.util.List;
import pdp.scrabble.dictionary.LettersCounter;
import pdp.scrabble.errorsHandling.CustomError;
import pdp.scrabble.game.Grid;
import pdp.scrabble.game.LettersDrawer;
import pdp.scrabble.game.Player;
import pdp.scrabble.game.actions.GameAction;

public interface IView {
  public GameAction askAction(Player player, Integer turn);

  public void displayGrid(Grid grid, Boolean isSuperScrabble);

  public void displayPlayerLetters(LettersCounter letters);

  public String getFromLangPack(String identifiant);

  public void showScore(Player player);

  public void showScore(List<Player> players);

  public void displayDraw(LettersDrawer draw);

  public void announceTurn(Player currentPlayer);

  public void showError(CustomError e);

  public void showError(Exception e);

  // public void showAbout(); //FIXME: J'ai pas compris alors help me
}
