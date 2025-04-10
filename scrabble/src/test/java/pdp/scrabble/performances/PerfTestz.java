package pdp.scrabble.performances;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Before;
import org.junit.Test;
import pdp.scrabble.configuration.Configuration;
import pdp.scrabble.dictionary.Dictionary;
import pdp.scrabble.dictionary.Letter;
import pdp.scrabble.dictionary.Word;
import pdp.scrabble.game.Direction;
import pdp.scrabble.game.GameModel;
import pdp.scrabble.game.GridWordPos;
import pdp.scrabble.game.OwnedWord;
import pdp.scrabble.game.Player;
import pdp.scrabble.game.actions.PlaceWordAction;
import pdp.scrabble.game.serializer.Serializer;

public class PerfTestz {

  private Dictionary enDict, frDict;
  private Configuration conf, confFr;
  private GameModel gm, gmFr;

  @Before
  public void setUp() throws IOException {
    enDict = new Dictionary();
    enDict.loadFromFile(Paths.get("dictionary/en.txt"));
    frDict = new Dictionary();
    frDict.loadFromFile(Paths.get("dictionary/fr.txt"));
    this.conf =
        new Configuration(
            true,
            "d",
            false,
            false,
            false,
            4,
            30,
            new boolean[] {false, false, false, false},
            15,
            2,
            5,
            "fr",
            "idk",
            java.nio.file.Paths.get("dictionary/en.txt"),
            java.nio.file.Paths.get("none"),
            java.nio.file.Paths.get("none"));
    this.confFr =
        new Configuration(
            true,
            "d",
            false,
            false,
            false,
            4,
            30,
            new boolean[] {false, false, false, false},
            15,
            2,
            5,
            "fr",
            "idk",
            java.nio.file.Paths.get("dictionary/fr.txt"),
            java.nio.file.Paths.get("none"),
            java.nio.file.Paths.get("none"));

    gm = new GameModel(conf);
    gmFr = new GameModel(conf);
  }

  @Test
  public void testLoadSmallHist() {
    long startTime = System.nanoTime();
    Serializer.loadModelFromFile(Paths.get("./saves/perfs/small.txt"));
    long timeElapsed = (System.nanoTime() - startTime);
    System.out.println("time taken to do [loadSmallHist] 1 it: " + timeElapsed + " ns");
  }

  @Test
  public void testLoadMediumHist() {
    long startTime = System.nanoTime();
    Serializer.loadModelFromFile(Paths.get("./saves/perfs/medium.txt"));
    long timeElapsed = (System.nanoTime() - startTime);
    System.out.println("time taken to do [loadMediumHist] 1 it: " + timeElapsed + " ns");
  }

  @Test
  public void testLoadLargeHist() {
    long startTime = System.nanoTime();
    Serializer.loadModelFromFile(Paths.get("./saves/perfs/large.txt"));
    long timeElapsed = (System.nanoTime() - startTime);
    System.out.println("time taken to do [loadLargeHist] 1 it: " + timeElapsed + " ns");
  }

  @Test
  public void testWriteSmallHist() throws IOException {
    GameModel model = Serializer.loadModelFromFile(Paths.get("./saves/perfs/small.txt"));
    Path temp = Files.createTempFile("temp", "writeSmallHist");
    long startTime = System.nanoTime();
    Serializer.saveCurrentToFile(temp, model);
    long timeElapsed = (System.nanoTime() - startTime);
    System.out.println("time taken to do [writeSmallHist] 1 it: " + timeElapsed + " ns");
  }

  @Test
  public void testWriteMediumHist() throws IOException {
    GameModel model = Serializer.loadModelFromFile(Paths.get("./saves/perfs/medium.txt"));
    Path temp = Files.createTempFile("temp", "writeMediumHist");
    long startTime = System.nanoTime();
    Serializer.saveCurrentToFile(temp, model);
    long timeElapsed = (System.nanoTime() - startTime);
    System.out.println("time taken to do [writeMediumHist] 1 it: " + timeElapsed + " ns");
  }

  @Test
  public void testWriteLargeHist() throws IOException {
    GameModel model = Serializer.loadModelFromFile(Paths.get("./saves/perfs/large.txt"));
    Path temp = Files.createTempFile("temp", "writeLargeHist");
    long startTime = System.nanoTime();
    Serializer.saveCurrentToFile(temp, model);
    long timeElapsed = (System.nanoTime() - startTime);

    System.out.println("time taken to do [writeLargeHist] 1 it: " + timeElapsed + " ns");
  }

  @Test
  public void testSearchWordEn() throws IOException {
    Word existent = new Word("EXISTENT");
    Word nonExistent = new Word("AAAAAAAA");

    long startTime = System.nanoTime();
    enDict.containsWord(existent);
    long timeElapsed = (System.nanoTime() - startTime);
    System.out.println("time taken to do [searchWordEn] existent 1 it: " + timeElapsed + " ns");

    startTime = System.nanoTime();
    enDict.containsWord(nonExistent);
    timeElapsed = (System.nanoTime() - startTime);
    System.out.println("time taken to do [searchWordEn] non existant 1 it: " + timeElapsed + " ns");
  }

