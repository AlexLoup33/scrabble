package pdp.scrabble.game;

import pdp.scrabble.dictionary.Letter;
import pdp.scrabble.dictionary.Word;

import java.util.LinkedList;

public class OwnedWord {
    public final GridWordPos position;
    public final Word word;
    public final Player owner;

    public OwnedWord(GridWordPos position, Word word, Player owner) {
        this.position = position;
        this.word = word;
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "OwnedWord{" + "position=" + position + ", word=" + word + ", owner=" + owner + '}';
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

//    public GridPos getPosAtWordIndex(int index) {
////      ca renvoi la position de la lettre a l'index donné
//        Letter l;
//        GridPos result;
//
//        for (int i = 0; i < this.word.getLength(); i++) {
//            l = this.word.getLetterAt(i);
//            if (this.position.direction() == Direction.Down) {
//                while () {
//                    getPosAtWordIndex(index + 1);
//                }
//            } else {
//
//            }
//        }
//        return result;
//    }

//for each letter of the word
    // for direction:
    //            while there is a letter at startPos.getPosAtWordIndex(index):
    //              index ++
// => index

}
