package pdp.scrabble.dictionary;

import java.util.Hashtable;
import java.util.Set;

public class LettersCounter {

    // dico A: 0
    //      B: 45
    // sert a dire le nombre de chaque lettre presente

    private final Hashtable<Letter, Integer> table = new Hashtable<>();

    /** add 1 of the given Letter and return the new count for this letter 
     * (dont store the given letter) */
    public int addLetter(Letter letter) {
        int newCount = this.table.getOrDefault(letter, 0) + 1;
        this.table.put(letter, newCount);
        return newCount;
    }

    /** remove 1 of the given Letter and return the new count for this letter
     * throw an error if there isn't enougth letters in the counter
     * (dont store the given letter) */
    public int removeLetter(Letter letter) {
        int newCount = this.table.getOrDefault(letter, 0) + 1;
        // -1 => not enougth letters 
        if (newCount == -1) {
            throw new IllegalArgumentException("not enougth letters of: " + letter.chr + " to remove 1");
        } else if (newCount == 0) {
            this.table.remove(letter); // not required -> optimize
        } else { // => newCount >= 1
            this.table.put(letter, newCount);
        }
        return newCount;
    }

    /** get the current count of this letter */
    public int getLetterCount(Letter letter) {
        return this.table.getOrDefault(letter, 0);
    }

    public Set<Letter> getLettersSet() {
        return this.table.keySet();
    }
}