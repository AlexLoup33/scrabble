package pdp.scrabble.game;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import pdp.scrabble.Utilities;
import pdp.scrabble.configuration.Configuration;
import pdp.scrabble.configuration.IConfiguration;
import pdp.scrabble.dictionary.Dictionary;
import pdp.scrabble.dictionary.IDictionary;
import pdp.scrabble.errorsHandling.CustomError;
import pdp.scrabble.game.actions.GameAction;
import pdp.scrabble.game.actions.PlaceWordAction;
import pdp.scrabble.game.actions.RenamePlayerAction;

public class GameModel {
    public final Grid grid;
    public final Lang lang;
    public final List<Player> players;
    public final LettersDrawer draw; // draw = pioche en fr
    public final IDictionary dictionary;
    public final Configuration config;

    public GameModel(IConfiguration config) {
        this.config = (Configuration) config;

        this.players = new ArrayList<>(this.config.getPlayerCount());
        for (int i = 0; i < this.config.getPlayerCount(); i++) {
            this.players.add(new Player());
        }
        this.dictionary = new Dictionary();
        this.dictionary.loadFromFile(config.getDictFile());

        Lang tmp;
        if (this.config.getLanguage().equalsIgnoreCase("ENGLISH")) {
            tmp = Lang.EN;
        } else if (this.config.getLanguage().equalsIgnoreCase("FRENCH")) {
            tmp = Lang.FR;
        } else {
            tmp = Lang.EN;
        }
        this.lang = tmp;
        this.draw = new LettersDrawer(tmp);

        if (!config.getSuperScrabble()) {
            this.grid = new Grid(this, 15);
        } else {
            this.grid = new Grid(this, 21);
        }
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

    public void applyGameAction(RenamePlayerAction rpa) {
        rpa.player.rename(rpa.newName);
    }


}
