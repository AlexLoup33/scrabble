package pdp.scrabble;

import static org.junit.Assert.*;
import static pdp.scrabble.game.Lang.FR;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pdp.scrabble.configuration.Configuration;
import pdp.scrabble.dictionary.Dictionary;
import pdp.scrabble.dictionary.Letter;
import pdp.scrabble.dictionary.LettersCounter;
import pdp.scrabble.dictionary.Word;
import pdp.scrabble.dictionary.WordsLoader;
import pdp.scrabble.errorsHandling.CustomError;
import pdp.scrabble.errorsHandling.ErrorType;
import pdp.scrabble.game.*;
import pdp.scrabble.game.History.History;
import pdp.scrabble.game.actions.*;
import pdp.scrabble.game.serializer.parser.CharInputStream;

public class MainTest {

  private LettersCounter lc;
  private final Path path = java.nio.file.Paths.get("dictionary/fr.txt");
  private GameModel gameModel;
  private Configuration config;

  public static void assertThrowsCustom(ErrorType errType, Runnable test) {
    try {
      test.run();
    } catch (CustomError e) {
      if (e.type != errType) {
        // System.err.println(e.toString());
        e.printStackTrace();
        throw e;
      }
    }
  }

  public static Letter[] lettersFromString(String s) {
    Letter[] res = new Letter[s.length()];
    for (int i = 0; i < s.length(); i++) {
      res[i] = new Letter(s.charAt(i));
    }
    return res;
  }

  @Before
  public void setUp() {
    lc = new LettersCounter();

    // DO NOT CHANGE WITHOUT ANY REASONS
    this.config =
        new Configuration(
            true,
            "d",
            false,
            false,
            false,
            4,
            30,
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
    this.gameModel = null;
    this.config = null;
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
    assertThrows(
        CustomError.class,
        () -> {
          WordsLoader.loadWordsFromFile(invalidPath);
        });
  }

  @Test
  public void testDictionary() {
    Dictionary Dictionary = new Dictionary();
    Dictionary.loadFromFile(path);

    assertTrue(Dictionary.containsWord(new Word("car")));

    assertFalse(Dictionary.containsWord(new Word("notagoodword")));

    LettersCounter lc = new LettersCounter();
    lc.addLetter(new Letter('A'));
    lc.addLetter(new Letter('B'));
    lc.addLetter(new Letter('R'));
  }

  @Test
  public void testRenamePlayer() {
    assertEquals("Player0", this.gameModel.getCurrentPlayer().getName());

    GameAction ga =
        new RenamePlayerAction(
            this.gameModel.getCurrentPlayer(), this.gameModel.getTurn(), "franck");
    this.gameModel.applyGameActionDispatch(ga, 0);

    assertEquals("franck", this.gameModel.getCurrentPlayer().getName());
  }

  @Test
  public void testPassTurn() {
    assertEquals("Player0", this.gameModel.getCurrentPlayer().getName());
    assertEquals(1, this.gameModel.getTurn());

    GameAction ga = new PassTurnAction(this.gameModel.getCurrentPlayer(), this.gameModel.getTurn());
    this.gameModel.applyGameActionDispatch(ga, 0);

    assertEquals("Player1", this.gameModel.getCurrentPlayer().getName());
    assertEquals(1, this.gameModel.getTurn());

    // Now pass all other players to reach the next turn
    for (int i = 0; i < this.config.getPlayerCount(); i++) {
      ga = new PassTurnAction(this.gameModel.getCurrentPlayer(), this.gameModel.getTurn());
      this.gameModel.applyGameActionDispatch(ga, 0);
    }

    // Next turn reached
    assertEquals(2, this.gameModel.getTurn());
  }

  @Test
  public void testForfeit() {
    // F36. Abandon
    // Le programme devra permettre à un joueur d’abandonner la partie en cours.

    // Before
    assertFalse(this.gameModel.getCurrentPlayer().getHasForfeited());
    assertEquals(
        "[0, 1, 2, 3]",
        this.gameModel.getActivePlayers().stream().map(p -> p.id).toList().toString());

    // Player forfeit
    GameAction ga =
        new ForfeitGameAction(this.gameModel.getCurrentPlayer(), this.gameModel.getTurn());
    this.gameModel.applyGameActionDispatch(ga, 0);

    // check forfeit
    assertTrue(ga.player.getHasForfeited());
    assertEquals(
        "[1, 2, 3]", this.gameModel.getActivePlayers().stream().map(p -> p.id).toList().toString());

    // Now pass all other players to reach the next turn
    for (int i = 0; i < this.config.getPlayerCount() - 1; i++) {
      ga = new PassTurnAction(this.gameModel.getCurrentPlayer(), this.gameModel.getTurn());
      this.gameModel.applyGameActionDispatch(ga, 0);
    }

    assertEquals(1, (int) this.gameModel.getCurrentPlayer().id);
  }

  @Test
  public void testPlaceWord() {
    Configuration config =
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
            -1,
            "fr",
            "idk",
            java.nio.file.Paths.get("dictionary/fr.txt"),
            java.nio.file.Paths.get("none"),
            java.nio.file.Paths.get("none"));

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

    gm.applyGameAction(new PlaceWordAction(p1, 1, l1, new GridWordPos(7, 6, Direction.Right)));

    gm.applyGameAction(new PlaceWordAction(p1, 2, l4, new GridWordPos(7, 11, Direction.Down)));

    assertThrows(
        CustomError.class,
        () -> {
          gm.applyGameAction(
              new PlaceWordAction(p1, 3, l5, new GridWordPos(11, 12, Direction.Right)));
        });

    gm.applyGameAction(new PlaceWordAction(p1, 4, l2, new GridWordPos(8, 10, Direction.Right)));

    gm.applyGameAction(new PlaceWordAction(p1, 5, l6, new GridWordPos(10, 12, Direction.Right)));

    assertThrows(
        CustomError.class,
        () -> {
          gm.applyGameAction(
              new PlaceWordAction(p1, 6, l7, new GridWordPos(11, 12, Direction.Right)));
        });

    gm.applyGameAction(new PlaceWordAction(p1, 8, l9, new GridWordPos(8, 6, Direction.Right)));

    gm.applyGameAction(new PlaceWordAction(p1, 7, l8, new GridWordPos(9, 5, Direction.Right)));

    assertThrows(
        CustomError.class,
        () -> {
          gm.applyGameAction(
              new PlaceWordAction(p1, 9, l3, new GridWordPos(10, 5, Direction.Right)));
        });

    assertThrows(
        CustomError.class,
        () -> {
          gm.applyGameAction(
              new PlaceWordAction(p1, 10, l8, new GridWordPos(2, 2, Direction.Right)));
        });
    assertEquals(53, p1.getScore());

    assertEquals(
        "_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ \n"
            + "_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ \n"
            + "_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ \n"
            + "_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ \n"
            + "_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ \n"
            + "_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ \n"
            + "_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ \n"
            + "_ _ _ _ _ _ L O U E E S _ _ _ \n"
            + "_ _ _ _ _ _ I N _ _ H E _ _ _ \n"
            + "_ _ _ _ _ L E T _ _ _ U _ _ _ \n"
            + "_ _ _ _ _ _ _ _ _ _ _ L U _ _ \n"
            + "_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ \n"
            + "_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ \n"
            + "_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ \n"
            + "_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ \n",
        gm.grid.toString());
  }

