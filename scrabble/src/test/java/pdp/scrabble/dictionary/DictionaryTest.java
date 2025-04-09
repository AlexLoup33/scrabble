package pdp.scrabble.dictionary;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import org.junit.Before;
import org.junit.Test;
import pdp.scrabble.game.Lang;

public class DictionaryTest {

  private Dictionary dict;

  @Before
  public void setUp() {
    dict = new Dictionary();
  }

  @Test
  public void testDictionaryLoadFromFile() {
    dict.loadFromFile(Lang.FR.getDictionaryPath());
    assertTrue(dict.containsWord(new Word("AALENIEN")));
    assertFalse(dict.containsWord(new Word("cpasunmotlegalca")));
  }

  @Test
  public void testCustomDictionary() {
    Dictionary customDict = new Dictionary();
    Dictionary defaultEnDict = new Dictionary();
    String customDictContent = "ABCDEF";
    String legalContent = "STICK";

    defaultEnDict.loadFromFile(Paths.get("dictionary/en.txt"));

    try {
      FileWriter dictWriter = new FileWriter("src/test/resources/dictionary/created.txt");
      dictWriter.write(customDictContent);
      dictWriter.close();
    } catch (IOException e) {
      throw new Error("the test crashed while opening the file: " + e.toString());
    }
    customDict.loadFromFile(Paths.get("src/test/resources/dictionary/created.txt"));

    // Our newly created custom dict contains our test word
    assertTrue(customDict.toString().contains(customDictContent));
    // but not the default dict
    assertFalse(defaultEnDict.toString().contains(customDictContent));

    // Our newly created custom dict does not contain standard word
    assertFalse(customDict.toString().contains(legalContent));
    // but the default does
    assertTrue(defaultEnDict.toString().contains(legalContent));
  }
}
