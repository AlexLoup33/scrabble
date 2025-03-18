package pdp.scrabble.game;

import java.util.LinkedList;

public record GridPos(int row, int col) {

  /**
   * find the "manhathan path" betwin this and other
   *
   * @param other can't have a col/row < to this
   * @return the list positions, move verticaly first then horizontaly the path will include this
   *     and other
   */
  public LinkedList<GridPos> getPath(GridPos other) {
    final LinkedList<GridPos> path = new LinkedList<>();
    for (int r = this.row; r < other.row; r++) {
      path.add(new GridPos(r, this.col));
    }
    for (int c = this.col; c <= other.col; c++) {
      path.add(new GridPos(other.row, c));
    }
    return path;
  }
}
