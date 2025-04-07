package pdp.scrabble.game;

import java.util.List;
import pdp.scrabble.dictionary.Letter;
import pdp.scrabble.dictionary.LettersCounter;
import pdp.scrabble.errorsHandling.CustomError;
import pdp.scrabble.errorsHandling.ErrorType;

public class LettersDrawer {
  private final RandomUser rng;
  private final Lang lang;
  private final LettersCounter content;
  private final int maxSize;
  private final boolean isSuperScrabble;

  public LettersDrawer(Lang lang, RandomUser rngGenerator, boolean isSuperScrabble) {
    this.rng = rngGenerator;
    this.lang = lang;
    this.isSuperScrabble = isSuperScrabble;
    this.content = new LettersCounter();
    LettersCounter initialLetters = this.lang.getLettersCount(this.isSuperScrabble);
    for (Letter l : initialLetters.getLettersSet()) {
      int count = initialLetters.getLetterCount(l);
      this.content.addLetter(l, count);
    }
    this.maxSize = this.content.size();
  }

  public int getCurrentSize() {
    return this.content.size();
  }

  /**
   * draw a random letter from the LetterDrawer, done in O(1) the drawer must still have some
   * letters remaining
   *
   * @return the poped letter from the drawer
   */
  public Letter popLetter() {
    if (this.getCurrentSize() <= 0) {
      throw new CustomError(
          ErrorType.DrawerError,
          "can't pop, the drawer is empty",
          "can't pop a letter from the drawer of current size: " + this.getCurrentSize());
    }
    int letterIndex = this.rng.getInt(0, this.getCurrentSize());
    int currIndex = 0;
    for (Letter l : this.content.getSortedLetterkeys()) {
      int count = this.content.getLetterCount(l);
      // => always (currIndex <= letterIndex)
      if (letterIndex < currIndex + count) {
        // => has selected this letter
        this.content.removeLetter(l);
        return l;
      }
      currIndex += count;
    }
    throw new Error("[BUG] couldn't draw a letter ...");
  }

  /**
   * add the letter to the LetterDrawer the drawer must have some space remaining
   *
   * @param letter the letter to add
   */
  public void pushLetter(Letter letter) {
    if (this.getCurrentSize() >= this.maxSize) {
      throw new CustomError(
          ErrorType.DrawerError,
          "can't push, the drawer is full",
          "can't push a letter in the drawer of current size: "
              + this.getCurrentSize()
              + " of maxSize: "
              + this.maxSize);
    }
    this.content.addLetter(letter);
  }

  /**
   * add all the the letters to the LetterDrawer the drawer must have enougth space remaining to add
   * all letters
   *
   * @param letters all the letters add
   */
  public void pushLetter(List<Letter> letters) {
    for (Letter letter : letters) {
      this.pushLetter(letter);
    }
  }

  /**
   * add multiple times the letter to the LetterDrawer the drawer must have enougth space remaining
   * to add all letters
   *
   * @param letters all the letters add
   * @param nbToPush the number of times to add it
   */
  public void pushLetter(Letter letter, int nbToPush) {
    if (this.maxSize < this.getCurrentSize() + nbToPush) {
      throw new CustomError(
          ErrorType.DrawerError,
          "not enough space to push the letters",
          "can't push "
              + nbToPush
              + " letters in the drawer of current size: "
              + this.getCurrentSize()
              + " and of maxSize: "
              + this.maxSize);
    }

    for (int i = 0; i < nbToPush; i++) {
      this.pushLetter(letter);
    }
  }

  /**
   * draw a certain number of letters from the drawer
   *
   * @param nbToDraw the number of letters to draw
   * @return the letters poped
   */
  public LettersCounter popLetter(int nbToDraw) {
    if (this.getCurrentSize() < nbToDraw) {
      throw new CustomError(
          ErrorType.DrawerError,
          "not enougth letters to draw",
          "can't pop "
              + nbToDraw
              + " letters from the drawer of current size: "
              + this.getCurrentSize());
    }

    LettersCounter drawedletters = new LettersCounter();
    for (int i = 0; i < nbToDraw; i++) {
      drawedletters.addLetter(this.popLetter());
    }

    return drawedletters;
  }

  @Override
  public String toString() {
    return "LettersDrawer{"
        + "lang="
        + lang
        + ", content="
        + content
        + ", maxSize="
        + maxSize
        + ", currentSize="
        + this.getCurrentSize()
        + '}';
  }

  public void pushMany(LettersCounter other) {
    for (Letter letter : other.getLettersSet()) {
      this.pushLetter(letter, other.getLetterCount(letter));
    }
  }

  /**
   * undo the letters pushed to the drawer (will remove the lettersPushed)
   *
   * @param lettersPushed the letters pushed in (ie what to remove)
   */
  public void undoPushLetter(LettersCounter lettersPushed) {
    this.content.removeOther(lettersPushed);
  }
}
