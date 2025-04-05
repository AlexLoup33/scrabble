package pdp.scrabble.view;

import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Semaphore;
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
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pdp.scrabble.configuration.Configuration;
import pdp.scrabble.dictionary.Letter;
import pdp.scrabble.dictionary.LettersCounter;
import pdp.scrabble.errorsHandling.CustomError;
import pdp.scrabble.errorsHandling.ErrorType;
import pdp.scrabble.game.Direction;
import pdp.scrabble.game.GameModel;
import pdp.scrabble.game.Grid;
import pdp.scrabble.game.GridWordPos;
import pdp.scrabble.game.Lang;
import pdp.scrabble.game.LettersDrawer;
import pdp.scrabble.game.Player;
import pdp.scrabble.game.RandomAPI;
import pdp.scrabble.game.actions.ExchangeLetterAction;
import pdp.scrabble.game.actions.ForfeitGameAction;
import pdp.scrabble.game.actions.GameAction;
import pdp.scrabble.game.actions.PassTurnAction;
import pdp.scrabble.game.actions.PlaceWordAction;
import pdp.scrabble.game.actions.QuitAction;
import pdp.scrabble.game.actions.RedoAction;
import pdp.scrabble.game.actions.RenamePlayerAction;
import pdp.scrabble.game.actions.RestartGameAction;
import pdp.scrabble.game.actions.UndoAction;
import pdp.scrabble.view.Component.ActionHistory;
import pdp.scrabble.view.Component.DisplayTurn;
import pdp.scrabble.view.Component.PlayerHand;
import pdp.scrabble.view.Component.ScrabbleBoard;
import pdp.scrabble.view.Component.ShowScore;

public class ViewGUI implements IView {

  private Stage primaryStage;
  private final HBox mainRow;
  private static Lang lang;
  private static Boolean isSuperScrabble;
  private final ResourceBundle langPack;

  private final ScrabbleBoard scrabbleBoard;
  private final PlayerHand playerHand;
  private final DisplayTurn displayTurn;
  private final ShowScore playerScores;
  private final ActionHistory actionHistory;

  private Semaphore semaphore = new Semaphore(1);
  private String action = null;
  // FIXME : Create a new player temporarily for the action
  private Player currentPlayerTEMP = new Player(0, 100);
  private Integer currentTurnTEMP = 0;

  // end FIXME

