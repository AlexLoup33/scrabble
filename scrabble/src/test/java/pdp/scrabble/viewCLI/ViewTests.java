package pdp.scrabble.viewCLI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import org.junit.Before;
import org.junit.Test;
import pdp.scrabble.MainTest;
import pdp.scrabble.Utilities;
import pdp.scrabble.configuration.Configuration;
import pdp.scrabble.dictionary.Letter;
import pdp.scrabble.errorsHandling.CustomError;
import pdp.scrabble.errorsHandling.ErrorType;
import pdp.scrabble.game.GameModel;
import pdp.scrabble.game.actions.GameAction;
import pdp.scrabble.game.actions.QuitAction;
import pdp.scrabble.game.actions.RedoAction;
import pdp.scrabble.game.actions.RestartGameAction;
import pdp.scrabble.game.actions.UndoAction;
import pdp.scrabble.view.CLI.ViewCLI;

public class ViewTests {

  public static void assertThrowsCustom(ErrorType errType, Runnable test) {
    MainTest.assertThrowsCustom(errType, test);
  }

  public static Letter[] lettersFromString(String s) {
    return MainTest.lettersFromString(s);
  }

  @Before
  public void setUp() {
    Utilities.logger.setRedirectState(false);
  }

  @Test
  public void testsOnNormalGame() {
    // on a (non superscrable, 3 players)
    Configuration config =
        new Configuration(
            true,
            "",
            false,
            false,
            false,
            3,
            15,
            new boolean[3],
            15,
            2,
            46,
            "fr",
            "idk",
            null,
            java.nio.file.Paths.get("none"),
            java.nio.file.Paths.get("none"));

    GameModel gameModel = new GameModel(config);
    ViewCLI view = new ViewCLI(gameModel.lang, config.getSuperScrabble());
    String expectedOutput;

    // tests on a brand new game
    // test announceTurn
    view.announceTurn(gameModel.getCurrentPlayer(), gameModel.getTurn());
    assertEquals("le joueur Player0 commence son tour 1\n", Utilities.logger.loggedOuts.getFirst());

    // test showRemainingTimes
    view.showRemainingTimes(gameModel.players);
    assertEquals("temps restant pour Player0: 15'00\"\n", Utilities.logger.loggedOuts.get(2));
    assertEquals("temps restant pour Player1: 15'00\"\n", Utilities.logger.loggedOuts.get(1));
    assertEquals("temps restant pour Player2: 15'00\"\n", Utilities.logger.loggedOuts.get(0));

    // test showScores
    view.showScores(gameModel.players);
    assertEquals("le score de Player0 est 0\n", Utilities.logger.loggedOuts.get(2));
    assertEquals("le score de Player1 est 0\n", Utilities.logger.loggedOuts.get(1));
    assertEquals("le score de Player2 est 0\n", Utilities.logger.loggedOuts.get(0));

    // test printing the empty grid
    view.displayGrid(gameModel.grid, false);
    expectedOutput =
        "  "
            + """
            1  2  3  4  5  6  7  8  9 10 11 12 13 14 15
            a [45m_[0m  _[0m  _[0m  [46m_[0m  _[0m  _[0m  _[0m  [45m_[0m  _[0m  _[0m  _[0m  [46m_[0m  _[0m  _[0m  [45m_[0m
            b _[0m  [41m_[0m  _[0m  _[0m  _[0m  [44m_[0m  _[0m  _[0m  _[0m  [44m_[0m  _[0m  _[0m  _[0m  [41m_[0m  _[0m
            c _[0m  _[0m  [41m_[0m  _[0m  _[0m  _[0m  [46m_[0m  _[0m  [46m_[0m  _[0m  _[0m  _[0m  [41m_[0m  _[0m  _[0m
            d [46m_[0m  _[0m  _[0m  [41m_[0m  _[0m  _[0m  _[0m  [46m_[0m  _[0m  _[0m  _[0m  [41m_[0m  _[0m  _[0m  [46m_[0m
            e _[0m  _[0m  _[0m  _[0m  [41m_[0m  _[0m  _[0m  _[0m  _[0m  _[0m  [41m_[0m  _[0m  _[0m  _[0m  _[0m
            f _[0m  [44m_[0m  _[0m  _[0m  _[0m  [44m_[0m  _[0m  _[0m  _[0m  [44m_[0m  _[0m  _[0m  _[0m  [44m_[0m  _[0m
            g _[0m  _[0m  [46m_[0m  _[0m  _[0m  _[0m  [46m_[0m  _[0m  [46m_[0m  _[0m  _[0m  _[0m  [46m_[0m  _[0m  _[0m
            h [45m_[0m  _[0m  _[0m  [46m_[0m  _[0m  _[0m  _[0m  [41m_[0m  _[0m  _[0m  _[0m  [46m_[0m  _[0m  _[0m  [45m_[0m
            i _[0m  _[0m  [46m_[0m  _[0m  _[0m  _[0m  [46m_[0m  _[0m  [46m_[0m  _[0m  _[0m  _[0m  [46m_[0m  _[0m  _[0m
            j _[0m  [44m_[0m  _[0m  _[0m  _[0m  [44m_[0m  _[0m  _[0m  _[0m  [44m_[0m  _[0m  _[0m  _[0m  [44m_[0m  _[0m
            k _[0m  _[0m  _[0m  _[0m  [41m_[0m  _[0m  _[0m  _[0m  _[0m  _[0m  [41m_[0m  _[0m  _[0m  _[0m  _[0m
            l [46m_[0m  _[0m  _[0m  [41m_[0m  _[0m  _[0m  _[0m  [46m_[0m  _[0m  _[0m  _[0m  [41m_[0m  _[0m  _[0m  [46m_[0m
            m _[0m  _[0m  [41m_[0m  _[0m  _[0m  _[0m  [46m_[0m  _[0m  [46m_[0m  _[0m  _[0m  _[0m  [41m_[0m  _[0m  _[0m
            n _[0m  [41m_[0m  _[0m  _[0m  _[0m  [44m_[0m  _[0m  _[0m  _[0m  [44m_[0m  _[0m  _[0m  _[0m  [41m_[0m  _[0m
            o [45m_[0m  _[0m  _[0m  [46m_[0m  _[0m  _[0m  _[0m  [45m_[0m  _[0m  _[0m  _[0m  [46m_[0m  _[0m  _[0m  [45m_[0m\n""";

    assertEquals(expectedOutput, Utilities.logger.loggedOuts.getFirst());
    // test displayPlayerLetters
    view.displayPlayerLetters(gameModel.getCurrentPlayer().bag);
    assertEquals(
        "lettres du joueur: [ A | L | V | T | D | D | M ]\n",
        Utilities.logger.loggedOuts.getFirst());
    // test show errors
    Utilities.logger.setRedirectState(true);
    view.showError(new Throwable("wololo"));
    assertTrue(
        Utilities.logger
            .loggedOuts
            .getFirst()
            .startsWith("\u001b[31mjava.lang.Throwable: wololo\n\tat pdp.scrabble."));
    view.showError(new CustomError(ErrorType.ExitNoError, "pomme", "de terre ^^"));
    assertTrue(
        Utilities.logger
            .loggedOuts
            .getFirst()
            .startsWith(
                "\u001b[31mErrorType: ExitNoError\nMessage: pomme\n"
                    + "Details: de terre ^^\n\tat pdp.scrabble."));
    view.showError(new CustomError(ErrorType.InvalidePlaceLetterAction, "poire", "banana :)"));
    assertEquals(
        "\u001B[33mError: ErrorType: InvalidePlaceLetterAction\n"
            + "Message: poire\nDetails: banana :)\u001B[0m\n",
        Utilities.logger.loggedOuts.getFirst());
    Utilities.logger.setRedirectState(false);

    // test show errors
    view.displayDraw(gameModel.draw);
    assertEquals("Nombre de lettres dans la pioche: 81\n", Utilities.logger.loggedOuts.getFirst());

    assertThrowsCustom(
        ErrorType.InvalidLangPackIdentifier,
        () -> {
          view.getFromLangPack("ca ca existe pas vraiment en fait ^^");
        });
  }

