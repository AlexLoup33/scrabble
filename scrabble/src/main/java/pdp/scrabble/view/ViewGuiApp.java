package pdp.scrabble.view;

import javafx.application.Application;
import javafx.stage.Stage;
import pdp.scrabble.Main;
import pdp.scrabble.controller.GameController;

public class ViewGuiApp extends Application {

  @Override
  public void start(Stage stage) throws Exception {
    GameController gc = new GameController(Main.getConfig(), stage);
    
    Thread gameThread = new Thread(gc::gameLoop);
    gameThread.setDaemon(true);
    gameThread.start();
  }
}
