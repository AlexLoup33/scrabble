package pdp.scrabble.view.CLI;

import static pdp.scrabble.errorsHandling.ErrorType.UnsupportedMultiplier;

import java.io.ByteArrayInputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;
import pdp.scrabble.Utilities;
import pdp.scrabble.dictionary.Letter;
import pdp.scrabble.dictionary.LettersCounter;
import pdp.scrabble.dictionary.Word;
import pdp.scrabble.errorsHandling.CustomError;
import pdp.scrabble.errorsHandling.ErrorType;
import pdp.scrabble.game.Direction;
import pdp.scrabble.game.Grid;
import pdp.scrabble.game.GridPos;
import pdp.scrabble.game.GridWordPos;
import pdp.scrabble.game.Lang;
import pdp.scrabble.game.LettersDrawer;
import pdp.scrabble.game.Player;
import pdp.scrabble.game.ScoreMultiplier;
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
import pdp.scrabble.view.IView;

public class ViewCLI implements IView {
  public static final String ANSI_RESET = "\u001B[0m";
  public static final String ANSI_RED = "\u001B[31m";
  public static final String ANSI_YELLOW = "\u001B[33m";

  private final ResourceBundle langPack;
  private final Scanner scanner;
  private final boolean superScrabble;

  public ViewCLI(Lang lang, boolean superScrabble) {
    this.langPack = lang.getViewCLI_langPack();
    this.scanner = new Scanner(System.in);
    this.superScrabble = superScrabble;
  }

  public ViewCLI(Lang lang, boolean superScrabble, ByteArrayInputStream in) {
    this.langPack = lang.getViewCLI_langPack();
    this.scaner = new Scanner(in);
    this.superScrabble = superScrabble;
  }

  @Override
  public GameAction askAction(Player player, Integer turn) throws CustomError {
    Utilities.verbose(
        "possible actions are: placeWord | exchangeLetter | passTurn "
            + "| renamePlayer | forfeitPlayer | restartGame | quit | undo | redo\n");
    Utilities.logger.println(this.getFromLangPack("ask_actionType"));
    String response = this.scanner.nextLine().strip();

    switch (response) {
      case "placeWord" -> {
        return this.ask_placeWordAction(player, turn);
      }
      case "passTurn" -> {
        return new PassTurnAction(player, turn);
      }
      case "forfeitPlayer" -> {
        return new ForfeitGameAction(player, turn);
      }
      case "restartGame" -> {
        return new RestartGameAction(player, turn);
      }
      case "quit" -> {
        return this.askQuitAction(player, turn);
      }
      case "undo" -> {
        return this.askUndoAction(player, turn);
      }
      case "redo" -> {
        return this.askRedoAction(player, turn);
      }
      case "exchangeLetter" -> {
        return this.ask_exchangeLetter(player, turn);
      }
      case "renamePlayer" -> {
        return this.ask_renamePlayer(player, turn);
      }
      default ->
          throw new CustomError(
              ErrorType.InvalidActionType,
              "invalide action kind",
              "the action: " + response + " isn't a valid action use -v to see the full list");
    }
  }

  private ExchangeLetterAction ask_exchangeLetter(Player player, Integer turn) {
    Utilities.verbose("type the letters to exchange: like: 'AJTT' to exchange an A, J and 2 T\n");
    Utilities.logger.print(this.getFromLangPack("ask_exchange"));
    String response = this.scanner.nextLine().strip();

    LinkedList<Letter> letters = new LinkedList<>();
    for (int index = 0; index < response.length(); index++) {
      char c = Character.toUpperCase(response.charAt(index));
      if (c == '#') {
        // => joker
        letters.push(Letter.joker());
      } else if (('A' <= c) && (c <= 'Z')) {
        // => letter
        letters.push(new Letter(c));
      } else {
        // => invalid !
        throw new CustomError(
            ErrorType.InvalideLetter, "invalide letter", "the letter: " + c + " isn't valid");
      }
    }
    return new ExchangeLetterAction(player, turn, letters);
  }

  private RenamePlayerAction ask_renamePlayer(Player player, Integer turn) {
    Utilities.verbose("type the new name for the current player\n");
    Utilities.logger.print(this.getFromLangPack("ask_rename"));
    String response = this.scanner.nextLine().strip();

    return new RenamePlayerAction(player, turn, response);
  }

