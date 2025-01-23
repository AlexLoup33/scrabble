package pdp.scrabble.dictionary;

// F46

import java.nio.file.Path;
import java.util.Set;

public interface IDictionary {

    void addWord(Word word);

    /** load all the words of a file in the dictionary, 
     * clear the words that are alredy in the dict */
    void loadFromFile(Path file_name);

    /** find all words from the dictionary that can be formed with up to the given letters */
    Set<Word> findWords(LettersCounter lc);

    /** test if the given word is inside the dictionary */
    boolean containsWord(Word word);
}