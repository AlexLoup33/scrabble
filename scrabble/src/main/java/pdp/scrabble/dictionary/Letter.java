package pdp.scrabble.dictionary;

import java.util.Objects;

/**
 * a letter class that ensure this is a valid scrable letter
 */
public class Letter {
    public final boolean isJoker;
    public final char chr;

    public Letter(char c) {
        if ((c < 'A') || ('Z' < c)) {
            throw new IllegalArgumentException("the letter: " + c + '(' + (int) c + ')' + " isn't valid");
        }
        this.chr = c;
        this.isJoker = false;
    }

    /**
     * contructor for the joker letter
     */
    public Letter() {
        this.chr = '#';
        this.isJoker = true;
    }

    @Override
    public boolean equals(Object o) {
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }
        Letter other = (Letter) o;
        return this.chr == other.chr;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.chr);
    }

    @Override
    public String toString() {
        return String.valueOf(this.chr);
    }

    public static String concatenate(Letter[] letters) {
        StringBuilder sb = new StringBuilder();
        for (Letter letter : letters) {
            sb.append(letter);
        }
        return sb.toString();
    }

    public static Letter[][] deepClone(Letter[][] original) {
        if (original == null) {
            return null;
        }

        Letter[][] clonedArray = new Letter[original.length][original.length];

        for (int i = 0; i < original.length; i++) {
            if (original[i] != null) {
                clonedArray[i] = new Letter[original[i].length];
                for (int j = 0; j < original[i].length; j++) {
                    if (original[i][j] != null) {
                        clonedArray[i][j] = new Letter(original[i][j].chr);
                    }
                }
            }
        }

        return clonedArray;
    }
}
