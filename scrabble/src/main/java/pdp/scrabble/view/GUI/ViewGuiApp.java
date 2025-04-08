package pdp.scrabble.view.GUI;

import javafx.application.Application;
import javafx.stage.Stage;
import pdp.scrabble.Main;

public class ViewGuiApp extends Application {

  @Override
  public void start(Stage stage) throws Exception {
    ViewGUI view = new ViewGUI(stage, Main.getConfig());

    // Thread gameThread = new Thread(gc::gameLoop);
    // gameThread.setDaemon(true);
    // gameThread.start();
  }
}
