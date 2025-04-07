package pdp.scrabble.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.nio.file.Paths;
import java.util.Arrays;
import org.junit.Test;
import pdp.scrabble.errorsHandling.CustomError;

public class ConfigurationBuilderTest {

  @Test
  public void testStandardConfInit() {
    String[] args = { "-g", "-s", "-p", "3", "-a", "2,3", "--ai-mode", "minimax" };
    ConfigurationBuilder rcBuilder = new ConfigurationBuilder(Paths.get("src/test/resources/scrabblerc/.default"));
    ConfigurationBuilder cliBuilder = new ConfigurationBuilder(args);
    Configuration cliConf = cliBuilder.getConfiguration();

    rcBuilder.merge(cliBuilder);
    Configuration merged = rcBuilder.getConfiguration();

    assertEquals(merged.getGui(), cliConf.getGui());
    assertEquals(merged.getSuperScrabble(), cliConf.getSuperScrabble());
    assertEquals(merged.getPlayerCount(), cliConf.getPlayerCount());
    assertEquals(merged.getAiMode(), cliConf.getAiMode());
    assertTrue(Arrays.equals(merged.getAiPlayers(), cliConf.getAiPlayers()));
  }

  @Test
  public void testValidConfFile() throws CustomError {
    ConfigurationBuilder confBuilder = new ConfigurationBuilder(Paths.get("src/test/resources/scrabblerc/.default"));
    Configuration conf = confBuilder.getConfiguration();

    assertFalse(conf.getVerbose());
    assertEquals(conf.getBlitzTime(), 30);
  }

  @Test
  public void testMissingConfFile() {
    CustomError e = assertThrows(CustomError.class, () -> new ConfigurationBuilder(Paths.get("nonExistent")));
    assertEquals("Could not access .scrabblerc", e.message);
  }

  @Test
  public void testIncompleteConfFile() {
    CustomError e = assertThrows(
        CustomError.class,
        () -> new ConfigurationBuilder(Paths.get("src/test/resources/scrabblerc/.incomplete")));
    assertEquals("Some properties are missing from .scrabblerc", e.message);
  }

  @Test
  public void testInvalidPlayerCount() {
    CustomError e = assertThrows(
        CustomError.class,
        () -> new ConfigurationBuilder(
            Paths.get("src/test/resources/scrabblerc/.invalidPlayerCount")));
    assertEquals(
        "[.scrabblerc] Value `5` for key `playerCount`, expected value in range 2-4", e.message);
  }

  @Test
  public void testMerge() throws CustomError {
    ConfigurationBuilder src = new ConfigurationBuilder(Paths.get("src/test/resources/scrabblerc/.default"));
    Configuration srcConf = src.getConfiguration();
    ConfigurationBuilder other = new ConfigurationBuilder(Paths.get("src/test/resources/scrabblerc/.valid"));
    Configuration otherConf = other.getConfiguration();

    src.merge(other);
    Configuration merged = src.getConfiguration();

    // Changed from merge
    assertEquals(merged.getVerbose(), otherConf.getVerbose());
    assertNotEquals(merged.getVerbose(), srcConf.getVerbose());

    // Same in both
    assertEquals(merged.getDictFile(), srcConf.getDictFile());
    assertEquals(merged.getDictFile(), otherConf.getDictFile());
  }

  @Test
  public void testSaveConfiguration() throws Exception {
    ConfigurationBuilder original = new ConfigurationBuilder(Paths.get("src/test/resources/scrabblerc/.default"));
    Configuration originalConf = original.getConfiguration();

    ConfigurationBuilder.saveConfiguration(
        Paths.get("src/test/resources/scrabblerc/created/.savedDefault"), originalConf);

    ConfigurationBuilder created = new ConfigurationBuilder(
        Paths.get("src/test/resources/scrabblerc/created/.savedDefault"));
    Configuration createdConf = created.getConfiguration();

    assertEquals(originalConf.getDebug(), createdConf.getDebug());
  }

  @Test
  public void testBooleanFlags() throws CustomError {
    String[] args = { "-v", "-d", "-b", "-s", "-g" };
    ConfigurationBuilder cliBuilder = new ConfigurationBuilder(args);
    Configuration cliConf = cliBuilder.getConfiguration();

    assertTrue(cliConf.getVerbose());
    assertEquals("+", cliConf.getDebug());
    assertTrue(cliConf.getBlitz());
    assertTrue(cliConf.getSuperScrabble());
    assertTrue(cliConf.getGui());
  }

  @Test
  public void testOptionsWithArguments() throws CustomError {
    String[] args = {
        "-t", "10",
        "-p", "3",
        "-l", "fr",
        "-a", "1,2,3,4",
        "--ai-mode", "mcts"
    };
    ConfigurationBuilder cliBuilder = new ConfigurationBuilder(args);
    Configuration cliConf = cliBuilder.getConfiguration();

    assertEquals(10, cliConf.getBlitzTime());
    assertEquals(3, cliConf.getPlayerCount());
    assertEquals("fr", cliConf.getLanguage());
    assertTrue(Arrays.equals(new boolean[] { true, true, true, true }, cliConf.getAiPlayers()));
    assertEquals("mcts", cliConf.getAiMode());
  }

  @Test
  public void testMultipleOptions() throws CustomError {
    String[] args = { "-v", "-d", "-t", "5" };
    ConfigurationBuilder cliBuilder = new ConfigurationBuilder(args);
    Configuration cliConf = cliBuilder.getConfiguration();

    assertTrue(cliConf.getVerbose());
    assertEquals("+", cliConf.getDebug());
    assertEquals(5, cliConf.getBlitzTime());
  }

  @Test
  public void testInvalidCliArg() {
    String[] args = { "--Nonexistent" };
    CustomError e = assertThrows(CustomError.class, () -> new ConfigurationBuilder(args));
    assertEquals("Some cli options were incorrect, -h for help.", e.message);
  }

  @Test
  public void testAiPlayersToString() {
    boolean[] aiPlayers = { false, true, false, true };
    ConfigurationTemplate template = new ConfigurationTemplate();
    assertEquals("null", template.aiPlayersToString());
    template.setAiPlayers(aiPlayers);
    assertEquals("2,4", template.aiPlayersToString());
    template.setAiPlayers("A");
    assertEquals("1,2,3,4", template.aiPlayersToString());
  }

  @Test
  public void testToString() {
    ConfigurationTemplate template = new ConfigurationTemplate();
    assertEquals(
        "ConfigurationTemplate={ verbose=false | debug=null | blitz=false | superScrabble=false | gui=false | playerCount=0 | blitzTime=0 | aiPlayers=null | aiDepth=0 | aiTime=0 | seed=0 | language=null | aiMode=null | dictFile=null | contestFile=null | historyFile=null }",
        template.toString());
  }

  @Test
  public void testSetHistoryFile() {
    ConfigurationTemplate template = new ConfigurationTemplate();
    String invalidPath = "history";
    assertThrows(CustomError.class, () -> template.setHistoryFile(invalidPath));
  }

  @Test
  public void testSetDictFile() {
    ConfigurationTemplate template = new ConfigurationTemplate();
    String invalidPath = "dict";
    assertThrows(CustomError.class, () -> template.setDictFile(invalidPath));
  }

  @Test
  public void testSetContestFile() {
    ConfigurationTemplate template = new ConfigurationTemplate();
    String invalidPath = "contest";
    assertThrows(CustomError.class, () -> template.setContestFile(invalidPath));
  }
}
