package pdp.scrabble.dictionary;

import java.util.Hashtable;

public class LettersCounter {

    // dico A: 0
    //      B: 45
    // sert a dire le nombre de chaque lettre presente

    private final Hashtable<Character, Integer> letters = new Hashtable<>();

    public LettersCounter() {
        // none
    }

    public Hashtable<Character, Integer> getLettersCounter() {
        return this.letters;
    }

    public int addLetter(Character letter) {
        if (this.letters.getOrDefault(letter, 0) == 0) {
            this.letters.put(letter, 1);
        } else {
            this.letters.put(letter, this.letters.getOrDefault(letter, 0) + 1);
        }
        return this.letters.getOrDefault(letter, 0);
    }

    public int removeLetter(char letter) {
        if (this.letters.getOrDefault(letter, 0) == 1) {
            this.letters.remove(letter);
        } else {
            this.letters.put(letter, this.letters.getOrDefault(letter, 0) - 1);
        }
        return this.letters.getOrDefault(letter, 0);
    }

    public int getLetterCount(Character letter) {
        return this.letters.getOrDefault(letter, 0);
    }
}