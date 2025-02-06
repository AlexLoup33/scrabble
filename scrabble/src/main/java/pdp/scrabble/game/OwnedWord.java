package pdp.scrabble.game;

import pdp.scrabble.dictionary.Word;

public class OwnedWord {
    public final GridWordPos position;
    public final Word word;
    public final Player owner;

    public OwnedWord(GridWordPos position, Word word, Player owner) {
        this.position = position;
        this.word = word;
        this.owner = owner;
    }
}
