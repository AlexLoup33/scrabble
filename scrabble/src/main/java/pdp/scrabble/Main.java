/**
 * Compile on Mac: mvn package
 *
 * <p>Run on Mac: java -classpath target/classes pdp.scrabble.Main
 *
 * <p>mvn spotless:apply
 */
package pdp.scrabble;

import javafx.application.Application;
import pdp.scrabble.configuration.Configuration;
import pdp.scrabble.configuration.ConfigurationInitializer;
import pdp.scrabble.controller.GameController;
import pdp.scrabble.view.ViewGuiApp;

public class Main {
  private static Configuration cfg = null;

  public static void main(String[] args) throws Exception {
    Configuration conf = ConfigurationInitializer.configurationInit(args);
    // TODO
    System.out.println("if this print is not equal to 10 with param '-b 10', problem !");
    System.out.println("conf = " + conf.blitzTime());

    if (conf.gui()) {
      Main.cfg = conf;
      Application.launch(ViewGuiApp.class, args);
    } else {
      GameController gc = new GameController(conf);
      gc.gameLoop();
    }
  }

  public static Configuration getConfig() {
    Configuration conf = Main.cfg;
    Main.cfg = null;
    return conf;
  }
}
