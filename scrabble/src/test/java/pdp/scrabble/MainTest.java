package pdp.scrabble;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static pdp.scrabble.errorsHandling.ErrorType.UnknownWord;
import static pdp.scrabble.game.Lang.FR;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pdp.scrabble.configuration.Configuration;
import pdp.scrabble.dictionary.Dawg;
import pdp.scrabble.dictionary.Dictionary;
import pdp.scrabble.dictionary.Letter;
import pdp.scrabble.dictionary.LettersCounter;
import pdp.scrabble.dictionary.Word;
import pdp.scrabble.dictionary.WordsLoader;
import pdp.scrabble.errorsHandling.CustomError;
import pdp.scrabble.game.*;
import pdp.scrabble.game.actions.*;
import pdp.scrabble.view.ViewCLI;

public class MainTest {

  private LettersCounter lc;
  private Dictionary dict;
  private final Path path = java.nio.file.Paths.get("dictionary/fr.txt");
  private static final ReentrantLock lock = new ReentrantLock();
  private GameModel gameModel;
  private Configuration config;

  @Before
  public void setUp() {
    lock.lock();
    lc = new LettersCounter();
    dict = new Dictionary();

    // DO NOT CHANGE WITHOUT ANY REASONS
    this.config = new Configuration(
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
        69,
        "fr",
        "idk",
        java.nio.file.Paths.get("dictionary/fr.txt"),
        java.nio.file.Paths.get("none"),
        java.nio.file.Paths.get("none"));

    gameModel = new GameModel(config);
  }

