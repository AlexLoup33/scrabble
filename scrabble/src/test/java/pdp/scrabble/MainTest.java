package pdp.scrabble;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pdp.scrabble.configuration.Configuration;
import pdp.scrabble.configuration.IConfiguration;
import pdp.scrabble.dictionary.Dawg;
import pdp.scrabble.dictionary.Dictionary;
import pdp.scrabble.dictionary.Letter;
import pdp.scrabble.dictionary.LettersCounter;
import pdp.scrabble.dictionary.Word;
import pdp.scrabble.dictionary.WordsLoader;
import pdp.scrabble.errorsHandling.CustomError;
import pdp.scrabble.game.Direction;
import pdp.scrabble.game.GameModel;
import pdp.scrabble.game.GridWordPos;
import pdp.scrabble.game.Lang;
import pdp.scrabble.game.LettersDrawer;
import pdp.scrabble.game.Player;
import pdp.scrabble.game.actions.PlaceWordAction;
import pdp.scrabble.view.ViewCLI;

public class MainTest {

  private LettersCounter lc;
  private Dictionary dict;
  private final Path path = java.nio.file.Paths.get("dictionary/fr.txt");
  private static final ReentrantLock lock = new ReentrantLock();

  @Before
  public void setUp() {
    lock.lock();
    lc = new LettersCounter();
    dict = new Dictionary();
  }

