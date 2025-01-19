package pdp.scrabble.dictionary;

import java.util.Hashtable;

public class LettersCounter {

    // dico A: 0
    //      B: 45
    // sert a dire le nombre de chaque lettre presente

    private Hashtable<Character, Integer> letters;

    public Hashtable getLettersCounter() {
        return this.letters;
    }

    public Hashtable addLetter(char letter) {
        this.letters.put(letter, this.letters.getOrDefault(letter, 0) + 1);
        return this.letters;
    }

    public Hashtable removeLetter(char letter) {
        this.letters.remove(letter);
        return this.letters;
    }

    public int getLetterCount(char letter) {
        if (this.letters.get(letter) != 0) {
            return this.letters.get(letter);
        } else {
            return 0;
        }
    }
}