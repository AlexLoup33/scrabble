package pdp.scrabble.configuration;

import java.nio.file.Path;

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
    public boolean[] getAiPlayers();

    public int getAiDepth();

    public int getAiTime();

    public String getAiMode();

    // Related to dictionnary
    public String getLanguage();

    // Related to Paths/Filenames
    public Path getDictFile();

    public Path getContestFile();

    public Path getHistoryFile();
}
