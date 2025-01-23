package pdp.scrabble.configuration;

public class ConfigurationObject {
  
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
  // TODO: Truly implement aiPlayersCount as Bitmap
  // Currently bitmap in string
  // with player 1 to the right
  // and player 4 to the left
  private String aiPlayers;
  private int aiDepth;
  private int aiTime;

  private String language;
  // Monte Carlo or MiniMax
  private String aiMode;

  // Paths/Filenames
  private String dictFile;
  private String contestFile;
  private String historyFile;

  public static ConfigurationObject getDefault() {
    ConfigurationObject conf = new ConfigurationObject();
    conf.setVerbose(false);
    conf.setDebug(false);
    conf.setBlitz(false);
    conf.setSuperS(false);
    conf.setGui(false);

    conf.setPlayerCount(2);
    conf.setBlitzTime(30);
    //Player 2 is AI by default
    conf.setAiPlayers("0010");
    // TODO: CHOSE DEFAULT DEPTH
    conf.setAiDepth(0);
    conf.setAiTime(5);

    conf.setLanguage("en");
    // TODO: CHOSE DEFAULT LOCATION FOR THESE FILES 
    conf.setAiMode("null");

    // TODO: CHOSE DEFAULT LOCATION FOR THESE FILES 
    conf.setDictFile("./");
    conf.setContestFile("./");
    conf.setHistoryFile("./");

    return conf;
  }

  public String toString() {
    return "{ "
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
    this.dictFile = dictFile;
  }

  public void setContestFile(String contestFile){
    this.contestFile = contestFile;
  }

  public void setHistoryFile(String historyFile){
    this.historyFile = historyFile;
  }

}