  public ViewGUI(Lang lang, Boolean isSuperScrabble, Stage stage, int gridSize) {
    this.lang = lang;
    this.isSuperScrabble = isSuperScrabble;
    this.langPack = lang.getViewGUI_langPack();

    // FIXME : Create a new player temporarily for the action
    RandomAPI rngGenerator = new RandomAPI(1L);
    LettersDrawer draw = new LettersDrawer(this.lang, rngGenerator, false);
    for (int i = currentPlayerTEMP.bag.size(); i < 7; i++) {
      currentPlayerTEMP.bag.addLetter(draw.popLetter());
    }
    // end FIXME

    this.primaryStage = stage;

    // MENU BAR INITIALIZATION
    MenuBar menuBar = new MenuBar();

    // First -> File : New Game | Load Game | Save Game | Help | Settings | Quit
    Menu fileMenu = new Menu(langPack.getString("file"));

    // Declaration of the Items
    MenuItem newGameItem = new MenuItem(langPack.getString("ngame"));
    MenuItem loadGameItem = new MenuItem(langPack.getString("lgame"));
    MenuItem saveGameItem = new MenuItem(langPack.getString("sgame"));
    MenuItem helpItem = new MenuItem(langPack.getString("help"));
    MenuItem settingsItem = new MenuItem(langPack.getString("settings"));
    MenuItem quitItem = new MenuItem(langPack.getString("quit"));

    // Actions for Items
    // New Game
    // TODO: Ask the controller to create a new game.
    // --> TODO: implement restartGameAction
    // newGameItem.setOnAction(e -> RestartGameAction.restart());
    newGameItem.setOnAction(e -> action = "NewGame");

    // Load Game
    // TODO: Open a file selector to load a game.
    loadGameItem.setOnAction(e -> action = "LoadGame");

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
    quitItem.setOnAction(
        e -> {
          Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
          alert.setTitle(langPack.getString("quit"));
          alert.setHeaderText(langPack.getString("savebeforequit"));
          alert.setContentText(langPack.getString("saveprompt"));

          ButtonType saveAndQuit = new ButtonType(langPack.getString("saveandquit"));
          ButtonType quitWithoutSaving = new ButtonType(langPack.getString("quitnosave"));
          ButtonType cancel =
              new ButtonType(langPack.getString("cancel"), ButtonBar.ButtonData.CANCEL_CLOSE);

          alert.getButtonTypes().setAll(saveAndQuit, quitWithoutSaving, cancel);

          alert
              .showAndWait()
              .ifPresent(
                  response -> {
                    if (response == saveAndQuit) {
                      new QuitAction(currentPlayerTEMP, currentTurnTEMP, true);
                    } else if (response == quitWithoutSaving) {
                      new QuitAction(currentPlayerTEMP, currentTurnTEMP, false);
                    }
                  });
        });

    // Adding all item to fileMenu
    fileMenu
        .getItems()
        .addAll(newGameItem, loadGameItem, saveGameItem, helpItem, settingsItem, quitItem);

    // Second -> Game : Hint | Undo | Redo
    Menu gameMenu = new Menu(langPack.getString("game"));

    MenuItem hintItem = new MenuItem(langPack.getString("hint"));
    MenuItem undoAllItem = new MenuItem(langPack.getString("undoall"));
    MenuItem undoItem = new MenuItem(langPack.getString("undo"));
    MenuItem redoItem = new MenuItem(langPack.getString("redo"));
    MenuItem redoAllItem = new MenuItem(langPack.getString("redoall"));

    // Action for Items
    // TODO: Display colored letters hint on the grid
    hintItem.setOnAction(e -> System.out.println("Hint..."));

    // TODO: Undo all actions
    undoAllItem.setOnAction(e -> System.out.println("Undo All..."));

    // TODO: Undo the last action
    undoItem.setOnAction(e -> new UndoAction(currentPlayerTEMP, currentTurnTEMP));

    // TODO: Redo the last action
    redoItem.setOnAction(e -> new RedoAction(currentPlayerTEMP, currentTurnTEMP));

    // TODO: Redo all actions
    redoAllItem.setOnAction(e -> System.out.println("Redo All..."));

    gameMenu.getItems().addAll(hintItem, undoAllItem, undoItem, redoItem, redoAllItem);

    // Third -> About : Version | Author | Licence
    Menu aboutMenu = new Menu(langPack.getString("about"));

    MenuItem versionItem = new MenuItem(langPack.getString("version"));
    MenuItem authorItem = new MenuItem(langPack.getString("author"));
    MenuItem licenceItem = new MenuItem(langPack.getString("license"));

    // Actions for Items
    // TODO: Display the version of the game
    versionItem.setOnAction(
        (ActionEvent event) -> {
          Alert alert = new Alert(Alert.AlertType.INFORMATION);
          alert.setTitle(langPack.getString("version"));
          alert.setHeaderText(null);
          alert.setContentText("Version 1.0.0");
          alert.showAndWait();
        });

    // TODO: Display the author of the game
    authorItem.setOnAction(
        (ActionEvent event) -> {
          Alert alert = new Alert(Alert.AlertType.INFORMATION);
          alert.setTitle(langPack.getString("author"));
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
          alert.setTitle(langPack.getString("licence"));
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

    // HISTO INITIALIZATION
    this.actionHistory = new ActionHistory();

    // GRID INITIALIZATION
    this.scrabbleBoard = new ScrabbleBoard(600, 600, gridSize);

    // Player score
    this.playerScores = new ShowScore();

    // Combine histo, scrabbleBoard, and playerScores into a single row
    mainRow = new HBox(10, actionHistory, scrabbleBoard, playerScores);
    mainRow.setStyle("-fx-alignment: center; -fx-padding: 10px;");

    // Display Turn
    this.displayTurn = new DisplayTurn(lang);

    // Player Hand
    this.playerHand = new PlayerHand(lang, isSuperScrabble);
    // this.playerHand.updateLetters(letters);

    // ACTION BUTTONS --------------------------------------------
    Button placeWordButton = new Button(langPack.getString("placew"));
    placeWordButton.setOnAction(e -> handlePlaceWord(currentPlayerTEMP, currentTurnTEMP));

    Button exchangeLetterButton = new Button(langPack.getString("exchange"));
    exchangeLetterButton.setOnAction(e -> handleExchangeLetter(currentPlayerTEMP, currentTurnTEMP));

    Button renamePlayerButton = new Button(langPack.getString("rename"));
    renamePlayerButton.setOnAction(e -> hadleNewPlayerName(currentPlayerTEMP, currentTurnTEMP));

    Button passTurnButton = new Button(langPack.getString("pass"));
    passTurnButton.setOnAction(e -> new PassTurnAction(currentPlayerTEMP, currentTurnTEMP));

    Button forfeitPlayerButton = new Button(langPack.getString("forfeit"));
    forfeitPlayerButton.setOnAction(e -> new ForfeitGameAction(currentPlayerTEMP, currentTurnTEMP));

    Button restartGameButton = new Button(langPack.getString("restart"));
    restartGameButton.setOnAction(e -> new RestartGameAction(currentPlayerTEMP, currentTurnTEMP));

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

  // --- Intern methods ---
  private void handlePlaceWord(Player currentPlayer, Integer currentTurn) {
    Dialog<String[]> dialog = new Dialog<>();
    dialog.setTitle(langPack.getString("placew"));
    dialog.setHeaderText(langPack.getString("placewdesc"));

    // OK and Cancel buttons
    ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
    dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

    // Text field for the word to place
    TextField wordField = new TextField();
    wordField.setPromptText(langPack.getString("enterword"));

    // Text field for the starting position
    TextField positionField = new TextField();
    positionField.setPromptText(langPack.getString("enterposition"));

    // Toggle buttons for orientation
    ToggleButton horizontalButton = new ToggleButton(langPack.getString("horizontal"));
    ToggleButton verticalButton = new ToggleButton(langPack.getString("vertical"));
    ToggleGroup orientationGroup = new ToggleGroup();
    horizontalButton.setToggleGroup(orientationGroup);
    verticalButton.setToggleGroup(orientationGroup);

    // Layout for orientation buttons
    HBox orientationBox = new HBox(10, horizontalButton, verticalButton);
    orientationBox.setStyle("-fx-alignment: center;");

    // Combine all inputs into a VBox
    VBox content = new VBox(10, wordField, positionField, orientationBox);
    content.setStyle("-fx-padding: 10px;");
    dialog.getDialogPane().setContent(content);

    // Handle the result
    dialog.setResultConverter(
        dialogButton -> {
          if (dialogButton == okButtonType) {
            String word = wordField.getText().trim();
            String position = positionField.getText().trim();
            String orientation =
                horizontalButton.isSelected() ? "H" : verticalButton.isSelected() ? "V" : null;
            if (!word.isEmpty() && !position.isEmpty() && orientation != null) {
              return new String[] {word, position, orientation};
            }
          }
          return null;
        });

    // Show the dialog and process the result
    String[] result = dialog.showAndWait().orElse(null);
    if (result != null) {
      String word = result[0].toUpperCase();
      String position = result[1];
      String orientation = result[2];

      // Parse the position (e.g., "a1" -> row=0, col=0)
      int row;
      try {
        row = position.charAt(0) - 'a';
      } catch (IndexOutOfBoundsException e) {
        throw new CustomError( // FIXME : Ludo 🙏 (même si c'est oob ça renvoi la gameAction donc
            // jsp ou ça devrai renvoyer une erreure)
            ErrorType.InvalidMoveFormat,
            "Invalid position format. Expected format: 'a1', 'b2', etc.",
            "The position: " + position + " isn't valid");
      }
      int col;
      try {
        col = Integer.parseInt(position.substring(1)) - 1;
      } catch (NumberFormatException e) {
        throw new CustomError( // FIXME : Ludo 🙏
            ErrorType.InvalidMoveFormat,
            "Invalid position format. Expected format: 'a1', 'b2', etc.",
            "The position: " + position + " isn't valid");
      }

      // Determine the direction
      final Direction direction;
      switch (orientation) {
        case "H" -> direction = Direction.Right;
        case "V" -> direction = Direction.Down;
        default ->
            throw new CustomError(
                ErrorType.InvalidMoveFormat,
                "Invalid orientation. Expected 'H' or 'V'.",
                "The orientation: " + orientation + " isn't valid");
      }

      // Create the GridWordPos object
      GridWordPos gridWordPos = new GridWordPos(row, col, direction);

      // Convert the word into an array of Letters
      Letter[] toPlace = new Letter[word.length()];
      for (int i = 0; i < word.length(); i++) {
        toPlace[i] = new Letter(word.charAt(i));
      }

      // Create the PlaceWordAction
      GameAction action = new PlaceWordAction(currentPlayer, currentTurn, toPlace, gridWordPos);
      System.out.println("Game action: " + action);
    }
  }

  private void handleExchangeLetter(Player currentPlayer, Integer currentTurn) {
    System.out.println("Exchange Letter action triggered");
    LettersCounter letters = currentPlayer.bag;

    // Create a dialog for selecting letters to exchange
    Dialog<java.util.List<Letter>> dialog = new Dialog<>();
    dialog.setTitle(langPack.getString("exchange"));
    dialog.setHeaderText(langPack.getString("exchangetxt"));

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
            return selectedLetters.isEmpty() ? null : selectedLetters;
          }
          return null;
        });
    // Show the dialog and process the selected letters
    List<Letter> selectedLetters = dialog.showAndWait().orElse(null);
    if (selectedLetters != null) {
      GameAction action = new ExchangeLetterAction(currentPlayer, currentTurn, selectedLetters);
      System.out.println("Game action :" + action);
    }
  }

