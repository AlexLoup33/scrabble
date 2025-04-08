package pdp.scrabble.dictionary;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public class Dictionary implements IDictionary {

  private final Set<Word> words = new HashSet<>();

  /**
   * add a word to the dictionary
   *
   * @param word
   * @return void
   */
  @Override
  public void addWord(Word word) {

    this.words.add(word);
  }

  /**
   * put every word from a file into the dictionary
   *
   * @param file_name
   * @return void
   */
  @Override
  public void loadFromFile(Path file_name) {
    this.words.clear();
    this.words.addAll(WordsLoader.loadWordsFromFile(file_name));
  }

  /**
   * check if the dictionary contains the word
   *
   * @param word
   * @return boolean, true if the word is in the dictionary
   */
  @Override
  public boolean containsWord(Word word) {
    return this.words.contains(word);
  }

  /**
   * @return dictionary as string
   */
  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append(this.getClass().getSimpleName());
    builder.append('(');
    for (Word w : this.words.stream().sorted().toList()) {
      builder.append(w);
      builder.append(',');
    }
    builder.append(')');
    return builder.toString();
  }
}
