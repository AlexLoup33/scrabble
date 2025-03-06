package pdp.scrabble.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pdp.scrabble.dictionary.LettersCounter;
import pdp.scrabble.errorsHandling.CustomError;
import pdp.scrabble.game.Grid;
import pdp.scrabble.game.Lang;
import pdp.scrabble.game.Player;
import pdp.scrabble.game.actions.GameAction;

public class ViewGUI extends Application implements IView {

  private static Lang lang;

  public ViewGUI() {}

  public static void setLang(Lang lang) {
    ViewGUI.lang = lang;
  }

  @Override
  public void start(Stage primaryStage) {
    String message = getLocalizedMessage();

    MenuBar menuBar = new MenuBar();
    Menu fileMenu = new Menu("File");
    MenuItem newMenuItem = new MenuItem("New");
    newMenuItem.setOnAction(e -> System.out.println("New..."));
    fileMenu.getItems().add(newMenuItem);
    Menu gameMenu = new Menu("Game");
    Menu aboutMenu = new Menu("About");
    menuBar.getMenus().addAll(fileMenu, gameMenu, aboutMenu);
    menuBar.setStyle("-fx-start-margin: 200px;");

    Label label = new Label(message);

    VBox root = new VBox(menuBar, label);
    Scene scene = new Scene(root, 400, 300);

    primaryStage.setTitle("Scrabble - GUI");
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  private String getLocalizedMessage() {
    if (lang == null) {
      return "No language set";
    } else
      switch (lang) {
        case EN -> {
          return "Welcome to the Scrabble game!";
        }
        case FR -> {
          return "Bienvenue au jeu du Scrabble !";
        }
        default -> {
          return "Welcome to the Scrabble !";
        }
      }
  }

  public static void main(String[] args) {
    setLang(Lang.FR);
    launch(args);
  }

  // --- IView methods ---
  @Override
  public GameAction askAction(Player player, Integer turn) {
    throw new UnsupportedOperationException("Unimplemented method 'askAction'");
  }

  @Override
  public void displayGrid(Grid grid, Boolean isSuperScrabble) {}

  @Override
  public void displayPlayerLetters(LettersCounter letters) {
    throw new UnsupportedOperationException("Unimplemented method 'displayPlayerLetters'");
  }

  @Override
  public String getFromLangPack(String identifiant) {
    throw new UnsupportedOperationException("Unimplemented method 'getFromLangPack'");
  }

  @Override
  public void showScore(Player player) {
    throw new UnsupportedOperationException("Unimplemented method 'showScore'");
  }

  @Override
  public void showScore(java.util.List<Player> players) {
    throw new UnsupportedOperationException("Unimplemented method 'showScore'");
  }

  @Override
  public void displayDrawer(LettersCounter draw) {
    throw new UnsupportedOperationException("Unimplemented method 'displayDrawer'");
  }

  @Override
  public void announceTurn(Player currentPlayer) {
    throw new UnsupportedOperationException("Unimplemented method 'announceTurn'");
  }

  @Override
  public void showError(CustomError e) {
    throw new UnsupportedOperationException("Unimplemented method 'showError'");
  }

  @Override
  public void showError(Exception e) {
    throw new UnsupportedOperationException("Unimplemented method 'showError'");
  }
}
