package pdp.scrabble.configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class ConfigurationBuilder {

  private Properties defaultProps;

  // FLAGS
  private Boolean verbose;
  private Boolean debug;
  private Boolean blitz;
  private Boolean superScrabble;
  private Boolean gui;

  private int playerCount;
  // Only relevant with blitz on
  private int blitzTime;

  // Only relevant with aiPlayers
  // TODO: Truly implement aiPlayers as Bitmap
  // Currently bitmap in string
  // with player 1 to the right
  // and player 4 to the left
  private String aiPlayers;
  private int aiDepth;
  private int aiTime;

  private String language;
  // Monte Carlo or MiniMax
  // 'mcts' or 'minimax'
  private String aiMode;

  // Paths/Filenames
  private String dictFile;
  private String contestFile;
  private String historyFile;

  // TODO: Use Custom Error module from test package
  // currently Exception does not stop execution
  public ConfigurationBuilder(Path src) throws Exception {
    // Step 1 : Get defaults
    Properties props = new Properties();

    // Step 2 : Override defaults with contents of scrabblerc
    try {
      // FileReader location is relative to user working directory at runtime
      BufferedReader scrabblerc = Files.newBufferedReader(src);
      props.load(scrabblerc);
    } catch (IOException e) {
      System.out.println(e);
    }

    convertPropsToPojo(props, this);
  }

  private Properties getDefaultProperties() {
    Properties props = new Properties();
    props.setProperty("verbose", "false");
    props.setProperty("debug", "false");
    props.setProperty("blitz", "false");
    props.setProperty("superScrabble", "false");
    props.setProperty("gui", "false");

    props.setProperty("playerCount", "2");
    props.setProperty("blitzTime", "30");
    // Player 2 is AI by default
    props.setProperty("aiPlayers", "0010");
    // TODO: CHOOSE DEFAULT DEPTH
    props.setProperty("aiDepth", "0");
    props.setProperty("aiTime", "5");

    props.setProperty("language", "en");
    props.setProperty("aiMode", "null");

    // TODO: CHOOSE DEFAULT LOCATION FOR THESE FILES
    props.setProperty("dictFile", "./");
    props.setProperty("contestFile", "./");
    props.setProperty("historyFile", "./");

    return props;
  }

  public void writeDefaultsToFile(Path target) {
    Properties defaults = getDefaultProperties();
    try {
      defaults.store(Files.newOutputStream(target), "[Default]");
    } catch (IOException e) {
      System.out.println(e);
    }
  }

  // TODO: Throw exceptions on invalid Data
  private void convertPropsToPojo(Properties props, ConfigurationBuilder target) throws Exception {
    for (String key : props.stringPropertyNames()) {
      Class cls = target.getClass();
      switch (key) {
        // FLAGS
        case "verbose":
        case "debug":
        case "blitz":
        case "superScrabble":
        case "gui":
          {
            // true if property == "true" / false in every other case
            // its fine to not check the value bcs flags are false by default
            Boolean value = Boolean.valueOf(props.getProperty(key));
            // Build setter reflectively from key
            // basically creating `set{key}(boolean value)` and calling it
            try {
              Method meth =
                  cls.getMethod(
                      "set" + Character.toUpperCase(key.charAt(0)) + key.substring(1),
                      new Class[] {Boolean.class});
              meth.invoke(target, value);
            } catch (Exception e) {
              System.out.println(e);
            }
            break;
          }
        // Ints
        case "playerCount":
        case "blitzTime":
        case "aiDepth":
        case "aiTime":
          {
            int value = 0;
            try {
              value = Integer.parseUnsignedInt(props.getProperty(key));
            } catch (NumberFormatException e) {
              // TODO: Use Custom Error module from test package
              // Currently exception does not stop Execution
              throw new Exception(
                  "[scrabblerc] Error: value `"
                      + props.getProperty(key)
                      + "` for key `"
                      + key
                      + "`, expected a `"
                      + Integer.TYPE
                      + "`");
            }
            // Build setter reflectively from key
            // basically creating `set{key}(int value)` and calling it
            try {
              Method meth =
                  cls.getMethod(
                      "set" + Character.toUpperCase(key.charAt(0)) + key.substring(1),
                      new Class[] {Integer.TYPE});
              meth.invoke(target, value);
            } catch (Exception e) {
              System.out.println(e);
            }
            break;
          }
        // Strings
        case "aiPlayers":
        case "language":
        case "aiMode":
        case "dictFile":
        case "contestFile":
        case "historyFile":
          {
            String value = props.getProperty(key);
            // Build setter reflectively from key
            // basically creating `set{key}(String value)` and calling it
            try {
              Method meth =
                  cls.getMethod(
                      "set" + Character.toUpperCase(key.charAt(0)) + key.substring(1),
                      new Class[] {String.class});
              meth.invoke(target, value);
            } catch (Exception e) {
              System.out.println(e);
            }
            break;
          }
        default:
      }
    }
  }

  public Configuration getConfiguration() {
    return new Configuration(
        verbose,
        debug,
        blitz,
        superScrabble,
        gui,
        playerCount,
        blitzTime,
        aiPlayers,
        aiDepth,
        aiTime,
        language,
        aiMode,
        dictFile,
        contestFile,
        historyFile);
  }

  public String toString() {
    return "ConfigurationBuilder={ "
        + "verbose="
        + verbose.toString()
        + " | "
        + "debug="
        + debug.toString()
        + " | "
        + "blitz="
        + blitz.toString()
        + " | "
        + "superScrabble="
        + superScrabble.toString()
        + " | "
        + "gui="
        + gui.toString()
        + " | "
        + "playerCount="
        + Integer.toString(playerCount)
        + " | "
        + "blitzTime="
        + Integer.toString(blitzTime)
        + " | "
        + "aiPlayers="
        + aiPlayers.toString()
        + " | "
        + "aiDepth="
        + Integer.toString(aiDepth)
        + " | "
        + "aiTime="
        + Integer.toString(aiTime)
        + " | "
        + "language="
        + language.toString()
        + " | "
        + "aiMode="
        + aiMode.toString()
        + " | "
        + "dictFile="
        + dictFile.toString()
        + " | "
        + "contestFile="
        + contestFile.toString()
        + " | "
        + "historyFile="
        + historyFile.toString()
        + " "
        + "}";
  }

  public void setVerbose(Boolean verbose) {
    this.verbose = verbose;
  }

  public void setDebug(Boolean debug) {
    this.debug = debug;
  }

  public void setBlitz(Boolean blitz) {
    this.blitz = blitz;
  }

  public void setSuperScrabble(Boolean superScrabble) {
    this.superScrabble = superScrabble;
  }

  public void setGui(Boolean gui) {
    this.gui = gui;
  }

  public void setPlayerCount(int playerCount) {
    this.playerCount = playerCount;
  }

  public void setBlitzTime(int blitzTime) {
    this.blitzTime = blitzTime;
  }

  public void setAiPlayers(String aiPlayers) {
    this.aiPlayers = aiPlayers;
  }

  public void setAiDepth(int aiDepth) {
    this.aiDepth = aiDepth;
  }

  public void setAiTime(int aiTime) {
    this.aiTime = aiTime;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public void setAiMode(String aiMode) {
    this.aiMode = aiMode;
  }

  public void setDictFile(String dictFile) {
    // TODO: ENFORCE STRING BEING A PATH
    this.dictFile = dictFile;
  }

  public void setContestFile(String contestFile) {
    // TODO: ENFORCE STRING BEING A PATH
    this.contestFile = contestFile;
  }

  public void setHistoryFile(String historyFile) {
    // TODO: ENFORCE STRING BEING A PATH
    this.historyFile = historyFile;
  }
}
