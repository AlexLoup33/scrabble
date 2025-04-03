package pdp.scrabble.dictionary;

import java.util.ArrayList;
import java.util.Comparator;
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

  private int currSize;

  public LettersCounter() {
    this.table = new Hashtable<>();
    this.currSize = 0;
  }

  public void clear() {
    this.table.clear();
    this.currSize = 0;
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
    this.currSize += count;
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
   * add all the letters of other to this
   *
   * @param other the letters to add (read only)
   */
  public void addOther(LettersCounter other) {
    for (Letter letter : other.getLettersSet()) {
      this.addLetter(letter, other.getLetterCount(letter));
    }
  }

  /**
   * add all the letters of other to this
   *
   * @param other the letters to add (read only)
   */
  public void addOther(Letter[] other) {
    for (int i = 0; i < other.length; i++) {
      this.addLetter(other[i]);
    }
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
    this.currSize -= count;
    return newCount;
  }

  /**
   * remove 1 of the given Letter
   *
   * @return the new count for this letter throw an error if there isn't enougth letters in the
   *     counter (don't store the given letter)
   */
  public int removeLetter(Letter letter) {
    return this.removeLetter(letter, 1);
  }

  /**
   * remove all the letters of other to this
   *
   * @param other the letters to remove (read only)
   */
  public void removeOther(LettersCounter other) {
    for (Letter letter : other.getLettersSet()) {
      this.removeLetter(letter, other.getLetterCount(letter));
    }
  }

  /** get the current count of this letter */
  public int getLetterCount(Letter letter) {
    return this.table.getOrDefault(letter, 0);
  }

  /** get the current letters (keys) */
  public Set<Letter> getLettersSet() {
    return this.table.keySet();
  }

  public List<Letter> getSortedLetterkeys() {
    ArrayList<Letter> res = new ArrayList<>();
    res.addAll(this.table.keySet());
    res.sort(Comparator.comparingInt(l -> (int) l.chr));
    return res;
  }

  public int size() {
    return this.currSize;
  }

  public Hashtable<Letter, Integer> getTable() {
    return this.table;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("LettersCounter{table=");
    builder.append(
        String.join(
            ",",
            this.getSortedLetterkeys().stream()
                .map(l -> (l.chr + ":" + this.getLetterCount(l)))
                .toList()));
    builder.append('}');
    return builder.toString();
  }

  /**
   * @return a shallow copy of this
   */
  public LettersCounter copy() {
    return new LettersCounter(this.table);
  }
}
