package pdp.scrabble.view;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
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
import pdp.scrabble.game.LettersDrawer;
import pdp.scrabble.game.Player;
import pdp.scrabble.game.actions.GameAction;
import pdp.scrabble.game.actions.RestartGameAction;
import pdp.scrabble.view.Component.GridView;

public class ViewGUI extends Application implements IView {

  private static Lang lang;
  private GridView gridView;

  public ViewGUI() {}

  public static void setLang(Lang lang) {
    ViewGUI.lang = lang;
  }

  @Override
  public void start(Stage primaryStage) {
    // Debug message at the top of the window
    String message = getLocalizedMessage();

    // MENU BAR INITIALIZATION
    MenuBar menuBar = new MenuBar();

    // First -> File : New Game | Load Game | Save Game | Help | Settings | Quit
    Menu fileMenu = new Menu("File");

    // Declaration of the Items
    MenuItem newGameItem = new MenuItem("New Game");
    MenuItem loadGameItem = new MenuItem("Load Game");
    MenuItem saveGameItem = new MenuItem("Save Game");
    MenuItem helpItem = new MenuItem("Help");
    MenuItem settingsItem = new MenuItem("Settings");
    MenuItem quitItem = new MenuItem("Quit");

    // Actions for Items

    // New Game
    // TO-DO: Ask the controller to create a new game.
    // --> TO-DO: implement restartGameAction
    //newGameItem.setOnAction(e -> RestartGameAction.restart());
    newGameItem.setOnAction(e -> System.out.println("restart Game..."));

    // Load Game
    // TO-DO: Open a file selector to load a game.
    loadGameItem.setOnAction(e -> System.out.println("Load Game..."));

    // Save Game
    // TO-DO: Open a file selector to save the game.
    saveGameItem.setOnAction(e -> System.out.println("Save Game..."));

    // Help
    // TO-DO: Display a window with the rules and the different actions possible.
    helpItem.setOnAction(e -> System.out.println("Help..."));

    // Settings
    // TO-DO: Implement settings window who change style.
    settingsItem.setOnAction(e -> System.out.println("Settings..."));

    // Quit
    quitItem.setOnAction(e -> System.exit(0));

    // Adding all item to fileMenu
    fileMenu
        .getItems()
        .addAll(newGameItem, loadGameItem, saveGameItem, helpItem, settingsItem, quitItem);

    // Second -> Game : Hint | Undo | Redo
    Menu gameMenu = new Menu("Game");

    MenuItem hintItem = new MenuItem("Hint");
    MenuItem undoItem = new MenuItem("Undo");
    MenuItem redoItem = new MenuItem("Redo");

    // Action for Items

    // TO-DO: Display colored letters hint on the grid
    hintItem.setOnAction(e -> System.out.println("Hint..."));

    // TO-DO: Undo the last action
    undoItem.setOnAction(e -> System.out.println("Undo..."));

    // TO-DO: Redo the last action
    redoItem.setOnAction(e -> System.out.println("Redo..."));

    gameMenu.getItems().addAll(hintItem, undoItem, redoItem);

    // Third -> About : Version | Author | Licence
    Menu aboutMenu = new Menu("About");

    MenuItem versionItem = new MenuItem("Version");
    MenuItem authorItem = new MenuItem("Author");
    MenuItem licenceItem = new MenuItem("Licence");

    // Actions for Items

    // TO-DO: Display the version of the game
    versionItem.setOnAction(e -> System.out.println("Version..."));

    // TO-DO: Display the author of the game
    authorItem.setOnAction(e -> System.out.println("Author..."));

    // TO-DO: Display the licence of the game
    licenceItem.setOnAction(e -> System.out.println("Licence..."));

    aboutMenu.getItems().addAll(versionItem, authorItem, licenceItem);

    // Adding all the Menu in the MenuBar and adding a bit of style
    menuBar.getMenus().addAll(fileMenu, gameMenu, aboutMenu);
    menuBar.setStyle("-fx-start-margin: 200px;");

    Label label = new Label(message);

    // GRID INITIALIZATION
    this.gridView = new GridView(15, 15);

    VBox root = new VBox(menuBar, label, this.gridView);
    root.setPadding(new Insets(10, 10, 10, 10));

    Scene scene = new Scene(root, 1200, 800);

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
  public void displayDraw(LettersDrawer draw) {
    throw new UnsupportedOperationException("Unimplemented method 'displayDraw'");
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