  private QuitAction askQuitAction(Player player, Integer turn) {
    Utilities.logger.print(this.getFromLangPack("ask_save") + "[y/N]");
    String response = this.scanner.nextLine().strip().toLowerCase();
    boolean isSaveWanted = (response.equals("y") || response.equals("yes"));
    return new QuitAction(player, turn, isSaveWanted);
  }

  private RedoAction askRedoAction(Player player, Integer turn) {
    Utilities.logger.print(this.getFromLangPack("ask_redo") + "[y/N]");
    String response = this.scanner.nextLine().strip().toLowerCase();
    if (response.equals("y") || response.equals("yes")) {
      return new RedoAction(player, turn);
    }
    throw new CustomError(
        ErrorType.CanceledAskAction, "canceled", "not all players agreed on redoing");
  }

  private UndoAction askUndoAction(Player player, Integer turn) {
    Utilities.logger.print(this.getFromLangPack("ask_undo") + "[y/N]");
    String response = this.scanner.nextLine().strip().toLowerCase();
    if (response.equals("y") || response.equals("yes")) {
      return new UndoAction(player, turn);
    }
    throw new CustomError(
        ErrorType.CanceledAskAction, "canceled", "not all players agreed on undoing");
  }

  private PlaceWordAction ask_placeWordAction(Player player, Integer turn) {
    Utilities.verbose("pattern for move is `yxdirection WORD`(eg. `a6h photo` or `b7v STICK` )\n");
    Utilities.logger.print(this.getFromLangPack("ask_move"));
    String response = this.scanner.nextLine();
    String[] move = response.split(" ");

    if (move.length != 2) {
      throw new CustomError(
          ErrorType.InvalidMoveFormat,
          "pattern for move is `yxdirection WORD`(eg. `a6h photo` or `b7v STICK` )",
          "the move: " + response + " isn't valid");
    }

    Utilities.debug('v', "move[0] = %s / move[1] = %s\n", move[0], move[1]);
    int maxRange = superScrabble ? 20 : 14;
    int row = (Character.toLowerCase(move[0].charAt(0)) - 97);
    if (row < 0 || row > maxRange) {
      throw new CustomError(
          ErrorType.InvalidMoveFormat,
          "row must be between a and " + (char) (97 + maxRange),
          "the move: " + response + " isn't valid");
    }
    int col;
    try {
      // actual col value from model starts from 0
      // [\\D] = remove everything but numbers
      col = Integer.parseInt(move[0].replaceAll("[\\D]", "")) - 1;
    } catch (NumberFormatException e) {
      throw new CustomError(
          ErrorType.InvalidMoveFormat,
          "pattern for move is `yxdirection WORD`(eg. `a6h photo` or `b7v STICK` )",
          "the move: " + response + " isn't valid");
    }
    if (col < 0 || col > maxRange) {
      throw new CustomError(
          ErrorType.InvalidMoveFormat,
          "col must be between 1 and " + (maxRange + 1),
          "the move: " + response + " isn't valid");
    }
    Utilities.debug('v', "row = %d / col = %d\n", row, col);

    GridWordPos gwp;

    final char direction = move[0].charAt(move[0].length() - 1);
    gwp =
        switch (direction) {
          case 'h' -> new GridWordPos(row, col, Direction.Right);
          case 'v' -> new GridWordPos(row, col, Direction.Down);
          default ->
              throw new CustomError(
                  ErrorType.InvalidMoveFormat,
                  "invalid direction",
                  "the direction: " + direction + " isn't valid");
        };

    Word word = new Word(move[1]);
    Letter[] toPlace = new Letter[word.getLength()];
    for (int i = 0; i < word.getLength(); i++) {
      toPlace[i] = word.getLetterAt(i);
    }
    Utilities.debug('v', "Length of toPlace word : %d\n", toPlace.length);
    player.addScore(0);

    return new PlaceWordAction(player, turn, toPlace, gwp);
  }

