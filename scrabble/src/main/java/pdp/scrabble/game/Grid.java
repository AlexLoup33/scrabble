package pdp.scrabble.game;

import pdp.scrabble.dictionary.Letter;

public class Grid {
    public final int size;
    private final Letter[][] placedLetters;
    public final ScoreMultiplier[][] scoreMatrix;

    public Grid(int size) {
        this.size = size;

        this.placedLetters = new Letter[size][size];
        this.scoreMatrix = new ScoreMultiplier[size][size];

        // Fill the grid with a null letter
//        for (int i = 0; i < size; i++) {
//            for (int j = 0; j < size; j++) {
//                this.placedLetters[i][j] = null;
//                if (i == j) {
//                    this.scoreMatrix[i][j] = new ScoreMultiplier(2, false);
//                }
//                if (i % 7 == 0 && j % 7 == 0) {
//                    this.scoreMatrix[i][j] = new ScoreMultiplier(3, false);
//                }
//            }
//        }
    }

    public void printGrid() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(this.placedLetters[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void printScoreMatrix() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (this.scoreMatrix[i][j] == null) {
                    System.out.print("   ");
                } else {
                    System.out.print(this.scoreMatrix[i][j] + " ");
                }
            }
            System.out.println();
        }
    }

}