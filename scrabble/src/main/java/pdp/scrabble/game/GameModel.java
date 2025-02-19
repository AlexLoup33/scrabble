package pdp.scrabble.game;

import java.util.ArrayList;
import java.util.List;

import pdp.scrabble.configuration.IConfiguration;
import pdp.scrabble.dictionary.Dictionary;
import pdp.scrabble.dictionary.IDictionary;

public class GameModel {
  public final Lang lang;
  public final Grid grid;
  public final List<Player> players;
  public final LettersDrawer draw; // draw = pioche en fr
  public final IDictionary dictionary;

  // Constructor
  public GameModel(IConfiguration config) {
    this.lang = Lang.getLang(config.getLanguage());
    // TODO get these infos from Config
    this.grid = new Grid(this, 15);
    this.dictionary = new Dictionary();
    this.players = new ArrayList<>();
    // this.dictionary.loadFromFile(this.lang.getDictionaryPath());
    this.dictionary.loadFromFile(config.getDictFile());
    this.draw = new LettersDrawer(this.lang);
  }
}
