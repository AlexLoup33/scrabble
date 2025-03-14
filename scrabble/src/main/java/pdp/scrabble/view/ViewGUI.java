package pdp.scrabble.view;

import java.util.List;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pdp.scrabble.dictionary.Letter;
import pdp.scrabble.dictionary.LettersCounter;
import pdp.scrabble.errorsHandling.CustomError;
import pdp.scrabble.game.Grid;
import pdp.scrabble.game.Lang;
import pdp.scrabble.game.LettersDrawer;
import pdp.scrabble.game.Player;
import pdp.scrabble.game.actions.GameAction;
import pdp.scrabble.view.Component.ScrabbleBoard;

public class ViewGUI extends Application implements IView {

  private static Lang lang;
  private static Boolean isSuperScrabble;

  private ScrabbleBoard scrabbleBoard;

  public ViewGUI() {}

  public static void setLang(Lang lang) {
    ViewGUI.lang = lang;
  }

  public static void setSuperScrabble(Boolean superScrabble) {
    isSuperScrabble = superScrabble;
  }

  @Override
  public void start(Stage stage) {
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
    // TODO: Ask the controller to create a new game.
    // --> TODO: implement restartGameAction
    // newGameItem.setOnAction(e -> RestartGameAction.restart());
    newGameItem.setOnAction(e -> System.out.println("restart Game..."));

    // Load Game
    // TODO: Open a file selector to load a game.
    loadGameItem.setOnAction(e -> System.out.println("Load Game..."));

    // Save Game
    // TODO: Open a file selector to save the game.
    saveGameItem.setOnAction(e -> System.out.println("Save Game..."));

    // Help
    // TODO: Display a window with the rules and the different actions possible.
    helpItem.setOnAction(e -> System.out.println("Help..."));

    // Settings
    // TODO: Implement settings window who change style.
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
    MenuItem undoAllItem = new MenuItem("Undo All");
    MenuItem undoItem = new MenuItem("Undo");
    MenuItem redoItem = new MenuItem("Redo");
    MenuItem redoAllItem = new MenuItem("Redo All");

    // Action for Items

    // TODO: Display colored letters hint on the grid
    hintItem.setOnAction(e -> System.out.println("Hint..."));

    // TODO: Undo all actions
    undoAllItem.setOnAction(e -> System.out.println("Undo All..."));

    // TODO: Undo the last action
    undoItem.setOnAction(e -> System.out.println("Undo..."));

    // TODO: Redo the last action
    redoItem.setOnAction(e -> System.out.println("Redo..."));

    // TODO: Redo all actions
    redoAllItem.setOnAction(e -> System.out.println("Redo All..."));

    gameMenu.getItems().addAll(hintItem, undoAllItem, undoItem, redoItem, redoAllItem);

    // Third -> About : Version | Author | Licence
    Menu aboutMenu = new Menu("About");

    MenuItem versionItem = new MenuItem("Version");
    MenuItem authorItem = new MenuItem("Author");
    MenuItem licenceItem = new MenuItem("Licence");

    // Actions for Items

    // TODO: Display the version of the game
    versionItem.setOnAction(
        (ActionEvent event) -> {
          Alert alert = new Alert(Alert.AlertType.INFORMATION);
          alert.setTitle("Version");
          alert.setHeaderText(null);
          alert.setContentText("Version 1.0.0");
          alert.showAndWait();
        });

    // TODO: Display the author of the game
    authorItem.setOnAction(
        (ActionEvent event) -> {
          Alert alert = new Alert(Alert.AlertType.INFORMATION);
          alert.setTitle("Author");
          alert.setHeaderText(null);
          alert.setContentText(
              """
                             Andrieu Ludovic
                             Cassard Aymeric
                             Cazet Valentin
                             Lou-Poueyou Alexandre
                             Mayol Jean
                             Petry Math\u00e9o
                             """);
          alert.showAndWait();
        });

    // TODO: Display the licence of the game
    licenceItem.setOnAction(
        (ActionEvent event) -> {
          Alert alert = new Alert(Alert.AlertType.INFORMATION);
          alert.setTitle("Licence");
          alert.setHeaderText(null);
          alert.setContentText(
              """
                             This game is licensed under the MIT License.
                             You can do whatever you want with it, as long as you include the original license.
                             """);
          alert.setContentText(
              alert.getContentText() + "\n\n" + "https://opensource.org/licenses/MIT");
          alert.showAndWait();
        });

    aboutMenu.getItems().addAll(versionItem, authorItem, licenceItem);

    // Adding all the Menu in the MenuBar and adding a bit of style
    menuBar.getMenus().addAll(fileMenu, gameMenu, aboutMenu);
    menuBar.setStyle("-fx-start-margin: 200px;");

    Label label = new Label(message);

    // GRID INITIALIZATION
    this.scrabbleBoard = new ScrabbleBoard();

    // ACTION BUTTONS
    Button placeWordButton = new Button("Place Word");
    placeWordButton.setOnAction(e -> System.out.println("Place Word action triggered"));

    Button exchangeLetterButton = new Button("Exchange Letter");
    exchangeLetterButton.setOnAction(
        e -> {
          // Assuming there is a method to get the current player's LettersCounter
          // TODO: Replace `getCurrentPlayerLetters()` with the actual method to retrieve the
          // player's letters
          LettersCounter letters = new LettersCounter();
          letters.addLetter(new Letter('A'));
          letters.addLetter(new Letter('E'));
          letters.addLetter(new Letter('I'));
          letters.addLetter(new Letter('O'));
          letters.addLetter(new Letter('U'));
          letters.addLetter(new Letter('L'));
          letters.addLetter(new Letter('N'));

          // Create a dialog for selecting letters to exchange
          Dialog<java.util.List<Letter>> dialog = new Dialog<>();
          dialog.setTitle("Exchange Letters");
          dialog.setHeaderText("Select the letters to exchange");

          VBox content = new VBox();
          content.setSpacing(10);

          // Create toggle buttons for each letter and arrange them in rows
          List<ToggleButton> toggleButtons = new java.util.ArrayList<>();
          HBox row = new HBox(10); // Horizontal row with spacing
          int buttonsPerRow = 5; // Number of buttons per row
          int count = 0;

          for (Letter letter : letters.getLettersSet()) {
            for (int i = 0; i < letters.getLetterCount(letter); i++) {
              ToggleButton toggleButton = new ToggleButton(letter.toString());
              toggleButtons.add(toggleButton);
              row.getChildren().add(toggleButton);
              count++;

              // If the row is full, add it to the content and start a new row
              if (count % buttonsPerRow == 0) {
                content.getChildren().add(row);
                row = new HBox(10);
              }
            }
          }

          // Add the last row if it has buttons
          if (!row.getChildren().isEmpty()) {
            content.getChildren().add(row);
          }

          dialog.getDialogPane().setContent(content);

          // Add OK and Cancel buttons
          ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
          dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

          // Handle the result
          dialog.setResultConverter(
              dialogButton -> {
                if (dialogButton == okButtonType) {
                  List<Letter> selectedLetters = new java.util.ArrayList<>();
                  for (ToggleButton toggleButton : toggleButtons) {
                    if (toggleButton.isSelected()) {
                      selectedLetters.add(new Letter(toggleButton.getText().charAt(0)));
                    }
                  }
                  // TODO: Replace with an askAction call to handle the selected letters
                  return selectedLetters;
                }
                return null;
              });

          // Show the dialog and process the selected letters
          dialog
              .showAndWait()
              .ifPresent(
                  selectedLetters -> {
                    if (!selectedLetters.isEmpty()) {
                      System.out.println("Selected letters to exchange: " + selectedLetters);
                    } else {
                      System.out.println("No letters selected for exchange.");
                    }
                  });
        });

    Button passTurnButton = new Button("Pass Turn");
    passTurnButton.setOnAction(e -> System.out.println("Pass Turn action triggered"));

    Button renamePlayerButton = new Button("Rename Player");
    renamePlayerButton.setOnAction(
        (ActionEvent event) -> {
          // Création de la boîte de dialogue
          Dialog<String> dialog = new Dialog<>();
          dialog.setTitle("Rename Player");

          // Boutons OK et Annuler
          ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
          dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

          // Champ de texte pour entrer le nouveau nom
          TextField nameField = new TextField();
          nameField.setPromptText("New player name");

          VBox content = new VBox();
          content.setSpacing(10);
          content.getChildren().addAll(new Label("Enter the new name of the player:"), nameField);

          dialog.getDialogPane().setContent(content);

          // Récupération du nom entré
          dialog.setResultConverter(
              dialogButton -> {
                if (dialogButton == okButtonType) {
                  return nameField.getText().trim();
                }
                return null;
              });

          // Affichage de la boîte de dialogue
          dialog
              .showAndWait()
              .ifPresent(
                  newName -> {
                    if (!newName.isEmpty()) {
                      System.out.println(
                          "Rename Player action triggered with new name: "
                              + newName); // TODO : Replace with a askAction call
                    }
                  });
        });

    Button forfeitPlayerButton = new Button("Forfeit Player");
    forfeitPlayerButton.setOnAction(e -> System.out.println("Forfeit Player action triggered"));

    Button restartGameButton = new Button("Restart Game");
    restartGameButton.setOnAction(e -> System.out.println("Restart Game action triggered"));

    // Adding buttons to the layout in a horizontal line
    HBox actionButtons =
        new HBox(
            10,
            placeWordButton,
            exchangeLetterButton,
            passTurnButton,
            renamePlayerButton,
            forfeitPlayerButton,
            restartGameButton);
    actionButtons.setStyle("-fx-padding: 10px; -fx-alignment: center;");

    VBox root = new VBox(menuBar, label, scrabbleBoard, actionButtons);

    Scene scene = new Scene(root, 1200, 800);

    stage.setTitle("Scrabble - GUI");
    stage.setScene(scene);

    stage.setOnCloseRequest(
        event -> {
          stage.close();
          System.exit(0);
        });

    stage.setResizable(false);
    stage.show();
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
  public void announceTurn(Player currentPlayer, int turn) {
    throw new UnsupportedOperationException("Unimplemented method 'announceTurn'");
  }

  @Override
  public void showError(CustomError e) {
    throw new UnsupportedOperationException("Unimplemented method 'showError'");
  }

  @Override
  public void showError(Throwable e) {
    throw new UnsupportedOperationException("Unimplemented method 'showError'");
  }
}
