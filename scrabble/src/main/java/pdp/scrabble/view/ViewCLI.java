package pdp.scrabble.view;

import static pdp.scrabble.errorsHandling.ErrorType.UnsupportedMultiplier;

import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.Scanner;
import pdp.scrabble.Utilities;
import pdp.scrabble.configuration.Configuration;
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
import pdp.scrabble.game.actions.RenamePlayerAction;
import pdp.scrabble.game.actions.RestartGameAction;

public class ViewCLI implements IView {
  public static final String ANSI_RESET = "\u001B[0m";
  public static final String ANSI_RED = "\u001B[31m";
  public static final String ANSI_YELLOW = "\u001B[33m";

  private final ResourceBundle langPack;
  private final Scanner scaner;
  private final boolean superScrabble;

  public ViewCLI(Lang lang, boolean superScrabble) {
    this.langPack = lang.getViewCLI_langPack();
    this.scaner = new Scanner(System.in);
    this.superScrabble = superScrabble;
  }

  @Override
  public GameAction askAction(Player player, Integer turn) throws CustomError {
    Utilities.verbose(
        "possible actions are: placeWord | exchangeLetter | passTurn "
            + "| renamePlayer | forfeitPlayer | restartGame\n");
    System.out.println(this.getFromLangPack("ask_actionType"));
    String response = this.scaner.nextLine().strip();

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
    System.out.print(this.getFromLangPack("ask_exchange"));
    String response = this.scaner.nextLine().strip();

    LinkedList<Letter> letters = new LinkedList<>();
    for (int index = 0; index < response.length(); index++) {
      char c = response.charAt(index);
      letters.push(new Letter(Character.toUpperCase(c)));
    }
    return new ExchangeLetterAction(player, turn, letters);
  }

  private RenamePlayerAction ask_renamePlayer(Player player, Integer turn) {
    Utilities.verbose("type the new name for the current player\n");
    System.out.print(this.getFromLangPack("ask_rename"));
    String response = this.scaner.nextLine().strip();

    return new RenamePlayerAction(player, 0, response);
  }

  private PlaceWordAction ask_placeWordAction(Player player, Integer turn) {
    Utilities.verbose("pattern for move is `yxdirection WORD`(eg. `a6h photo` or `b7v STICK` )\n");
    System.out.print(this.getFromLangPack("ask_move"));
    String response = this.scaner.nextLine();
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
    System.out.print("  ");
    for (int i = 0; i < grid.size - 1; i++) {
      if (i < 8) System.out.print(i + 1 + "  ");
      else System.out.print(i + 1 + " ");
    }
    System.out.println(grid.size);

    String colorReset = "\u001B[0m";
    // Display each row
    for (int i = 0; i < grid.size; i++) {
      System.out.print((char) (i + 97) + " ");
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
          if (j == grid.size - 1) System.out.print(backgroundColor + "_" + colorReset);
          else System.out.print(backgroundColor + "_" + colorReset + "  ");
        } else {
          if (j == grid.size - 1) System.out.print(color + letter + colorReset);
          else System.out.print(color + letter + "  " + colorReset);
        }
      }
      System.out.print("\n");
    }
  }

  @Override
  public void displayPlayerLetters(LettersCounter letters) {
    System.out.print(this.getFromLangPack("show_letters"));
    System.out.print("[ ");
    // Foreach with letters.getLetterSet()
    boolean isFirst = true;
    for (Letter letter : letters.getLettersSet()) {
      for (int i = 0; i < letters.getLetterCount(letter); i++) {
        if (!isFirst) {
          System.out.print(" | ");
        } else {
          isFirst = false;
        }
        System.out.print(letter);
      }
    }
    System.out.println(" ]");
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
  public void showScore(Player player) {
    System.out.printf(
        langPack.getString("viewcli_score") + "\n", player.getName(), player.getScore());
  }

  @Override
  public void displayDraw(LettersDrawer draw) {
    System.out.println(langPack.getString("display_draw") + " " + draw.getCurrentSize());
  }

  @Override
  public void announceTurn(Player currentPlayer, int turn) {
    System.out.printf(langPack.getString("turn_begin") + "\n", currentPlayer.getName(), turn);
    /*
     * for (int i = 3; i > 0; i--) {
     * System.out.println(i);
     * try {
     * Thread.sleep(1000);
     * } catch (InterruptedException e) {
     * // Can never happen as long as the app is single-threaded
     * System.out.println(e);
     * }
     * }
     * System.out.print("\033c");
     * System.out.printf(langPack.getString("turn_begin") + "\n",
     * currentPlayer.getName());
     */
  }

  @Override
  public void showError(CustomError e) {
    String color = e.isFatal() ? ANSI_RED : ANSI_YELLOW;
    System.out.printf("%sError: %s%s\n", color, e.toString(), ANSI_RESET);
  }

  @Override
  public void showError(Throwable e) {
    System.out.print(ANSI_RED + e.toString());
    e.printStackTrace(System.out);
    System.out.println(ViewCLI.ANSI_RESET);
  }

  @Override
  public void showScore(java.util.List<Player> players) {
    for (Player player : players) {
      showScore(player);
    }
  }

  protected void finalize() throws Throwable {
    this.scaner.close();
  }

  @Override
  public void initialize(Configuration conf) {
    System.out.println("Vue initialisée avec la configuration : " + conf);
  }
}
