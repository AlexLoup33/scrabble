package pdp.scrabble.game;

import java.util.ArrayList;
import java.util.List;

public class GameModel {

    public static Grid grid;
    public static List<Player> players = new ArrayList<>();
    public static Draw draw; // draw = pioche en fr

    // Constructor
    public GameModel() {
        // TODO get these infos from Config
        this.grid = new Grid(15);
    }


}