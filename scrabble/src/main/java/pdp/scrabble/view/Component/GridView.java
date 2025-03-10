package pdp.scrabble.view.Component;

import javafx.scene.layout.GridPane;
import pdp.scrabble.game.ScoreMultiplier;

public class GridView extends GridPane {

  private final int width;
  private final int height;

  // private GridPane scrabbleBoard;

  public GridView(int width, int height) {
    // scrabbleBoard = new GridPane();
    this.width = width;
    this.height = height;

    /*Style*/
    this.setStyle(
        "-fx-background-color: #FFFFFF; -fx-border-color: #000000; -fx-border-width: 2px;");
    this.setGridLinesVisible(true);
    this.setHgap(this.width + 1);
    this.setVgap(this.height + 1);
    this.setPadding(new javafx.geometry.Insets(10, 10, 10, 10));
    //

    for (int i = 0; i < this.width; i++) {
      Cell cell = new Cell(i, 0, null, false);
      cell.setLetter((i + 1) + "");
      GridPane.setConstraints(cell, i, 0);
      this.getChildren().add(cell);
    }
    for (int j = 0; j < this.height; j++) {
      Cell cell = new Cell(0, j, null, false);
      cell.setLetter((char) (j + 97) + "");
      GridPane.setConstraints(cell, 0, j);
      this.add(cell, 0, j);
    }

    Cell tmp = new Cell(width, height, ScoreMultiplier.SIMPLE_LETTER, false);
    tmp.setLetter("A");
    this.add(tmp, width, width);
  }
}
