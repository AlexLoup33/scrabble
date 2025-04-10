package pdp.scrabble.view.GUI;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pdp.scrabble.Utilities;
import pdp.scrabble.configuration.Configuration;
import pdp.scrabble.dictionary.Letter;
import pdp.scrabble.dictionary.LettersCounter;
import pdp.scrabble.errorsHandling.CustomError;
import pdp.scrabble.errorsHandling.ErrorType;
import pdp.scrabble.game.Direction;
import pdp.scrabble.game.GameModel;
import pdp.scrabble.game.Grid;
import pdp.scrabble.game.GridWordPos;
import pdp.scrabble.game.LettersDrawer;
import pdp.scrabble.game.Player;
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
import pdp.scrabble.game.serializer.Serializer;
import pdp.scrabble.view.GUI.Component.ActionHistory;
import pdp.scrabble.view.GUI.Component.DisplayTurn;
import pdp.scrabble.view.GUI.Component.PlayerHand;
import pdp.scrabble.view.GUI.Component.ScrabbleBoard;
import pdp.scrabble.view.GUI.Component.ShowScore;
import pdp.scrabble.view.IView;

public class ViewGUI implements IView {
  private GameModel gameModel;

  private final Stage primaryStage;
  private Scene scene;
  private VBox root;
  private HBox mainRow;
  private ResourceBundle langPack;

  private ScrabbleBoard scrabbleBoard;
  private PlayerHand playerHand;
  private DisplayTurn displayTurn;
  private ShowScore playerScores;
  private ActionHistory actionHistory;
  private long lastActionTimeMs;

  public ViewGUI(Stage stage, GameModel gameModel) {
    this.primaryStage = stage;

    this.setupFromNewModel(gameModel);
    this.scene = new Scene(this.root, 1200, 800);

    primaryStage.setTitle("Scrabble - GUI");
    primaryStage.setScene(this.scene);

    primaryStage.setOnCloseRequest(
        event -> {
          primaryStage.close();
          System.exit(0);
        });

    primaryStage.setResizable(false);
    primaryStage.show();
    this.updateGUI();
  }

  public ViewGUI(Stage stage, Configuration configuration) {
    this(stage, ViewGUI.constructModelFromConfig(configuration));
  }

  private static GameModel constructModelFromConfig(Configuration configuration) {
    if (configuration.getHistoryFile() != null) {
      return Serializer.loadModelFromFile(configuration.getHistoryFile());
    } else {
      return new GameModel(configuration);
    }
  }

