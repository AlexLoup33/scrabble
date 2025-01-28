/**
 * Compile on Mac: mvn package
 *
 * <p>Run on Mac: java -classpath target/classes pdp.scrabble.Main
 */
package pdp.scrabble;

import pdp.scrabble.dictionary.Letter;
import pdp.scrabble.dictionary.LettersCounter;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("--- Start ---");

        LettersCounter counter = new LettersCounter();
        counter.addLetter(new Letter('A'));
        counter.addLetter(new Letter('B'));
        counter.addLetter(new Letter('A'));
        counter.removeLetter(new Letter('A'));

        // Basic test implementation
        if (counter.getLetterCount(new Letter('A')) != 1
                && counter.getLetterCount(new Letter('B')) != 0) {
            throw new Exception("getLetterCount");
        }

        System.out.println("--- End ---");
    }
}