  private void hadleNewPlayerName(Player currentPlayer, Integer currentTurn) {
    Dialog<String> dialog = new Dialog<>();
    dialog.setTitle(langPack.getString("rename"));
    dialog.setHeaderText(langPack.getString("renametxt") + currentPlayer.getName());

    // OK and Cancel buttons
    ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
    dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

    // Text field for entering the new name
    TextField nameField = new TextField();
    nameField.setPromptText(langPack.getString("renametxt1"));

    VBox content = new VBox();
    content.setSpacing(10);
    content.getChildren().addAll(new Label(langPack.getString("renametxt2")), nameField);

    dialog.getDialogPane().setContent(content);

    // Retrieve the entered name
    dialog.setResultConverter(
        dialogButton -> {
          if (dialogButton == okButtonType) {
            return nameField.getText().trim();
          }
          return null;
        });

    // Display the dialog and return the result
    String newName = dialog.showAndWait().orElse(null);
    if (newName != null && !newName.isEmpty()) {
      GameAction action = new RenamePlayerAction(currentPlayer, currentTurn, newName);
      System.out.println("Game action :" + action);
    }
  }

  // --- IView methods ---
  @Override
  public GameAction askAction(Player player, Integer turn) {
    if (action == null) {
      return null;
    } else {
      switch (action) {
        case "passTurn" -> {
          System.out.println("Pass Turn");
          action = null;
          return passTurnAction(player, turn);
        }
        default -> {
          break;
        }
      }
    }

    return null;
  }

