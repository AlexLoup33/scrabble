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

    // Oral W7: val
    // F30 test a montrer; OK
    // F32 test mot pas legal; a montrer OK

    // Oral W7: Aymeric
    // F22 OK, F17 OK, F18 OK, F31 OK

    // Oral W7: Ludo
    // F37 idk

    // F35 OK
    // F13 OK
    // F23 OK
    // nope F12, F29, F37

    System.out.println("--- End ---");
  }
}
