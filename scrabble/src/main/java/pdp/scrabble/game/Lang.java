package pdp.scrabble.game;

import pdp.scrabble.dictionary.LettersCounter;

public enum Lang {
    EN("english", "en", new LettersCounter()),
    FR("français", "fr", new LettersCounter());

    String displayName;
    String codeLang;
    LettersCounter lc;

    Lang(String displayName, String codeLang, LettersCounter lc) {
        this.displayName = displayName;
        this.codeLang = codeLang;
        this.lc = lc;
    }
}
