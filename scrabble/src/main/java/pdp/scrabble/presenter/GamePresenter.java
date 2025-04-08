package pdp.scrabble.presenter;

import pdp.scrabble.Utilities;
import pdp.scrabble.configuration.Configuration;
import pdp.scrabble.errorsHandling.CustomError;
import pdp.scrabble.errorsHandling.ErrorType;
import pdp.scrabble.game.GameModel;
import pdp.scrabble.game.Player;
import pdp.scrabble.game.actions.GameAction;
import pdp.scrabble.game.serializer.Serializer;
import pdp.scrabble.view.CLI.ViewCLI;
import pdp.scrabble.view.IView;

public class GamePresenter {

  private final GameModel model;
  private final IView view;

  public GamePresenter(Configuration curConf) {
    if (curConf.getHistoryFile() != null) {
      this.model = Serializer.loadModelFromFile(curConf.getHistoryFile());
    } else {
      this.model = new GameModel(curConf);
    }
    // curConf -> scrabblerc + cli args
    // conf -> is this game restored from save ?
    //  - Yes : conf from old game
    //  - No  : conf == curConf
    Configuration conf = this.model.config;
    if (curConf.getGui()) {
      throw new CustomError(null, null, null);
    } else {
      this.view = new ViewCLI(this.model.lang, conf.getSuperScrabble());
    }
  }

  public void gameLoop() {
    CustomError recoverableException = null;

    Utilities.debug('l', "GameLoop start\n");
    Utilities.verbose("Mode blitz: %s\n", this.model.config.getBlitz());
    Utilities.verbose("Blitz time: %s\n", this.model.config.blitzTime());
    Utilities.verbose("Mode super scrabble: %s\n", this.model.config.getSuperScrabble());

    while (!this.isGameFinished()) {
      int turn = model.getTurn();
      Utilities.debug('l', "round number %d\n", turn);
      try {
        Player playing = model.getCurrentPlayer();
        Utilities.debug('l', "%s begins his turn\n", playing.getName());
        Utilities.debug('l', "numbers of letters in draw: %d\n", model.draw.getCurrentSize());
        view.announceTurn(playing, turn);
        if (this.model.config.getBlitz()) {
          view.showRemainingTimes(model.players);
        }
        for (Player onGames : model.players) {
          view.showScore(onGames);
        }
        view.displayGrid(model.grid, this.model.config.getSuperScrabble());
        view.displayPlayerLetters(playing.bag);
        if (recoverableException != null) {
          view.showError(recoverableException);
        }
        Utilities.debug('l', "Attempt to askAction\n");

        long startTime = System.currentTimeMillis();

        GameAction ga = view.askAction(playing, turn);
        long timeElapsed = (System.currentTimeMillis() - startTime) / 1000;
        this.model.applyGameActionDispatch(ga, timeElapsed);
        Utilities.debug('l', "Action received\n");
        Utilities.verbose("Player took %ss to play\n", timeElapsed);

        // Player 0 is a start of table round except when player 0 chose a
        // non-turn ending action (e.g., rename)

        Utilities.debug('l', "%s ends his turn\n", playing.getName());
        recoverableException = null;
      } catch (CustomError e) {
        this.handleErrors(e);
      } catch (Throwable e) {
        this.handleErrors(e);
      }
    }
  }

  private void handleErrors(Throwable e) {
    this.view.showError(e);
    throw new CustomError(
        ErrorType.ExitWithError,
        "couldn't handle the error",
        "the error: " + e + " caused an exit");
  }

  private void handleErrors(CustomError e) {
    if (e.type == ErrorType.ExitNoError || e.type == ErrorType.ExitWithError) {
      throw e; // re-throw exits
    }
    this.view.showError(e);
    if (e.isFatal()) {
      throw new CustomError(
          ErrorType.ExitWithError,
          "couldn't handle the error",
          "the error: " + e + " caused an exit");
    }
  }

  public boolean isGameFinished() {
    // test if there are enough letters to be drawn
    if (model.draw.getCurrentSize() < 7) {
      return true;
    }
    // test if enough players remain
    if (this.model.getActivePlayers().size() <= 1) {
      return true;
    }
    // test: 3 game turn with everyone passing
    if (this.model.nbEveryonePassedTurn() >= 3) {
      return true;
    }

    return false; // the game isn't finished
  }
}
