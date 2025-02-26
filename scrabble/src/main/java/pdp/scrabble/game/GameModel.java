package pdp.scrabble.game;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import pdp.scrabble.Utilities;
import pdp.scrabble.configuration.IConfiguration;
import pdp.scrabble.dictionary.Dictionary;
import pdp.scrabble.dictionary.IDictionary;
import pdp.scrabble.errorsHandling.CustomError;
import pdp.scrabble.game.actions.GameAction;
import pdp.scrabble.game.actions.PlaceWordAction;

public class GameModel {
    public final Lang lang;
    public final Grid grid;
    public final List<Player> players;
    public final LettersDrawer draw; // draw = pioche en fr
    public final IDictionary dictionary;

    public final Boolean verbose;
    public final String debug;
    public final Boolean blitz;
    public final Boolean gui;
    public final int playerCount;
    public final int blitzTime;
    public final boolean[] aiPlayers;
    public final int aiDepth;
    public final int aiTime;
    public final String aiMode;
    public final Lang language;
    public final Path contestFile;
    public final Path historyFile;

    public GameModel(IConfiguration config) {
        this.lang = Lang.getLang(config.getLanguage());

        this.verbose = config.getVerbose();
        this.debug = config.getDebug();
        this.blitz = config.getBlitz();
        this.gui = config.getGui();
        this.playerCount = config.getPlayerCount();
        this.blitzTime = config.getBlitzTime();
        this.aiPlayers = config.getAiPlayers();
        this.aiDepth = config.getAiDepth();
        this.aiTime = config.getAiTime();
        this.aiMode = config.getAiMode();
        if (config.getLanguage().equalsIgnoreCase("en")) {
            this.language = Lang.EN;
        } else if (config.getLanguage().equalsIgnoreCase("fr")) {
            this.language = Lang.FR;
        } else {
            this.language = Lang.EN;
        }
        this.contestFile = config.getContestFile();
        this.historyFile = config.getHistoryFile();


        if (!config.getSuperScrabble()) {
            this.grid = new Grid(this, 15);
        } else {
            this.grid = new Grid(this, 21);
        }
        this.players = new ArrayList<>(this.playerCount);
        for (int i = 0; i < config.getPlayerCount(); i++) {
            this.players.add(new Player());
        }
        this.dictionary = new Dictionary();
        this.dictionary.loadFromFile(config.getDictFile());

        this.draw = new LettersDrawer(this.language);
    }

    public boolean applyGameAction(GameAction played) {
        if (played instanceof PlaceWordAction) {
            try {
                grid.placeLetters((PlaceWordAction) played);
                return true;
            } catch (CustomError e) {
                System.out.println(e);
                // view.showError(e.toUserFormatedText()); NOT IMPLEMENTED
                return false;
            }
        }
        Utilities.debug('l', "%s is playing a non implemented action\n", played.player);
        return false;
    }
}
