package pdp.scrabble.dictionary;

// F46

import java.nio.file.Path;
import java.util.Set;

public interface IDictionary {

    void addWord(Word word);

    void loadFromFile(Path file_name);

    Set<Word> findWords(LettersCounter lc);

    boolean containsWord(Word word);
}