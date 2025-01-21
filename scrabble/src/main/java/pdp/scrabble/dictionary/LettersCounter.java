package pdp.scrabble.dictionary;

import java.util.Hashtable;
import java.util.Set;

import static java.lang.Character.toUpperCase;

public class LettersCounter {

    // dico A: 0
    //      B: 45
    // sert a dire le nombre de chaque lettre presente

    private final Hashtable<Character, Integer> letters = new Hashtable<>();

    public Hashtable<Character, Integer> getLettersCounter() {
        return this.letters;
    }

    public int addLetter(Character letter) {
        letter = toUpperCase(letter);
        this.letters.put(letter, this.letters.getOrDefault(letter, 0) + 1);
        return this.getLetterCount(letter);
    }

    public int removeLetter(char letter) {
        letter = toUpperCase(letter);
        if (this.letters.getOrDefault(letter, -1) > 1) {
            this.letters.put(letter, this.letters.getOrDefault(letter, -1) - 1);
            return this.getLetterCount(letter);
        }
        if (this.letters.getOrDefault(letter, -1) == 1) {
            this.letters.remove(letter);
            return this.getLetterCount(letter);
        }
        if (this.letters.getOrDefault(letter, -1) == -1) {
            return 0;
        }
        return 0;
    }

    public int getLetterCount(Character letter) {
        return this.letters.getOrDefault(letter, 0);
    }

    public Set<Character> getKey() {
        return this.letters.keySet();
    }
}