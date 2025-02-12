package pdp.scrabble.game;

import pdp.scrabble.dictionary.Dictionary;
import pdp.scrabble.dictionary.IDictionary;

import java.util.ArrayList;
import java.util.List;

public class GameModel {

    public static Grid grid;
    public static List<Player> players = new ArrayList<>();
    public static Draw draw; // draw = pioche en fr
    public static IDictionary dictionary;

    // Constructor
    public GameModel() {
        // TODO get these infos from Config
        this.grid = new Grid(15);
        this.dictionary = new Dictionary();
        this.dictionary.loadFromFile(java.nio.file.Paths.get("dictionary/fr.txt"));

    }


}