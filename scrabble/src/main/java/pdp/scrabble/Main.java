package pdp.scrabble;

// Compile on Mac:
// mvn package

// Run on Mac:
// java -classpath target/classes pdp.scrabble.Main


import pdp.scrabble.dictionary.LettersCounter;

import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("--- Start ---");

        LettersCounter counter = new LettersCounter();
        counter.addLetter('A');
        counter.addLetter('B');
        counter.addLetter('A');
        counter.removeLetter('A');

        // Basic test implementation
        if (counter.getLetterCount('A') != 1 && counter.getLetterCount('B') != 0) {
            throw new Exception("getLetterCount");
        }

        System.out.println("--- End ---");
    }
}