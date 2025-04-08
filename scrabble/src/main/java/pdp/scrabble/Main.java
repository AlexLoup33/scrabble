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
import pdp.scrabble.errorsHandling.CustomError;
import pdp.scrabble.errorsHandling.ErrorType;
import pdp.scrabble.presenter.GamePresenter;
import pdp.scrabble.view.GUI.ViewGuiApp;

public class Main {
  private static Configuration cfg = null;

  public static void main(String[] args) throws Exception {
    Configuration conf = ConfigurationInitializer.configurationInit(args);

    if (conf.gui()) {
      Main.cfg = conf;
      Application.launch(ViewGuiApp.class, args);
    } else {
      GamePresenter gc = new GamePresenter(conf);
      try {
        gc.gameLoop();
      } catch (CustomError e) {
        if (e.type == ErrorType.ExitNoError) {
          System.exit(0);
        } else if (e.type == ErrorType.ExitWithError) {
          System.exit(1);
        } else {
          throw e;
        }
      }
    }
  }

  public static Configuration getConfig() {
    Configuration conf = Main.cfg;
    Main.cfg = null;
    return conf;
  }
}