  @Test
  public void testsAskAction() {
    Configuration config =
        new Configuration(
            true,
            "",
            true,
            false,
            false,
            4,
            30,
            new boolean[4],
            15,
            2,
            4846,
            "en",
            "idk",
            null,
            java.nio.file.Paths.get("none"),
            java.nio.file.Paths.get("none"));

    String inputs =
        "blabla\n"
            + "placeWord\nh8h SODOM\n"
            + "placeWord\nh12v MOBILES\n"
            + "placeWord\nCPABON\n"
            + "placeWord\na45h MOBILES\n"
            + "placeWord\n_1h MOBILES\n"
            + "placeWord\na1u MOBILES\n"
            + "passTurn\n"
            + "forfeitPlayer\n"
            + "restartGame\n"
            + "quit\nN\n"
            + "undo\ny\n"
            + "redo\ny\n"
            + "exchangeLetter\nEKIRE#\n"
            + "renamePlayer\nBLIPOILLE\n";
    GameModel gameModel = new GameModel(config);
    ViewCLI view =
        new ViewCLI(
            gameModel.lang, config.getSuperScrabble(), new ByteArrayInputStream(inputs.getBytes()));

    // failing action case
    assertThrowsCustom(
        ErrorType.InvalidActionType,
        () -> {
          view.askAction(gameModel.getCurrentPlayer(), gameModel.getTurn());
        });

    // placeWord
    GameAction ga = view.askAction(gameModel.getCurrentPlayer(), gameModel.getTurn());
    assertEquals("0 PlaceWord h8h SODOM", ga.toSerializedString());
    gameModel.applyGameActionDispatch(ga, 1);

    // placeWord
    ga = view.askAction(gameModel.getCurrentPlayer(), gameModel.getTurn());
    assertEquals("1 PlaceWord h12v MOBILES", ga.toSerializedString());
    gameModel.applyGameActionDispatch(ga, 1);

    // failing placeWord case
    for (int i = 0; i < 4; i++) {
      assertThrowsCustom(
          ErrorType.InvalidMoveFormat,
          () -> {
            view.askAction(gameModel.getCurrentPlayer(), gameModel.getTurn());
          });
    }

    // passTurn
    ga = view.askAction(gameModel.getCurrentPlayer(), gameModel.getTurn());
    assertEquals("2 PassTurn", ga.toSerializedString());
    gameModel.applyGameActionDispatch(ga, 1);

    // forfeitPlayer
    ga = view.askAction(gameModel.getCurrentPlayer(), gameModel.getTurn());
    assertEquals("3 ForfeitGame", ga.toSerializedString());
    gameModel.applyGameActionDispatch(ga, 1);

    // restartGame
    ga = view.askAction(gameModel.getCurrentPlayer(), gameModel.getTurn());
    assertEquals(RestartGameAction.class, ga.getClass());

    // quit
    ga = view.askAction(gameModel.getCurrentPlayer(), gameModel.getTurn());
    assertEquals(QuitAction.class, ga.getClass());

    // undo
    ga = view.askAction(gameModel.getCurrentPlayer(), gameModel.getTurn());
    assertEquals(UndoAction.class, ga.getClass());

    // redo
    ga = view.askAction(gameModel.getCurrentPlayer(), gameModel.getTurn());
    assertEquals(RedoAction.class, ga.getClass());

    // exchange letter
    ga = view.askAction(gameModel.getCurrentPlayer(), gameModel.getTurn());
    assertEquals("0 ExchangeLetter [K,I,R,E,E,$]", ga.toSerializedString());
    gameModel.applyGameActionDispatch(ga, 1);

    // rename
    ga = view.askAction(gameModel.getCurrentPlayer(), gameModel.getTurn());
    assertEquals("1 RenamePlayer \"BLIPOILLE\"", ga.toSerializedString());
    gameModel.applyGameActionDispatch(ga, 1);

    // test printing the empty grid
    view.displayGrid(gameModel.grid, false);
    String expectedOutput =
        "  1  2  3  4  5  6  7  8  9 10 11 12 13 14 15\n"
            + "a [45m_[0m  _[0m  _[0m  [46m_[0m  _[0m  _[0m  _[0m  [45m_[0m  _[0m  _[0m  _[0m  [46m_[0m  _[0m  _[0m  [45m_[0m\n"
            + "b _[0m  [41m_[0m  _[0m  _[0m  _[0m  [44m_[0m  _[0m  _[0m  _[0m  [44m_[0m  _[0m  _[0m  _[0m  [41m_[0m  _[0m\n"
            + "c _[0m  _[0m  [41m_[0m  _[0m  _[0m  _[0m  [46m_[0m  _[0m  [46m_[0m  _[0m  _[0m  _[0m  [41m_[0m  _[0m  _[0m\n"
            + "d [46m_[0m  _[0m  _[0m  [41m_[0m  _[0m  _[0m  _[0m  [46m_[0m  _[0m  _[0m  _[0m  [41m_[0m  _[0m  _[0m  [46m_[0m\n"
            + "e _[0m  _[0m  _[0m  _[0m  [41m_[0m  _[0m  _[0m  _[0m  _[0m  _[0m  [41m_[0m  _[0m  _[0m  _[0m  _[0m\n"
            + "f _[0m  [44m_[0m  _[0m  _[0m  _[0m  [44m_[0m  _[0m  _[0m  _[0m  [44m_[0m  _[0m  _[0m  _[0m  [44m_[0m  _[0m\n"
            + "g _[0m  _[0m  [46m_[0m  _[0m  _[0m  _[0m  [46m_[0m  _[0m  [46m_[0m  _[0m  _[0m  _[0m  [46m_[0m  _[0m  _[0m\n"
            + "h [45m_[0m  _[0m  _[0m  [46m_[0m  _[0m  _[0m  _[0m  S  [0mO  [0mD  [0mO  [0mM  [0m_[0m  _[0m  [45m_[0m\n"
            + "i _[0m  _[0m  [46m_[0m  _[0m  _[0m  _[0m  [46m_[0m  _[0m  [46m_[0m  _[0m  _[0m  O  [0m[46m_[0m  _[0m  _[0m\n"
            + "j _[0m  [44m_[0m  _[0m  _[0m  _[0m  [44m_[0m  _[0m  _[0m  _[0m  [44m_[0m  _[0m  B  [0m_[0m  [44m_[0m  _[0m\n"
            + "k _[0m  _[0m  _[0m  _[0m  [41m_[0m  _[0m  _[0m  _[0m  _[0m  _[0m  [41m_[0m  I  [0m_[0m  _[0m  _[0m\n"
            + "l [46m_[0m  _[0m  _[0m  [41m_[0m  _[0m  _[0m  _[0m  [46m_[0m  _[0m  _[0m  _[0m  L  [0m_[0m  _[0m  [46m_[0m\n"
            + "m _[0m  _[0m  [41m_[0m  _[0m  _[0m  _[0m  [46m_[0m  _[0m  [46m_[0m  _[0m  _[0m  E  [0m[41m_[0m  _[0m  _[0m\n"
            + "n _[0m  [41m_[0m  _[0m  _[0m  _[0m  [44m_[0m  _[0m  _[0m  _[0m  [44m_[0m  _[0m  S  [0m_[0m  [41m_[0m  _[0m\n"
            + "o [45m_[0m  _[0m  _[0m  [46m_[0m  _[0m  _[0m  _[0m  [45m_[0m  _[0m  _[0m  _[0m  [46m_[0m  _[0m  _[0m  [45m_[0m\n";

    assertEquals(expectedOutput, Utilities.logger.loggedOuts.getFirst());
  }

