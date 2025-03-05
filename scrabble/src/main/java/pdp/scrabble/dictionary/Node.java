package pdp.scrabble.dictionary;

import java.util.HashMap;
import java.util.Map;

public class Node {
  /** a node class for Dawg's tree generation */
  Map<Character, Node> children = new HashMap<>();

  boolean isEndOfWord;

  /** Constructor */
  Node() {
    this.isEndOfWord = false;
  }
}
