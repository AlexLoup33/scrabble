package pdp.scrabble.configuration;

import java.nio.file.Path;

public record Configuration(
        boolean verbose,
        boolean debug,
        boolean blitz,
        boolean superScrabble,
        boolean gui,
        int playerCount,
        int blitzTime,
        boolean[] aiPlayers,
        int aiDepth,
        int aiTime,
        String language,
        String aiMode,
        Path dictFile,
        Path contestFile,
        Path historyFile)
        implements IConfiguration {
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
    public boolean[] getAiPlayers() {
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
    public Path getDictFile() {
        return this.dictFile;
    }

    @Override
    public Path getContestFile() {
        return this.contestFile;
    }

    @Override
    public Path getHistoryFile() {
        return this.historyFile;
    }
}
