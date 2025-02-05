package pdp.scrabble.game;

import pdp.scrabble.dictionary.Letter;

public class Grid {
    public final int size;
    public final Letter[][] letterMatrix;

    public Grid(int size) {
        this.size = size;

        this.letterMatrix = new Letter[size][size];

        // Fill the grid with a null letter
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                this.letterMatrix[i][j] = null;
            }
        }
    }
}
