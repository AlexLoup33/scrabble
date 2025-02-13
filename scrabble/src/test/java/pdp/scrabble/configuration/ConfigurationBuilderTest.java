package pdp.scrabble.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;

import java.nio.file.Paths;
import org.junit.Test;
import pdp.scrabble.errorsHandling.CustomError;

public class ConfigurationBuilderTest {

  @Test
  public void testValidConfFile() throws CustomError {
    ConfigurationBuilder confBuilder =
        new ConfigurationBuilder(Paths.get("src/test/resources/scrabblerc/.default"));
    Configuration conf = confBuilder.getConfiguration();

    assertFalse(conf.getVerbose());
    assertEquals(conf.getBlitzTime(), 30);
  }

  @Test
  public void testMissingConfFile() {
    CustomError e =
        assertThrows(CustomError.class, () -> new ConfigurationBuilder(Paths.get("nonExistent")));
    assertEquals("Could not access .scrabblerc", e.message);
  }

  @Test
  public void testIncompleteConfFile() {
    CustomError e =
        assertThrows(
            CustomError.class,
            () -> new ConfigurationBuilder(Paths.get("src/test/resources/scrabblerc/.incomplete")));
    assertEquals("Some properties are missing from .scrabblerc", e.message);
  }

  @Test
  public void testInvalidPlayerCount() {
    CustomError e =
        assertThrows(
            CustomError.class,
            () ->
                new ConfigurationBuilder(
                    Paths.get("src/test/resources/scrabblerc/.invalidPlayerCount")));
    assertEquals(
        "[.scrabblerc] Value `5` for key `playerCount`, expected value in range 1-4", e.message);
  }

  @Test
  public void testMerge() throws CustomError {
    ConfigurationBuilder src =
        new ConfigurationBuilder(Paths.get("src/test/resources/scrabblerc/.default"));
    Configuration srcConf = src.getConfiguration();
    ConfigurationBuilder other =
        new ConfigurationBuilder(Paths.get("src/test/resources/scrabblerc/.valid"));
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
    ConfigurationBuilder original =
        new ConfigurationBuilder(Paths.get("src/test/resources/scrabblerc/.default"));
    Configuration originalConf = original.getConfiguration();

    ConfigurationBuilder.saveConfiguration(
        Paths.get("src/test/resources/scrabblerc/created/.savedDefault"), originalConf);

    ConfigurationBuilder created =
        new ConfigurationBuilder(Paths.get("src/test/resources/scrabblerc/created/.savedDefault"));
    Configuration createdConf = created.getConfiguration();

    assertEquals(originalConf.getDebug(), createdConf.getDebug());
  }

  @Test
  public void testInvalidCliArg() {
    String[] args = {"--Nonexistent"};
    CustomError e = assertThrows(CustomError.class, () -> new ConfigurationBuilder(args));
    assertEquals("Some cli options were incorrect, -h for help.", e.message);
  }
}
