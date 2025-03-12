package pdp.scrabble.controller;

import javafx.application.Application;
import pdp.scrabble.Utilities;
import pdp.scrabble.configuration.Configuration;
import pdp.scrabble.errorsHandling.CustomError;
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
      this.view = new ViewCLI(this.model.lang, conf.getSuperScrabble());
    }
  }

  public void gameLoop() {
    int turn = 1;
    CustomError recoverableException = null;

    Utilities.debug('l', "game loop starts\n");

    while (!this.isGameFinished()) {
      Utilities.debug('l', "round number %d\n", turn);
      try {
        Player playing = model.getCurrentActivePlayer();
        Utilities.debug('l', "%s begins his turn\n", playing.getName());
        drawLetters(playing);
        Utilities.debug('l', "numbers of letters in draw : %d\n", model.draw.getCurrentSize());
        view.announceTurn(playing);
        for (Player onGames : model.players) {
          view.showScore(onGames);
        }
        // Implemented but maybe need to draw every letters before
        // actually showing number at turn 1
        // view.displayDraw(model.draw);

        view.displayGrid(model.grid, false);
        view.displayPlayerLetters(playing.bag);
        if (recoverableException != null) {
          view.showError(recoverableException);
        }

        GameAction played = view.askAction(playing, turn); // blocking
        model.applyGameActionDispatch(played);

        // Player 0 is start of table round except when player 0 chose a
        // non turn ending action (eg. rename)
        boolean isRoundOver =
            (model.currentPlayer == 0 && model.getCurrentActivePlayer() != playing);
        if (isRoundOver) turn++;
        Utilities.debug('l', "%s ends his turn\n", playing.getName());
        recoverableException = null;
      } catch (Exception | Error e) {
        recoverableException = handleBadPath(e);
      }
    }
  }

  private void drawLetters(Player playing) {
    if (model.draw.getCurrentSize() < 7) {
      throw new Error("draw letter count is under 7");
      // TODO endgame
    }
    for (int i = playing.bag.size(); i < 7; i++) {
      playing.bag.addLetter(model.draw.popLetter());
    }
  }

  private CustomError handleBadPath(Throwable e) {
    if (e.getClass() == CustomError.class) {
      CustomError custom = (CustomError) e;
      if (custom.isFatal()) {
        terminateProgram(custom);
      } else {
        return custom;
      }
    } else {
      terminateProgram(e);
    }
    return null;
  }

  private void terminateProgram(Throwable e) {
    if (e.getClass() == Exception.class) {
      view.showError((Exception) e);
    } else {
      System.out.println(e);
    }
    System.exit(1);
  }

  private void terminateProgram(CustomError e) {
    view.showError(e);
    System.exit(1);
  }

  public boolean isGameFinished() {
    // TODO compute based of real Scrabble rules
    if (model.draw.getCurrentSize() <= 0) {
      return true;
    }
    return false;
  }
}
