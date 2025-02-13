package pdp.scrabble.game;

import pdp.scrabble.dictionary.Letter;
import pdp.scrabble.dictionary.Word;
import pdp.scrabble.errorsHandling.CustomError;
import pdp.scrabble.game.actions.PlaceWordAction;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import static pdp.scrabble.errorsHandling.ErrorType.*;

public class Grid {
    public final int size;
    private Letter[][] placedLetters;
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
                    System.out.print(this.placedLetters[i][j] + "  ");
                }
            }
            System.out.println();
        }
    }

    public List<OwnedWord> placeLetters(PlaceWordAction pwa) {
        Letter[][] placedLettersCopy = this.placedLetters.clone();
        LinkedList<OwnedWord> placedWordsCopy = (LinkedList<OwnedWord>) this.placedWords.clone();

        // try to place the letter
        // we will try every action, and only if one of the sub-action isn't possible, we roll back
        try {
            // Direction: down
            if (pwa.startPosition.direction() == Direction.Down) {
                // 1 Place letter
                int letter_index = 0;
                int row = pwa.startPosition.row();
                while (letter_index < pwa.toPlace.length && row < this.size) {
                    // if there is no other letter
                    if (this.placedLetters[row][pwa.startPosition.col()] == null) {
                        // place letter
                        this.placedLetters[row][pwa.startPosition.col()] = pwa.toPlace[letter_index];
                        letter_index++;
                    }
                    row++;
                }


                // Find the start index of the new word created
                int start = findStart(pwa);

                StringBuilder sb = new StringBuilder();
                while (start < this.size && this.placedLetters[start][pwa.startPosition.col()] != null) {
                    sb.append(this.placedLetters[start][pwa.startPosition.col()]);
                    start++;
                }

//                System.out.println("DEBUG vertical formed word: " + sb.toString());
//                System.out.println("DEBUG contains word: " +GameModel.dictionary.containsWord(new Word(sb.toString())));
                if (GameModel.dictionary.containsWord(new Word(sb.toString()))) {
                    this.placedWords.add(new OwnedWord(pwa.startPosition,
                            new Word(sb.toString()),
                            pwa.player));
                } else {
                    throw new CustomError(NotAValidWord, "NotAValidWord", "NotAValidWord");
                }

                // Check new horizontal word

            }
            // Direction: right
            else {
                // 1 Place letter
                int letter_index = 0;
                int col = pwa.startPosition.col();
                while (letter_index < pwa.toPlace.length && col < this.size) {
                    // if there is no other letter
                    if (this.placedLetters[pwa.startPosition.row()][col] == null) {
                        // place letter
                        this.placedLetters[pwa.startPosition.row()][col] = pwa.toPlace[letter_index];
                        letter_index++;
                    }
                    col++;
                }

                // Find the start index of the new word created
                String sb = getHorizontalWord(pwa, pwa.startPosition.row());

                System.out.println("DEBUG vertical formed word: " + sb);
                System.out.println("DEBUG contains word: " + GameModel.dictionary.containsWord(new Word(sb)));
                if (GameModel.dictionary.containsWord(new Word(sb))) {
                    this.placedWords.add(new OwnedWord(pwa.startPosition,
                            new Word(sb),
                            pwa.player));
                } else {
                    throw new CustomError(NotAValidWord, "NotAValidWord", "NotAValidWord");
                }
            }
        } finally {
            this.placedLetters = placedLettersCopy;
            this.placedWords = placedWordsCopy;
        }
        return null;
    }

    private String getHorizontalWord(PlaceWordAction pwa, int row) {
        int start = findStart(pwa);

        StringBuilder sb = new StringBuilder();
        while (start < this.size
                && this.placedLetters[row][start] != null) {
            sb.append(this.placedLetters[row][start]);
            start++;
        }
        return sb.toString();
    }

    private int findStart(PlaceWordAction pwa) {
        // find start
        int start = 0;
        if (pwa.startPosition.direction() == Direction.Down) {
            start = pwa.startPosition.row();
            while (this.placedLetters[start][pwa.startPosition.col()] != null
                    && pwa.startPosition.row() != 0
                    && start > 0) {
                start--;
            }
            if (start > 0) {
                start++;
            }
            return start;
        } else { // direction == Direction.Right
            start = pwa.startPosition.col();
            while (this.placedLetters[pwa.startPosition.row()][start] != null
                    && pwa.startPosition.col() != 0
                    && start > 0) {
                start--;
            }
            if (start > 0) {
                start++;
            }
            return start;
        }

    }
}