package pdp.scrabble.view;

import java.util.ResourceBundle;

import static pdp.scrabble.errorsHandling.ErrorType.UnsupportedMultiplier;

import java.util.Scanner;

import pdp.scrabble.Utilities;
import pdp.scrabble.dictionary.Letter;
import pdp.scrabble.dictionary.LettersCounter;
import pdp.scrabble.errorsHandling.CustomError;
import pdp.scrabble.game.Direction;
import pdp.scrabble.errorsHandling.CustomError;
import pdp.scrabble.errorsHandling.ErrorType;
import pdp.scrabble.game.Grid;
import pdp.scrabble.game.GridPos;
import pdp.scrabble.game.GridWordPos;
import pdp.scrabble.game.Player;
import pdp.scrabble.game.ScoreMultiplier;
import pdp.scrabble.game.actions.GameAction;
import pdp.scrabble.game.actions.PlaceWordAction;

public class ViewCLI implements IView {

  private final ResourceBundle langPack;

  public ViewCLI(String langPackCode) {
    this.langPack = ResourceBundle.getBundle("pack_" + langPackCode);
  }

  @Override
  // Only place word for now
  // Very little error checking / handling
  // TODO: add exchange/pass/give up
  public GameAction askAction(Player player) throws Exception {
    // TODO: open scanner stdin in a reasonable place
    Scanner sc = new Scanner(System.in);
    Utilities.debug(
        'v', "pattern for move is `y-x-direction WORD`(eg. `a6h PHOTO` or `b7v STICK` )\n");
    System.out.print("Enter your move : ");
    String response = sc.nextLine();
    String[] move = response.split(" ");

    if (move.length != 2) {
      throw new Exception("Invalid move format");
    }

    Utilities.debug('v', "move[0] = %s / move[1] = %s\n", move[0], move[1]);
    // WARNING WARNING WARNING REMOVE MODULO
    int row = (move[0].charAt(0) - 97) % 15;
    int col = Integer.parseInt(move[0].replaceAll("[\\D]", ""));
    Utilities.debug('v', "row = %d / col = %d\n", row, col);

    GridWordPos gwp;

    switch (move[0].charAt(2)) {
      case 'h':
        gwp = new GridWordPos(row, col, Direction.Right);
        break;
      case 'v':
        gwp = new GridWordPos(row, col, Direction.Down);
        break;
      default:
        throw new Exception("Invalid direction");
    }

    char[] chars = move[1].toCharArray();
    Letter[] toPlace = new Letter[chars.length];
    for (int i = 0; i < chars.length; i++) {
      toPlace[i] = new Letter(chars[i]);
    }
    Utilities.debug('v', "Length of toPlace word : %d\n", toPlace.length);

    return new PlaceWordAction(player, 0, toPlace, gwp);
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

  @Override
  public void displayGrid(Grid grid) {
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

  Letter letter = grid.getLetter(i, j);if(letter==null)
  {
    if (j == grid.size - 1)
      System.out.print(backgroundColor + "_" + colorReset);
    else
      System.out.print(backgroundColor + "_" + colorReset + "  ");
  }else
  {
    if (j == grid.size - 1)
      System.out.print(color + letter + colorReset);
    else
      System.out.print(color + letter + "  " + colorReset);
  }
  }System.out.print("\n");}}

  @Override
  public void displayPlayerLetters(LettersCounter letters) {
    System.out.println("Player letters:");
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
  public void displayPlayerLetters(LettersCounter letters) {
    System.out.println("Player letters:");
    System.out.print("[ ");
    // Foreach with letters.getLetterSet()
    for (Letter letter : letters.getLettersSet()) {
      System.out.print(letter + " | ");
    }
    System.out.println("]");
  }

  @Override
  public String getLangPack(String key) {
    if (!langPack.containsKey(key)) {
      throw new CustomError(
          ErrorType.InvalidKeyArgument,
          "Key : " + key + " not in:" + langPack,
          "Invalid KeyArgument. Please check the file pack_ and verify your keys");
    }
    return langPack.getString(key);
  }
}
