package pdp.scrabble.game;

public record GridWordPos(int row, int col, Direction direction) {

  public GridPos getPosAtIndex(int index) {
    switch (this.direction) {
      case Down -> {
        return new GridPos(this.row + 1, this.col);
      }
      case Right -> {
        return new GridPos(this.row, this.col + 1);
      }
      default ->
          throw new IllegalArgumentException("[BUG] direction: " + direction + " isn't supported");
    }
  }
}
