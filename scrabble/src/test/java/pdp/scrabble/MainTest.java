package pdp.scrabble;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pdp.scrabble.dictionary.*;

public class MainTest {

    private LettersCounter lc;
    private Dictionary dict;
    private final Path path = java.nio.file.Paths.get("dictionary/fr.txt");

    @Before
    public void setUp() {
        lc = new LettersCounter();
        dict = new Dictionary();
    }

    @After
    public void tearDown() {
        lc = null;
        dict = null;
    }

    @Test
    public void testLettersCounter() {
        // test addLetter, getLetterCount
        lc.addLetter(new Letter('A'));
        lc.addLetter(new Letter('B'));
        lc.addLetter(new Letter('C'));
        lc.addLetter(new Letter('B'));
        assertEquals(1, lc.getLetterCount(new Letter('A')));
        assertEquals(2, lc.getLetterCount(new Letter('B')));
        assertEquals(1, lc.getLetterCount(new Letter('C')));
        assertEquals(0, lc.getLetterCount(new Letter('D')));

        // test removeLetter
        lc.removeLetter(new Letter('A'));
        assertEquals(0, lc.getLetterCount(new Letter('A')));
        lc.removeLetter(new Letter('C'));
        assertEquals(0, lc.getLetterCount(new Letter('C')));
        assertEquals(0, lc.getLetterCount(new Letter('Z')));

        // test getLettersSet
        assertTrue(lc.getLettersSet().contains(new Letter('B')));
        assertFalse(lc.getLettersSet().contains(new Letter('A')));
    }

//    @Test
//    public void testRemoveNonExistingLetter() {
//        lc.addLetter(new Letter('D'));
//        assertEquals(1, lc.getLetterCount(new Letter('D')));
//        assertThrows(
//                IllegalArgumentException.class,
//                () -> {
//                    lc.removeLetter(new Letter('E'));
//                });
//    }

    @Test
    public void testEmptyCounter() {
        assertEquals(0, lc.getLetterCount(new Letter('F')));
    }

    /**
     * Test the basic functionality of the Dictionary class
     */
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
        dict.loadFromFile(java.nio.file.Paths.get("dictionary/fr.txt"));
        assertTrue(dict.containsWord(new Word("AALENIEN")));
        assertFalse(dict.containsWord(new Word("cpasunmotlegalca")));
    }

    @Test
    public void testWordsLoader() {
        List<Word> words = WordsLoader.loadWordsFromFile(path);
        assertFalse(words.isEmpty());
        assertTrue(words.contains(new Word("AALENIEN")));

        Path invalidPath = Paths.get("../FAKE_FILE.txt");
        try {
            WordsLoader.loadWordsFromFile(invalidPath);
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }

    @Test
    public void testDawg() {
        Dawg dawg = new Dawg();
        dawg.loadFromFile(path);

        assertTrue(dawg.containsWord(new Word("car")));

        assertFalse(dawg.containsWord(new Word("notagoodword")));

        dawg.addWord(new Word("notagoodword"));
        assertTrue(dawg.containsWord(new Word("notagoodword")));

        LettersCounter lc = new LettersCounter();
        lc.addLetter(new Letter('A'));
        lc.addLetter(new Letter('B'));
        lc.addLetter(new Letter('R'));

        Set<Word> result = dawg.findWords(lc);
        for (Word word : result) {
            assertTrue(dawg.containsWord(word));
        }
    }

    @Test
    public void testDictionary() {
        Dictionary Dictionary = new Dictionary();
        Dictionary.loadFromFile(path);

        assertTrue(Dictionary.containsWord(new Word("car")));

        assertFalse(Dictionary.containsWord(new Word("notagoodword")));

        Dictionary.addWord(new Word("notagoodword"));
        assertTrue(Dictionary.containsWord(new Word("notagoodword")));

        LettersCounter lc = new LettersCounter();
        lc.addLetter(new Letter('A'));
        lc.addLetter(new Letter('B'));
        lc.addLetter(new Letter('R'));

        Set<Word> result = Dictionary.findWords(lc);
        for (Word word : result) {
            assertTrue(Dictionary.containsWord(word));
        }
    }
}
