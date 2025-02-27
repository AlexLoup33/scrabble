package pdp.scrabble.game;

import java.nio.file.Path;
import java.util.Locale;
import java.util.ResourceBundle;

import pdp.scrabble.dictionary.Letter;
import pdp.scrabble.dictionary.LettersCounter;
import pdp.scrabble.dictionary.LettersPoints;
import pdp.scrabble.errorsHandling.CustomError;
import pdp.scrabble.errorsHandling.ErrorType;

public enum Lang {
    EN(Locale.ENGLISH) {
        @Override
        public LettersCounter getLettersCount() {
            LettersCounter letterCounts = new LettersCounter();
            letterCounts.addLetter(new Letter(), 2);
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

    FR(Locale.FRENCH) {
        @Override
        public LettersCounter getLettersCount() {
            LettersCounter letterCounts = new LettersCounter();
            letterCounts.addLetter(new Letter(), 2);
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
    private static final String viewCliLangPack = "pack_cli_";

    public final Locale locale;

    /**
     * create a new language available for scrable the name of the Lang must be an
     * UPPERCASE of the
     * lang code in order to keep thing easy
     *
     * @param locale the local to use with this lang
     */
    private Lang(Locale locale) {
        this.locale = locale;
        if (!this.getCodeLang().equals(this.locale.getLanguage())) {
            throw new CustomError(
                    ErrorType.MalformedLangError,
                    "couldn't create the Lang: '" + this.name() + "'",
                    "you can't create a Lang with name code: '"
                            + this.getCodeLang()
                            + "'' from Locale("
                            + locale.getLanguage()
                            + ")");
        }
        if (!(this.locale.getCountry().equals(""))) {
            throw new CustomError(
                    ErrorType.MalformedLangError,
                    "couldn't create the Lang: '" + this.name() + "'",
                    "you can't create a Lang with a Local of a country '" + locale + "'");
        }
    }

    /**
     * @return the name of the language to be displayed (in its lang)
     */
    public String getDisplayName() {
        return this.locale.getDisplayLanguage(this.locale);
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
            if (lang.getCodeLang().equals(codeLang)) {
                // => matching codeLang
                return lang;
            }
        }
        // => no matching codeLang
        throw new CustomError(
                ErrorType.InvalidLangError,
                "invalid lang code: " + codeLang,
                "there are no Lang named with the codeLang: " + codeLang);
    }

    /**
     * the filename is constructed with
     *
     * @return where to find the file of the dictionary for this lang
     */
    public Path getDictionaryPath() {
        return Path.of(Lang.dictionaryDirectory, this.getCodeLang() + ".txt");
    }

    /**
     * @return the ressource bundle for the ViewCLI
     */
    public ResourceBundle getViewCLI_langPack() {
        return ResourceBundle.getBundle(Lang.viewCliLangPack + this.getCodeLang());
    }

    /**
     * @return the counts of the initial letters in the drawer for this lang
     */
    public LettersCounter getLettersCount() {
        throw new UnsupportedOperationException(
                "the lang " + this.name() + " havn't implemented getLettersCount");
    }

    /**
     * @return the points for each letters in this lang
     */
    public LettersPoints getLettersPoints() {
        throw new UnsupportedOperationException(
                "the lang " + this.name() + " havn't implemented getLettersPoints");
    }

    @Override
    public String toString() {
        return this.locale.toString();
    }
}
