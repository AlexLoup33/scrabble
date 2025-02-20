/**
 * Compile on Mac: mvn package
 *
 * <p>Run on Mac: java -classpath target/classes pdp.scrabble.Main
 */
package pdp.scrabble;

import java.util.Arrays;
import pdp.scrabble.configuration.Configuration;
import pdp.scrabble.configuration.ConfigurationInitializer;
import pdp.scrabble.controller.GameController;
import pdp.scrabble.game.GameModel;
import pdp.scrabble.game.Grid;
import pdp.scrabble.game.Player;
import pdp.scrabble.view.ViewCLI;

public class Main {
  public static void main(String[] args) throws Exception {
    Configuration conf = ConfigurationInitializer.configurationInit(args);

    Player p1 = new Player("franck");
    Player p2 = new Player("franck2");
    ViewCLI view = new ViewCLI();

    GameModel gm = new GameModel(conf);
    Grid grid = new Grid(gm, 15);
    gm.dictionary.loadFromFile(java.nio.file.Paths.get("dictionary/fr.txt"));
    gm.players = Arrays.asList(p1, p2);

    GameController gc = new GameController(gm, view);
    gc.gameLoop();
  }
}
