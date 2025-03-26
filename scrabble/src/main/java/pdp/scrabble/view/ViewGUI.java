package pdp.scrabble.view;

import java.util.List;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pdp.scrabble.configuration.Configuration;
import pdp.scrabble.dictionary.Letter;
import pdp.scrabble.dictionary.LettersCounter;
import pdp.scrabble.errorsHandling.CustomError;
import pdp.scrabble.game.Grid;
import pdp.scrabble.game.Lang;
import pdp.scrabble.game.LettersDrawer;
import pdp.scrabble.game.Player;
import pdp.scrabble.game.actions.GameAction;
import pdp.scrabble.view.Component.DisplayTurn;
import pdp.scrabble.view.Component.PlayerHand;
import pdp.scrabble.view.Component.ScrabbleBoard;

public class ViewGUI implements IView {
  private Stage primaryStage;
  private HBox mainRow;
  private static Lang lang;
  private static Boolean isSuperScrabble;

  private ScrabbleBoard scrabbleBoard;
  private PlayerHand playerHand;
  private DisplayTurn displayTurn;

  public ViewGUI(Lang lang, Boolean isSuperScrabble, Stage stage) {
    this.lang = lang;
    this.isSuperScrabble = isSuperScrabble;

    this.primaryStage = stage;

    // MENU BAR INITIALIZATION
    MenuBar menuBar = new MenuBar();

    // First -> File : New Game | Load Game | Save Game | Help | Settings | Quit
    Menu fileMenu = new Menu("File");

    // Declaration of the Items
    MenuItem newGameItem = new MenuItem("New Game");
    MenuItem loadGameItem = new MenuItem("Load Game");
    MenuItem saveGameItem = new MenuItem("Save Game");
    MenuItem helpItem = new MenuItem(("help"));
    MenuItem settingsItem = new MenuItem(("settings"));
    MenuItem quitItem = new MenuItem(("quit"));

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
    Menu gameMenu = new Menu(("game"));

    MenuItem hintItem = new MenuItem(("hint"));
    MenuItem undoAllItem = new MenuItem(("undoall"));
    MenuItem undoItem = new MenuItem(("undo"));
    MenuItem redoItem = new MenuItem(("redo"));
    MenuItem redoAllItem = new MenuItem(("redoall"));

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
    Menu aboutMenu = new Menu(("about"));

    MenuItem versionItem = new MenuItem(("version"));
    MenuItem authorItem = new MenuItem(("author"));
    MenuItem licenceItem = new MenuItem(("license"));

    // Actions for Items

    // TODO: Display the version of the game
    versionItem.setOnAction(
        (ActionEvent event) -> {
          Alert alert = new Alert(Alert.AlertType.INFORMATION);
          alert.setTitle(("version"));
          alert.setHeaderText(null);
          alert.setContentText("Version 1.0.0");
          alert.showAndWait();
        });

    // TODO: Display the author of the game
    authorItem.setOnAction(
        (ActionEvent event) -> {
          Alert alert = new Alert(Alert.AlertType.INFORMATION);
          alert.setTitle(("author"));
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

    // TODO: Display the licence of the gam
    licenceItem.setOnAction(
        (ActionEvent event) -> {
          Alert alert = new Alert(Alert.AlertType.INFORMATION);
          alert.setTitle(("licence"));
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

    // Historique des actions
    // Create a gray square aligned with the ScrabbleBoard
    StackPane histo = new StackPane();
    histo.setStyle("-fx-background-color: lightgray; -fx-border-color: black;");
    histo.setPrefSize(200, 400);

    // GRID INITIALIZATION
    this.scrabbleBoard = new ScrabbleBoard(600, 600, 15);

    // Player score
    VBox playerScores = new VBox();
    playerScores.setSpacing(10);
    playerScores.setStyle("-fx-alignment: center;"); // Center the VBox

    // Example players and scores (replace with actual player data)
    List<String> players = List.of("Player 1: 10", "Player 2: 20", "Player 3: 30", "Player 4: 40");

    for (String player : players) {
      // Create a gray square for each player
      StackPane playerBox = new StackPane();
      playerBox.setStyle("-fx-background-color: lightgray; -fx-border-color: black;");
      playerBox.setPrefSize(200, 50); // Size of each square

      // Display the player's name and score
      String[] playerData = player.split(": ");
      Label playerLabel = new Label(playerData[0] + ": " + playerData[1]);
      playerLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");
      playerLabel.setAlignment(Pos.CENTER);

      // Add the label to the square
      playerBox.getChildren().add(playerLabel);

      // Add the square to the VBox
      playerScores.getChildren().add(playerBox);
    }

    // Combine histo, scrabbleBoard, and playerScores into a single row
    mainRow = new HBox(10, histo, scrabbleBoard, playerScores);
    mainRow.setStyle("-fx-alignment: center; -fx-padding: 10px;");

    // Main du joueur provisoir :
    // player's letters
    // LettersCounter letters = new LettersCounter();
    // letters.addLetter(new Letter('A'));
    // letters.addLetter(new Letter('P'));
    // letters.addLetter(new Letter('Q'));
    // letters.addLetter(new Letter('O'));
    // letters.addLetter(new Letter('W'));
    // letters.addLetter(new Letter('V'));
    // letters.addLetter(new Letter('M'));

    // Display Turn
    this.displayTurn = new DisplayTurn(lang);

    // Player Hand

    this.playerHand = new PlayerHand(lang, isSuperScrabble);
    // this.playerHand.updateLetters(letters);

    // ACTION BUTTONS --------------------------------------------
    Button placeWordButton = new Button(("placew"));
    placeWordButton.setOnAction(e -> System.out.println("Place Word action triggered"));

    Button exchangeLetterButton = new Button(("exchange"));
    exchangeLetterButton.setOnAction(
        e -> {
          // Retrieve the current player's LettersCounter
          // LettersCounter letters = getCurrentPlayerLetters(); // Replace with the actual method
          // to retrieve the LettersCounter
          LettersCounter letters = new LettersCounter();

          // Create a dialog for selecting letters to exchange
          Dialog<java.util.List<Letter>> dialog = new Dialog<>();
          dialog.setTitle(("exchange"));
          dialog.setHeaderText(("exchangetxt"));

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
                  // Call askAction to handle the selected letters
                  askAction(null, null); // Replace null with appropriate parameters
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

    Button passTurnButton = new Button(("pass"));
    passTurnButton.setOnAction(e -> System.out.println("Pass Turn action triggered"));

    Button renamePlayerButton = new Button(("rename"));
    renamePlayerButton.setOnAction(
        (ActionEvent event) -> {
          // Création de la boîte de dialogue
          Dialog<String> dialog = new Dialog<>();
          dialog.setTitle(("rename"));

          // Boutons OK et Annuler
          ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
          dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

          // Champ de texte pour entrer le nouveau nom
          TextField nameField = new TextField();
          nameField.setPromptText(("renametxt1"));

          VBox content = new VBox();
          content.setSpacing(10);
          content.getChildren().addAll(new Label(("renametxt2")), nameField);

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

    Button forfeitPlayerButton = new Button(("forfeit"));
    forfeitPlayerButton.setOnAction(e -> System.out.println("Forfeit Player action triggered"));

    Button restartGameButton = new Button(("restart"));
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

    VBox root = new VBox(menuBar, displayTurn, mainRow, playerHand, actionButtons);

    Scene scene = new Scene(root, 1200, 800);

    primaryStage.setTitle("Scrabble - GUI");
    primaryStage.setScene(scene);

    primaryStage.setOnCloseRequest(
        event -> {
          primaryStage.close();
          System.exit(0);
        });

    primaryStage.setResizable(false);
    primaryStage.show();
  }

  // --- IView methods ---
  @Override
  public GameAction askAction(Player player, Integer turn) {
    throw new UnsupportedOperationException("Unimplemented method 'askAction'");
  }

  @Override
  public void displayGrid(Grid grid, Boolean isSuperScrabble) {
    // Update the scrabble board with the new grid
    System.out.println("Updating scrabble board with new grid");
    this.mainRow.getChildren().remove(scrabbleBoard);
    this.scrabbleBoard.update(grid, isSuperScrabble);
    this.mainRow.getChildren().add(1, scrabbleBoard);
  }

  @Override
  public void displayPlayerLetters(LettersCounter letters) {
    this.playerHand.updateLetters(letters);
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
    this.displayTurn.updateTurn(turn, currentPlayer);
  }

  @Override
  public void showError(CustomError e) {
    throw new UnsupportedOperationException("Unimplemented method 'showError'");
  }

  @Override
  public void showError(Throwable e) {
    // Dialog to display the error message
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle(("error"));
    alert.setHeaderText(("error"));
    alert.setContentText(e.getMessage());
    alert.showAndWait();
  }

  @Override
  public void initialize(Configuration conf) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'initialize'");
  }
}
