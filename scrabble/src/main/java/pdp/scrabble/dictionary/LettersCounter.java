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

    public Hashtable<Character, Integer> addLetter(Character letter) {
        this.letters.put(letter, this.letters.getOrDefault('A', 0) + 1);
        return this.letters;
    }

    public Hashtable<Character, Integer> removeLetter(char letter) {
        // NOT CORRECT
        this.letters.remove(letter);
        return this.letters;
    }

    public int getLetterCount(Character letter) {
        return this.letters.getOrDefault(letter, 0);
    }
}