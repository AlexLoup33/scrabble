package pdp.scrabble.game;

import pdp.scrabble.dictionary.LettersCounter;
import pdp.scrabble.errorsHandling.CustomError;
import pdp.scrabble.errorsHandling.EnumError;

import java.util.HashSet;
import java.util.Set;

public class Player {
    public static final Set<String> names = new HashSet<>();
    public final String name;
    public final int score;
    public final LettersCounter bag;

    public Player(String name) throws CustomError {
        if (this.names.contains(name)) {
            throw new CustomError(EnumError.SamePlayerName,
                    "Name already taken: " + name,
                    false);
        }
        this.names.add(name);
        this.name = name;
        this.score = 0;
        this.bag = new LettersCounter();
    }
}
