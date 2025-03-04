package pdp.scrabble.view;

import java.util.List;
import pdp.scrabble.dictionary.LettersCounter;
import pdp.scrabble.errorsHandling.CustomError;
import pdp.scrabble.game.Grid;
import pdp.scrabble.game.Lang;
import pdp.scrabble.game.Player;
import pdp.scrabble.game.actions.GameAction;

public class ViewGUI implements IView {

  public ViewGUI(Lang lang) {
    // TODO Auto-generated constructor stub
    throw new UnsupportedOperationException("Unimplemented constructor");
  }

  @Override
  public GameAction askAction(Player player, Integer turn) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'askAction'");
  }

  @Override
  public void displayGrid(Grid grid, Boolean isSuperScrabble) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'displayGrid'");
  }

  @Override
  public void displayPlayerLetters(LettersCounter letters) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'displayPlayerLetters'");
  }

  @Override
  public String getFromLangPack(String identifiant) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getFromLangPack'");
  }

  @Override
  public void showScore(Player player) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'showScore'");
  }

  @Override
  public void showScore(List<Player> players) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'showScore'");
  }

  @Override
  public void displayDrawer(LettersCounter draw) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'displayDrawer'");
  }

  @Override
  public void announceTurn(Player currentPlayer) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'announceTurn'");
  }

  @Override
  public void showError(CustomError e) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'showError'");
  }

  @Override
  public void showError(Exception e) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'showError'");
  }
}
