package pdp.scrabble.view;

import static pdp.scrabble.errorsHandling.ErrorType.UnsupportedMultiplier;

import java.util.LinkedList;
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

  private final ResourceBundle langPack;
  private final Scanner scaner;

  public ViewCLI(Lang lang) {
    this.langPack = lang.getViewCLI_langPack();
    this.scaner = new Scanner(System.in);
  }

  @Override
  public GameAction askAction(Player player, Integer turn) {
    // 1) ask what type of action:
    // placeWord | exchangeLetter | passTurn |
    // renamePlayer | forfeitGame | restartGame
    Utilities.verbose(
        "possible actions are: placeWord | exchangeLetter | passTurn "
            + "| renamePlayer | forfeitPlayer | restartGame\n");
    System.out.println(this.getFromLangPack("ask_actionType"));
    String response = this.scaner.nextLine().strip();

    switch (response) {
      case "placeWord":
        return this.ask_placeWordAction(player, turn);
      case "passTurn":
        return new PassTurnAction(player, turn);
      case "forfeitPlayer":
        return new ForfeitGameAction(player, turn);
      case "restartGame":
        return new RestartGameAction(player, turn);
      case "exchangeLetter":
        return this.ask_exchangeLetter(player, turn);
      case "renamePlayer":
        return this.ask_renamePlayer(player, turn);
      default:
        throw new CustomError(ErrorType.InvalideActionType,
            "invalide action kind", "the action: " +
                response + " isn't a valid action use -v to see the full list");
    }

  }

  private ExchangeLetterAction ask_exchangeLetter(Player player, Integer turn) {
    Utilities.verbose(
        "type the letters to exchange: like: 'AJTT' to exchange an A, J and 2 T\n");
    System.out.print(this.getFromLangPack("ask_exchange"));
    String response = this.scaner.nextLine().strip();

    LinkedList<Letter> letters = new LinkedList<>();
    for (int index = 0; index < response.length(); index++) {
      letters.push(new Letter(response.charAt(index)));
    }
    return new ExchangeLetterAction(player, turn, letters);
  }

  private RenamePlayerAction ask_renamePlayer(Player player, Integer turn) {
    Utilities.verbose(
        "type the new name for the current player\n");
    System.out.print(this.getFromLangPack("ask_rename"));
    String response = this.scaner.nextLine().strip();

    return new RenamePlayerAction(player, 0, response);
  }

  // Very little error checking / handling
  private PlaceWordAction ask_placeWordAction(Player player, Integer turn) {
    Utilities.verbose(
        "pattern for move is `yxdirection WORD`(eg. `a6h photo` or `b7v STICK` )\n");
    System.out.print(this.getFromLangPack("ask_move"));
    String response = this.scaner.nextLine();
    String[] move = response.split(" ");

    if (move.length != 2) {
      throw new CustomError(ErrorType.InvalideMoveFormat, "Invalid move format",
          "the move: " + move.toString() + " isn't valid");
    }

    Utilities.debug('v', "move[0] = %s / move[1] = %s\n", move[0], move[1]);
    // WARNING WARNING WARNING REMOVE MODULO
    int row = (move[0].charAt(0) - 97) % 15;
    int col = Integer.parseInt(move[0].replaceAll("[\\D]", ""));
    Utilities.debug('v', "row = %d / col = %d\n", row, col);

    GridWordPos gwp;

    final char direction = move[0].charAt(2);
    switch (direction) {
      case 'h':
        gwp = new GridWordPos(row, col, Direction.Right);
        break;
      case 'v':
        gwp = new GridWordPos(row, col, Direction.Down);
        break;
      default:
        throw new CustomError(ErrorType.InvalideMoveFormat, "invalide direction",
            "the direction: " + direction + " isn't valid");
    }

    Word word = new Word(move[1]);
    Letter[] toPlace = new Letter[word.getLength()];
    for (int i = 0; i < word.getLength(); i++) {
      toPlace[i] = word.getLetterAt(i);
    }
    Utilities.debug('v', "Length of toPlace word : %d\n", toPlace.length);

    return new PlaceWordAction(player, turn, toPlace, gwp);
  }

  @Override
  public void displayGrid(Grid grid, Boolean isSuperScrabble) {
    // Display the first row of the grid with the column numbers
    System.out.print("  ");
    for (int i = 0; i < grid.size - 1; i++) {
      if (i < 8)
        System.out.print(i + 1 + "  ");
      else
        System.out.print(i + 1 + " ");
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

        // TO-DO: Prendre en compte le mode SuperScrabble
        switch (mult) {
          case SIMPLE_LETTER, DOUBLE_LETTER, TRIPLE_LETTER, DOUBLE_WORD, TRIPLE_WORD -> {
            color = mult.getLetterColor(isSuperScrabble).getTextColor();
            backgroundColor = mult.getLetterColor(isSuperScrabble).getBackgroundColor();
          }
          case QUADRUPLE_LETTER, QUADRUPLE_WORD -> {
            if (!isSuperScrabble) {
              throw new CustomError(
                  UnsupportedMultiplier,
                  "Illegal Multiplier",
                  "Illegal utilisation of QUADRUPLE multiplier in another game mode");
            }
            color = mult.getLetterColor(isSuperScrabble).getTextColor();
            backgroundColor = mult.getLetterColor(isSuperScrabble).getBackgroundColor();
          }
          default -> {
            throw new CustomError(
                UnsupportedMultiplier,
                "Unsupported Multiplier",
                "Unexistant Multiplier case detected while initializing the display of the grid");
          }
        }

        Letter letter = grid.getLetter(i, j);
        if (letter == null) {
          if (j == grid.size - 1)
            System.out.print(backgroundColor + "_" + colorReset);
          else
            System.out.print(backgroundColor + "_" + colorReset + "  ");
        } else {
          if (j == grid.size - 1)
            System.out.print(color + letter + colorReset);
          else
            System.out.print(color + letter + "  " + colorReset);
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
  protected void finalize() throws Throwable {
    this.scaner.close();
  }
}
