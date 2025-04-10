package pdp.scrabble.view.GUI.Component;

import static java.lang.Integer.min;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import pdp.scrabble.dictionary.Letter;
import pdp.scrabble.game.Grid;
import pdp.scrabble.game.GridPos;
import pdp.scrabble.game.ScoreMultiplier;

public class ScrabbleBoard extends Canvas {
  private final int CELL_SIZE;
  private final int BOARD_SIZE;

  private int width;
  private int height;

  private GraphicsContext gc;

  public ScrabbleBoard(int width, int height, int size) {
    this.width = width;
    this.height = height;

    BOARD_SIZE = size + 1;
    CELL_SIZE = min(width, height) / BOARD_SIZE;

    this.gc = this.getGraphicsContext2D();

    this.setWidth(CELL_SIZE * BOARD_SIZE);
    this.setHeight(CELL_SIZE * BOARD_SIZE);
  }

  public void update(Grid grid, Boolean isSuperScrabble) {
    // Draw all the board in BEIGE
    gc.clearRect(0, 0, width, height);
    gc.setFill(Color.WHITE);
    gc.fillRect(0, 0, width, height);

    gc.setStroke(Color.BLACK);
    gc.setLineWidth(2);

    for (int col = 0; col < BOARD_SIZE; col++) {
      for (int row = 0; row < BOARD_SIZE; row++) {
        gc.strokeRect(col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE);

        // First, we set up the border with col = 0 and row = 0 for display row index (number)
        // and column index (letter)
        gc.setFill(Color.BLACK);
        if (col == 0 || row == 0) {
          if (row == 0 && col != 0) {
            // Change the color of the text to black
            gc.fillText(
                String.valueOf(col),
                col * CELL_SIZE + CELL_SIZE / 2,
                row * CELL_SIZE + CELL_SIZE / 2);
          } else if (col == 0 && row != 0) {
            gc.fillText(
                (char) (row + 96) + "",
                col * CELL_SIZE + CELL_SIZE / 2,
                row * CELL_SIZE + CELL_SIZE / 2);
          }
        } else {
          GridPos pos = new GridPos(row - 1, col - 1);
          Letter letter = grid.getLetter(row - 1, col - 1);
          String color =
              ScoreMultiplier.SIMPLE_LETTER.getLetterColor(isSuperScrabble, true).getGuiColor();
          if (letter != null) {
            gc.setFill(Color.BEIGE);
            gc.fillRect(col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE);

            gc.setFill(Color.BLACK);
            gc.strokeRect(col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            gc.strokeText(
                letter.toString(),
                col * CELL_SIZE + CELL_SIZE / 2,
                row * CELL_SIZE + CELL_SIZE / 2);
          } else {
            Color c;
            ScoreMultiplier mult = grid.getMultiplier(pos);
            if (mult == ScoreMultiplier.SIMPLE_LETTER) {
              c = Color.BEIGE;
            } else {
              color = mult.getLetterColor(isSuperScrabble, true).getGuiColor();
              c = Color.valueOf(color);
            }
            gc.setFill(c);
            gc.fillRect(col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            gc.strokeRect(col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE);
          }
        }
      }
    }
  }
}
