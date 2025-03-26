package pdp.scrabble.view.Component;

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

    for (int row = 0; row < BOARD_SIZE; row++) {
      for (int col = 0; col < BOARD_SIZE; col++) {
        gc.strokeRect(row * CELL_SIZE, col * CELL_SIZE, CELL_SIZE, CELL_SIZE);

        // First we set up the border with row = 0 and col = 0 for display column index (number) and
        // row index (letter)
        if (row == 0 || col == 0) {
          if (row == 0 && col != 0) {
            gc.fillText(
                String.valueOf(col),
                row * CELL_SIZE + CELL_SIZE / 2,
                col * CELL_SIZE + CELL_SIZE / 2);
          } else if (col == 0 && row != 0) {
            gc.fillText(
                (char) (row + 96) + "",
                row * CELL_SIZE + CELL_SIZE / 2,
                col * CELL_SIZE + CELL_SIZE / 2);
          }
        } else {
          GridPos pos = new GridPos(row - 1, col - 1);
          Letter letter = grid.getLetter(row, col);
          String color = "";

          if (letter != null) {
            color =
                ScoreMultiplier.SIMPLE_LETTER.getLetterColor(isSuperScrabble).getBackgroundColor();
            gc.setFill(Color.valueOf(color));
            gc.fillRect(row * CELL_SIZE, col * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            gc.strokeRect(row * CELL_SIZE, col * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            gc.strokeText(
                letter.toString(),
                row * CELL_SIZE + CELL_SIZE / 2,
                col * CELL_SIZE + CELL_SIZE / 2);
          } else {
            ScoreMultiplier mult = grid.getMultiplier(pos);
            color = mult.getLetterColor(isSuperScrabble).getBackgroundColor();
            gc.setFill(Color.valueOf(color));
            gc.fillRect(row * CELL_SIZE, col * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            gc.strokeRect(row * CELL_SIZE, col * CELL_SIZE, CELL_SIZE, CELL_SIZE);
          }
        }
      }
    }
  }
}
