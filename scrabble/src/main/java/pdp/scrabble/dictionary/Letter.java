package pdp.scrabble.dictionary;

import java.util.Objects;

/** a letter class that ensure this is a valid scrable letter */
public class Letter {
  public final char chr;

  public Letter(char c) {
    if ((c < 'A') || ('Z' < c)) {
      throw new IllegalArgumentException("the letter: " + c + '(' + (int) c + ')' + " isn't valid");
    }
    this.chr = c;
  }

  @Override
  public boolean equals(Object o) {
    if ((o == null) || (getClass() != o.getClass())) {
      return false;
    }
    Letter other = (Letter) o;
    return this.chr == other.chr;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this.chr);
  }

  @Override
  public String toString() {
    return this.getClass().getSimpleName() + '(' + this.chr + ')';
  }
}
