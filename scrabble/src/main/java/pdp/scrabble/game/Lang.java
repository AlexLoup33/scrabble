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
        public LettersCounter getLettersCount(boolean isSuperScrabble) {
            LettersCounter letterCounts = new LettersCounter();
            char[] letters = {
                    'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
                    'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
            };
            int[] counts = {
                    9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1
            };
            int[] counts_ss = {
                    16, 4, 6, 8, 24, 4, 5, 5, 13, 2, 2, 7, 6, 13, 15, 4, 2, 13, 10, 15, 7, 3, 4, 2, 4, 2
            };

            letterCounts.addLetter(Letter.joker(), 2);
            if (isSuperScrabble) {
                letterCounts.addLetter(Letter.joker(), 2);
            }

            for (int i = 0; i < letters.length; i++) {
                if (!isSuperScrabble) {
                    letterCounts.addLetter(new Letter(letters[i]), counts[i]);
                } else {
                    letterCounts.addLetter(new Letter(letters[i]), counts_ss[i]);
                }
            }

            return letterCounts;
        }

        @Override
        public LettersPoints getLettersPoints(boolean isSuperScrabble) {
            LettersPoints letterPoints = new LettersPoints();
            char[] letters = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
            int[] points = {1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1, 1, 3, 10, 1, 1, 1, 1, 4, 4, 8, 4, 10};
            int[] points_ss = {1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1, 1, 3, 10, 1, 1, 1, 1, 4, 4, 8, 4, 10};

            for (int i = 0; i < letters.length; i++) {
                if (!isSuperScrabble) {
                    letterPoints.addLetter(new Letter(letters[i]), points[i]);
                } else {
                    letterPoints.addLetter(new Letter(letters[i]), points_ss[i]);
                }

            }

            return letterPoints;
        }
    },

    FR(Locale.FRENCH) {
        @Override
        public LettersCounter getLettersCount(boolean isSuperScrabble) {
            LettersCounter letterCounts = new LettersCounter();

            char[] letters = {
                    'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
                    'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
            };
            int[] counts = {
                    9, 2, 2, 3, 15, 2, 2, 2, 8, 1, 1, 5, 3, 6, 6, 2, 1, 6, 6, 6, 6, 2, 1, 1, 1, 1
            };
            int[] counts_ss = {
                    9, 1, 2, 3, 15, 1, 1, 1, 8, 1, 0, 5, 3, 6, 6, 2, 1, 6, 6, 6, 6, 2, 1, 1, 1, 0
            };

            letterCounts.addLetter(Letter.joker(), 2);
            for (int i = 0; i < letters.length; i++) {
                if (!isSuperScrabble) {
                    letterCounts.addLetter(new Letter(letters[i]), counts[i]);
                } else {
                    letterCounts.addLetter(new Letter(letters[i]), counts_ss[i]);
                }

            }

            return letterCounts;
        }

        @Override
        public LettersPoints getLettersPoints(boolean isSuperScrabble) {
            LettersPoints letterPoints = new LettersPoints();
            char[] letters = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
            int[] points = {1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 10, 1, 2, 1, 1, 3, 8, 1, 1, 1, 1, 4, 10, 10, 10, 10};
            int[] points_ss = {1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1, 1, 3, 10, 1, 1, 1, 1, 4, 10, 8, 10, 0};

            for (int i = 0; i < letters.length; i++) {
                if (!isSuperScrabble) {
                    letterPoints.addLetter(new Letter(letters[i]), points[i]);
                } else {
                    letterPoints.addLetter(new Letter(letters[i]), points_ss[i]);
                }
            }

            return letterPoints;
        }
    };

    private static final String dictionaryDirectory = "dictionary/";
    private static final String viewCliLangPack = "pack_cli_";

    public final Locale locale;
    private Path customDictPath;

    /**
     * create a new language available for scrabble the name of the Lang must be an UPPERCASE of the
     * lang code to keep thing easy
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
        if (!(this.locale.getCountry().isEmpty())) {
            throw new CustomError(
                    ErrorType.MalformedLangError,
                    "couldn't create the Lang: '" + this.name() + "'",
                    "you can't create a Lang with a Local of a country '" + locale + "'");
        }
        // handle a custom dictionary path
        this.customDictPath = null;
    }

    void set_customDictPath(Path customDictPath) {
        this.customDictPath = customDictPath;
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
        if (this.customDictPath == null) {
            return Path.of(Lang.dictionaryDirectory, this.getCodeLang() + ".txt");
        }
        return this.customDictPath;
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
    public LettersCounter getLettersCount(boolean isSuperScrabble) {
        throw new UnsupportedOperationException(
                "the lang " + this.name() + " haven't implemented getLettersCount");
    }

    /**
     * @return the points for each letters in this lang
     */
    public LettersPoints getLettersPoints(boolean isSuperScrabble) {
        throw new UnsupportedOperationException(
                "the lang " + this.name() + " haven't implemented getLettersPoints");
    }

    @Override
    public String toString() {
        return this.locale.toString();
    }
}
