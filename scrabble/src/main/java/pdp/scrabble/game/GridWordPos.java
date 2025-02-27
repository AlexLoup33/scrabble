package pdp.scrabble.game;

import pdp.scrabble.errorsHandling.CustomError;
import pdp.scrabble.errorsHandling.ErrorType;

public record GridWordPos(int row, int col, Direction direction) {

  /**
   * The `getPosAtIndex` method returns the position of a letter in a word at a given index on the
   * grid.
   *
   * @param index The index of the letter in the word.
   * @return The position of the letter in the word at the given index as a GridPos object.
   * @throws IllegalArgumentException if the direction is not supported.
   */
  public GridPos getPosAtIndex(int index) {
    switch (this.direction) {
      case Down -> {
        return new GridPos(this.row + index, this.col);
      }
      case Right -> {
        return new GridPos(this.row, this.col + index);
      }
      default ->
          throw new CustomError(
              ErrorType.InvalideWordDirection,
              "invalide direction",
              "[BUG] direction: " + direction + " isn't supported");
    }
  }

  @Override
  protected Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
}