  private void setupFromNewModel(GameModel newModel) {
    this.gameModel = newModel;
    this.langPack = this.gameModel.lang.getViewGUI_langPack();
    this.lastActionTimeMs = System.currentTimeMillis();

    // MENU BAR INITIALIZATION
    MenuBar menuBar = new MenuBar();

    Menu fileMenu = new Menu(langPack.getString("file"));
    Menu gameMenu = new Menu(langPack.getString("game"));
    Menu aboutMenu = new Menu(langPack.getString("about"));

    // First -> File : New Game | Load Game | Save Game | Help | Settings | Quit
    // Declaration of the Items
    MenuItem newGameItem = new MenuItem(langPack.getString("ngame"));
    MenuItem loadGameItem = new MenuItem(langPack.getString("lgame"));
    MenuItem saveGameItem = new MenuItem(langPack.getString("sgame"));
    MenuItem helpItem = new MenuItem(langPack.getString("help"));
    MenuItem settingsItem = new MenuItem(langPack.getString("settings"));
    MenuItem quitItem = new MenuItem(langPack.getString("quit"));
    // Actions for Items
    newGameItem.setOnAction(e -> this.handleNewGame());
    loadGameItem.setOnAction(e -> this.handleLoadGame());
    saveGameItem.setOnAction(e -> this.handleSaveGame());
    helpItem.setOnAction(e -> this.handleShowHelp());
    settingsItem.setOnAction(e -> this.handleSettings());
    quitItem.setOnAction(e -> this.handleQuit());
    // Adding all item to fileMenu
    fileMenu.getItems().addAll(newGameItem, loadGameItem, saveGameItem);
    fileMenu.getItems().addAll(helpItem, settingsItem, quitItem);

    // Second -> Game : Hint | Undo | Redo
    MenuItem hintItem = new MenuItem(langPack.getString("hint"));
    MenuItem undoItem = new MenuItem(langPack.getString("undo"));
    MenuItem redoItem = new MenuItem(langPack.getString("redo"));
    MenuItem undoAllItem = new MenuItem(langPack.getString("undoall"));
    MenuItem redoAllItem = new MenuItem(langPack.getString("redoall"));
    // Action for Items
    hintItem.setOnAction(e -> this.handleShowHint());
    undoItem.setOnAction(e -> this.handleUndoOne());
    redoItem.setOnAction(e -> this.handleRedoOne());
    undoAllItem.setOnAction(e -> this.handleUndoAll());
    redoAllItem.setOnAction(e -> this.handleRedoAll());
    // adding all items
    gameMenu.getItems().addAll(hintItem, undoAllItem, undoItem, redoItem, redoAllItem);

    // Third -> About : Version | Author | Licence
    MenuItem versionItem = new MenuItem(langPack.getString("version"));
    MenuItem authorItem = new MenuItem(langPack.getString("author"));
    MenuItem licenceItem = new MenuItem(langPack.getString("license"));
    // Actions for Items
    versionItem.setOnAction(
        e -> {
          this.handleShowVersion();
        });
    authorItem.setOnAction(
        e -> {
          this.handleShowAuthors();
        });
    licenceItem.setOnAction(
        e -> {
          this.handleShowLicense();
        });
    // add all the items
    aboutMenu.getItems().addAll(versionItem, authorItem, licenceItem);

    // Adding all the Menu in the MenuBar and adding a bit of style
    menuBar.getMenus().addAll(fileMenu, gameMenu, aboutMenu);
    menuBar.setStyle("-fx-start-margin: 200px;");

    // HISTO INITIALIZATION
    this.actionHistory = new ActionHistory();
    // GRID INITIALIZATION
    this.scrabbleBoard = new ScrabbleBoard(600, 600, this.gameModel.grid.size);
    // Player score
    this.playerScores = new ShowScore();
    // Combine histo, scrabbleBoard, and playerScores into a single row
    mainRow = new HBox(10, actionHistory, scrabbleBoard, playerScores);
    mainRow.setStyle("-fx-alignment: center; -fx-padding: 10px;");
    // Display Turn
    this.displayTurn = new DisplayTurn(this.gameModel.lang);
    // Player Hand
    this.playerHand = new PlayerHand(this.gameModel.lang, this.gameModel.config.superScrabble());

    // ACTION BUTTONS --------------------------------------------
    Button placeWordButton = new Button(langPack.getString("placew"));
    Button exchangeLetterButton = new Button(langPack.getString("exchange"));
    Button renamePlayerButton = new Button(langPack.getString("rename"));
    Button passTurnButton = new Button(langPack.getString("pass"));
    Button forfeitPlayerButton = new Button(langPack.getString("forfeit"));
    Button restartGameButton = new Button(langPack.getString("restart"));

    placeWordButton.setOnAction(e -> this.handlePlaceWord());
    exchangeLetterButton.setOnAction(e -> this.handleExchangeLetter());
    renamePlayerButton.setOnAction(e -> this.handleNewPlayerName());
    passTurnButton.setOnAction(e -> this.handlePassTurn());
    forfeitPlayerButton.setOnAction(e -> this.handleForfeitPlayer());
    restartGameButton.setOnAction(e -> this.handleRestartGame());

    // Adding buttons to the layout in a horizontal line
    HBox actionButtons = new HBox(10);
    actionButtons.getChildren().addAll(placeWordButton, exchangeLetterButton, passTurnButton);
    actionButtons.getChildren().addAll(renamePlayerButton, forfeitPlayerButton, restartGameButton);
    actionButtons.setStyle("-fx-padding: 10px; -fx-alignment: center;");

    if (this.root == null) {
      this.root = new VBox();
    } else {
      this.root.getChildren().clear();
    }
    this.root.getChildren().addAll(menuBar, displayTurn, mainRow, playerHand, actionButtons);
    this.updateGUI();
  }

  private void handleRedoAll() {
    boolean ask = this.askYesNo(langPack.getString("redoall"), langPack.getString("redoallText"));
    if (ask) {
      this.gameModel.redoAll();
      this.updateGUI();
    }
  }

  private void handleRedoOne() {
    boolean ask = this.askYesNo(langPack.getString("redo"), langPack.getString("redoText"));
    if (ask) {
      this.executeGameAction(
          new RedoAction(this.gameModel.getCurrentPlayer(), this.gameModel.getTurn()));
    }
  }

  private void handleUndoOne() {
    boolean ask = this.askYesNo(langPack.getString("undo"), langPack.getString("undoText"));
    if (ask) {
      this.executeGameAction(
          new UndoAction(this.gameModel.getCurrentPlayer(), this.gameModel.getTurn()));
    }
  }

