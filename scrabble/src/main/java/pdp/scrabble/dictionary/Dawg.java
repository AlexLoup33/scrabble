package pdp.scrabble.dictionary;

import static pdp.scrabble.dictionary.Dictionary.generatePermutations;

import java.nio.file.Path;
import java.util.*;

public class Dawg implements IDictionary {
    private final Node root;

    public Dawg() {
        this.root = new Node();
    }

    /**
     * add a word to the dictionary
     *
     * @param word
     * @return void
     */
    @Override
    public void addWord(Word word) {
        // convert Word to String
        String wordStr = word.toString();
        // recursive
        addWord_recur(root, wordStr, 0);
    }

    /**
     * add recursively each letter of a word if a node for this letter doesn't exist, it will be
     * created
     *
     * @param node  current node to explore
     * @param word
     * @param index word letter index
     * @return void
     */
    private void addWord_recur(Node node, String word, int index) {
        // check if this is the last letter to mark node as final
        if (index == word.length()) {
            node.isEndOfWord = true;
            return;
        }

        char ch = word.charAt(index);
        node.children.putIfAbsent(
                ch, new Node()); // new node if this letter doesn't exist, follow node if exists
        addWord_recur(node.children.get(ch), word, index + 1); // follow node then pass to next letter
    }

    /**
     * check if the dawg contains the word
     *
     * @param word
     * @return boolean, true if the word is in the dictionary
     */
    @Override
    public boolean containsWord(Word word) {
        // convert Word to String
        String wordStr = word.toString();
        // recursive
        return containsWord_recur(root, wordStr, 0);
    }

    /**
     * check recursively each letter of a word if a node for this letter doesn't exist, it will be
     * created
     *
     * @param node  current node to explore
     * @param word
     * @param index word letter index
     * @return boolean
     */
    private boolean containsWord_recur(Node node, String word, int index) {
        if (index == word.length()) { // if index is the last letter
            return node.isEndOfWord; // true is node is final
        }

        char ch = word.charAt(index);
        Node childNode = node.children.get(ch); // find a child node with letter 'ch'
        if (childNode == null) { // if null, no child node with this letter => invalid word
            return false;
        }

        // explore child
        return containsWord_recur(childNode, word, index + 1);
    }

    /**
     * put every word from a file into the dawg
     *
     * @param file_name
     * @return void
     */
    @Override
    public void loadFromFile(Path file_name) {
        for (Word word : WordsLoader.loadWordsFromFile(file_name)) {
            addWord(word);
        }
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

            // convert List<Character> to Word
            for (List<Letter> word : permutations) {

                StringBuilder stringBuilder = new StringBuilder();
                for (Letter ch : word) {
                    stringBuilder.append(ch);
                }
                String word_as_str = stringBuilder.toString();
                Word w = new Word(word_as_str);

                if (this.containsWord(w)) {
                    result.add(w);
                }
            }
        }
        return result;
    }

    @Override
    public String toString() {
        throw new UnsupportedOperationException("to implement");
    }
}