  @Test
  public void testLang() {
    Lang lang = Lang.getLang("fr");
    assertEquals(lang, Lang.getLang("fR"));
    assertEquals(FR, lang);
    assertEquals("français", lang.getDisplayName());
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
      LettersDrawer drawer = new LettersDrawer(lang, new RandomAPI(-1), false);
      // check it can draw all the letters
      final int totalLetters = drawer.getCurrentSize();
      final LettersCounter drawedLetters = new LettersCounter();
      for (int i = 0; i < totalLetters; i++) {
        drawedLetters.addLetter(drawer.popLetter());
      }
      // check if the drawer is empty
      assertThrows(CustomError.class, drawer::popLetter);
      /// assert the letters drawn are correct
      // check the letters set
      final LettersCounter langLetters = lang.getLettersCount(false);
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
    ResourceBundle langPack = Lang.EN.getViewCLILangPack();
    assertNotNull(langPack);
    String result = langPack.getString("test");

    assertTrue(result != null && !result.isEmpty());
    String expected = "a small tests message in english";
    assertEquals(expected, result);
  }

  @Test
  public void testRestartGame() {
    // F41. Restart a game
    // At any time, it should be possible to restart a game from the beginning by
    // abandoning the current game.

    // ------- Before ------------------------------------

    // ------- Player place a word
    Letter[] l1 = new Letter[5];
    {
      l1[0] = new Letter('A');
      l1[1] = new Letter('G');
      l1[2] = new Letter('I');
      l1[3] = new Letter('L');
      l1[4] = new Letter('E');
    }

    this.gameModel.applyGameActionDispatch(
        new PlaceWordAction(
            this.gameModel.players.get(0),
            this.gameModel.getTurn(),
            l1,
            new GridWordPos(7, 5, Direction.Right)),
        0L);

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
            _ _ _ _ _ A G I L E _ _ _ _ _\s
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
    assertEquals(432, this.gameModel.players.get(0).getScore());

    // ------- Pass some turns
    GameAction ga = new PassTurnAction(this.gameModel.getCurrentPlayer(), this.gameModel.getTurn());
    this.gameModel.applyGameActionDispatch(ga, 0);

    // Now pass all other players to reach the next turn
    for (int i = 0; i < 4; i++) {
      ga = new PassTurnAction(this.gameModel.getCurrentPlayer(), this.gameModel.getTurn());
      this.gameModel.applyGameActionDispatch(ga, 0);
    }
    assertEquals(2, this.gameModel.getTurn());

    // ------- Player restart
    GameAction ga2 =
        new RestartGameAction(this.gameModel.getCurrentPlayer(), this.gameModel.getTurn());
    this.gameModel.applyGameActionDispatch(ga2, 0);

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
  public void testExchangeLetter() {
    assertEquals(69, this.gameModel.config.getSeed());

    assertEquals(
        "LettersCounter{table=A:1,E:1,G:1,I:1,L:2,R:1}",
        this.gameModel.getCurrentPlayer().bag.toString());

    List<Letter> toExchange = new ArrayList<>();
    toExchange.add(new Letter('L'));
    toExchange.add(new Letter('A'));

    ExchangeLetterAction ga =
        new ExchangeLetterAction(
            this.gameModel.getCurrentPlayer(), this.gameModel.getTurn(), toExchange);
    this.gameModel.applyGameActionDispatch(ga, 0);
    assertEquals("LettersCounter{table=A:1,M:1}", ga.lettersDrawed.toString());
    assertEquals("LettersCounter{table=A:1,E:1,G:1,I:1,L:1,M:1,R:1}", ga.player.bag.toString());

    // Try illegal action
    String bagBefore = this.gameModel.getCurrentPlayer().bag.toString();
    List<Letter> toExchange2 = new ArrayList<>();
    toExchange2.add(new Letter('Z'));
    ExchangeLetterAction ga2 =
        new ExchangeLetterAction(
            this.gameModel.getCurrentPlayer(), this.gameModel.getTurn(), toExchange2);
    assertThrows(CustomError.class, () -> this.gameModel.applyGameActionDispatch(ga2, 0));
    // check same player and same bag
    assertEquals(ga2.player, this.gameModel.getCurrentPlayer());
    assertEquals(bagBefore, ga2.player.bag.toString());

    List<Letter> toExchange3 = new ArrayList<>();
    toExchange3.add(new Letter('U'));

    GameAction ga3 =
        new ExchangeLetterAction(
            this.gameModel.getCurrentPlayer(), this.gameModel.getTurn(), toExchange3);
    this.gameModel.applyGameActionDispatch(ga3, 0);

    assertEquals("LettersCounter{table=#:1,A:2,E:1,N:1,O:1,S:1}", ga3.player.bag.toString());
  }

  @Test
  public void testModeBlitz() {
    Configuration config =
        new Configuration(
            true,
            "d",
            true,
            false,
            false,
            4,
            30,
            new boolean[4],
            15,
            2,
            69,
            "en",
            "idk",
            java.nio.file.Paths.get("dictionary/en.txt"),
            java.nio.file.Paths.get("none"),
            java.nio.file.Paths.get("none"));

    GameModel gm = new GameModel(config);

    // check the default time for each player with t=10'
    for (Player p : gm.players) {
      assertEquals(1800, p.getRemainingTime(), 0);
    }

    for (Player p : gm.players) {
      PassTurnAction pta = new PassTurnAction(p, gm.getTurn());
      gm.applyGameActionDispatch(pta, 66);
      assertEquals(1734.0, p.getRemainingTime(), 0);
    }

    // Set the remaining time to 0 for a player
    Player p = gm.players.get(0);
    assertFalse(p.getHasForfeited());
    PassTurnAction pta = new PassTurnAction(p, gm.getTurn());
    gm.applyGameActionDispatch(pta, 9999);
    assertTrue(p.getHasForfeited());
  }

  @Test
  public void testTimeBlitz() {
    // check the default time for each player with default config, so t=30'
    for (Player p : this.gameModel.players) {
      assertEquals(1800, p.getRemainingTime(), 0);
    }

    Configuration config =
        new Configuration(
            true,
            "d",
            true,
            false,
            false,
            4,
            10,
            new boolean[4],
            15,
            2,
            69,
            "en",
            "idk",
            java.nio.file.Paths.get("dictionary/en.txt"),
            java.nio.file.Paths.get("none"),
            java.nio.file.Paths.get("none"));

    GameModel gm = new GameModel(config);

    // check the default time for each player with t=10'
    for (Player p : gm.players) {
      assertEquals(600, p.getRemainingTime(), 0);
    }
  }

  @Test
  public void TestToSerializedString() {
    List<Letter> toExchange = new ArrayList<>();
    toExchange.add(new Letter('L'));
    toExchange.add(new Letter('A'));

    ExchangeLetterAction excLet =
        new ExchangeLetterAction(
            this.gameModel.getCurrentPlayer(), this.gameModel.getTurn(), toExchange);
    assertEquals("0 ExchangeLetter [A,L]", excLet.toSerializedString());

    ForfeitGameAction forfait =
        new ForfeitGameAction(this.gameModel.getCurrentPlayer(), this.gameModel.getTurn());
    assertEquals("0 ForfeitGame", forfait.toSerializedString());
    assertEquals(
        "ForfeitGameAction{player=Player{id=0, name='Player0', score=0, bag=LettersCounter{table=A:1,E:1,G:1,I:1,L:2,R:1}, hasForfeited=false, hasPassed=false, remainingTime=1800},turn=1",
        forfait.toString());

    PassTurnAction pass =
        new PassTurnAction(this.gameModel.getCurrentPlayer(), this.gameModel.getTurn());
    assertEquals("0 PassTurn", pass.toSerializedString());

    Letter[] toPlace = new Letter[5];
    {
      String w = "LOUEE";
      for (int i = 0; i < w.length(); i++) {
        toPlace[i] = new Letter(w.charAt(i));
        this.gameModel.getCurrentPlayer().bag.addLetter(toPlace[i]);
      }
    }

    PlaceWordAction place =
        new PlaceWordAction(
            this.gameModel.getCurrentPlayer(),
            this.gameModel.getTurn(),
            toPlace,
            new GridWordPos(0, 1, Direction.Right));
    assertEquals("0 PlaceWord a2h LOUEE", place.toSerializedString());

    History<GameAction> history = new History<>();
    for (int i = 0; i < 5; i++) {
      PassTurnAction pa =
          new PassTurnAction(gameModel.getCurrentPlayer(), this.gameModel.getTurn());
      gameModel.applyGameActionDispatch(pa, 0);
      history.addPlayedAction(pa);
    }
    RenamePlayerAction ra =
        new RenamePlayerAction(this.gameModel.getCurrentPlayer(), this.gameModel.getTurn(), "BOB");
    gameModel.applyGameActionDispatch(ra, 0);
    history.addPlayedAction(ra);

    assertEquals(
        "1. 0 PassTurn 1 PassTurn 2 PassTurn 3 PassTurn;\n"
            + //
            "2. 0 PassTurn 1 RenamePlayer \"BOB\";",
        history.toSerializedString());
  }

  @Test
  public void TestCharInputStream() {
    CharInputStream cis = new CharInputStream(path);
    assertEquals('A', cis.next());
    assertEquals('A', cis.peek());
    assertEquals(false, cis.eof());
    assertThrows(CustomError.class, () -> cis.croak(null));
  }

  @Test
  public void TestsUndoRedo() {
    // placing valid word
    GridWordPos s = new GridWordPos(7, 5, Direction.Right);
    Letter[] w = lettersFromString("ARILLE");
    this.gameModel.applyGameActionDispatch(
        new PlaceWordAction(this.gameModel.getCurrentPlayer(), this.gameModel.getTurn(), w, s), 1);
    // pass turn
    this.gameModel.applyGameActionDispatch(
        new PassTurnAction(this.gameModel.getCurrentPlayer(), this.gameModel.getTurn()), 2);
    // rename player
    this.gameModel.applyGameActionDispatch(
        new RenamePlayerAction(
            this.gameModel.getCurrentPlayer(), this.gameModel.getTurn(), "PIKACHU"),
        3);
    // rename player
    this.gameModel.applyGameActionDispatch(
        new RenamePlayerAction(
            this.gameModel.getCurrentPlayer(), this.gameModel.getTurn(), "BEBOU"),
        4);
    // rename player
    this.gameModel.applyGameActionDispatch(
        new ExchangeLetterAction(
            this.gameModel.getCurrentPlayer(),
            this.gameModel.getTurn(),
            Arrays.asList(lettersFromString("VUSEENM"))),
        4);
    // forefeit
    this.gameModel.applyGameActionDispatch(
        new ForfeitGameAction(this.gameModel.getCurrentPlayer(), this.gameModel.getTurn()), 5);

    // try some undo redo of the game
    this.gameModel.undoAll();
    this.gameModel.redoAll();
    this.gameModel.undoAll();
    this.gameModel.redoAll();
    // undo 3
    for (int i = 0; i < 3; i++) {
      this.gameModel.applyGameActionDispatch(
          new UndoAction(this.gameModel.getCurrentPlayer(), this.gameModel.getTurn()), 5);
    }
    // redo one
    this.gameModel.applyGameActionDispatch(
        new RedoAction(this.gameModel.getCurrentPlayer(), this.gameModel.getTurn()), 6);
    // placing valid word
    s = new GridWordPos(6, 8, Direction.Down);
    w = lettersFromString("ULVES");
    this.gameModel.applyGameActionDispatch(
        new PlaceWordAction(this.gameModel.getCurrentPlayer(), this.gameModel.getTurn(), w, s), 14);
  }

  @Test
  public void TestSomeEdgeCases() {
    // test forfeiting all players
    for (int i = 0; i < this.gameModel.players.size(); i++) {
      this.gameModel.applyGameActionDispatch(
          new ForfeitGameAction(this.gameModel.getCurrentPlayer(), this.gameModel.getTurn()), 5);
    }
    assertEquals(0, this.gameModel.getActivePlayers().size());
    this.gameModel.undoAll();

    // placing valid word (of size 7 => score a scrable)
    Letter[] w = lettersFromString("ANOXIES");
    this.gameModel.getCurrentPlayer().bag.clear();
    this.gameModel.getCurrentPlayer().bag.addOther(w);
    GridWordPos s = new GridWordPos(7, 5, Direction.Right);
    PlaceWordAction pwa =
        new PlaceWordAction(this.gameModel.getCurrentPlayer(), this.gameModel.getTurn(), w, s);
    assertEquals(0, pwa.player.getScore());
    this.gameModel.applyGameActionDispatch(pwa, 1);
    assertEquals(84, pwa.player.getScore());
    // test action with wrong player
    assertThrowsCustom(
        ErrorType.WrongPlayerAction,
        () -> {
          this.gameModel.applyGameActionDispatch(
              new PassTurnAction(this.gameModel.players.get(0), this.gameModel.getTurn()), 0);
        });
    assertThrowsCustom(
        ErrorType.WrongTurnAction,
        () -> {
          this.gameModel.applyGameActionDispatch(
              new PassTurnAction(this.gameModel.getCurrentPlayer(), this.gameModel.getTurn() - 1),
              0);
        });

    this.gameModel.undoAll();
    // test missing letters
    this.gameModel.getCurrentPlayer().bag.clear();
    this.gameModel.getCurrentPlayer().bag.addOther(w);
    assertThrowsCustom(
        ErrorType.PlayerDoesntHaveLettersInIsBag,
        () -> {
          Letter[] w2 = lettersFromString("ZZZZZZ");
          GridWordPos s2 = new GridWordPos(7, 5, Direction.Right);
          this.gameModel.applyGameActionDispatch(
              new PlaceWordAction(
                  this.gameModel.getCurrentPlayer(), this.gameModel.getTurn(), w2, s2),
              0);
        });

    // test unsupported ops
    // restart
    assertThrows(
        UnsupportedOperationException.class,
        () -> {
          new RestartGameAction(null, 0).toSerializedString();
        });
    assertThrows(
        UnsupportedOperationException.class,
        () -> {
          new RestartGameAction(null, 0).undo(null);
        });
    // quit
    assertThrows(
        UnsupportedOperationException.class,
        () -> {
          new QuitAction(null, 0, true).toSerializedString();
        });
    assertThrows(
        UnsupportedOperationException.class,
        () -> {
          new QuitAction(null, 0, true).undo(null);
        });
    // redo
    assertThrows(
        UnsupportedOperationException.class,
        () -> {
          new RedoAction(null, 0).toSerializedString();
        });
    assertThrows(
        UnsupportedOperationException.class,
        () -> {
          new RedoAction(null, 0).undo(null);
        });
    // undo
    assertThrows(
        UnsupportedOperationException.class,
        () -> {
          new UndoAction(null, 0).toSerializedString();
        });
    assertThrows(
        UnsupportedOperationException.class,
        () -> {
          new UndoAction(null, 0).undo(null);
        });
  }

  @Test
  public void TestPlaceLettersAndFilter() {
    assertThrowsCustom(
        ErrorType.UnfilteredPlaceWordAction, // unfiltered place word action
        () -> {
          GridWordPos s = new GridWordPos(7, 7, Direction.Right);
          Letter[] w = lettersFromString("");
          this.gameModel.grid.placeLetters(
              new PlaceWordAction(
                  this.gameModel.getCurrentPlayer(), this.gameModel.getTurn(), w, s));
        });

    assertThrowsCustom(
        ErrorType.UnknownWord, // empty word
        () -> {
          GridWordPos s = new GridWordPos(7, 7, Direction.Right);
          Letter[] w = lettersFromString("");
          this.gameModel.grid.placeLetters(
              new PlaceWordAction(
                  this.gameModel.getCurrentPlayer(), this.gameModel.getTurn(), w, w, s, s));
        });
    assertThrowsCustom(
        ErrorType.InvalidePlaceLetterAction, // start out of bound
        () -> {
          GridWordPos s = new GridWordPos(500, 500, Direction.Right);
          Letter[] w = lettersFromString("AA");
          this.gameModel.grid.placeLetters(
              new PlaceWordAction(
                  this.gameModel.getCurrentPlayer(), this.gameModel.getTurn(), w, w, s, s));
        });
    assertThrowsCustom(
        ErrorType.PositionOutOfGird, // word too long (reached out of bound)
        () -> {
          GridWordPos s = new GridWordPos(7, 7, Direction.Right);
          Letter[] w = lettersFromString("RISTOURNASSIONS");
          this.gameModel.grid.placeLetters(
              new PlaceWordAction(
                  this.gameModel.getCurrentPlayer(), this.gameModel.getTurn(), w, w, s, s));
        });
    assertThrowsCustom(
        ErrorType.FirstMoveIllegal, // not starting at the end
        () -> {
          GridWordPos s = new GridWordPos(5, 5, Direction.Right);
          Letter[] w = lettersFromString("FELDSPATH");
          this.gameModel.grid.placeLetters(
              new PlaceWordAction(
                  this.gameModel.getCurrentPlayer(), this.gameModel.getTurn(), w, w, s, s));
        });
    { // placing valid word
      GridWordPos s = new GridWordPos(7, 5, Direction.Right);
      Letter[] w = lettersFromString("FELDSPATH");
      this.gameModel.grid.placeLetters(
          this.gameModel.grid.filterPwa(
              new PlaceWordAction(
                  this.gameModel.getCurrentPlayer(), this.gameModel.getTurn(), w, s)));
      this.gameModel.players.get(0).addScore(1); // to enable placing more
    }
    assertThrowsCustom(
        ErrorType.InvalidePlaceLetterAction, // trying to replace a letter
        () -> {
          GridWordPos s = new GridWordPos(7, 7, Direction.Right);
          Letter[] w = lettersFromString("A");
          this.gameModel.grid.placeLetters(
              this.gameModel.grid.filterPwa(
                  new PlaceWordAction(
                      this.gameModel.getCurrentPlayer(), this.gameModel.getTurn(), w, s)));
        });

    { // placing valid word
      GridWordPos s = new GridWordPos(5, 7, Direction.Down);
      Letter[] w = lettersFromString("FELAS");
      this.gameModel.grid.placeLetters(
          this.gameModel.grid.filterPwa(
              new PlaceWordAction(
                  this.gameModel.getCurrentPlayer(), this.gameModel.getTurn(), w, s)));
    }
    assertThrowsCustom(
        ErrorType.InvalidePlaceLetterAction, // placing letters over existing letters
        () -> {
          GridWordPos s = new GridWordPos(7, 7, Direction.Right);
          Letter[] w = lettersFromString("ZZZ");
          this.gameModel.grid.placeLetters(
              new PlaceWordAction(
                  this.gameModel.getCurrentPlayer(), this.gameModel.getTurn(), w, w, s, s));
        });
  }

  @Test
  public void testAiPlayer() {
    AI ai = new AI(1, 30);
    assertTrue(ai.getIsAi());
    GameAction ga = ai.getAction(gameModel);
    assertTrue(ga instanceof PassTurnAction);
  }
}
