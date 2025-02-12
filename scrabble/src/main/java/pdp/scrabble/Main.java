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

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("--- Start ---");

//        Configuration config = new Configuration();
        GameModel gm = new GameModel();
        GameModel.dictionary.loadFromFile(java.nio.file.Paths.get("dictionary/fr.txt"));

        Player p1 = new Player("franck");
        Player p2 = new Player("franck2");

        Grid grid = new Grid(15);
        Letter[] l = new Letter[3];
        l[0] = new Letter('C');
        l[1] = new Letter('A');
        l[2] = new Letter('R');

        Letter[] l2 = new Letter[2];
        l2[0] = new Letter('R');
        l2[1] = new Letter('E');

        Letter[] l3 = new Letter[4];
        l3[0] = new Letter('M');
        l3[1] = new Letter('E');
        l3[2] = new Letter('N');
        l3[3] = new Letter('T');

        grid.placeLetters(new PlaceWordAction(
                p1,
                0,
                l,
                new GridWordPos(0, 3, Direction.Down))
        );
        grid.placeLetters(new PlaceWordAction(
                p1,
                0,
                l2,
                new GridWordPos(3, 3, Direction.Down))
        );
        grid.placeLetters(new PlaceWordAction(
                p1,
                0,
                l3,
                new GridWordPos(10, 3, Direction.Down))
        );
        grid.printGrid();


        System.out.println("--- End ---");
    }
}
