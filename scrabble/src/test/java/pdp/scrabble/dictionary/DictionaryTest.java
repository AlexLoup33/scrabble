package pdp.scrabble.dictionary;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import org.junit.Before;
import org.junit.Test;

public class DictionaryTest {

  private Dictionary dict;

  @Before
  public void setUp() {
    dict = new Dictionary();
  }

  /** Test the basic functionality of the Dictionary class */
  @Test
  public void testDictionaryBasicFunc() {
    // test addWord, containsWord
    Word word = new Word("test");
    dict.addWord(word);
    assertTrue(dict.containsWord(word));
    assertFalse(dict.containsWord(new Word("testDeux")));

    // test toString
    Word word2 = new Word("frank");
    dict.addWord(word2);
    assertTrue(dict.toString().contains(word.toString()));
    assertTrue(dict.toString().contains(word2.toString()));
  }

  @Test
  public void testDictionaryLoadFromFile() {
    dict.loadFromFile(Paths.get("dictionary/fr.txt"));
    assertTrue(dict.containsWord(new Word("AALENIEN")));
    assertFalse(dict.containsWord(new Word("cpasunmotlegalca")));
  }

  @Test
  public void testCustomDictionary() {
    try {
      Dictionary customDict = new Dictionary();
      Dictionary defaultEnDict = new Dictionary();
      String customDictContent = "ABCDEF";
      String legalContent = "STICK";

      defaultEnDict.loadFromFile(Paths.get("dictionary/en.txt"));

      FileWriter dictWriter = new FileWriter("src/test/resources/dictionary/created.txt");
      dictWriter.write(customDictContent);
      dictWriter.close();
      customDict.loadFromFile(Paths.get("src/test/resources/dictionary/created.txt"));

      // Our newly created custom dict contains our test word
      assertTrue(customDict.toString().contains(customDictContent));
      // but not the default dict
      assertFalse(defaultEnDict.toString().contains(customDictContent));

      // Our newly created custom dict does not contain standard word
      assertFalse(customDict.toString().contains(legalContent));
      // but the default does
      assertTrue(defaultEnDict.toString().contains(legalContent));

    } catch (IOException e) {
      System.out.println(e);
    }
  }
}