  @Test
  public void testSearchWordFr() throws IOException {
    Word existent = new Word("EXISTE");
    Word nonExistent = new Word("AAAAAAAA");

    long startTime = System.nanoTime();
    frDict.containsWord(existent);
    long timeElapsed = (System.nanoTime() - startTime);
    System.out.println("time taken to do [searchWordFr] existent 1 it: " + timeElapsed + " ns");

    startTime = System.nanoTime();
    frDict.containsWord(nonExistent);
    timeElapsed = (System.nanoTime() - startTime);
    System.out.println("time taken to do [searchWordFr] non existant 1 it: " + timeElapsed + " ns");
  }

  @Test
  public void testScoreCalcEn() throws IOException {
    Player p = new Player(0, 30);
    OwnedWord s = new OwnedWord(new GridWordPos(7, 0, Direction.Right), new Word("EH"), p);
    OwnedWord m = new OwnedWord(new GridWordPos(7, 0, Direction.Right), new Word("COEXISTENT"), p);
    OwnedWord l =
        new OwnedWord(new GridWordPos(7, 0, Direction.Right), new Word("ZOOGEOGRAPHICAL"), p);

    long startTime = System.nanoTime();
    gm.grid.getScore(s);
    long timeElapsed = (System.nanoTime() - startTime);
    System.out.println(
        "time taken to do [testScoreCalcEn] small word 1 it: " + timeElapsed + " ns");

    startTime = System.nanoTime();
    gm.grid.getScore(m);
    timeElapsed = (System.nanoTime() - startTime);
    System.out.println(
        "time taken to do [testScoreCalcEn] medium word 1 it: " + timeElapsed + " ns");

    startTime = System.nanoTime();
    gm.grid.getScore(l);
    timeElapsed = (System.nanoTime() - startTime);
    System.out.println(
        "time taken to do [testScoreCalcEn] large word 1 it: " + timeElapsed + " ns");
  }

  @Test
  public void testScoreCalcFr() throws IOException {
    Player p = new Player(0, 30);
    OwnedWord s = new OwnedWord(new GridWordPos(7, 0, Direction.Right), new Word("EH"), p);
    OwnedWord m = new OwnedWord(new GridWordPos(7, 0, Direction.Right), new Word("COEXISTENT"), p);
    OwnedWord l =
        new OwnedWord(new GridWordPos(7, 0, Direction.Right), new Word("ZOOTECHNICIENNE"), p);

    long startTime = System.nanoTime();
    gmFr.grid.getScore(s);
    long timeElapsed = (System.nanoTime() - startTime);
    System.out.println(
        "time taken to do [testScoreCalcFr] small word 1 it: " + timeElapsed + " ns");

    startTime = System.nanoTime();
    gmFr.grid.getScore(m);
    timeElapsed = (System.nanoTime() - startTime);
    System.out.println(
        "time taken to do [testScoreCalcFr] medium word 1 it: " + timeElapsed + " ns");

    startTime = System.nanoTime();
    gmFr.grid.getScore(l);
    timeElapsed = (System.nanoTime() - startTime);
    System.out.println(
        "time taken to do [testScoreCalcFr] large word 1 it: " + timeElapsed + " ns");
  }

  @Test
  public void testplaceWord() throws IOException {
    PlaceWordAction testEmpty =
        new PlaceWordAction(
            gm.getCurrentPlayer(),
            gm.getTurn(),
            new Letter[] {
              new Letter('E'), new Letter('M'), new Letter('P'), new Letter('T'), new Letter('Y')
            },
            new GridWordPos(7, 7, Direction.Right));

    long startTime = System.nanoTime();
    PlaceWordAction testEmptyFilt = gm.grid.filterPwa(testEmpty);
    gm.grid.placeLetters(testEmptyFilt);
    long timeElapsed = (System.nanoTime() - startTime);
    System.out.println(
        "time taken to do [testplaceWord] on empty grid (_____) 1 it: " + timeElapsed + " ns");

    // Load the large save file
    GameModel loadedGm = Serializer.loadModelFromFile(Paths.get("./saves/perfs/large.txt"));
    // Remove letters in a convenient way to benchmark
    // place Word on occupied grid
    try {
      Field placedLettersField = loadedGm.grid.getClass().getDeclaredField("placedLetters");
      placedLettersField.setAccessible(true);
      Letter[][] letters = (Letter[][]) placedLettersField.get(loadedGm.grid);
      Letter[][] copied = new Letter[15][15];
      for (int i = 0; i < 15; i++) for (int j = 0; j < 15; j++) copied[i][j] = letters[i][j];
      copied[5][4] = null; // Remove J
      copied[5][6] = null; // Remove R
      copied[5][8] = null; // Remove S
      placedLettersField.set(loadedGm.grid, copied);
    } catch (IllegalAccessException | NoSuchFieldException e) {
      fail();
    }

    PlaceWordAction testOccupied =
        new PlaceWordAction(
            loadedGm.getCurrentPlayer(),
            loadedGm.getTurn(),
            new Letter[] {
              new Letter('F'),
              new Letter('J'),
              new Letter('O'),
              new Letter('R'),
              new Letter('D'),
              new Letter('S')
            },
            new GridWordPos(5, 3, Direction.Right));

    startTime = System.nanoTime();
    PlaceWordAction testOccupiedFilt = loadedGm.grid.filterPwa(testOccupied);
    loadedGm.grid.placeLetters(testOccupiedFilt);
    timeElapsed = (System.nanoTime() - startTime);
    System.out.println(
        "time taken to do [testplaceWord] on occupied grid (x_x_x_) 1 it: " + timeElapsed + " ns");
  }
}
