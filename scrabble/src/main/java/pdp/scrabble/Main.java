/**
 * Compile on Mac: mvn package
 *
 * <p>Run on Mac: java -classpath target/classes pdp.scrabble.Main
 *
 * <p>mvn spotless:apply
 */
package pdp.scrabble;

import pdp.scrabble.configuration.Configuration;
import pdp.scrabble.configuration.ConfigurationInitializer;
import pdp.scrabble.controller.GameController;

public class Main {
  public static void main(String[] args) throws Exception {
    Configuration conf = ConfigurationInitializer.configurationInit(args);

    GameController gc = new GameController(conf);
    gc.gameLoop();

    // Oral W8: val
    // F33, F41

    // Oral W8: Ludo
    // F36
    // TODO F34, 37

    // nope F12, F29

    System.out.println("--- End ---");
  }
}
