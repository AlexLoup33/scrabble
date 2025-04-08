package pdp.scrabble.view;

import javafx.application.Application;
import javafx.stage.Stage;
import pdp.scrabble.Main;
import pdp.scrabble.presenter.GamePresenter;

public class ViewGuiApp extends Application {

  @Override
  public void start(Stage stage) throws Exception {
    GamePresenter gc = new GamePresenter(Main.getConfig(), stage);

    // Thread gameThread = new Thread(gc::gameLoop);
    // gameThread.setDaemon(true);
    // gameThread.start();
  }
}
