package pdp.scrabble.controller;

import pdp.scrabble.Utilities;
import pdp.scrabble.game.GameModel;
import pdp.scrabble.game.Player;
import pdp.scrabble.game.actions.GameAction;
import pdp.scrabble.view.IView;

public class GameController {

  private GameModel model;
  private IView view;

  public GameController(GameModel model, IView view) {
    this.model = model;
    this.view = view;
  }

  public void gameLoop() {
    boolean playDone;
    // We only play 3 table rounds for now
    // Need to compute an end game condition to go beyond
    Utilities.debug('l', "game loop starts\n");
    for (int i = 0; i < 3; i++) {
      Utilities.debug('l', "round number %d\n", i);
      for (Player playing : model.players) {
        playDone = false;
        Utilities.debug('l', "%s begins his turn\n", playing.getName());
        drawLetters(playing);
        // TODO draw.currentSize is not really the numbers of letters in draw
        Utilities.debug('l', "numbers of letters in draw : %d\n", model.draw.getCurrentSize());
        // view.announceTurn(Player cur); NOT IMPLEMENTED
        // view.displayDraw(); NOT IMPLEMENTED
        view.displayGrid(model.grid, false);
        view.displayPlayerLetters(playing.bag);
        while (!playDone) {
          try {
            GameAction played = view.askAction(playing); // blocking
            // TODO : Check validity of played
            playDone = model.applyGameAction(played);
          } catch (Exception e) {
            System.out.println(e);
          }
        }
        Utilities.debug('l', "%s ends his turn\n", playing.getName());
      }
    }
  }

  private void drawLetters(Player playing) {
    if (model.draw.getCurrentSize() < 7) {
      throw new Error("draw letter count is under 7");
      // TODO endgame
    }
    for (int i = playing.bag.sumValues(); i < 7; i++) {
      playing.bag.addLetter(model.draw.popLetter());
    }
  }
}
