package pdp.scrabble.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import pdp.scrabble.dictionary.Letter;
import pdp.scrabble.dictionary.LettersCounter;
import pdp.scrabble.errorsHandling.CustomError;
import pdp.scrabble.errorsHandling.ErrorType;

public class LettersDrawer {
  private final Random rng;
  private int currentSize;
  private final int maxSize;
  private final ArrayList<Letter> table;
  private Lang lang;

  public LettersDrawer(Lang lang) {
    this.lang = lang;
    this.rng = new Random();
    this.table = new ArrayList<>();
    this.reset();
    this.maxSize = this.currentSize;
  }

  public void reset() {
    this.table.clear();
    this.currentSize = 0;
    LettersCounter initialLetters = this.lang.getLettersCount();
    for (Letter l : initialLetters.getLettersSet()) {
      for (int i = 0; i < initialLetters.getLetterCount(l); i++) {
        this.table.add(l);
        this.currentSize += 1;
      }
    }
  }

  public int getCurrentSize() {
    return this.currentSize;
  }

  /**
   * draw a random letter from the LetterDrawer, done in O(1) the drawer must still have some
   * letters remaining
   *
   * @return the poped letter from the drawer
   */
  public Letter popLetter() {
    if (this.currentSize <= 0) {
      throw new CustomError(
          ErrorType.DrawerError,
          "can't pop, the drawer is empty",
          "can't pop a letter from the drawer of current size: " + this.currentSize);
    }
    int popIndex;
    final int lastIndex = this.currentSize - 1;
    if (lastIndex == 0) {
      popIndex = 0;
    } else {
      popIndex = this.rng.nextInt(0, lastIndex);
    }
    Letter popedLetter = this.table.get(popIndex);
    // move the current end at the poped letter
    this.table.set(popIndex, this.table.get(lastIndex));
    this.table.set(lastIndex, null);
    this.currentSize -= 1;
    return popedLetter;
  }

  /**
   * add the letter to the LetterDrawer the drawer must have some space remaining
   *
   * @param letter the letter to add
   */
  public void pushLetter(Letter letter) {
    if (this.currentSize >= this.maxSize) {
      throw new CustomError(
          ErrorType.DrawerError,
          "can't push, the drawer is full",
          "can't push a letter in the drawer of current size: "
              + this.currentSize
              + " of maxSize: "
              + this.maxSize);
    }
    this.table.set(this.currentSize, letter);
    this.currentSize += 1;
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
    if (this.maxSize < this.currentSize + nbToPush) {
      throw new CustomError(
          ErrorType.DrawerError,
          "not enougth space to push the letters",
          "can't push "
              + nbToPush
              + " letters in the drawer of current size: "
              + this.currentSize
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
    if (this.currentSize < nbToDraw) {
      throw new CustomError(
          ErrorType.DrawerError,
          "not enougth letters to draw",
          "can't pop "
              + nbToDraw
              + " letters from the drawer of current size: "
              + this.currentSize);
    }

    LettersCounter drawedletters = new LettersCounter();
    for (int i = 0; i < nbToDraw; i++) {
      drawedletters.addLetter(this.popLetter());
    }

    return drawedletters;
  }
}
