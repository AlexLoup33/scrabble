package pdp.scrabble.game;

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

  // Constructor
  /*
   * public GameModel() {
   * this.lang = Lang.FR;
   * // TODO get these infos from Config
   * this.grid = new Grid(this, 15);
   * this.dictionary = new Dictionary();
   * this.players = new ArrayList<>();
   * this.dictionary.loadFromFile(this.lang.getDictionaryPath());
   * this.draw = new LettersDrawer(this.lang);
   * }
   */

  public GameModel(IConfiguration config) {
    this.lang = Lang.getLang(config.getLanguage());
    // TODO get these infos from Config
    this.grid = new Grid(this, 15);
    this.dictionary = new Dictionary();
    this.players = new ArrayList<>();
    for (int i = 0; i < config.getPlayerCount(); i++) {
      this.players.add(new Player());
    }
    this.dictionary.loadFromFile(config.getDictFile());
    this.draw = new LettersDrawer(this.lang);
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
