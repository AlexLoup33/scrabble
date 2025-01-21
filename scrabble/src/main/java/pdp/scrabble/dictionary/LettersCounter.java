package pdp.scrabble.dictionary;

import java.util.Hashtable;

public class LettersCounter {

    // dico A: 0
    //      B: 45
    // sert a dire le nombre de chaque lettre presente

    private final Hashtable<Character, Integer> letters = new Hashtable<>();

    public Hashtable<Character, Integer> getLettersCounter() {
        return this.letters;
    }

    public int addLetter(Character letter) {
        this.letters.put(letter, this.letters.getOrDefault(letter, 0) + 1);
        return this.getLetterCount(letter);
    }

    public int removeLetter(char letter) {
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
}