  @Test
  public void testModeSuperScrabble() {
    Configuration config =
        new Configuration(
            true,
            "d",
            false,
            true,
            false,
            4,
            10,
            new boolean[4],
            15,
            2,
            69,
            "en",
            "idk",
            null,
            java.nio.file.Paths.get("none"),
            java.nio.file.Paths.get("none"));

    final GameModel gameModel = new GameModel(config);
    final ViewCLI view = new ViewCLI(gameModel.lang, gameModel.config.superScrabble());
    Utilities.logger.setRedirectState(false);

    // test for the new draw with more letters
    view.displayGrid(gameModel.grid, gameModel.config.superScrabble());
    String expectedOutput =
        "  1  2  3  4  5  6  7  8  9 10 11 12 13 14 15 16 17 18 19 20 21\n"
            + "a [45m_[0m  _[0m  _[0m  [46m_[0m  _[0m  _[0m  _[0m  [41;1m_[0m  _[0m  _[0m  [41m_[0m  _[0m  _[0m  [41;1m_[0m  _[0m  _[0m  _[0m  [46m_[0m  _[0m  _[0m  [45m_[0m\n"
            + "b _[0m  [41m_[0m  _[0m  _[0m  [46;1m_[0m  _[0m  _[0m  _[0m  [41m_[0m  _[0m  _[0m  _[0m  [41m_[0m  _[0m  _[0m  _[0m  [46;1m_[0m  _[0m  _[0m  [41m_[0m  _[0m\n"
            + "c _[0m  _[0m  [41m_[0m  _[0m  _[0m  [45m_[0m  _[0m  _[0m  _[0m  [41m_[0m  _[0m  [41m_[0m  _[0m  _[0m  _[0m  [45m_[0m  _[0m  _[0m  [41m_[0m  _[0m  _[0m\n"
            + "d [46m_[0m  _[0m  _[0m  [41;1m_[0m  _[0m  _[0m  [46m_[0m  _[0m  _[0m  _[0m  [41;1m_[0m  _[0m  _[0m  _[0m  [46m_[0m  _[0m  _[0m  [41;1m_[0m  _[0m  _[0m  [46m_[0m\n"
            + "e _[0m  [46;1m_[0m  _[0m  _[0m  [41m_[0m  _[0m  _[0m  _[0m  [46;1m_[0m  _[0m  _[0m  _[0m  [46;1m_[0m  _[0m  _[0m  _[0m  [41m_[0m  _[0m  _[0m  [46;1m_[0m  _[0m\n"
            + "f _[0m  _[0m  [45m_[0m  _[0m  _[0m  [41m_[0m  _[0m  _[0m  _[0m  [46m_[0m  _[0m  [46m_[0m  _[0m  _[0m  _[0m  [41m_[0m  _[0m  _[0m  [45m_[0m  _[0m  _[0m\n"
            + "g _[0m  _[0m  _[0m  [46m_[0m  _[0m  _[0m  [41m_[0m  _[0m  _[0m  _[0m  [46m_[0m  _[0m  _[0m  _[0m  [41m_[0m  _[0m  _[0m  [46m_[0m  _[0m  _[0m  _[0m\n"
            + "h [41;1m_[0m  _[0m  _[0m  _[0m  _[0m  _[0m  _[0m  [41m_[0m  _[0m  _[0m  _[0m  _[0m  _[0m  [41m_[0m  _[0m  _[0m  _[0m  _[0m  _[0m  _[0m  [41;1m_[0m\n"
            + "i _[0m  [41m_[0m  _[0m  _[0m  [46;1m_[0m  _[0m  _[0m  _[0m  [46;1m_[0m  _[0m  _[0m  _[0m  [46;1m_[0m  _[0m  _[0m  _[0m  [46;1m_[0m  _[0m  _[0m  [41m_[0m  _[0m\n"
            + "j _[0m  _[0m  [41m_[0m  _[0m  _[0m  [46m_[0m  _[0m  _[0m  _[0m  [46m_[0m  _[0m  [46m_[0m  _[0m  _[0m  _[0m  [46m_[0m  _[0m  _[0m  [41m_[0m  _[0m  _[0m\n"
            + "k [46m_[0m  _[0m  _[0m  [41;1m_[0m  _[0m  _[0m  [46m_[0m  _[0m  _[0m  _[0m  [41m_[0m  _[0m  _[0m  _[0m  [46m_[0m  _[0m  _[0m  [41;1m_[0m  _[0m  _[0m  [46m_[0m\n"
            + "l _[0m  _[0m  [41m_[0m  _[0m  _[0m  [46m_[0m  _[0m  _[0m  _[0m  [46m_[0m  _[0m  [46m_[0m  _[0m  _[0m  _[0m  [46m_[0m  _[0m  _[0m  [41m_[0m  _[0m  _[0m\n"
            + "m _[0m  [41m_[0m  _[0m  _[0m  [46;1m_[0m  _[0m  _[0m  _[0m  [46;1m_[0m  _[0m  _[0m  _[0m  [46;1m_[0m  _[0m  _[0m  _[0m  [46;1m_[0m  _[0m  _[0m  [41m_[0m  _[0m\n"
            + "n [41;1m_[0m  _[0m  _[0m  _[0m  _[0m  _[0m  _[0m  [41m_[0m  _[0m  _[0m  _[0m  _[0m  _[0m  [41m_[0m  _[0m  _[0m  _[0m  _[0m  _[0m  _[0m  [41;1m_[0m\n"
            + "o _[0m  _[0m  _[0m  [46m_[0m  _[0m  _[0m  [41m_[0m  _[0m  _[0m  _[0m  [46m_[0m  _[0m  _[0m  _[0m  [41m_[0m  _[0m  _[0m  [46m_[0m  _[0m  _[0m  _[0m\n"
            + "p _[0m  _[0m  [45m_[0m  _[0m  _[0m  [41m_[0m  _[0m  _[0m  _[0m  [46m_[0m  _[0m  [46m_[0m  _[0m  _[0m  _[0m  [41m_[0m  _[0m  _[0m  [45m_[0m  _[0m  _[0m\n"
            + "q _[0m  [46;1m_[0m  _[0m  _[0m  [41m_[0m  _[0m  _[0m  _[0m  [46m_[0m  _[0m  _[0m  _[0m  [46;1m_[0m  _[0m  _[0m  _[0m  [41m_[0m  _[0m  _[0m  [46;1m_[0m  _[0m\n"
            + "r [46m_[0m  _[0m  _[0m  [41;1m_[0m  _[0m  _[0m  [46m_[0m  _[0m  _[0m  _[0m  [41;1m_[0m  _[0m  _[0m  _[0m  [46m_[0m  _[0m  _[0m  [41;1m_[0m  _[0m  _[0m  [46m_[0m\n"
            + "s _[0m  _[0m  [41m_[0m  _[0m  _[0m  [45m_[0m  _[0m  _[0m  _[0m  [41m_[0m  _[0m  [41m_[0m  _[0m  _[0m  _[0m  [45m_[0m  _[0m  _[0m  [41m_[0m  _[0m  _[0m\n"
            + "t _[0m  [41m_[0m  _[0m  _[0m  [46;1m_[0m  _[0m  _[0m  _[0m  [41m_[0m  _[0m  _[0m  _[0m  [41m_[0m  _[0m  _[0m  _[0m  [46;1m_[0m  _[0m  _[0m  [41m_[0m  _[0m\n"
            + "u [45m_[0m  _[0m  _[0m  [46m_[0m  _[0m  _[0m  _[0m  [41;1m_[0m  _[0m  _[0m  [41m_[0m  _[0m  _[0m  [41;1m_[0m  _[0m  _[0m  _[0m  [46m_[0m  _[0m  _[0m  [45m_[0m\n";
    assertEquals(expectedOutput, Utilities.logger.loggedOuts.getFirst());
  }
}
