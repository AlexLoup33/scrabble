package pdp.scrabble;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pdp.scrabble.dictionary.Dictionary;
import pdp.scrabble.dictionary.Letter;
import pdp.scrabble.dictionary.LettersCounter;
import pdp.scrabble.dictionary.Word;
import pdp.scrabble.dictionary.WordsLoader;

public class MainTest {

    private LettersCounter lc;
    private Dictionary dict;

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

    @Test
    /** Test the basic functionality of the Dictionary class **/
    public void testDictionaryBasicFunc() {
        // test addWord, containsWord
        Word word = new Word("test");
        dict.addWord(word);
        assertTrue(dict.containsWord(word));
        assertFalse(dict.containsWord(new Word("testN")));

        // test toString
        Word word2 = new Word("testicule");
        dict.addWord(word2);
        assertTrue(dict.toString().contains(word.toString()));
        assertTrue(dict.toString().contains(word2.toString()));
    }

    // @Test
    // public void testDictionaryFindWords() {
    //     lc.addLetter(new Letter('B'));
    //     lc.addLetter(new Letter('A'));
    //     lc.addLetter(new Letter('C'));

    //     dict.addWord(new Word("bac"));
    //     dict.addWord(new Word("ba"));
    //     dict.addWord(new Word("baba"));
    //     dict.addWord(new Word("aa"));
    //     dict.addWord(new Word("ab"));

    //     assertTrue(dict.findWords(lc).contains(new Word("bac")));
    //     assertTrue(dict.findWords(lc).contains(new Word("ba")));
    //     assertTrue(dict.findWords(lc).contains(new Word("ab")));

    //     assertFalse(dict.findWords(lc).contains(new Word("ca"))); // 'ca' is not in the dictionary
    //     assertFalse(dict.findWords(lc).contains(new Word("baba"))); // not enough 'b' and 'a'

    //     assertFalse(dict.findWords(lc).contains(new Word("aa"))); // not enough 'a'
    //     lc.addLetter(new Letter('A'));
    //     assertTrue(dict.findWords(lc).contains(new Word("aa")));
    // }

    @Test
    public void testDictionaryLoadFromFile() {
        dict.loadFromFile(java.nio.file.Paths.get("../dictScrableFR.txt")); //TODO: fix path
        assertTrue(dict.containsWord(new Word("AALENIEN")));
        assertFalse(dict.containsWord(new Word("zebi")));
    }

    @Test
    public void testWordsLoader() {
        Path validPath = Paths.get("../dictScrableFR.txt"); //TODO: fix path
        List<Word> words = WordsLoader.loadWordsFromFile(validPath);
        assertFalse(words.isEmpty());
        assertTrue(words.contains(new Word("AALENIEN")));

        Path invalidPath = Paths.get("../FAKE_FILE.txt");
        try {
            WordsLoader.loadWordsFromFile(invalidPath);
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }
}