  @Override
  public void displayGrid(Grid grid, Boolean isSuperScrabble) {
    // Display the first row of the grid with the column numbers
    StringBuilder builder = new StringBuilder();
    builder.append("  ");
    for (int i = 0; i < grid.size - 1; i++) {
      if (i < 8) builder.append(i + 1 + "  ");
      else builder.append(i + 1 + " ");
    }
    builder.append(grid.size + "\n");

    // Display each row
    for (int i = 0; i < grid.size; i++) {
      builder.append((char) ('a' + i) + " ");
      for (int j = 0; j < grid.size; j++) {
        GridPos pos = new GridPos(i, j);
        ScoreMultiplier mult = grid.getMultiplier(pos);

        String color = "";
        String backgroundColor = "";

        switch (mult) {
          case SIMPLE_LETTER, DOUBLE_LETTER, TRIPLE_LETTER, DOUBLE_WORD, TRIPLE_WORD -> {
            color = mult.getLetterColor(isSuperScrabble, false).getTextColor();
            backgroundColor = mult.getLetterColor(isSuperScrabble, false).getBackgroundColor();
          }
          case QUADRUPLE_LETTER, QUADRUPLE_WORD -> {
            if (!isSuperScrabble) {
              throw new CustomError(
                  UnsupportedMultiplier,
                  "Illegal Multiplier",
                  "Illegal utilisation of QUADRUPLE multiplier in non super scrabble game mode");
            }
            color = mult.getLetterColor(isSuperScrabble, false).getTextColor();
            backgroundColor = mult.getLetterColor(isSuperScrabble, false).getBackgroundColor();
          }
          default -> {
            throw new CustomError(
                UnsupportedMultiplier,
                "Unsupported Multiplier",
                "Non-existent Multiplier case detected while initializing the display of the grid");
          }
        }

        Letter letter = grid.getLetter(i, j);
        if (letter == null) {
          if (j == grid.size - 1) builder.append(backgroundColor + "_" + ANSI_RESET);
          else builder.append(backgroundColor + "_" + ANSI_RESET + "  ");
        } else {
          if (j == grid.size - 1) builder.append(color + letter + ANSI_RESET);
          else builder.append(color + letter + "  " + ANSI_RESET);
        }
      }
      builder.append("\n");
    }
    Utilities.logger.print(builder.toString());
  }

  @Override
  public void displayPlayerLetters(LettersCounter letters) {
    StringBuilder builder = new StringBuilder();
    builder.append(this.getFromLangPack("show_letters"));
    builder.append("[ ");
    // Foreach with letters.getLetterSet()
    boolean isFirst = true;
    for (Letter letter : letters.getLettersSet()) {
      for (int i = 0; i < letters.getLetterCount(letter); i++) {
        if (!isFirst) {
          builder.append(" | ");
        } else {
          isFirst = false;
        }
        builder.append(letter);
      }
    }
    builder.append(" ]\n");
    Utilities.logger.print(builder.toString());
  }

  @Override
  public String getFromLangPack(String key) {
    if (!langPack.containsKey(key)) {
      throw new CustomError(
          ErrorType.InvalidLangPackIdentifier,
          "Key : " + key + " not in:" + langPack,
          "Invalid KeyArgument. Please check the file pack_ and verify your keys");
    }
    return langPack.getString(key);
  }

  @Override
  public void showScores(List<Player> players) {
    for (Player player : players) {
      Utilities.logger.printf(
          langPack.getString("viewcli_score") + "\n", player.getName(), player.getScore());
    }
  }

  @Override
  public void displayDraw(LettersDrawer draw) {
    Utilities.logger.println(langPack.getString("display_draw") + " " + draw.getCurrentSize());
  }

  @Override
  public void announceTurn(Player currentPlayer, int turn) {
    Utilities.logger.println("--------------------");
    Utilities.logger.printf(langPack.getString("turn_begin") + "\n", currentPlayer.getName(), turn);
  }

  @Override
  public void showError(CustomError e) {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    if (e.isFatal()) {
      Utilities.logger.printf("%sError: %s", ANSI_RED, e.toString());
      e.printStackTrace(pw);
      pw.print(ANSI_RESET + "\n");
      Utilities.logger.print(sw.toString());
    } else {
      Utilities.logger.printf("%sError: %s%s\n", ANSI_YELLOW, e.toString(), ANSI_RESET);
    }
  }

  @Override
  public void showError(Throwable e) {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    Utilities.logger.print(ANSI_RED + e.toString());
    e.printStackTrace(pw);
    pw.print(ANSI_RESET + "\n");
    Utilities.logger.print(sw.toString());
  }

  @Override
  public void showRemainingTimes(List<Player> players) {
    for (Player player : players) {
      Utilities.logger.printf(
          getFromLangPack("blitz_remaining") + "\n",
          player.getName(),
          player.getRemainingTimeAsString());
    }
  }

  protected void finalize() throws Throwable {
    this.scanner.close();
  }
}
