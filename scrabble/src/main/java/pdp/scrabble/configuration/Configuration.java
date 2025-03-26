package pdp.scrabble.configuration;

import java.nio.file.Path;

public record Configuration(
    boolean verbose,
    String debug,
    boolean blitz,
    boolean superScrabble,
    boolean gui,
    int playerCount,
    int blitzTime,
    boolean[] aiPlayers,
    int aiDepth,
    int aiTime,
    int seed,
    String language,
    String aiMode,
    Path dictFile,
    Path contestFile,
    Path historyFile) {

  public Boolean getVerbose() {
    return this.verbose;
  }

  public String getDebug() {
    return this.debug;
  }

  public Boolean getBlitz() {
    return this.blitz;
  }

  public Boolean getSuperScrabble() {
    return this.superScrabble;
  }

  public Boolean getGui() {
    return this.gui;
  }

  public int getPlayerCount() {
    return this.playerCount;
  }

  public int getBlitzTime() {
    return this.blitzTime;
  }

  public boolean[] getAiPlayers() {
    return this.aiPlayers;
  }

  public int getAiDepth() {
    return this.aiDepth;
  }

  public int getAiTime() {
    return this.aiTime;
  }

  public int getSeed() {
    return this.seed;
  }

  public String getAiMode() {
    return this.aiMode;
  }

  public String getLanguage() {
    return this.language;
  }

  public Path getDictFile() {
    return this.dictFile;
  }

  public Path getContestFile() {
    return this.contestFile;
  }

  public Path getHistoryFile() {
    return this.historyFile;
  }
}
