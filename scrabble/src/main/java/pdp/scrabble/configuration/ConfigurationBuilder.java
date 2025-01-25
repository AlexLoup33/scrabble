package pdp.scrabble.configuration;

import java.util.Properties;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;

public class ConfigurationBuilder {

  private Properties defaultProps;

  // FLAGS
  private Boolean verbose;
  private Boolean debug;
  private Boolean blitz;
  private Boolean superS;
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

  public ConfigurationBuilder() {
    // Step 1 : Get defaults 
    defaultProps = getDefaultProperties();
    Properties props = new Properties(defaultProps);

    // Step 2 : Override defaults with contents of scrabblerc
    try {
      BufferedReader scrabblerc = new BufferedReader(new FileReader("./.scrabblerc"));
      props.load(scrabblerc);

    } catch(IOException e) {
      System.out.println(e);
    }
      // TODO: Step 3 : Override scrabblerc with CLI options
      // TODO: props.load(CLI)

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
    //Player 2 is AI by default
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

  private void convertPropsToPojo(Properties props, ConfigurationBuilder target) {
    for (String key : props.stringPropertyNames()) {
      Class cls = target.getClass();
      switch (key) {
        // FLAGS
        case "verbose":
        case "debug":
        case "blitz":
        case "superS":
        case "gui": {
            // true if property == "true" / false in every other case
            // its fine to not check the value bcs flags are false by default
            Boolean value = Boolean.valueOf(props.getProperty(key));
            // Build setter reflectively from key
            // basically creating `set{key}(boolean value)` and calling it
            try {
              Method meth = cls.getMethod("set"
                  + Character.toUpperCase(key.charAt(0))
                  + key.substring(1), new Class[]{Boolean.class});
              meth.invoke(target, value);
            } catch(Exception e) {
              System.out.println(e);
            }
            break;
        }
        // Ints
        case "playerCount":
        case "blitzTime":
        case "aiDepth":
        case "aiTime": {
            // If the key from scrabblerc is not unsigned integer,
            // get the key from default properties
            int value = Integer.parseUnsignedInt(defaultProps.getProperty(key));
            try {
              value = Integer.parseUnsignedInt(props.getProperty(key));
            } catch(NumberFormatException e) {
              System.out.println("[scrabblerc] Warning: value `" + props.getProperty(key)
                 + "` was ignored for key `" + key + "`, default is `" + value + "`");
            }
            // Build setter reflectively from key
            // basically creating `set{key}(int value)` and calling it
            try {
              Method meth = cls.getMethod("set"
                  + Character.toUpperCase(key.charAt(0))
                  + key.substring(1), new Class[]{Integer.TYPE});
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
        case "historyFile": {
            String value = props.getProperty(key);
            // Build setter reflectively from key
            // basically creating `set{key}(String value)` and calling it
            try {
              Method meth = cls.getMethod("set"
                  + Character.toUpperCase(key.charAt(0))
                  + key.substring(1), new Class[]{String.class});
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
            superS,
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
            historyFile
      );
  }

  public String toString() {
    return "ConfigurationBuilder={ "
    + "verbose=" + verbose.toString() + " | "
    + "debug=" + debug.toString() + " | "
    + "blitz=" + blitz.toString() + " | "
    + "superS=" + superS.toString() + " | "
    + "gui=" + gui.toString() + " | "
    + "playerCount=" + Integer.toString(playerCount) + " | "
    + "blitzTime=" + Integer.toString(blitzTime) + " | "
    + "aiPlayers=" + aiPlayers.toString() + " | "
    + "aiDepth=" + Integer.toString(aiDepth) + " | "
    + "aiTime=" + Integer.toString(aiTime) + " | "
    + "language=" + language.toString() + " | "
    + "aiMode=" + aiMode.toString() + " | "
    + "dictFile=" + dictFile.toString() + " | "
    + "contestFile=" + contestFile.toString() + " | "
    + "historyFile=" + historyFile.toString() + " "
    + "}";
  }

  public void setVerbose(Boolean verbose){
    this.verbose = verbose;
  }

  public void setDebug(Boolean debug){
    this.debug = debug;
  }

  public void setBlitz(Boolean blitz){
    this.blitz = blitz;
  }

  public void setSuperS(Boolean superS){
    this.superS = superS;
  }

  public void setGui(Boolean gui){
    this.gui = gui;
  }

  public void setPlayerCount(int playerCount){
    this.playerCount = playerCount;
  }

  public void setBlitzTime(int blitzTime){
    this.blitzTime = blitzTime;
  }

  public void setAiPlayers(String aiPlayers){
    this.aiPlayers = aiPlayers;
  }

  public void setAiDepth(int aiDepth){
    this.aiDepth = aiDepth;
  }

  public void setAiTime(int aiTime){
    this.aiTime = aiTime;
  }

  public void setLanguage(String language){
    this.language = language;
  }

  public void setAiMode(String aiMode){
    this.aiMode = aiMode;
  }

  public void setDictFile(String dictFile){
    //TODO: ENFORCE STRING BEING A PATH
    this.dictFile = dictFile;
  }

  public void setContestFile(String contestFile){
    //TODO: ENFORCE STRING BEING A PATH
    this.contestFile = contestFile;
  }

  public void setHistoryFile(String historyFile){
    //TODO: ENFORCE STRING BEING A PATH
    this.historyFile = historyFile;
  }

}
