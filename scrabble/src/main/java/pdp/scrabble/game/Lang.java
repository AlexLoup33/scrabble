package pdp.scrabble.game;

import java.nio.file.Path;
import java.nio.file.Paths;

import pdp.scrabble.dictionary.Letter;
import pdp.scrabble.dictionary.LettersCounter;
import pdp.scrabble.dictionary.LettersPoints;
import pdp.scrabble.errorsHandling.CustomError;
import pdp.scrabble.errorsHandling.ErrorType;

public enum Lang {
    EN("english", "en.txt") {
        @Override
        public LettersCounter getLettersCount() {
            LettersCounter letterCounts = new LettersCounter();
            letterCounts.addLetter(new Letter('A'), 9);
            letterCounts.addLetter(new Letter('B'), 2);
            letterCounts.addLetter(new Letter('C'), 2);
            letterCounts.addLetter(new Letter('D'), 4);
            letterCounts.addLetter(new Letter('E'), 12);
            letterCounts.addLetter(new Letter('F'), 2);
            letterCounts.addLetter(new Letter('G'), 3);
            letterCounts.addLetter(new Letter('H'), 2);
            letterCounts.addLetter(new Letter('I'), 9);
            letterCounts.addLetter(new Letter('J'), 1);
            letterCounts.addLetter(new Letter('K'), 1);
            letterCounts.addLetter(new Letter('L'), 4);
            letterCounts.addLetter(new Letter('M'), 2);
            letterCounts.addLetter(new Letter('N'), 6);
            letterCounts.addLetter(new Letter('O'), 8);
            letterCounts.addLetter(new Letter('P'), 2);
            letterCounts.addLetter(new Letter('Q'), 1);
            letterCounts.addLetter(new Letter('R'), 6);
            letterCounts.addLetter(new Letter('S'), 4);
            letterCounts.addLetter(new Letter('T'), 6);
            letterCounts.addLetter(new Letter('U'), 4);
            letterCounts.addLetter(new Letter('V'), 2);
            letterCounts.addLetter(new Letter('W'), 2);
            letterCounts.addLetter(new Letter('X'), 1);
            letterCounts.addLetter(new Letter('Y'), 2);
            letterCounts.addLetter(new Letter('Z'), 1);
            return letterCounts;
        }

        @Override
        public LettersPoints getLettersPoints() {
            LettersPoints letterPoints = new LettersPoints();
            letterPoints.addLetter(new Letter('A'), 1);
            letterPoints.addLetter(new Letter('B'), 3);
            letterPoints.addLetter(new Letter('C'), 3);
            letterPoints.addLetter(new Letter('D'), 2);
            letterPoints.addLetter(new Letter('E'), 1);
            letterPoints.addLetter(new Letter('F'), 4);
            letterPoints.addLetter(new Letter('G'), 2);
            letterPoints.addLetter(new Letter('H'), 4);
            letterPoints.addLetter(new Letter('I'), 1);
            letterPoints.addLetter(new Letter('J'), 8);
            letterPoints.addLetter(new Letter('K'), 5);
            letterPoints.addLetter(new Letter('L'), 1);
            letterPoints.addLetter(new Letter('M'), 3);
            letterPoints.addLetter(new Letter('N'), 1);
            letterPoints.addLetter(new Letter('O'), 1);
            letterPoints.addLetter(new Letter('P'), 3);
            letterPoints.addLetter(new Letter('Q'), 10);
            letterPoints.addLetter(new Letter('R'), 1);
            letterPoints.addLetter(new Letter('S'), 1);
            letterPoints.addLetter(new Letter('T'), 1);
            letterPoints.addLetter(new Letter('U'), 1);
            letterPoints.addLetter(new Letter('V'), 4);
            letterPoints.addLetter(new Letter('W'), 4);
            letterPoints.addLetter(new Letter('X'), 8);
            letterPoints.addLetter(new Letter('Y'), 4);
            letterPoints.addLetter(new Letter('Z'), 10);

            return letterPoints;
        }
    },

