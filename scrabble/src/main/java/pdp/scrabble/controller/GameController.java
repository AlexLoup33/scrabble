package pdp.scrabble.controller;

import java.util.concurrent.CountDownLatch;

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
    private IView view;

    public GameController(Configuration conf) {
        this.model = new GameModel(conf);
        if (conf.getGui()) {
            CountDownLatch latch = new CountDownLatch(1);

            // Lancer JavaFX dans un thread séparé
            new Thread(
                    () -> {
                        ViewGUI.setLang(this.model.lang);
                        ViewGUI.setSuperScrabble(conf.getSuperScrabble());
                        ViewGUI.launchJavaFX(latch); // Démarrer JavaFX
                    })
                    .start();

            try {
                latch.await(); // Attendre que JavaFX soit prêt
                view = ViewGUI.getInstance(); // Récupérer l'instance de la vue
                view.initialize(conf); // Initialiser la vue
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            this.view = new ViewCLI(this.model.lang, conf.getSuperScrabble());
        }
    }

    public void gameLoop() {
        CustomError recoverableException = null;

        Utilities.debug('l', "GameLoop start\n");

        while (!this.isGameFinished()) {
            int turn = model.getTurn();
            Utilities.debug('l', "round number %d\n", turn);
            try {
                Player playing = model.getCurrentPlayer();
                Utilities.debug('l', "%s begins his turn\n", playing.getName());
                drawLetters(playing);
                Utilities.debug('l', "numbers of letters in draw: %d\n", model.draw.getCurrentSize());
                view.announceTurn(playing, turn);
                for (Player onGames : model.players) {
                    view.showScore(onGames);
                }
                // Implemented but maybe need to draw every letter before
                // actually showing number at turn 1
                // view.displayDraw(model.draw);
                view.displayGrid(model.grid, this.model.config.getSuperScrabble());
                view.displayPlayerLetters(playing.bag);
                if (recoverableException != null) {
                    view.showError(recoverableException);
                }

                GameAction played = view.askAction(playing, turn); // blocking
                model.applyGameActionDispatch(played);

                // Player 0 is a start of table round except when player 0 chose a
                // non-turn ending action (e.g., rename)

                Utilities.debug('l', "%s ends his turn\n", playing.getName());
                recoverableException = null;
            } catch (Exception | Error e) {
                recoverableException = handleBadPath(e);
            }
        }
    }

    private void drawLetters(Player playing) {
        if (model.draw.getCurrentSize() < 7) {
            throw new Error("[BUG] draw letter count is strictly under 7");
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
        view.showError(e);
        System.exit(1);
    }

    private void terminateProgram(CustomError e) {
        view.showError(e);
        System.exit(1);
    }

    public boolean isGameFinished() {
        // TODO compute based of real Scrabble rules

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
