/**
 * Compile on Mac: mvn package
 *
 * <p>Run on Mac: java -classpath target/classes pdp.scrabble.Main
 */
package pdp.scrabble;

import pdp.scrabble.configuration.Configuration;
import pdp.scrabble.configuration.ConfigurationInitializer;
import pdp.scrabble.controller.GameController;
import pdp.scrabble.dictionary.Letter;
import pdp.scrabble.game.GameModel;
import pdp.scrabble.game.Grid;
import pdp.scrabble.game.actions.PlaceWordAction;
import pdp.scrabble.view.ViewCLI;

public class Main {
    public static void main(String[] args) throws Exception {
        Configuration conf = ConfigurationInitializer.configurationInit(args);

        ViewCLI view = new ViewCLI("fr");

        String testCode = view.getLangPack("test");
        System.out.println(testCode);

        GameModel gm = new GameModel(conf);
        Grid grid = new Grid(gm, 15);
        gm.dictionary.loadFromFile(java.nio.file.Paths.get("dictionary/fr.txt"));

        GameController gc = new GameController(gm, view);
        gc.gameLoop();

        // Oral W7: val
        // F30 test a montrer
        // F32 test mot pas legal; a montrer

        // Oral W7: Aymeric
        // F22, F17, F18, F20 ?, F31 ?

        // Oral W7: Ludo
        // TODO F37 probably

        // Oral W7: Alex

        // Oral W7: Matheo

        // Oral W7: Jean

        System.out.println("--- End ---");
    }
    // nope F12, F29, F37
}