  @After
  public void tearDown() {
    this.lc = null;
    this.dict = null;
    this.gameModel = null;
    this.config = null;
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
  public void testF31() {
    // System.out.println(this.gameModel.getCurrentPlayer().getName());
    assertEquals("Player0", this.gameModel.getCurrentPlayer().getName());

    GameAction ga = new RenamePlayerAction(
        this.gameModel.getCurrentPlayer(), this.gameModel.getTurn(), "franck");
    this.gameModel.applyGameActionDispatch(ga);

    // System.out.println(this.gameModel.getCurrentPlayer().getName());
    assertEquals("franck", this.gameModel.getCurrentPlayer().getName());
  }

  @Test
  public void testF33() {
    // System.out.println(this.gameModel.getCurrentPlayer().getName());
    // System.out.println(this.gameModel.getTurn());
    assertEquals("Player0", this.gameModel.getCurrentPlayer().getName());
    assertEquals(1, this.gameModel.getTurn());

    GameAction ga = new PassTurnAction(this.gameModel.getCurrentPlayer(), this.gameModel.getTurn());
    this.gameModel.applyGameActionDispatch(ga);

    // System.out.println(this.gameModel.getCurrentPlayer().getName());
    // System.out.println(this.gameModel.getTurn());
    assertEquals("Player1", this.gameModel.getCurrentPlayer().getName());
    assertEquals(1, this.gameModel.getTurn());

    // Now pass all other players to reach the next turn
    for (int i = 0; i < this.config.getPlayerCount(); i++) {
      ga = new PassTurnAction(this.gameModel.getCurrentPlayer(), this.gameModel.getTurn());
      this.gameModel.applyGameActionDispatch(ga);
    }

    // Next turn reached
    // System.out.println(this.gameModel.getTurn());
    assertEquals(2, this.gameModel.getTurn());
  }

  @Test
  public void testF36() {
    // F36. Abandon
    // Le programme devra permettre à un joueur d’abandonner la partie en cours.

    // Before
    assertFalse(this.gameModel.getCurrentPlayer().getHasForfeited());
    assertEquals(
        "[Player{id=0, name='Player0', score=0, bag=LettersCounter{table={}}}, Player{id=1, name='Player1', score=0, bag=LettersCounter{table={}}}, Player{id=2, name='Player2', score=0, bag=LettersCounter{table={}}}, Player{id=3, name='Player3', score=0, bag=LettersCounter{table={}}}]",
        this.gameModel.getActivePlayers().toString());

    // Player forfeit
    GameAction ga = new ForfeitGameAction(this.gameModel.getCurrentPlayer(), this.gameModel.getTurn());
    this.gameModel.applyGameActionDispatch(ga);

    // check forfeit
    assertTrue(this.gameModel.players.get(0).getHasForfeited());
    assertEquals(
        "[Player{id=1, name='Player1', score=0, bag=LettersCounter{table={}}}, Player{id=2, name='Player2', score=0, bag=LettersCounter{table={}}}, Player{id=3, name='Player3', score=0, bag=LettersCounter{table={}}}]",
        this.gameModel.getActivePlayers().toString());

    // Now pass all other players to reach the next turn
    for (int i = 0; i < this.config.getPlayerCount() - 1; i++) {
      ga = new PassTurnAction(this.gameModel.getCurrentPlayer(), this.gameModel.getTurn());
      this.gameModel.applyGameActionDispatch(ga);
    }

    assertEquals(1, (int) this.gameModel.getCurrentPlayer().id);
  }

  @Test
  public void testPlaceLetters() {
    Configuration config = new Configuration(
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
        -1,
        "fr",
        "idk",
        java.nio.file.Paths.get("dictionary/fr.txt"),
        java.nio.file.Paths.get("none"),
        java.nio.file.Paths.get("none"));
    ViewCLI view = new ViewCLI(FR, false);

    GameModel gm = new GameModel(config);
    gm.dictionary.loadFromFile(java.nio.file.Paths.get("dictionary/fr.txt"));

    Player p1 = gm.players.get(0);

    Letter[] l1 = new Letter[5];
    {
      String w = "LOUEE";
      for (int i = 0; i < w.length(); i++) {
        l1[i] = new Letter(w.charAt(i));
        p1.bag.addLetter(l1[i]);
      }
    }

    Letter[] l2 = new Letter[1];
    {
      l2[0] = new Letter('H');
      p1.bag.addLetter(l2[0]);
    }

    Letter[] l3 = new Letter[2];
    {
      String w = "EE";
      for (int i = 0; i < w.length(); i++) {
        l3[i] = new Letter(w.charAt(i));
        p1.bag.addLetter(l3[i]);
      }
    }

    Letter[] l4 = new Letter[4];
    {
      String w = "SEUL";
      for (int i = 0; i < w.length(); i++) {
        l4[i] = new Letter(w.charAt(i));
        p1.bag.addLetter(l4[i]);
      }
    }

    Letter[] l5 = new Letter[8];
    {
      String w = "ELEGANTE";
      for (int i = 0; i < w.length(); i++) {
        l5[i] = new Letter(w.charAt(i));
        p1.bag.addLetter(l5[i]);
      }
    }

    Letter[] l6 = new Letter[1];
    {
      l6[0] = new Letter('U');
      p1.bag.addLetter(l6[0]);
    }

    Letter[] l7 = new Letter[2];
    {
      String w = "RE";
      for (int i = 0; i < w.length(); i++) {
        l7[i] = new Letter(w.charAt(i));
        p1.bag.addLetter(l7[i]);
      }
    }

    Letter[] l8 = new Letter[3];
    {
      String w = "LET";
      for (int i = 0; i < w.length(); i++) {
        l8[i] = new Letter(w.charAt(i));
        p1.bag.addLetter(l8[i]);
      }
    }

    Letter[] l9 = new Letter[2];
    {
      String w = "IN";
      for (int i = 0; i < w.length(); i++) {
        l9[i] = new Letter(w.charAt(i));
        p1.bag.addLetter(l9[i]);
      }
    }

    assertThrows(CustomError.class, () -> {
      gm.grid.placeLetters(new PlaceWordAction(p1, 0, l1,
          new GridWordPos(0, 1, Direction.Right))).toString();
    });

    System.out.println(p1.bag);
    gm.applyGameActionDispatch(new PlaceWordAction(p1, 1, l1,
        new GridWordPos(7, 6, Direction.Right)));

    gm.applyGameActionDispatch(new PlaceWordAction(p1, 2, l4,
        new GridWordPos(7, 11, Direction.Down)));

    assertThrows(CustomError.class, () -> {
      gm.applyGameActionDispatch(new PlaceWordAction(p1, 3, l5,
          new GridWordPos(11, 12, Direction.Right)));
    });

    gm.applyGameActionDispatch(new PlaceWordAction(p1, 4, l2,
        new GridWordPos(8, 10, Direction.Right)));

    gm.applyGameActionDispatch(new PlaceWordAction(p1, 5, l6,
        new GridWordPos(10, 12, Direction.Right)));

    assertThrows(CustomError.class, () -> {
      gm.applyGameActionDispatch(new PlaceWordAction(p1, 6, l7,
          new GridWordPos(11, 12, Direction.Right)));
    });

    gm.applyGameActionDispatch(new PlaceWordAction(p1, 7, l8,
        new GridWordPos(9, 5, Direction.Right)));

    gm.applyGameActionDispatch(new PlaceWordAction(p1, 8, l9,
        new GridWordPos(8, 6, Direction.Right)));

    assertThrows(CustomError.class, () -> {
      gm.applyGameAction(new PlaceWordAction(p1, 9, l3,
          new GridWordPos(10, 5, Direction.Right)));
    });
    // view.displayGrid(gm.grid, false);

  }

  @Test
  public void testLang() {
    Lang lang = Lang.getLang("fr");
    assertEquals(lang, Lang.getLang("fR"));
    assertEquals(lang, FR);
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
      LettersDrawer drawer = new LettersDrawer(lang, new RandomAPI(-1));
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
    Configuration config = new Configuration(
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
        -1,
        "fr",
        "idk",
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

      String expectedOutput = """
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

  @Test
  public void testF41() {
    // F41. Recommencer une partie
    // À tout moment, il doit être possible de recommencer une
    // partie depuis le début en abandonnant la partie en cours.

    // ------- Before ------------------------------------

    // ------- Player place a word
    Letter[] l1 = new Letter[4];
    {
      l1[0] = new Letter('T');
      l1[1] = new Letter('E');
      l1[2] = new Letter('S');
      l1[3] = new Letter('T');
    }

    this.gameModel.grid.placeLetters(
        new PlaceWordAction(
            this.gameModel.players.get(0),
            this.gameModel.getTurn(),
            l1,
            new GridWordPos(7, 4, Direction.Right)));

    // Test grid
    assertEquals(
        """
            _ _ _ _ _ _ _ _ _ _ _ _ _ _ _\s
            _ _ _ _ _ _ _ _ _ _ _ _ _ _ _\s
            _ _ _ _ _ _ _ _ _ _ _ _ _ _ _\s
            _ _ _ _ _ _ _ _ _ _ _ _ _ _ _\s
            _ _ _ _ _ _ _ _ _ _ _ _ _ _ _\s
            _ _ _ _ _ _ _ _ _ _ _ _ _ _ _\s
            _ _ _ _ _ _ _ _ _ _ _ _ _ _ _\s
            _ _ _ _ T E S T _ _ _ _ _ _ _\s
            _ _ _ _ _ _ _ _ _ _ _ _ _ _ _\s
            _ _ _ _ _ _ _ _ _ _ _ _ _ _ _\s
            _ _ _ _ _ _ _ _ _ _ _ _ _ _ _\s
            _ _ _ _ _ _ _ _ _ _ _ _ _ _ _\s
            _ _ _ _ _ _ _ _ _ _ _ _ _ _ _\s
            _ _ _ _ _ _ _ _ _ _ _ _ _ _ _\s
            _ _ _ _ _ _ _ _ _ _ _ _ _ _ _\s
            """,
        this.gameModel.grid.toString());

    // ------- Player score ++
    this.gameModel.players.get(0).addScore(420);
    assertEquals(420, this.gameModel.players.get(0).getScore());

    // ------- Pass some turns
    GameAction ga = new PassTurnAction(this.gameModel.getCurrentPlayer(), this.gameModel.getTurn());
    this.gameModel.applyGameActionDispatch(ga);

    // Now pass all other players to reach the next turn
    for (int i = 0; i < 4; i++) {
      ga = new PassTurnAction(this.gameModel.getCurrentPlayer(), this.gameModel.getTurn());
      this.gameModel.applyGameActionDispatch(ga);
    }
    assertEquals(2, this.gameModel.getTurn());

    // ------- Player restart
    GameAction ga2 = new RestartGameAction(this.gameModel.getCurrentPlayer(), this.gameModel.getTurn());
    this.gameModel.applyGameActionDispatch(ga2);

    // ------- After------------------------------------
    assertEquals(
        """
            _ _ _ _ _ _ _ _ _ _ _ _ _ _ _\s
            _ _ _ _ _ _ _ _ _ _ _ _ _ _ _\s
            _ _ _ _ _ _ _ _ _ _ _ _ _ _ _\s
            _ _ _ _ _ _ _ _ _ _ _ _ _ _ _\s
            _ _ _ _ _ _ _ _ _ _ _ _ _ _ _\s
            _ _ _ _ _ _ _ _ _ _ _ _ _ _ _\s
            _ _ _ _ _ _ _ _ _ _ _ _ _ _ _\s
            _ _ _ _ _ _ _ _ _ _ _ _ _ _ _\s
            _ _ _ _ _ _ _ _ _ _ _ _ _ _ _\s
            _ _ _ _ _ _ _ _ _ _ _ _ _ _ _\s
            _ _ _ _ _ _ _ _ _ _ _ _ _ _ _\s
            _ _ _ _ _ _ _ _ _ _ _ _ _ _ _\s
            _ _ _ _ _ _ _ _ _ _ _ _ _ _ _\s
            _ _ _ _ _ _ _ _ _ _ _ _ _ _ _\s
            _ _ _ _ _ _ _ _ _ _ _ _ _ _ _\s
            """,
        this.gameModel.grid.toString());
    assertEquals(0, this.gameModel.players.get(0).getScore());
    assertEquals(1, this.gameModel.getTurn());
    assertEquals(0, (int) this.gameModel.getCurrentPlayer().id);
  }

  @Test
  public void testF34() {
    assertEquals(69, this.gameModel.config.getSeed());

    // Fill player's bag
    LettersCounter newLetters = this.gameModel.draw.popLetter(7);
    for (Letter l : newLetters.getLettersSet()) {
      this.gameModel.getCurrentPlayer().bag.addLetter(l);
    }
    assertEquals(
        "LettersCounter{table={L=1, A=1, U=1, I=1, H=1, R=1}}",
        this.gameModel.players.get(0).bag.toString());
    // System.out.println(this.gameModel.players.get(0).bag);

    List<Letter> toExchange = new ArrayList<>();
    toExchange.add(new Letter('L'));
    toExchange.add(new Letter('A'));

    GameAction ga = new ExchangeLetterAction(
        this.gameModel.players.get(0), this.gameModel.getTurn(), toExchange);
    this.gameModel.applyGameActionDispatch(ga);

    // System.out.println(this.gameModel.players.get(0).bag);
    assertEquals(
        "LettersCounter{table={U=1, I=2, H=1, R=1, E=1}}",
        this.gameModel.players.get(0).bag.toString());

    // Try illegal action
    List<Letter> toExchange2 = new ArrayList<>();
    toExchange2.add(new Letter('Z'));
    GameAction ga2 = new ExchangeLetterAction(
        this.gameModel.players.get(0), this.gameModel.getTurn(), toExchange2);

    assertThrows(CustomError.class, () -> this.gameModel.applyGameActionDispatch(ga2));

    // With empty draw
    this.gameModel.draw.popLetter(95); // clear draw

    assertEquals(
        "LettersCounter{table={U=1, I=2, H=1, R=1, E=1}}",
        this.gameModel.players.get(0).bag.toString());

    List<Letter> toExchange3 = new ArrayList<>();
    toExchange3.add(new Letter('U'));

    GameAction ga3 = new ExchangeLetterAction(
        this.gameModel.players.get(0), this.gameModel.getTurn(), toExchange3);
    this.gameModel.applyGameActionDispatch(ga3);

    assertEquals(
        "LettersCounter{table={U=1, I=2, H=1, R=1, E=1}}",
        this.gameModel.players.get(0).bag.toString());
  }
}
