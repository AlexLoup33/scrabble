package pdp.scrabble.dictionary;

import java.text.Normalizer;

/* a class that hold a normalized word with letters that are compatible with the scrable
 */
public class Word {
    private final String word;

    public Word(String baseWord) {
        this.word = Word.normalize(baseWord);
    }

    public String getString() {
        return this.word;
    }

    private static String normalize(String text) {
        // 
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
}
