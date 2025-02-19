package pdp.scrabble.view;

import java.lang.Boolean;
import pdp.scrabble.dictionary.Letter;
import pdp.scrabble.dictionary.LettersCounter;
import pdp.scrabble.errorsHandling.CustomError;
import static pdp.scrabble.errorsHandling.ErrorType.UnsupportedMultiplier;
import pdp.scrabble.game.Grid;
import pdp.scrabble.game.GridPos;
import pdp.scrabble.game.ScoreMultiplier;
import pdp.scrabble.game.actions.GameAction;

public class ViewCLI implements IView {

  @Override
  public GameAction askAction() {
    return null;
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

        // TO-DO: Prendre en compte le mode SuperScrabble
        switch (mult) {
          case SIMPLE_LETTER, DOUBLE_LETTER, TRIPLE_LETTER, DOUBLE_WORD, TRIPLE_WORD -> {
            color = mult.getLetterColor(isSuperScrabble).getTextColor();
            backgroundColor = mult.getLetterColor(isSuperScrabble).getBackgroundColor();
          }
          case QUADRUPLE_LETTER, QUADRUPLE_WORD -> {
            if (!isSuperScrabble){
              throw new CustomError(UnsupportedMultiplier, "Illegal Multiplier", "Illegal utilisation of QUADRUPLE multiplier in another game mode");
            }
            color = mult.getLetterColor(isSuperScrabble).getTextColor();
            backgroundColor = mult.getLetterColor(isSuperScrabble).getBackgroundColor();
          }
          default -> {
            throw new CustomError(UnsupportedMultiplier, "Unsupported Multiplier", "Unexistant Multiplier case detected while initializing the display of the grid");
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
    System.out.println("Player letters:");
    System.out.print("[ ");
    // Foreach with letters.getLetterSet()
    for (Letter letter : letters.getLettersSet()) {
      System.out.print(letter + " | ");
    }
    System.out.println("]");
  }
}
