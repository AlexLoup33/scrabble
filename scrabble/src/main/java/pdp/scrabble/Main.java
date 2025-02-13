/**
 * Compile on Mac: mvn package
 *
 * <p>Run on Mac: java -classpath target/classes pdp.scrabble.Main
 */
package pdp.scrabble;

import pdp.scrabble.dictionary.Letter;
import pdp.scrabble.game.*;
import pdp.scrabble.game.actions.PlaceWordAction;

import java.lang.module.Configuration;
import java.util.ArrayList;
import java.util.List;

import pdp.scrabble.configuration.ConfigurationBuilder;

public class Main {
    public static void main(String[] args) throws Exception {
        ConfigurationBuilder confBuilder = new ConfigurationBuilder(args);
        System.out.println("--- Start ---");

//        Configuration config = new Configuration();
        GameModel gm = new GameModel();
        GameModel.dictionary.loadFromFile(java.nio.file.Paths.get("dictionary/fr.txt"));

        Player p1 = new Player("franck");
        Player p2 = new Player("franck2");

        Grid grid = new Grid(15);
        Letter[] l1 = new Letter[5];
        l1[0] = new Letter('L');
        l1[1] = new Letter('O');
        l1[2] = new Letter('U');
        l1[3] = new Letter('E');
        l1[4] = new Letter('E');

        Letter[] l2 = new Letter[4];
        l2[0] = new Letter('T');
        l2[1] = new Letter('I');
        l2[2] = new Letter('S');
        l2[3] = new Letter('A');


        System.out.println(
                grid.placeLetters(new PlaceWordAction(
                        p1,
                        0,
                        l1,
                        new GridWordPos(2, 0, Direction.Right))
                ));
//        System.out.println(
//                grid.placeLetters(new PlaceWordAction(
//                        p1,
//                        0,
//                        l2,
//                        new GridWordPos(0, 5, Direction.Down))
//                ));


        grid.printGrid();


        System.out.println("--- End ---");
    }
}
