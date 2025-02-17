package pdp.scrabble.game;

import java.util.ArrayList;
import java.util.List;
import pdp.scrabble.dictionary.Dictionary;
import pdp.scrabble.dictionary.IDictionary;

public class GameModel {
  public final Lang lang;
  public final Grid grid;
  public final List<Player> players;
  public final LettersDrawer draw; // draw = pioche en fr
  public final IDictionary dictionary;

  // Constructor
  public GameModel() {
    this.lang = Lang.FR;
    // TODO get these infos from Config
    this.grid = new Grid(this, 15);
    this.dictionary = new Dictionary();
    this.players = new ArrayList<>();
    this.dictionary.loadFromFile(this.lang.getDictionaryPath());
    this.draw = new LettersDrawer(this.lang);
  }
}