  private void handleUndoAll() {
    boolean ask = this.askYesNo(langPack.getString("undoall"), langPack.getString("undoallText"));
    if (ask) {
      this.gameModel.undoAll();
      this.updateGUI();
    }
  }

  private void handleShowHint() {
    this.showSimpleAlert(
        langPack.getString("hint"), AlertType.INFORMATION, langPack.getString("hintText"));
  }

  private boolean askYesNo(String title, String content) {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle(title);
    alert.setContentText(content);

    ButtonType yes = new ButtonType(langPack.getString("yes"));
    ButtonType no = new ButtonType(langPack.getString("no"));

    alert.getButtonTypes().setAll(yes, no);

    AtomicBoolean res = new AtomicBoolean(false);
    alert
        .showAndWait()
        .ifPresent(
            response -> {
              res.set(response == yes);
            });
    return res.get();
  }

  private void showSimpleAlert(String title, AlertType type, String content) {
    Alert alert = new Alert(type);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(content);
    alert.showAndWait();
  }

  private void handleShowLicense() {
    this.showSimpleAlert(
        langPack.getString("license"),
        AlertType.INFORMATION,
        "This game is licensed under the MIT License.\n"
            + "You can do whatever you want with it, as long as you include the original license."
            + "\n\nhttps://opensource.org/licenses/MIT");
  }

  private void handleShowAuthors() {
    this.showSimpleAlert(
        langPack.getString("author"),
        AlertType.INFORMATION,
        "Andrieu Ludovic\nCassard Aymeric\n"
            + "Cazet Valentin\nLou-Poueyou Alexandre\n"
            + "Mayol Jean\nPetry Math\u00e9o\n");
  }

  private void handleShowVersion() {
    this.showSimpleAlert(
        langPack.getString("version"), AlertType.INFORMATION, Utilities.getProjetVersion());
  }

