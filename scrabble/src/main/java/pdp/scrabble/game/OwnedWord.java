package pdp.scrabble.game;

import pdp.scrabble.dictionary.Word;
import pdp.scrabble.errorsHandling.CustomError;
import pdp.scrabble.errorsHandling.ErrorType;

public class OwnedWord {
  public final GridWordPos position;
  public final Word word;
  public final Player owner;

  public OwnedWord(GridWordPos position, Word word, Player owner) {
    this.position = position;
    this.word = word;
    this.owner = owner;
  }

  @Override
  public String toString() {
    return "OwnedWord{" + "position=" + position + ", word=" + word + ", owner=" + owner + '}';
  }

  @Override
  protected Object clone() throws CloneNotSupportedException {
    return super.clone();
  }

  public GridPos getPosAtWordIndex(int index) {

    if (index > this.word.getLength()) {
      throw new CustomError(
          ErrorType.IndexOutOfBounds,
          "IndexOutOfBounds",
          "Try to find the position of a letter not in the word");
    }

    if (this.position.direction() == Direction.Down) {
      return new GridPos(this.position.row() + index, this.position.col());
    } else {
      return new GridPos(this.position.row(), this.position.col() + index);
    }
  }
}
