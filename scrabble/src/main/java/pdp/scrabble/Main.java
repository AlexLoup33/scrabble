package pdp.scrabble;

// Compile on Mac:
// mvn package

// Run on Mac:
// java -classpath target/classes pdp.scrabble.Main


import pdp.scrabble.dictionary.Dictionary;
import pdp.scrabble.dictionary.LettersCounter;
import pdp.scrabble.dictionary.Word;

import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("--- Start ---");

        // Create a 'bag of letters' (=LettersCounter)
        LettersCounter lc = new LettersCounter();
        lc.addLetter('A');
        lc.addLetter('b');
        lc.addLetter('C');

        // Create a Dictionary with some words
        Dictionary dict = new Dictionary();
        dict.addWord(new Word("bac"));
        dict.addWord(new Word("ba"));
        dict.addWord(new Word("baba"));
        dict.addWord(new Word("aa"));
        dict.addWord(new Word("ab"));
        System.out.println(dict);

        // Search for words containing lc letters
        System.out.println(dict.findWords(lc));


        System.out.println("--- End ---");
    }
}