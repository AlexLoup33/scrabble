package pdp.scrabble.view.Component;

import javafx.scene.layout.GridPane;
import pdp.scrabble.game.ScoreMultiplier;

public class GridView extends GridPane {

  private final int width;
  private final int height;

  public GridView(int width, int height) {
    this.width = width;
    this.height = height;

    /*Style*/
    this.setStyle(
        "-fx-background-color: #FFFFFF; -fx-border-color: #000000; -fx-border-width: 2px;");
    this.setGridLinesVisible(true);
    /*this.setHgap(this.width + 1);
    this.setVgap(this.height + 1);*/
    this.setPadding(new javafx.geometry.Insets(10, 10, 10, 10));

    for (int i = 1; i < this.width + 1; i++) {
      Cell cell = new Cell(i, 0, null, false);
      cell.setLetter((i) + "");
      GridPane.setConstraints(cell, i, 1);
      this.getChildren().add(cell);
    }
    for (int j = 1; j < this.height + 1; j++) {
      Cell cell = new Cell(0, j, null, false);
      cell.setLetter((char) ((j - 1) + 97) + "");
      GridPane.setConstraints(cell, 0, j + 1);
      this.add(cell, 0, j);
    }

    Cell tmp = new Cell(width, height, ScoreMultiplier.SIMPLE_LETTER, false);
    tmp.setLetter("A");
    this.add(tmp, width, width);
  }
}
