/**
 * Compile on Mac: mvn package
 *
 * <p>Run on Mac: java -classpath target/classes pdp.scrabble.Main
 */
package pdp.scrabble;

import pdp.scrabble.configuration.Configuration;
import pdp.scrabble.configuration.ConfigurationInitializer;
import pdp.scrabble.controller.GameController;
import pdp.scrabble.game.GameModel;
import pdp.scrabble.game.Grid;
import pdp.scrabble.view.ViewCLI;

public class Main {
    public static void main(String[] args) throws Exception {
        Configuration conf = ConfigurationInitializer.configurationInit(args);

        ViewCLI view = new ViewCLI();

        GameModel gm = new GameModel(conf);
        Grid grid = new Grid(gm, 15);
        gm.dictionary.loadFromFile(java.nio.file.Paths.get("dictionary/fr.txt"));

        GameController gc = new GameController(gm, view);
        gc.gameLoop();

        // TODO
        // F30 faire test
        // F32 test mot pas legal; a montrer
        // nope F22, F17, F18, F12, F29, F37
    }
}
