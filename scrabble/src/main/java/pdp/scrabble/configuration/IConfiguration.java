package pdp.scrabble.configuration;

public interface IConfiguration {
  // Related to GameMode
  public Boolean getVerbose();

  public Boolean getDebug();

  public Boolean getBlitz();

  public Boolean getSuperScrabble();

  public Boolean getGui();

  public int getPlayerCount();

  // Related to blitz
  public int getBlitzTime();

  // Related to AI
  public String getAiPlayers();

  public int getAiDepth();

  public int getAiTime();

  public String getAiMode();

  // Related to dictionnary
  public String getLanguage();

  // Related to Paths/Filenames
  public String getDictFile();

  public String getContestFile();

  public String getHistoryFile();
}
