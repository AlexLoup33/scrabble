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

import pdp.scrabble.configuration.Configuration;
import pdp.scrabble.configuration.ConfigurationBuilderTest;
import pdp.scrabble.configuration.IConfiguration;
import pdp.scrabble.dictionary.*;
import pdp.scrabble.errorsHandling.CustomError;
import pdp.scrabble.game.*;
import pdp.scrabble.game.actions.PlaceWordAction;
import pdp.scrabble.view.ViewCLI;

public class MainTest {

  private LettersCounter lc;
  private Dictionary dict;
  private final Path path = java.nio.file.Paths.get("dictionary/fr.txt");
  private IConfiguration config;

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
    IConfiguration config = new IConfiguration() {
      @Override
      public String getLanguage() {
        return "fr"; // Langue 'fr'
      }

      @Override
      public Path getDictFile() {
        return Paths.get("dictionary/fr.txt"); // Chemin du fichier dictionnaire
      }

      @Override
      public Boolean getVerbose() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getVerbose'");
      }

      @Override
      public String getDebug() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDebug'");
      }

      @Override
      public Boolean getBlitz() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBlitz'");
      }

      @Override
      public Boolean getSuperScrabble() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSuperScrabble'");
      }

      @Override
      public Boolean getGui() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getGui'");
      }

      @Override
      public int getPlayerCount() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPlayerCount'");
      }

      @Override
      public int getBlitzTime() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBlitzTime'");
      }

      @Override
      public boolean[] getAiPlayers() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAiPlayers'");
      }

      @Override
      public int getAiDepth() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAiDepth'");
      }

      @Override
      public int getAiTime() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAiTime'");
      }

      @Override
      public String getAiMode() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAiMode'");
      }

      @Override
      public Path getContestFile() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getContestFile'");
      }

      @Override
      public Path getHistoryFile() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getHistoryFile'");
      }
    };
    GameModel gm = new GameModel(config);
    gm.dictionary.loadFromFile(java.nio.file.Paths.get("dictionary/fr.txt"));

    Player p1 = new Player("franck");

    Grid grid = new Grid(gm, 15);

    Letter[] l1 = new Letter[5];
    l1[0] = new Letter('L');
    l1[1] = new Letter('O');
    l1[2] = new Letter('U');
    l1[3] = new Letter('E');
    l1[4] = new Letter('E');

    Letter[] l2 = new Letter[4];
    l2[0] = new Letter('T');
    l2[1] = new Letter('I');
    l2[2] = new Letter('S');
    l2[3] = new Letter('A');

    assertEquals(
        "[OwnedWord{position=GridWordPos[row=2, col=0, direction=Right], word=LOUEE, owner=Player{id=0, name='franck', score=0, bag=LettersCounter{table={}}}}]",
        grid.placeLetters(new PlaceWordAction(p1, 0, l1, new GridWordPos(2, 0, Direction.Right)))
            .toString());

    assertEquals(
        "[OwnedWord{position=GridWordPos[row=0, col=5, direction=Down], word=TISA, owner=Player{id=0, name='franck', score=0, bag=LettersCounter{table={}}}}, OwnedWord{position=GridWordPos[row=0, col=5, direction=Down], word=LOUEES, owner=Player{id=0, name='franck', score=0, bag=LettersCounter{table={}}}}]",
        grid.placeLetters(new PlaceWordAction(p1, 0, l2, new GridWordPos(0, 5, Direction.Down)))
            .toString());

    assertEquals(
        "[OwnedWord{position=GridWordPos[row=8, col=8, direction=Down], word=LOUEE, owner=Player{id=0, name='franck', score=0, bag=LettersCounter{table={}}}}]",
        grid.placeLetters(new PlaceWordAction(p1, 0, l1, new GridWordPos(8, 8, Direction.Down)))
            .toString());

    assertEquals(
        "[OwnedWord{position=GridWordPos[row=13, col=6, direction=Right], word=TISA, owner=Player{id=0, name='franck', score=0, bag=LettersCounter{table={}}}}, OwnedWord{position=GridWordPos[row=13, col=6, direction=Right], word=LOUEES, owner=Player{id=0, name='franck', score=0, bag=LettersCounter{table={}}}}]",
        grid.placeLetters(new PlaceWordAction(p1, 0, l2, new GridWordPos(13, 6, Direction.Right)))
            .toString());

    // to print:

    // // Test 1
    // System.out.println(
    // grid.placeLetters(new PlaceWordAction(
    // p1,
    // 0,
    // l1,
    // new GridWordPos(2, 0, Direction.Right))
    // ));
    // System.out.println(
    // grid.placeLetters(new PlaceWordAction(
    // p1,
    // 0,
    // l2,
    // new GridWordPos(0, 5, Direction.Down))
    // ));
    //
    // // Test 2
    // System.out.println(
    // grid.placeLetters(new PlaceWordAction(
    // p1,
    // 0,
    // l1,
    // new GridWordPos(8, 8, Direction.Down))
    // ));
    // System.out.println(
    // grid.placeLetters(new PlaceWordAction(
    // p1,
    // 0,
    // l2,
    // new GridWordPos(13, 6, Direction.Right))
    // ));
    //
    //
    // grid.printGrid();

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
      /// assert the letters drawed are correct
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

  public static void PropertiesTest(String[] args) {
    ViewCLI view = new ViewCLI("fr");
    String result = view.getLangPack("test");
    System.out.println(result);

    assertTrue(result != null && !result.isEmpty());
  }
}
