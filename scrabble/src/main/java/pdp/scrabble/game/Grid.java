package pdp.scrabble.game;

import pdp.scrabble.dictionary.Letter;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

public class Grid {
    public final int size;
    private final Letter[][] placedLetters;
    private final Hashtable<GridPos, ScoreMultiplier> multiplicators = new Hashtable<>();
    private LinkedList<OwnedWord> placedWords = new LinkedList<>();

    public Grid(int size) {
        this.size = size;
        this.placedLetters = new Letter[size][size];
    }

    public void printGrid() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (this.placedLetters[i][j] == null) {
                    System.out.print("❎ ");
                } else {
                    System.out.print(this.placedLetters[i][j] + " ");
                }
            }
            System.out.println();
        }
    }

    public List<OwnedWord> placeLetters(List<Letter> letter, GridWordPos gwp) {
        // todo
        return null;
    }


}