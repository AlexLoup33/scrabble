package pdp.scrabble.dictionary;

import java.text.Normalizer;
import java.util.Objects;

/** a class that hold a normalized word with letters that are compatible with the scrabble */
public class Word {
    private final Letter[] letters;
    private final String word;

    public Word(String baseWord) {
        this.letters = new Letter[baseWord.length()];
        baseWord = Word.normalize(baseWord);
        // => baseWord is composed of valid letters only
        for (int index = 0; index < baseWord.length(); index++) {
            this.letters[index] = new Letter(baseWord.charAt(index));
        }
        // => all letters are valid
        this.word = baseWord;
    }

    /** transforme the characters of the given text to legal letters */
    private static String normalize(String text) {
        String newText = Normalizer.normalize(text, Normalizer.Form.NFD);
        newText = newText.toUpperCase();
        // replace specific chars
        newText = newText.replace(" ", "");
        newText = newText.replace("Ç", "C");
        newText = newText.replace("Œ", "OE");
        // delete all non ascii chars
        newText = newText.replaceAll("[^\\p{ASCII}]", "");
        return newText;
    }

    @Override
    public String toString() {
        return this.word;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Word word1 = (Word) o;
        return Objects.equals(this.word, word1.word);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.word);
    }
}
