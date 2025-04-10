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

public class PerfTestsMeans {

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
    for (int i = 0; i < 100; i++)
      Serializer.loadModelFromFile(Paths.get("./saves/perfs/small.txt"));
    long timeElapsed = (System.nanoTime() - startTime);
    System.out.println(
        "time taken to do [loadSmallHist] mean of 100 it: " + (timeElapsed / 100) + " ns");
  }

  @Test
  public void testLoadMediumHist() {
    long startTime = System.nanoTime();
    for (int i = 0; i < 100; i++)
      Serializer.loadModelFromFile(Paths.get("./saves/perfs/medium.txt"));
    long timeElapsed = (System.nanoTime() - startTime);
    System.out.println(
        "time taken to do [loadMediumHist] mean of 100 it: " + (timeElapsed / 100) + " ns");
  }

  @Test
  public void testLoadLargeHist() {
    long startTime = System.nanoTime();
    for (int i = 0; i < 100; i++)
      Serializer.loadModelFromFile(Paths.get("./saves/perfs/large.txt"));
    long timeElapsed = (System.nanoTime() - startTime);
    System.out.println(
        "time taken to do [loadLargeHist] mean of 100 it: " + (timeElapsed / 100) + " ns");
  }

  @Test
  public void testWriteSmallHist() throws IOException {
    GameModel model = Serializer.loadModelFromFile(Paths.get("./saves/perfs/small.txt"));
    Path temp = Files.createTempFile("temp", "writeSmallHist");

    long startTime = System.nanoTime();
    for (int i = 0; i < 100; i++) Serializer.saveCurrentToFile(temp, model);
    long timeElapsed = (System.nanoTime() - startTime);
    System.out.println(
        "time taken to do [writeSmallHist] mean of 100 it: " + (timeElapsed / 100) + " ns");
  }

  @Test
  public void testWriteMediumHist() throws IOException {
    GameModel model = Serializer.loadModelFromFile(Paths.get("./saves/perfs/medium.txt"));
    Path temp = Files.createTempFile("temp", "writeMediumHist");

    long startTime = System.nanoTime();
    for (int i = 0; i < 100; i++) Serializer.saveCurrentToFile(temp, model);
    long timeElapsed = (System.nanoTime() - startTime);
    System.out.println(
        "time taken to do [writeMediumHist] mean of 100 it: " + (timeElapsed / 100) + " ns");
  }

  @Test
  public void testWriteLargeHist() throws IOException {
    GameModel model = Serializer.loadModelFromFile(Paths.get("./saves/perfs/large.txt"));
    Path temp = Files.createTempFile("temp", "writeLargeHist");
    long startTime = System.nanoTime();
    for (int i = 0; i < 100; i++) Serializer.saveCurrentToFile(temp, model);
    long timeElapsed = (System.nanoTime() - startTime);
    System.out.println(
        "time taken to do [writeLargeHist] mean of 100 it: " + (timeElapsed / 100) + " ns");
  }

  @Test
  public void testSearchWordEn() throws IOException {
    Word existent = new Word("EXISTENT");
    Word nonExistent = new Word("AAAAAAAA");

    long totalElapsed = 0L;
    for (int i = 0; i < 100; i++) {
      enDict = new Dictionary();
      enDict.loadFromFile(Paths.get("dictionary/en.txt"));
      long startTime = System.nanoTime();
      enDict.containsWord(existent);
      long timeElapsed = (System.nanoTime() - startTime);
      totalElapsed += timeElapsed;
    }
    System.out.println(
        "time taken to do [searchWordEn] existent mean of 100 it: " + (totalElapsed / 100) + " ns");

    totalElapsed = 0L;
    for (int i = 0; i < 100; i++) {
      enDict = new Dictionary();
      enDict.loadFromFile(Paths.get("dictionary/en.txt"));
      long startTime = System.nanoTime();
      enDict.containsWord(nonExistent);
      long timeElapsed = (System.nanoTime() - startTime);
      totalElapsed += timeElapsed;
    }
    System.out.println(
        "time taken to do [searchWordEn] non existant mean of 100 it: "
            + (totalElapsed / 100)
            + " ns");
  }

  @Test
  public void testSearchWordFr() throws IOException {
    Word existent = new Word("EXISTE");
    Word nonExistent = new Word("AAAAAAAA");

    long totalElapsed = 0L;
    for (int i = 0; i < 100; i++) {
      frDict = new Dictionary();
      frDict.loadFromFile(Paths.get("dictionary/fr.txt"));
      long startTime = System.nanoTime();
      frDict.containsWord(existent);
      long timeElapsed = (System.nanoTime() - startTime);
      totalElapsed += timeElapsed;
    }
    System.out.println(
        "time taken to do [searchWordFr] existent mean of 100 it: " + (totalElapsed / 100) + " ns");

    totalElapsed = 0L;
    for (int i = 0; i < 100; i++) {
      frDict = new Dictionary();
      frDict.loadFromFile(Paths.get("dictionary/fr.txt"));
      long startTime = System.nanoTime();
      frDict.containsWord(nonExistent);
      long timeElapsed = (System.nanoTime() - startTime);
      totalElapsed += timeElapsed;
    }
    System.out.println(
        "time taken to do [searchWordFr] non existant mean of 100 it: "
            + (totalElapsed / 100)
            + " ns");
  }

  @Test
  public void testScoreCalcEn() throws IOException {
    Player p = new Player(0, 30);
    OwnedWord s = new OwnedWord(new GridWordPos(7, 0, Direction.Right), new Word("EH"), p);
    OwnedWord m = new OwnedWord(new GridWordPos(7, 0, Direction.Right), new Word("COEXISTENT"), p);
    OwnedWord l =
        new OwnedWord(new GridWordPos(7, 0, Direction.Right), new Word("ZOOGEOGRAPHICAL"), p);

    long totalElapsed = 0L;
    for (int i = 0; i < 100; i++) {
      gm = new GameModel(conf); // ignore cache
      long startTime = System.nanoTime();
      gm.grid.getScore(s);
      long timeElapsed = (System.nanoTime() - startTime);
      totalElapsed += timeElapsed;
    }
    System.out.println(
        "time taken to do [testScoreCalcEn] small word mean of 100 it: "
            + (totalElapsed / 100)
            + " ns");

    totalElapsed = 0L;
    for (int i = 0; i < 100; i++) {
      gm = new GameModel(conf);
      long startTime = System.nanoTime();
      gm.grid.getScore(m);
      long timeElapsed = (System.nanoTime() - startTime);
      totalElapsed += timeElapsed;
    }
    System.out.println(
        "time taken to do [testScoreCalcEn] medium word mean of 100 it: "
            + (totalElapsed / 100)
            + " ns");

    totalElapsed = 0L;
    for (int i = 0; i < 100; i++) {
      gm = new GameModel(conf);
      long startTime = System.nanoTime();
      gm.grid.getScore(l);
      long timeElapsed = (System.nanoTime() - startTime);
      totalElapsed += timeElapsed;
    }
    System.out.println(
        "time taken to do [testScoreCalcEn] large word mean of 100 it: "
            + (totalElapsed / 100)
            + " ns");
  }

  @Test
  public void testScoreCalcFr() throws IOException {
    Player p = new Player(0, 30);
    OwnedWord s = new OwnedWord(new GridWordPos(7, 0, Direction.Right), new Word("EH"), p);
    OwnedWord m = new OwnedWord(new GridWordPos(7, 0, Direction.Right), new Word("COEXISTENT"), p);
    OwnedWord l =
        new OwnedWord(new GridWordPos(7, 0, Direction.Right), new Word("ZOOTECHNICIENNE"), p);

    long totalElapsed = 0L;
    for (int i = 0; i < 100; i++) {
      gmFr = new GameModel(conf);
      long startTime = System.nanoTime();
      gmFr.grid.getScore(s);
      long timeElapsed = (System.nanoTime() - startTime);
      totalElapsed += timeElapsed;
    }
    System.out.println(
        "time taken to do [testScoreCalcFr] small word mean of 100 it: "
            + (totalElapsed / 100)
            + " ns");

    totalElapsed = 0L;
    for (int i = 0; i < 100; i++) {
      gmFr = new GameModel(conf);
      long startTime = System.nanoTime();
      gmFr.grid.getScore(m);
      long timeElapsed = (System.nanoTime() - startTime);
      totalElapsed += timeElapsed;
    }
    System.out.println(
        "time taken to do [testScoreCalcFr] medium word mean of 100 it: "
            + (totalElapsed / 100)
            + " ns");

    totalElapsed = 0L;
    for (int i = 0; i < 100; i++) {
      gmFr = new GameModel(conf);
      long startTime = System.nanoTime();
      gmFr.grid.getScore(l);
      long timeElapsed = (System.nanoTime() - startTime);
      totalElapsed += timeElapsed;
    }
    System.out.println(
        "time taken to do [testScoreCalcFr] large word mean of 100 it: "
            + (totalElapsed / 100)
            + " ns");
  }

  @Test
  public void testplaceWord() throws IOException {
    long totalElapsed = 0L;
    PlaceWordAction testEmpty =
        new PlaceWordAction(
            gm.getCurrentPlayer(),
            gm.getTurn(),
            new Letter[] {
              new Letter('E'), new Letter('M'), new Letter('P'), new Letter('T'), new Letter('Y')
            },
            new GridWordPos(7, 7, Direction.Right));
    for (int i = 0; i < 100; i++) {
      gm = new GameModel(conf);
      long startTime = System.nanoTime();
      PlaceWordAction testEmptyFilt = gm.grid.filterPwa(testEmpty);
      gm.grid.placeLetters(testEmptyFilt);
      long timeElapsed = (System.nanoTime() - startTime);
      totalElapsed += timeElapsed;
    }

    System.out.println(
        "time taken to do [testplaceWord] on empty grid (_____) mean of 100 it: "
            + (totalElapsed / 100)
            + " ns");

    // Load the large save file
    totalElapsed = 0L;
    for (int it = 0; it < 100; it++) {
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

      long startTime = System.nanoTime();
      PlaceWordAction testOccupiedFilt = loadedGm.grid.filterPwa(testOccupied);
      loadedGm.grid.placeLetters(testOccupiedFilt);
      long timeElapsed = (System.nanoTime() - startTime);
      totalElapsed += timeElapsed;
    }
    System.out.println(
        "time taken to do [testplaceWord] on occupied grid (x_x_x_) mean of 100 it: "
            + (totalElapsed / 100)
            + " ns");
  }
}
