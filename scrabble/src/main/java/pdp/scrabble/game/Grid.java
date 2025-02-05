package pdp.scrabble.game;

import pdp.scrabble.dictionary.Letter;

import java.util.ArrayList;
import java.util.List;

public class Grid {
    private final int size;
    private final Letter[][] letterMatrix;


    public Grid(int size) {
        this.size = size;

        this.letterMatrix = new Letter[size][size];


        // Fill the map with grass
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                this.letterMatrix[i][j] = null;
            }
        }
    }
}
