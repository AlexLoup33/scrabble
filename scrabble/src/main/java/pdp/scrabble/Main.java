/**
 * Compile on Mac: mvn package
 *
 * <p>Run on Mac: java -classpath target/classes pdp.scrabble.Main
 */
package pdp.scrabble;

import pdp.scrabble.configuration.ConfigurationBuilder;
import pdp.scrabble.dictionary.Letter;
import pdp.scrabble.game.*;
import pdp.scrabble.game.actions.PlaceWordAction;

public class Main {
  public static void main(String[] args) throws Exception {
    ConfigurationBuilder cliBuilder = new ConfigurationBuilder(args);
    System.out.println("--- Start ---");

    GameModel gm = new GameModel();
    gm.dictionary.loadFromFile(java.nio.file.Paths.get("dictionary/fr.txt"));

    Player p1 = new Player("franck");

    Grid grid = new Grid(gm, 15);

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

    // Test 1
    System.out.println(
        grid.placeLetters(new PlaceWordAction(p1, 0, l1, new GridWordPos(2, 0, Direction.Right))));
    System.out.println(
        grid.placeLetters(new PlaceWordAction(p1, 0, l2, new GridWordPos(0, 5, Direction.Down))));

    // Test 2
    System.out.println(
        grid.placeLetters(new PlaceWordAction(p1, 0, l1, new GridWordPos(8, 8, Direction.Down))));
    System.out.println(
        grid.placeLetters(new PlaceWordAction(p1, 0, l2, new GridWordPos(13, 6, Direction.Right))));

    grid.printGrid();

    System.out.println("--- End ---");
  }
}
