package pdp.scrabble.game;

import pdp.scrabble.dictionary.LettersCounter;

import java.util.HashSet;
import java.util.Set;

public class Player {
    public static final Set<Integer> id_set = new HashSet<>();
    public final Integer id;
    public final String name;
    public final int score;
    public final LettersCounter bag;

    public Player(String name) {
        Integer max_id = -1;
        for (Integer d : id_set) {
            if (d > max_id) {
                max_id = d;
            }
        }
        this.id = max_id + 1;
        id_set.add(this.id);

        this.name = name;
        this.score = 0;
        this.bag = new LettersCounter();
    }
}
