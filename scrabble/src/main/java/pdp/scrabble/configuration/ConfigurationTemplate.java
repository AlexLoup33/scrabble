package pdp.scrabble.configuration;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;

import pdp.scrabble.errorsHandling.CustomError;
import pdp.scrabble.errorsHandling.ErrorType;

/**
 * Provides a template for ConfigurationBuilder to create a Configuration object incrementally. This
 * is needed because Configuration record is immutable. All the setters throws errors if the values
 * does not match the requirements.
 */
public class ConfigurationTemplate {

    // FLAGS
    private boolean verbose;
    private String debug;
    private boolean blitz;
    private boolean superScrabble;
    private boolean gui;

    private int playerCount;
    // Only relevant with blitz on
    private int blitzTime;

    // Only relevant with aiPlayers
    private boolean[] aiPlayers;
    private int aiDepth;
    private int aiTime;

    private String language;
    // Monte Carlo or MiniMax
    // 'mcts' or 'minimax'
    private String aiMode;

    // Paths/Filenames
    private Path dictFile;
    private Path contestFile;
    private Path historyFile;

    public ConfigurationTemplate() {
        this.aiPlayers = new boolean[]{false, false, false, false};
    }

    public void setVerbose(Boolean verbose) {
        this.verbose = verbose;
    }

    public void setDebug(String debug) {
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

    public void setPlayerCount(int playerCount) throws CustomError {
        if (playerCount > 4 || playerCount < 2) {
            throw createError(
                    "Value `" + playerCount + "` for key `playerCount`" + ", expected value in range 2-4");
        }
        this.playerCount = playerCount;
    }

    public void setBlitzTime(int blitzTime) {
        this.blitzTime = blitzTime;
    }

    public void setAiPlayers(String aiPlayers) {
        if (aiPlayers.contains("A")) {
            this.aiPlayers = new boolean[]{true, true, true, true};
            return;
        }
        if (aiPlayers.contains("1")) {
            this.aiPlayers[0] = true;
        }
        if (aiPlayers.contains("2")) {
            this.aiPlayers[1] = true;
        }
        if (aiPlayers.contains("3")) {
            this.aiPlayers[2] = true;
        }
        if (aiPlayers.contains("4")) {
            this.aiPlayers[3] = true;
        }
    }

    public void setAiDepth(int aiDepth) {
        this.aiDepth = aiDepth;
    }

    public void setAiTime(int aiTime) {
        this.aiTime = aiTime;
    }

    public void setLanguage(String language) {
        // TODO: language check, waiting for language spec
        this.language = language;
    }

    public void setAiMode(String aiMode) throws CustomError {
        if (aiMode.equals("null")) {
            this.aiMode = null;
        } else if (aiMode.equals("minimax") || aiMode.equals("mcts")) {
            this.aiMode = aiMode;
        } else {
            throw createError(
                    "Value `" + aiMode + "` for key `aiMode`" + ", expected `minimax`, `mcts` or `null`");
        }
    }

    public void setDictFile(String dictFile) throws CustomError {
        try {
            this.dictFile = Path.of(dictFile);
            if (!Files.isRegularFile(this.dictFile)) {
                throw createError(
                        "Value `" + dictFile + "` for key `dictFile`" + ", expected a regular file");
            }
        } catch (InvalidPathException e) {
            throw createError("Invalid dictfile, reason : " + e.getReason());
        }
    }

    public void setContestFile(String contestFile) throws CustomError {
        if (contestFile.equals("null")) {
            this.contestFile = null;
        } else {
            try {
                this.contestFile = Path.of(contestFile);
                if (!Files.isRegularFile(this.contestFile)) {
                    throw createError(
                            "Value `"
                                    + contestFile
                                    + "` for key `contestFile`"
                                    + ", expected a regular file, or `null`");
                }
            } catch (InvalidPathException e) {
                throw createError("Invalid contestFile, reason : " + e.getReason());
            }
        }
    }

    public void setHistoryFile(String historyFile) throws CustomError {
        if (historyFile.equals("null")) {
            this.historyFile = null;
        } else {
            try {
                this.historyFile = Path.of(historyFile);
                if (!Files.isRegularFile(this.historyFile)) {
                    throw createError(
                            "Value `"
                                    + historyFile
                                    + "` for key `historyFile`"
                                    + ", expected a regular file, or `null`");
                }
            } catch (InvalidPathException e) {
                throw createError("Invalid historyFile, reason : " + e.getReason());
            }
        }
    }

    private CustomError createError(String msg) {
        return new CustomError(ErrorType.FailedConfigFileParsing, msg, "user skill issue");
    }

    public String aiPlayersToString() {
        StringBuilder value = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            if (this.aiPlayers[i]) {
                if (value.length() != 0) {
                    value.append(',');
                }
                value.append(i + 1);
            }
        }
        if (value.length() == 0) {
            return "null";
        }
        return value.toString();
    }

    public String toString() {
        return "ConfigurationTemplate={ "
                + "verbose="
                + verbose
                + " | "
                + "debug="
                + debug
                + " | "
                + "blitz="
                + blitz
                + " | "
                + "superScrabble="
                + superScrabble
                + " | "
                + "gui="
                + gui
                + " | "
                + "playerCount="
                + Integer.toString(playerCount)
                + " | "
                + "blitzTime="
                + Integer.toString(blitzTime)
                + " | "
                + "aiPlayers="
                + ConfigurationBuilder.aiPlayersToString(aiPlayers)
                + " | "
                + "aiDepth="
                + Integer.toString(aiDepth)
                + " | "
                + "aiTime="
                + Integer.toString(aiTime)
                + " | "
                + "language="
                // + language.toString()
                + language
                + " | "
                + "aiMode="
                + aiMode
                + " | "
                + "dictFile="
                // + (dictFile != null ? dictFile.toString() : "null")
                + dictFile
                + " | "
                + "contestFile="
                + contestFile
                + " | "
                + "historyFile="
                + historyFile
                + " "
                + "}";
    }

    public void setAiPlayers(boolean[] aiPlayers) {
        this.aiPlayers = aiPlayers;
    }

    public void setDictFile(Path dictFile) {
        this.dictFile = dictFile;
    }

    public void setContestFile(Path contestFile) {
        this.contestFile = contestFile;
    }

    public void setHistoryFile(Path historyFile) {
        this.historyFile = historyFile;
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
}
