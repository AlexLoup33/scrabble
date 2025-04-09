package pdp.scrabble.serialize;

import java.io.ByteArrayInputStream;
import java.nio.file.Paths;
import org.junit.Test;
import pdp.scrabble.MainTest;
import pdp.scrabble.Utilities;
import pdp.scrabble.configuration.Configuration;
import pdp.scrabble.dictionary.Letter;
import pdp.scrabble.errorsHandling.CustomError;
import pdp.scrabble.errorsHandling.ErrorType;
import pdp.scrabble.game.GameModel;
import pdp.scrabble.game.actions.GameAction;
import pdp.scrabble.game.serializer.Serializer;
import pdp.scrabble.view.CLI.ViewCLI;

public class SerializationTests {

  public static void assertThrowsCustom(ErrorType errType, Runnable test) {
    MainTest.assertThrowsCustom(errType, test);
  }

  public static Letter[] lettersFromString(String s) {
    return MainTest.lettersFromString(s);
  }

  @Test
  public void testsQuitAndSave() {
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
        "placeWord\nh8h SODOM\n"
            + "placeWord\nh12v MOBILES\n"
            + "passTurn\n"
            + "forfeitPlayer\n"
            + "exchangeLetter\nEKIRE#\n"
            + "renamePlayer\nBLIPOILLE\n"
            + "quit\ny\n";
    GameModel gameModel = new GameModel(config);
    Utilities.logger.setRedirectState(false);
    ViewCLI view =
        new ViewCLI(
            gameModel.lang, config.getSuperScrabble(), new ByteArrayInputStream(inputs.getBytes()));

    for (int i = 0; i < 6; i++) {
      // all the actions of the game
      GameAction ga = view.askAction(gameModel.getCurrentPlayer(), gameModel.getTurn());
      gameModel.applyGameActionDispatch(ga, i);
    }

    GameAction ga = view.askAction(gameModel.getCurrentPlayer(), gameModel.getTurn());
    try {
      gameModel.applyGameActionDispatch(ga, 0);
    } catch (CustomError e) {
      e.handleIfErrorType(ErrorType.ExitNoError);
    }
  }

  @Test
  public void TestLoadGame() {

    Serializer.loadModelFromFile(Paths.get("src/test/resources/savedGames/testGame1.txt"));
    assertThrowsCustom(
        ErrorType.FailedConfigFileParsing,
        () -> {
          Serializer.loadModelFromFile(Paths.get("src/test/resources/savedGames/testGame2.txt"));
        });
    assertThrowsCustom(
        ErrorType.FailedConfigFileParsing,
        () -> {
          Serializer.loadModelFromFile(Paths.get("src/test/resources/savedGames/testGame3.txt"));
        });
  }
}
