package pdp.scrabble.game;

public enum ScoreMultiplier {
    DOUBLE_LETTER(2, true),
    TRIPLE_LETTER(3, true),
    DOUBLE_WORD(2, false),
    TRIPLE_WORD(3, false);

    private final int multi;
    private final boolean letterMultiplier;

    ScoreMultiplier(int multi, boolean letterMultiplier) {
        this.multi = multi;
        this.letterMultiplier = letterMultiplier;
    }

    public int getMulti() {
        return multi;
    }

    public boolean isLetterMultiplier() {
        return letterMultiplier;
    }
}

