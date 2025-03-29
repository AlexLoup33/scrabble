package pdp.scrabble.dictionary;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import pdp.scrabble.errorsHandling.CustomError;
import pdp.scrabble.errorsHandling.ErrorType;

public class LettersCounter {

  /**
   * dico: <br>
   * A: 0 <br>
   * B: 45<br>
   * sert a dire le nombre de chaque lettre presente
   */
  private final Hashtable<Letter, Integer> table;

  public LettersCounter() {
    this.table = new Hashtable<>();
  }

  public void reset() {
    this.table.clear();
  }

  public LettersCounter(List<Letter> letters) {
    this();
    for (Letter letter : letters) {
      this.addLetter(letter);
    }
  }

  public LettersCounter(Map<Letter, Integer> letters) {
    this();
    for (Letter letter : letters.keySet()) {
      this.addLetter(letter, letters.get(letter));
    }
  }

  /**
   * add count of the given Letter
   *
   * @param count must be >= 0
   * @return the new count for this letter (don't store the given letter)
   *     <p>Modif: addLetter en public car utiliser dans la classe Lang
   */
  public int addLetter(Letter letter, int count) {
    if (count < 0) {
      throw new CustomError(
          ErrorType.InvalidLetterCountError,
          "count must be >= 0",
          "the count " + count + " isn't >= 0");
    }
    int newCount = this.table.getOrDefault(letter, 0) + count;
    this.table.put(letter, newCount);
    return newCount;
  }

  /**
   * add 1 of the given Letter
   *
   * @return the new count for this letter (don't store the given letter)
   */
  public int addLetter(Letter letter) {
    return this.addLetter(letter, 1);
  }

  /**
   * remove count of the given Letter
   *
   * @param count the number of times to remove the letter (must be >= 0)
   * @return the new count for this letter throw an error if there isn't enougth letters in the
   *     counter (don't store the given letter)
   */
  public int removeLetter(Letter letter, int count) {
    if (count < 0) {
      throw new CustomError(
          ErrorType.InvalidLetterCountError,
          "count must be >= 0",
          "the count " + count + " isn't >= 0");
    }
    int newCount = this.table.getOrDefault(letter, 0) - count;
    // -1 => letter not present in this LettersCounter
    if (newCount == -1) {
      throw new CustomError(
          ErrorType.InvalidLetterCountError,
          "the letter " + letter + " can't be removed " + count + " times",
          "Can't remove the letter "
              + letter
              + " because it isn't stored in at least "
              + count
              + " times");
    } else if (newCount == 0) {
      this.table.remove(letter); // not required -> optimize
    } else { // => newCount >= 1
      this.table.put(letter, newCount);
    }
    return newCount;
  }

  /**
   * remove 1 of the given Letter
   *
   * @return the new count for this letter throw an error if there isn't enougth letters in the
   *     counter (don't store the given letter)
   */
  public int removeLetter(Letter letter) {
    int newCount = this.table.getOrDefault(letter, 0) - 1;
    // -1 => letter not present in this LettersCounter
    if (newCount == -1) {
      throw new CustomError(
          ErrorType.InvalidLetterCountError,
          "the letter " + letter + " can't be removed",
          "Can't remove the letter " + letter + " because it isn't stored in");
    } else if (newCount == 0) {
      this.table.remove(letter); // not required -> optimize
    } else { // => newCount >= 1
      this.table.put(letter, newCount);
    }
    return newCount;
  }

  /** get the current count of this letter */
  public int getLetterCount(Letter letter) {
    return this.table.getOrDefault(letter, 0);
  }

  /** get the current letters (keys) */
  public Set<Letter> getLettersSet() {
    return this.table.keySet();
  }

  public int size() {
    return this.table.values().stream().reduce(0, Integer::sum);
  }

  public Hashtable<Letter, Integer> getTable() {
    return this.table;
  }

  @Override
  public String toString() {
    return "LettersCounter{" + "table=" + table + '}';
  }
}
