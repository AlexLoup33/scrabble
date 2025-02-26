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

        ViewCLI view = new ViewCLI();

        GameModel gm = new GameModel(conf);
        Grid grid = new Grid(gm, 15);
        gm.dictionary.loadFromFile(java.nio.file.Paths.get("dictionary/fr.txt"));

        GameController gc = new GameController(gm, view);
        gc.gameLoop();

        // TODO
        // F30 faire test
        // F32 test mot pas legal; a montrer

        Letter[] l2 = new Letter[4];
        l2[0] = new Letter('T');
        l2[1] = new Letter('I');
        l2[2] = new Letter('S');
        l2[3] = new Letter('A');

        // Test 1
        /*
         * System.out.println(
         * grid.placeLetters(new PlaceWordAction(p1, 0, l1, new GridWordPos(2, 0,
         * Direction.Right))));
         * System.out.println(
         * grid.placeLetters(new PlaceWordAction(p1, 0, l2, new GridWordPos(0, 5,
         * Direction.Down))));
         * 
         * // Test 2
         * System.out.println(
         * grid.placeLetters(new PlaceWordAction(p1, 0, l1, new GridWordPos(8, 8,
         * Direction.Down))));
         * System.out.println(
         * grid.placeLetters(new PlaceWordAction(p1, 0, l2, new GridWordPos(13, 6,
         * Direction.Right))));
         * 
         * // grid.printGrid();
         * ViewCLI view = new ViewCLI("fr");
         * view.displayGrid(grid);
         * view.displayPlayerLetters(p1.bag);
         */
        System.out.println("--- End ---");
    }
    // nope F22, F17, F18, F12, F29, F37
}
