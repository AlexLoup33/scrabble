package pdp.scrabble.game;

public class ScoreMultiplier {
    public final int multi;
    public final boolean isLetterMultiplier;


    public ScoreMultiplier(int multi, boolean isLetterMultiplier) {
        this.multi = multi;
        this.isLetterMultiplier = isLetterMultiplier;
    }
}
