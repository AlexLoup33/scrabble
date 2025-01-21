package pdp.scrabble;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;
import static java.nio.file.Path.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pdp.scrabble.dictionary.Dictionary;
import pdp.scrabble.dictionary.LettersCounter;
import pdp.scrabble.dictionary.Word;

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
        lc.addLetter('A');
        lc.addLetter('b');
        lc.addLetter('C');
        lc.addLetter('c');
        assertEquals(1, lc.getLetterCount('A'));
        assertEquals(1, lc.getLetterCount('b'));
        assertEquals(2, lc.getLetterCount('C'));
        assertEquals(0, lc.getLetterCount('D'));

        // test removeLetter
        lc.removeLetter('A');
        assertEquals(0, lc.getLetterCount('A'));
        lc.removeLetter('c');
        assertEquals(1, lc.getLetterCount('C'));

        // test getLettersSet
        assertFalse(lc.getLettersSet().contains('b')); //TODO: fix this (uppercase)
        assertTrue(lc.getLettersSet().contains('B'));
        assertFalse(lc.getLettersSet().contains('A')); 
    }

    @Test
    /** Test the basic functionality of the Dictionary class **/
    public void testDictionaryBasicFunc() {
        // test addWord, containsWord
        Word word = new Word("test");
        dict.addWord(word);
        assertTrue(dict.containsWord(word));
        assertFalse(dict.containsWord(new Word("test2")));
    }

    @Test
    public void testDictionaryFindWords() {
        lc.addLetter('b');
        lc.addLetter('a');
        lc.addLetter('c');

        dict.addWord(new Word("bac"));
        dict.addWord(new Word("ba"));
        dict.addWord(new Word("baba"));
        dict.addWord(new Word("aa"));
        dict.addWord(new Word("ab"));

        assertTrue(dict.findWords(lc).contains(new Word("bac")));
        assertTrue(dict.findWords(lc).contains(new Word("ba")));
        assertTrue(dict.findWords(lc).contains(new Word("ab")));

        assertFalse(dict.findWords(lc).contains(new Word("ca"))); // 'ca' is not in the dictionary
        assertFalse(dict.findWords(lc).contains(new Word("baba"))); // not enough 'b' and 'a'

        assertFalse(dict.findWords(lc).contains(new Word("aa"))); // not enough 'a'
        lc.addLetter('a');
        assertTrue(dict.findWords(lc).contains(new Word("aa")));
    }

    @Test
    public void testDictionaryLoadFromFile() {
        dict.loadFromFile(java.nio.file.Paths.get("../dictScrableFR.txt")); //TODO: fix path
        assertTrue(dict.containsWord(new Word("AALENIEN")));
        assertFalse(dict.containsWord(new Word("zebi")));

    }
}
