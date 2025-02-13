package pdp.scrabble.dictionary;

import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import pdp.scrabble.errorsHandling.CustomError;
import pdp.scrabble.errorsHandling.ErrorType;

/*IMPORTANT A CHANGER:
 * Classe servant a associer les points a chaque lettres. Reprend quasiment les mêmes classes que
 * LettersCounter qui compte le nombre de lettres. 
 * A faire: une superclasse dont heritera LettersCounter et LettersPoints
 */
public class LettersPoints extends LettersCounter {

    private final Hashtable<Letter, Integer> table;

    public LettersPoints() {
        this.table = new Hashtable<>();
    }

    public LettersPoints(List<Letter> letters) {
        this();
        for (Letter letter : letters) {
            this.addLetter(letter);
        }
    }

    public int addLetter(Letter letter, int count) {
        if (count < 0) {
            throw new CustomError(ErrorType.InvalidLetterCountError,
                    "count must be >= 0", "the count " + count + " isn't >= 0");
        }
        int newCount = this.table.getOrDefault(letter, 0) + count;
        this.table.put(letter, newCount);
        return newCount;
    }

    public int addLetter(Letter letter) {
        return this.addLetter(letter, 1);
    }

    public int getLettersPoints(Letter letter) {
        return this.table.getOrDefault(letter, 0);
    }

    /**
     * get the current letters (keys)
     */
    public Set<Letter> getLettersSet() {
        return this.table.keySet();
    }

}