  @After
  public void tearDown() {
    lc = null;
    dict = null;
    lock.unlock();
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
  public void testRemoveNonExistingLetter() {
    lc.addLetter(new Letter('D'));
    assertEquals(1, lc.getLetterCount(new Letter('D')));
    assertThrows(
        CustomError.class,
        () -> {
          lc.removeLetter(new Letter('E'));
        });
  }

  @Test
  public void testEmptyCounter() {
    assertEquals(0, lc.getLetterCount(new Letter('F')));
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

  @Test
  public void testPlaceLetters() {
    IConfiguration config =
        new Configuration(
            true,
            "d",
            false,
            false,
            false,
            4,
            10,
            new boolean[4],
            15,
            2,
            "fr",
            "chepa",
            java.nio.file.Paths.get("dictionary/fr.txt"),
            java.nio.file.Paths.get("none"),
            java.nio.file.Paths.get("none"));

    GameModel gm = new GameModel(config);
    gm.dictionary.loadFromFile(java.nio.file.Paths.get("dictionary/fr.txt"));

    Player p1 = gm.players.get(0);

    Letter[] l1 = new Letter[5];
    {
      l1[0] = new Letter('L');
      l1[1] = new Letter('O');
      l1[2] = new Letter('U');
      l1[3] = new Letter('E');
      l1[4] = new Letter('E');
    }

    Letter[] l2 = new Letter[4];
    {
      l2[0] = new Letter('T');
      l2[1] = new Letter('I');
      l2[2] = new Letter('S');
      l2[3] = new Letter('A');
    }

    Letter[] l3 = new Letter[4];
    {
      l3[0] = new Letter('Z');
      l3[1] = new Letter('I');
      l3[2] = new Letter('Z');
      l3[3] = new Letter('A');
    }

    // test 1: "LOUEE" right
    assertEquals(
        "[OwnedWord{position=GridWordPos[row=2, col=0, direction=Right], word=LOUEE, owner=Player{id=0, name='Player0', score=0, bag=LettersCounter{table={}}}}]",
        gm.grid
            .placeLetters(new PlaceWordAction(p1, 0, l1, new GridWordPos(2, 0, Direction.Right)))
            .toString());
    // System.out.println(
    // gm.grid.placeLetters(new PlaceWordAction(
    // p1,
    // 0,
    // l1,
    // new GridWordPos(2, 0, Direction.Right))
    // ));

    // test 2: "TISA" down
    assertEquals(
        "[OwnedWord{position=GridWordPos[row=0, col=5, direction=Down], word=TISA, owner=Player{id=0, name='Player0', score=0, bag=LettersCounter{table={}}}}]",
        gm.grid
            .placeLetters(new PlaceWordAction(p1, 0, l2, new GridWordPos(0, 5, Direction.Down)))
            .toString());
    // System.out.println(
    // gm.grid.placeLetters(new PlaceWordAction(
    // p1,
    // 0,
    // l2,
    // new GridWordPos(0, 5, Direction.Down))
    // ));

    // test 3: "LOUEE" dowm
    assertEquals(
        "[OwnedWord{position=GridWordPos[row=8, col=8, direction=Down], word=LOUEE, owner=Player{id=0, name='Player0', score=0, bag=LettersCounter{table={}}}}]",
        gm.grid
            .placeLetters(new PlaceWordAction(p1, 0, l1, new GridWordPos(8, 8, Direction.Down)))
            .toString());
    // System.out.println(
    // gm.grid.placeLetters(new PlaceWordAction(
    // p1,
    // 0,
    // l1,
    // new GridWordPos(8, 8, Direction.Down))
    // ));

    // test 4: "TISA" right
    assertEquals(
        "[OwnedWord{position=GridWordPos[row=13, col=6, direction=Right], word=TISA, owner=Player{id=0, name='Player0', score=0, bag=LettersCounter{table={}}}}]",
        gm.grid
            .placeLetters(new PlaceWordAction(p1, 0, l2, new GridWordPos(13, 6, Direction.Right)))
            .toString());
    // System.out.println(
    // gm.grid.placeLetters(new PlaceWordAction(
    // p1,
    // 0,
    // l2,
    // new GridWordPos(13, 6, Direction.Right))
    // ));

    // test 5: "ZIZA" right, not a valid word
    assertThrows(
        CustomError.class,
        () -> {
          gm.grid
              .placeLetters(new PlaceWordAction(p1, 0, l3, new GridWordPos(13, 0, Direction.Right)))
              .toString();
        });

    // view.displayGrid(gm.grid, false);

  }

  @Test
  public void testLang() {
    Lang lang = Lang.getLang("fr");
    assertEquals(lang, Lang.getLang("fR"));
    assertEquals(lang, Lang.FR);
    assertEquals(lang.getDisplayName(), "français");
    assertThrows(
        CustomError.class,
        () -> {
          Lang.getLang("pasUneLang");
        });
    assertEquals(lang.getDictionaryPath(), Path.of("dictionary/fr.txt"));
  }

  @Test
  public void testLettersDrawer() {
    for (Lang lang : Lang.values()) {
      LettersDrawer drawer = new LettersDrawer(lang);
      // check it can draw all the letters
      final int totalLetters = drawer.getCurrentSize();
      final LettersCounter drawedLetters = new LettersCounter();
      for (int i = 0; i < totalLetters; i++) {
        drawedLetters.addLetter(drawer.popLetter());
      }
      // check if the drawer is empty
      assertThrows(
          CustomError.class,
          () -> {
            drawer.popLetter();
          });
      /// assert the letters drawn are correct
      // check the letters set
      final LettersCounter langLetters = lang.getLettersCount();
      assertEquals(drawedLetters.getLettersSet(), langLetters.getLettersSet());
      // check the letters count
      for (Letter letter : langLetters.getLettersSet()) {
        assertEquals(drawedLetters.getLetterCount(letter), langLetters.getLetterCount(letter));
      }
      // add back all the letters
      for (Letter letter : langLetters.getLettersSet()) {
        for (int i = 0; i < langLetters.getLetterCount(letter); i++) {
          drawer.pushLetter(letter);
        }
      }
      // check if the drawer is full
      assertThrows(
          CustomError.class,
          () -> {
            drawer.pushLetter(new Letter('A'));
          });
    }
  }

  @Test
  public void testLangPack() {
    ResourceBundle langPack = Lang.EN.getViewCLI_langPack();
    assertTrue(langPack != null);
    String result = langPack.getString("test");

    assertTrue(result != null && !result.isEmpty());
    String expected = "a small tests message in english";
    assertEquals(expected, result);
  }

  @Test
  public void testPrintGrid() {
    PrintStream originalOut = System.out;
    IConfiguration config =
        new Configuration(
            true,
            "d",
            false,
            false,
            false,
            4,
            10,
            new boolean[4],
            15,
            2,
            "fr",
            "chepa",
            java.nio.file.Paths.get("dictionary/fr.txt"),
            java.nio.file.Paths.get("none"),
            java.nio.file.Paths.get("none"));

    GameModel gm = new GameModel(config);
    gm.dictionary.loadFromFile(java.nio.file.Paths.get("dictionary/fr.txt"));
    ViewCLI view = new ViewCLI(gm.lang, config.getSuperScrabble());

    final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    PrintStream outStremCap = new PrintStream(outputStreamCaptor);
    try {
      System.setOut(outStremCap);
      view.displayGrid(gm.grid, false);
      String output = outputStreamCaptor.toString().trim();

      String expectedOutput =
          """
          1  2  3  4  5  6  7  8  9 10 11 12 13 14 15
          a [45m_[0m  _[0m  _[0m  [46m_[0m  _[0m  _[0m  _[0m  [45m_[0m  _[0m  _[0m  _[0m  [46m_[0m  _[0m  _[0m  [45m_[0m
          b _[0m  [41m_[0m  _[0m  _[0m  _[0m  [44m_[0m  _[0m  _[0m  _[0m  [44m_[0m  _[0m  _[0m  _[0m  [41m_[0m  _[0m
          c _[0m  _[0m  [41m_[0m  _[0m  _[0m  _[0m  [46m_[0m  _[0m  [46m_[0m  _[0m  _[0m  _[0m  [41m_[0m  _[0m  _[0m
          d [46m_[0m  _[0m  _[0m  [41m_[0m  _[0m  _[0m  _[0m  [46m_[0m  _[0m  _[0m  _[0m  [41m_[0m  _[0m  _[0m  [46m_[0m
          e _[0m  _[0m  _[0m  _[0m  [41m_[0m  _[0m  _[0m  _[0m  _[0m  _[0m  [41m_[0m  _[0m  _[0m  _[0m  _[0m
          f _[0m  [44m_[0m  _[0m  _[0m  _[0m  [44m_[0m  _[0m  _[0m  _[0m  [44m_[0m  _[0m  _[0m  _[0m  [44m_[0m  _[0m
          g _[0m  _[0m  [46m_[0m  _[0m  _[0m  _[0m  [46m_[0m  _[0m  [46m_[0m  _[0m  _[0m  _[0m  [46m_[0m  _[0m  _[0m
          h [45m_[0m  _[0m  _[0m  [46m_[0m  _[0m  _[0m  _[0m  [41m_[0m  _[0m  _[0m  _[0m  [46m_[0m  _[0m  _[0m  [45m_[0m
          i _[0m  _[0m  [46m_[0m  _[0m  _[0m  _[0m  [46m_[0m  _[0m  [46m_[0m  _[0m  _[0m  _[0m  [46m_[0m  _[0m  _[0m
          j _[0m  [44m_[0m  _[0m  _[0m  _[0m  [44m_[0m  _[0m  _[0m  _[0m  [44m_[0m  _[0m  _[0m  _[0m  [44m_[0m  _[0m
          k _[0m  _[0m  _[0m  _[0m  [41m_[0m  _[0m  _[0m  _[0m  _[0m  _[0m  [41m_[0m  _[0m  _[0m  _[0m  _[0m
          l [46m_[0m  _[0m  _[0m  [41m_[0m  _[0m  _[0m  _[0m  [46m_[0m  _[0m  _[0m  _[0m  [41m_[0m  _[0m  _[0m  [46m_[0m
          m _[0m  _[0m  [41m_[0m  _[0m  _[0m  _[0m  [46m_[0m  _[0m  [46m_[0m  _[0m  _[0m  _[0m  [41m_[0m  _[0m  _[0m
          n _[0m  [41m_[0m  _[0m  _[0m  _[0m  [44m_[0m  _[0m  _[0m  _[0m  [44m_[0m  _[0m  _[0m  _[0m  [41m_[0m  _[0m
          o [45m_[0m  _[0m  _[0m  [46m_[0m  _[0m  _[0m  _[0m  [45m_[0m  _[0m  _[0m  _[0m  [46m_[0m  _[0m  _[0m  [45m_[0m""";

      assertEquals(expectedOutput, output);
    } finally {
      System.setOut(originalOut);
      outStremCap.close();
    }
  }
}
