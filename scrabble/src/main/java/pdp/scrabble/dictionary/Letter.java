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

    /**
     * Creates a deep clone of a two-dimensional array of {@link Letter} objects.
     * <p>
     * This method iterates through the provided 2D array and creates a new array
     * where each {@link Letter} object is cloned. If the original array or any of
     * its elements are null, the method handles these cases appropriately.
     *
     * @param original the original 2D array of {@link Letter} objects to be cloned.
     *                 If this parameter is null, the method returns null.
     * @return a new 2D array of {@link Letter} objects that is a deep clone of the
     * original array. If the original array is null, returns null.
     */
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
