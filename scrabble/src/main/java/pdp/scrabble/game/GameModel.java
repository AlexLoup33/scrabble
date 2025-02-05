package pdp.scrabble.game;

import java.util.ArrayList;
import java.util.List;

public class GameModel {

    private static Grid grid;
    private static List<Player> players = new ArrayList<>();
    private static Draw draw; // draw = pioche en fr

    // Constructor
    public GameModel() {
        // TODO get these infos from Config
        this.grid = new Grid(10);
    }


}
