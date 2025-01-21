package pdp.scrabble.dictionary;

import java.nio.file.Path;
import java.util.*;

public class Dictionary implements IDictionary {

    public final Set<Word> words = new HashSet<>();

    @Override
    public void addWord(Word word) {
        this.words.add(word);
    }

    @Override
    public void loadFromFile(Path file_name) {
        this.words.addAll(WordsLoader.loadWordsFromFile(file_name));
    }

    @Override
    public boolean containsWord(Word word) {
        return this.words.contains(word);
    }

    @Override
    public Set<Word> findWords(LettersCounter lc) {
        Set<Word> result = new HashSet<>();

        // transform LettersCounter to a list
        List<Character> lc_list = new ArrayList<>();
        for (Character key : lc.getLettersSet()) {
            int freq = lc.getLetterCount(key);
            for (int i = 0; i < freq; ++i) {
                lc_list.add(key);
            }
        }

        // add (if exists) every word which can be created by these letters
        for (int word_size = 1; word_size <= lc_list.size(); word_size++) {
            // generate all letter permutation for a final word size of word_size
            List<List<Character>> permutations = new ArrayList<>();
            generatePermutations(lc_list, new ArrayList<>(), permutations, word_size);

//            System.out.println("Size " + word_size + " letter: " + permutations);

            // convert List<Character> to Word
            for (List<Character> word : permutations) {

                StringBuilder stringBuilder = new StringBuilder();
                for (Character ch : word) {
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

    private static void generatePermutations(List<Character> original, List<Character> current, List<List<Character>> permutations, int length) {
        if (current.size() == length) {
            permutations.add(new ArrayList<>(current));
            return;
        }
        for (int i = 0; i < original.size(); i++) {
            current.add(original.get(i));
            List<Character> remaining = new ArrayList<>(original);
            remaining.remove(i);
            generatePermutations(remaining, current, permutations, length);
            current.remove(current.size() - 1);
        }
    }

    @Override
    public String toString() {
        return "Dictionary{" +
                "words=" + words +
                '}';
    }
}