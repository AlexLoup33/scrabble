package pdp.scrabble.dictionary;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.Map.Entry;

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
     * find every words that can be created be the inputted letters
     *
     * @param lc
     * @return empty set if no word found, set with word else
     */
    @Override
    public Set<Word> findWords(LettersCounter lc) {
        Set<Word> result = new HashSet<>();

        // transform LettersCounter to a list
        List<Letter> lc_list = new ArrayList<>();
        for (Letter key : lc.getLettersSet()) {
            int freq = lc.getLetterCount(key);
            for (int i = 0; i < freq; ++i) {
                lc_list.add(key);
            }
        }

        // add (if exists) every word which can be created by these letters
        for (int word_size = 1; word_size <= lc_list.size(); word_size++) {
            // generate all letter permutation for a final word size of word_size
            List<List<Letter>> permutations = new ArrayList<>();
            generatePermutations(lc_list, new ArrayList<>(), permutations, word_size);

            // System.err.println("Size " + word_size + " letter: " + permutations);
            // System.err.println("Nb for " + word_size + " is: " + permutations.size());

            // convert List<Character> to Word
            for (List<Letter> word : permutations) {

                StringBuilder stringBuilder = new StringBuilder();
                for (Letter ch : word) {
                    stringBuilder.append(ch);
                }
                String word_as_str = stringBuilder.toString();
                Word w = new Word(word_as_str);

                if (this.words.contains(w)) {
                    result.add(w);
                }
            }
        }
        return result;
    }

    /**
     * do not use, internal function
     */
    public static void generatePermutations(
            List<Letter> original, List<Letter> current, List<List<Letter>> permutations, int length) {
        if (current.size() == length) {
            permutations.add(new ArrayList<>(current));
            return;
        }
        for (int i = 0; i < original.size(); i++) {
            current.add(original.get(i));
            List<Letter> remaining = new ArrayList<>(original);
            remaining.remove(i);
            generatePermutations(remaining, current, permutations, length);
            current.remove(current.size() - 1);
        }
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

    /*
     * get the path on the dictionary and complete the new LettersCounter with the
     * language
     * that we want.
     * 
     * @param file_name
     * 
     * @return LettersCounter
     */
    public LettersCounter getDictionaryPath(Path file_name) {

        /* LettersCounter letters = new LettersCounter(); */
        Path french = java.nio.file.Paths.get("dictionary/fr.txt");
        Path english = java.nio.file.Paths.get("dictionary/en.txt");
        Map<Letter, Integer> letterCounts = new HashMap<>();

        if (file_name.equals(french)) {
            letterCounts.put(new Letter('A'), 9);
            letterCounts.put(new Letter('B'), 2);
            letterCounts.put(new Letter('C'), 2);
            letterCounts.put(new Letter('D'), 3);
            letterCounts.put(new Letter('E'), 15);
            letterCounts.put(new Letter('F'), 2);
            letterCounts.put(new Letter('G'), 2);
            letterCounts.put(new Letter('H'), 2);
            letterCounts.put(new Letter('I'), 8);
            letterCounts.put(new Letter('J'), 1);
            letterCounts.put(new Letter('K'), 1);
            letterCounts.put(new Letter('L'), 5);
            letterCounts.put(new Letter('M'), 3);
            letterCounts.put(new Letter('N'), 6);
            letterCounts.put(new Letter('O'), 6);
            letterCounts.put(new Letter('P'), 2);
            letterCounts.put(new Letter('Q'), 1);
            letterCounts.put(new Letter('R'), 6);
            letterCounts.put(new Letter('S'), 6);
            letterCounts.put(new Letter('T'), 6);
            letterCounts.put(new Letter('U'), 6);
            letterCounts.put(new Letter('V'), 2);
            letterCounts.put(new Letter('W'), 1);
            letterCounts.put(new Letter('X'), 1);
            letterCounts.put(new Letter('Y'), 1);
            letterCounts.put(new Letter('Z'), 1);

        } else {
            letterCounts.put(new Letter('A'), 9);
            letterCounts.put(new Letter('B'), 2);
            letterCounts.put(new Letter('C'), 2);
            letterCounts.put(new Letter('D'), 4);
            letterCounts.put(new Letter('E'), 12);
            letterCounts.put(new Letter('F'), 2);
            letterCounts.put(new Letter('G'), 3);
            letterCounts.put(new Letter('H'), 2);
            letterCounts.put(new Letter('I'), 9);
            letterCounts.put(new Letter('J'), 1);
            letterCounts.put(new Letter('K'), 1);
            letterCounts.put(new Letter('L'), 4);
            letterCounts.put(new Letter('M'), 2);
            letterCounts.put(new Letter('N'), 6);
            letterCounts.put(new Letter('O'), 8);
            letterCounts.put(new Letter('P'), 2);
            letterCounts.put(new Letter('Q'), 1);
            letterCounts.put(new Letter('R'), 6);
            letterCounts.put(new Letter('S'), 4);
            letterCounts.put(new Letter('T'), 6);
            letterCounts.put(new Letter('U'), 4);
            letterCounts.put(new Letter('V'), 2);
            letterCounts.put(new Letter('W'), 2);
            letterCounts.put(new Letter('X'), 1);
            letterCounts.put(new Letter('Y'), 2);
            letterCounts.put(new Letter('Z'), 1);
        }
        LettersCounter letters = new LettersCounter(letterCounts);

        return letters;
    }
}
