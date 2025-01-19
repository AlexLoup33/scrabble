package pdp.scrabble.dictionary;

// F46

public interface Dictionary {

    void addWord(String word);

    void load_from_file(String file_name);

    void find_world(LettersCounter lc);

}