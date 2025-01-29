package pdp.scrabble.dictionary;

// F46

import java.nio.file.Path;
import java.util.Set;

public interface IDictionary {

  /**
   * add a word to the dictionary
   *
   * @param word
   * @return void
   */
  void addWord(Word word);

  /**
   * put every word from a file into the dictionary
   *
   * @param file_name
   * @return void
   */
  void loadFromFile(Path file_name);

  /**
   * find every words that can be created be the inputted letters
   *
   * @param lc
   * @return empty set if no word found, set with word else
   */
  Set<Word> findWords(LettersCounter lc);

  /**
   * check if the dictionary contains the word
   *
   * @param word
   * @return boolean, true if the word is in the dictionary
   */
  boolean containsWord(Word word);
}
