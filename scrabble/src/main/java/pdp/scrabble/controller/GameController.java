package pdp.scrabble.controller;

import javafx.application.Application;
import pdp.scrabble.Utilities;
import pdp.scrabble.configuration.Configuration;
import pdp.scrabble.game.GameModel;
import pdp.scrabble.game.Player;
import pdp.scrabble.game.actions.GameAction;
import pdp.scrabble.view.IView;
import pdp.scrabble.view.ViewCLI;
import pdp.scrabble.view.ViewGUI;

public class GameController {

  private final GameModel model;
  private final IView view;

  public GameController(Configuration conf) {
    this.model = new GameModel(conf);
    if (conf.getGui()) {
      this.view = new ViewGUI();

      ViewGUI.setLang(this.model.lang);

      Application.launch(ViewGUI.class, new String[] {});
    } else {
      this.view = new ViewCLI(this.model.lang);
    }
  }

  public void gameLoop() {
    boolean playDone;
    int turn = 0;

    Utilities.debug('l', "game loop starts\n");

    while (!this.isGameFinished()) {
      Utilities.debug('l', "round number %d\n", turn);

      for (Player playing : model.players) {
        playDone = false;
        Utilities.debug('l', "%s begins his turn\n", playing.getName());
        drawLetters(playing);
        Utilities.debug('l', "numbers of letters in draw : %d\n", model.draw.getCurrentSize());
        view.announceTurn(playing);
        // Implemented but maybe need to draw every letters before
        // actually showing number
        // view.displayDraw(model.draw);

        view.displayGrid(model.grid, false);
        view.displayPlayerLetters(playing.bag);

        while (!playDone) {
          GameAction played = view.askAction(playing, turn); // blocking
          playDone = model.applyGameActionDispatch(played);
        }

        Utilities.debug('l', "%s ends his turn\n", playing.getName());
        turn++;
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

  public boolean isGameFinished() {
    // TODO compute based of real Scrabble rules
    return false;
  }
}
