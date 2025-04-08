package controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import pdp.scrabble.configuration.Configuration;
import pdp.scrabble.configuration.ConfigurationInitializer;
import pdp.scrabble.controller.GameController;
import pdp.scrabble.game.GameModel;
import pdp.scrabble.game.serializer.Serializer;

public class GameControllerTest {
  @Test
  public void TestGameController() {
    String[] args = {"-h"};
    Configuration config = ConfigurationInitializer.configurationInit(args);
    GameController gcTest = new GameController(config);
    GameModel model = null;
    assertNotNull(gcTest);
    assertEquals(Serializer.loadModelFromFile(config.getHistoryFile()), model);

    GameController gcTest2 = new GameController(config, null);
    assertNotNull(gcTest2);
  }
}
