package pdp.scrabble.dictionary;

// F46

import java.nio.file.Path;

public interface IDictionary {

  /**
   * put every word from a file into the dictionary
   *
   * @param file_name
   * @return void
   */
  void loadFromFile(Path fileName);

  /**
   * check if the dictionary contains the word
   *
   * @param word
   * @return boolean, true if the word is in the dictionary
   */
  boolean containsWord(Word word);
}