  private PassTurnAction passTurnAction(Player player, Integer turn) {
    this.actionHistory.addMessage(player.getName() + " " + langPack.getString("passaction"));
    return new PassTurnAction(player, turn);
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
    if (!langPack.containsKey(identifiant)) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle(langPack.getString("error"));
      alert.setHeaderText(langPack.getString("error"));
      alert.setContentText(
          "Key : "
              + identifiant
              + " not in: "
              + langPack
              + "\nInvalid KeyArgument. Please check the file pack_ and verify your keys");
      alert.showAndWait();
      throw new CustomError(
          ErrorType.InvalidLangPackIdentifier,
          "Key : " + identifiant + " not in:" + langPack,
          "Invalid KeyArgument. Please check the file pack_ and verify your keys");
    }
    return langPack.getString(identifiant);
  }

  @Override
  public void showScore(Player player) {
    this.playerScores.updateScore(player);
  }

  @Override
  public void showScore(java.util.List<Player> players) {
    this.playerScores.updateScores(players);
  }

  @Override
  public void displayDraw(LettersDrawer draw) {
    this.actionHistory.addMessage(langPack.getString("drawaction") + " " + draw.getCurrentSize());
  }

  @Override
  public void announceTurn(Player currentPlayer, int turn) {
    this.actionHistory.addMessage(
        langPack.getString("turn")
            + " "
            + turn
            + " | "
            + langPack.getString("player")
            + " "
            + currentPlayer.getName());
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
    alert.setTitle(langPack.getString("error"));
    alert.setHeaderText(langPack.getString("error"));
    alert.setContentText(e.getMessage());
    alert.showAndWait();
  }

  @Override
  public void initialize(Configuration conf) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'initialize'");
  }

  @Override
  public void showRemainingTimes(List<Player> players) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'showRemainingTimes'");
  }

  @Override
  public void updateGUI(GameModel model) {
    this.displayGrid(model.grid, model.config.getGui());
    this.showScore(model.getCurrentPlayer());
    this.showScore(model.players);
    this.displayTurn.updateTurn(model.getTurn(), model.getCurrentPlayer());
    this.announceTurn(model.getCurrentPlayer(), model.getTurn());
    this.displayDraw(model.draw);
    this.displayPlayerLetters(model.getCurrentPlayer().bag);
  }
}
