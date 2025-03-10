package pdp.scrabble.view.Component;

import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;

public class GridView extends GridPane {

  private final int width;
  private final int height;

  // private GridPane scrabbleBoard;

  public GridView(int width, int height) {
    // scrabbleBoard = new GridPane();

    this.width = width;
    this.height = height;

    this.getRowConstraints().add(new RowConstraints(width + 1));
    this.getColumnConstraints().add(new ColumnConstraints(height + 1));

    for (int i = 1; i < width + 1; i++) {
      Pane emptyPane = new Pane();
      Text num = new Text(i + "");

      emptyPane.getChildren().add(num);
      this.add(emptyPane, i, 0);
    }
  }
}
