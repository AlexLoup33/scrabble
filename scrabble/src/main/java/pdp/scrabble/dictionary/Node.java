package pdp.scrabble.dictionary;

import java.util.HashMap;
import java.util.Map;

// only used in Dawg

public class Node {
    Map<Character, Node> children = new HashMap<>();
    boolean isEndOfWord;

    // Constructor
    Node() {
        this.isEndOfWord = false;
    }
}