    FR("français", "en.txt") {
        @Override
        public LettersCounter getLettersCount() {
            LettersCounter letterCounts = new LettersCounter();
            letterCounts.addLetter(new Letter('A'), 9);
            letterCounts.addLetter(new Letter('B'), 2);
            letterCounts.addLetter(new Letter('C'), 2);
            letterCounts.addLetter(new Letter('D'), 3);
            letterCounts.addLetter(new Letter('E'), 15);
            letterCounts.addLetter(new Letter('F'), 2);
            letterCounts.addLetter(new Letter('G'), 2);
            letterCounts.addLetter(new Letter('H'), 2);
            letterCounts.addLetter(new Letter('I'), 8);
            letterCounts.addLetter(new Letter('J'), 1);
            letterCounts.addLetter(new Letter('K'), 1);
            letterCounts.addLetter(new Letter('L'), 5);
            letterCounts.addLetter(new Letter('M'), 3);
            letterCounts.addLetter(new Letter('N'), 6);
            letterCounts.addLetter(new Letter('O'), 6);
            letterCounts.addLetter(new Letter('P'), 2);
            letterCounts.addLetter(new Letter('Q'), 1);
            letterCounts.addLetter(new Letter('R'), 6);
            letterCounts.addLetter(new Letter('S'), 6);
            letterCounts.addLetter(new Letter('T'), 6);
            letterCounts.addLetter(new Letter('U'), 6);
            letterCounts.addLetter(new Letter('V'), 2);
            letterCounts.addLetter(new Letter('W'), 1);
            letterCounts.addLetter(new Letter('X'), 1);
            letterCounts.addLetter(new Letter('Y'), 1);
            letterCounts.addLetter(new Letter('Z'), 1);
            return letterCounts;
        }

        @Override
        public LettersPoints getLettersPoints() {
            LettersPoints letterPoints = new LettersPoints();
            letterPoints.addLetter(new Letter('A'), 1);
            letterPoints.addLetter(new Letter('B'), 3);
            letterPoints.addLetter(new Letter('C'), 3);
            letterPoints.addLetter(new Letter('D'), 2);
            letterPoints.addLetter(new Letter('E'), 1);
            letterPoints.addLetter(new Letter('F'), 4);
            letterPoints.addLetter(new Letter('G'), 2);
            letterPoints.addLetter(new Letter('H'), 4);
            letterPoints.addLetter(new Letter('I'), 1);
            letterPoints.addLetter(new Letter('J'), 8);
            letterPoints.addLetter(new Letter('K'), 10);
            letterPoints.addLetter(new Letter('L'), 1);
            letterPoints.addLetter(new Letter('M'), 2);
            letterPoints.addLetter(new Letter('N'), 1);
            letterPoints.addLetter(new Letter('O'), 1);
            letterPoints.addLetter(new Letter('P'), 3);
            letterPoints.addLetter(new Letter('Q'), 8);
            letterPoints.addLetter(new Letter('R'), 1);
            letterPoints.addLetter(new Letter('S'), 1);
            letterPoints.addLetter(new Letter('T'), 1);
            letterPoints.addLetter(new Letter('U'), 1);
            letterPoints.addLetter(new Letter('V'), 4);
            letterPoints.addLetter(new Letter('W'), 10);
            letterPoints.addLetter(new Letter('X'), 10);
            letterPoints.addLetter(new Letter('Y'), 10);
            letterPoints.addLetter(new Letter('Z'), 10);

            return letterPoints;
        }
    };

    private static final String dictionaryDirectory = "dictionary/";
    public final String displayName;
    private final String fileName;

    private Lang(String displayName, String fileName) {
        this.displayName = displayName;
        this.fileName = fileName;
    }

    public String getCodeLang() {
        return this.name().toLowerCase();
    }

    /**
     * @param codeLang must be a valid codeLang (will be lowercased)
     * @return the Lang with the associated codeLang
     */
    public static Lang getLang(String codeLang) {
        codeLang = codeLang.toLowerCase();
        for (Lang lang : Lang.values()) {
            if (lang.getCodeLang() == codeLang) {
                // => matching codeLang
                return lang;
            }
        }
        // => no matching codeLang
        throw new CustomError(ErrorType.InvalidLangError,
                "invalid lang code: " + codeLang, "there are no Lang named with the codeLang: " + codeLang);
    }

    /**
     * @return where to find the file of the dictionary for this lang
     */
    public Path getDictionaryPath() {
        return Path.of(Lang.dictionaryDirectory, this.fileName);
    }

    /**
     * 
     * @return the counts of the initial letters in the drawer for this lang
     */
    public LettersCounter getLettersCount() {
        throw new UnsupportedOperationException("the lang " + this.name() + " havn't implemented getLettersCount");
    }

    /**
     * 
     * @return the points for each letters in this lang
     */
    public LettersPoints getLettersPoints() {
        throw new UnsupportedOperationException("the lang " + this.name() + " havn't implemented getLettersPoints");
    }
}
