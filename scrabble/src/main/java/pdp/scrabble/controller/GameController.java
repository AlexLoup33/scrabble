package pdp.scrabble.controller;

import pdp.scrabble.Utilities;
import pdp.scrabble.configuration.Configuration;
import pdp.scrabble.game.GameModel;
import pdp.scrabble.game.Player;
import pdp.scrabble.game.actions.GameAction;
import pdp.scrabble.view.IView;
import pdp.scrabble.view.ViewCLI;

public class GameController {

  private GameModel model;
  private IView view;

  public GameController(Configuration conf) {
    this.model = new GameModel(conf);
    this.view = new ViewCLI(this.model.lang);
  }

  public void gameLoop() {
    boolean playDone;
    // We only play 3 table rounds for now
    // Need to compute an end game condition to go beyond
    Utilities.debug('l', "game loop starts\n");
    for (int turn = 0; turn < 3; turn++) {
      Utilities.debug('l', "round number %d\n", turn);
      for (Player playing : model.players) {
        playDone = false;
        Utilities.debug('l', "%s begins his turn\n", playing.getName());
        drawLetters(playing);
        Utilities.debug('l', "numbers of letters in draw : %d\n", model.draw.getCurrentSize());
        // view.announceTurn(Player cur); NOT IMPLEMENTED
        // view.displayDraw(); NOT IMPLEMENTED
        view.displayGrid(model.grid, false);
        view.displayPlayerLetters(playing.bag);
        while (!playDone) {
          GameAction played = view.askAction(playing, turn); // blocking
          playDone = model.applyGameAction(played);
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
