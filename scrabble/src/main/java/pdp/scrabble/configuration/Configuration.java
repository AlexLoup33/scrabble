package pdp.scrabble.configuration;

public record Configuration(boolean verbose, boolean debug, boolean blitz, boolean superScrabble, boolean gui, int playerCount, int blitzTime, String aiPlayers, int aiDepth, int aiTime, String language, String aiMode, String dictFile, String contestFile, String historyFile) implements IConfiguration{
    @Override
    public Boolean getVerbose() {
        return this.verbose;
    }

    @Override
    public Boolean getDebug() {
        return this.debug;
    }

    @Override
    public Boolean getBlitz() {
        return this.blitz;
    }

    @Override
    public Boolean getSuperScrabble() {
        return this.superScrabble;
    }

    @Override
    public Boolean getGui() {
        return this.gui;
    }

    @Override
    public int getPlayerCount() {
        return this.playerCount;
    }

    @Override
    public int getBlitzTime() {
        return this.blitzTime;
    }

    @Override
    public String getAiPlayers() {
        return this.aiPlayers;
    }

    @Override
    public int getAiDepth() {
        return this.aiDepth;
    }

    @Override
    public int getAiTime() {
        return this.aiTime;
    }

    @Override
    public String getAiMode() {
        return this.aiMode;
    }

    @Override
    public String getLanguage() {
        return this.language;
    }

    @Override
    public String getDictFile() {
        return this.dictFile;
    }

    @Override
    public String getContestFile() {
        return this.contestFile;
    }

    @Override
    public String getHistoryFile() {
        return this.historyFile;
    }
}
