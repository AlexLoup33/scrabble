package pdp.scrabble.dictionary;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Dictionary implements IDictionary {

  /** for fast tests */
  private static final HashMap<Path, Set<Word>> cachedDicts = new HashMap<>();

  private final Set<Word> words = new HashSet<>();

  /**
   * put every word from a file into the dictionary
   *
   * @param fileName
   * @return void
   */
  @Override
  public void loadFromFile(Path fileName) {
    this.words.clear();
    if (!cachedDicts.containsKey(fileName)) {
      this.words.addAll(WordsLoader.loadWordsFromFile(fileName));
      cachedDicts.put(fileName, this.words);
    } else {
      this.words.addAll(cachedDicts.get(fileName));
    }
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