  private void handleQuit() {
    Player player = this.gameModel.getCurrentPlayer();
    int turn = this.gameModel.getTurn();

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
              if (!(response == cancel)) {
                this.executeGameAction(new QuitAction(player, turn, (response != saveAndQuit)));
              }
            });
  }

  private void handleSettings() {
    langPack.getString("settings");
    langPack.getString("settingsTxt");
    // TODO: HERE
  }

  private void handleShowHelp() {
    this.showSimpleAlert(
        langPack.getString("help"), AlertType.INFORMATION, langPack.getString("helpcontent"));
  }

  /**
   * ask where to save
   *
   * @return whether it has save or not
   */
  private boolean handleSaveGame() {
    // create the default filname
    LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    String defaultFileName = now.toString() + ".txt";

    // configure dialog
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Save Game");
    fileChooser.setInitialDirectory(new File("saves"));
    fileChooser.setInitialFileName(defaultFileName);
    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

    // Show dialog and save if a file is selected
    File file = fileChooser.showSaveDialog(this.primaryStage);
    if (file != null) {
      this.gameModel.saveToFile(file.toPath());
      return true;
    }
    return false;
  }

  private void handleLoadGame() {
    // configure dialog
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Load Game");
    fileChooser.setInitialDirectory(new File("saves"));
    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

    // Show dialog and save if a file is selected
    File file = fileChooser.showOpenDialog(this.primaryStage);
    if (file != null) {

      this.setupFromNewModel(Serializer.loadModelFromFile(file.toPath()));
    }
  }

  private void handleNewGame() {
    this.setupFromNewModel(new GameModel(this.gameModel.config.getWithRandomSeed()));
  }

  private void executeGameAction(GameAction ga) {
    // computye the duration of the decision and update last action time
    long currTimeMs = System.currentTimeMillis();
    long durationSec = (currTimeMs - this.lastActionTimeMs) / 1000;
    this.lastActionTimeMs = currTimeMs;
    // exec the action
    try {
      this.gameModel.applyGameActionDispatch(ga, durationSec);
    } catch (CustomError e) {
      this.handleErrors(e);
    } catch (Throwable e) {
      this.handleErrors(e);
    }
    // update the view
    this.updateGUI();
  }

  private void handleErrors(Throwable e) {
    this.showError(e);
    System.exit(1);
  }

  private void handleErrors(CustomError e) {
    if (e.type == ErrorType.ExitNoError) {
      System.exit(0);
    } else if (e.type == ErrorType.ExitWithError) {
      System.exit(1);
    }
    this.showError(e);
    if (e.isFatal()) {
      System.exit(1);
    }
  }

  // --- Intern methods ---
  private void handlePlaceWord() {
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
      this.executeGameAction(
          new PlaceWordAction(
              this.gameModel.getCurrentPlayer(), this.gameModel.getTurn(), toPlace, gridWordPos));
    }
  }

  private void handleExchangeLetter() {
    LettersCounter letters = this.gameModel.getCurrentPlayer().bag;

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
      this.executeGameAction(
          new ExchangeLetterAction(
              this.gameModel.getCurrentPlayer(), this.gameModel.getTurn(), selectedLetters));
    }
  }

  private void handleNewPlayerName() {
    Dialog<String> dialog = new Dialog<>();
    dialog.setTitle(langPack.getString("rename"));
    dialog.setHeaderText(
        langPack.getString("renametxt") + this.gameModel.getCurrentPlayer().getName());

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
      this.executeGameAction(
          new RenamePlayerAction(
              this.gameModel.getCurrentPlayer(), this.gameModel.getTurn(), newName));
    }
  }

  private void handleRestartGame() {
    this.executeGameAction(
        new RestartGameAction(this.gameModel.getCurrentPlayer(), this.gameModel.getTurn()));
  }

  private void handleForfeitPlayer() {
    this.executeGameAction(
        new ForfeitGameAction(this.gameModel.getCurrentPlayer(), this.gameModel.getTurn()));
  }

  // --- IView methods ---
  @Override
  public GameAction askAction(Player player, Integer turn) {
    throw new UnsupportedOperationException("not supported method 'askAction' for the GUI");
  }

  private void handlePassTurn() {
    this.executeGameAction(
        new PassTurnAction(this.gameModel.getCurrentPlayer(), this.gameModel.getTurn()));
  }

  @Override
  public void displayGrid(Grid grid, Boolean isSuperScrabble) {
    // Update the scrabble board with the new grid
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
      throw new CustomError(
          ErrorType.InvalidLangPackIdentifier,
          "Key : " + identifiant + " not in:" + langPack,
          "Invalid KeyArgument. Please check the file pack_ and verify your keys");
    }
    return langPack.getString(identifiant);
  }

  public void showScores(java.util.List<Player> players) {
    this.playerScores.updateScores(players);
  }

  @Override
  public void displayDraw(LettersDrawer draw) {
    // TODO: update somthing on the interface to show the number of remaining letters in drawer
  }

  @Override
  public void announceTurn(Player currentPlayer, int turn) {
    this.displayTurn.updateTurn(turn, currentPlayer);
  }

  @Override
  public void showError(CustomError e) {
    if (e.isFatal()) {
      this.showError((Throwable) e);
    } else {
      this.showErrorBase(e.toString(), AlertType.WARNING);
    }
  }

  @Override
  public void showError(Throwable e) {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    e.printStackTrace(pw);
    this.showErrorBase(sw.toString(), AlertType.ERROR);
  }

  /**
   * generic methode to show an error
   *
   * @param text the text of the error
   * @param type the kind of error for the alert
   */
  private void showErrorBase(String text, AlertType type) {
    // Dialog to display the error message
    Alert alert = new Alert(type);
    alert.setTitle(langPack.getString("error"));
    alert.setHeaderText(langPack.getString("error"));

    TextArea textArea = new TextArea(text);
    textArea.setEditable(false);
    textArea.setWrapText(false);
    textArea.setMaxWidth(Double.MAX_VALUE);
    textArea.setMaxHeight(Double.MAX_VALUE);
    GridPane.setVgrow(textArea, Priority.ALWAYS);
    GridPane.setHgrow(textArea, Priority.ALWAYS);

    alert.getDialogPane().setExpandableContent(textArea);
    alert.getDialogPane().setExpanded(true);
    alert.showAndWait();
  }

  @Override
  public void showRemainingTimes(List<Player> players) {
    this.playerScores.updatePlayersTimes(players);
  }

  public void updateGUI() {
    this.displayGrid(this.gameModel.grid, this.gameModel.config.superScrabble());
    this.showScores(this.gameModel.players);
    if (this.gameModel.config.blitz()) {
      this.showRemainingTimes(this.gameModel.players);
    }
    this.displayTurn.updateTurn(this.gameModel.getTurn(), this.gameModel.getCurrentPlayer());
    this.announceTurn(this.gameModel.getCurrentPlayer(), this.gameModel.getTurn());
    this.displayDraw(this.gameModel.draw);
    this.displayPlayerLetters(this.gameModel.getCurrentPlayer().bag);
  }
}
