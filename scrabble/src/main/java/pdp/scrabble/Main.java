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

    if (conf.gui()){
      Main.cfg = conf;
      Application.launch(ViewGuiApp.class, args);
    } else {
      GameController gc = new GameController(conf);
      gc.gameLoop();
    }
    
    // Question:

    // give-up des features ?
    // F1 to F9, how ?? peut rendre early ?
  }

  public static Configuration getConfig(){
    Configuration conf = Main.cfg;
    Main.cfg = null;
    return conf;
  }
}
