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

        List<OwnedWord> result = new LinkedList<>();

        // try to place the letter
        // we will try every action, and only if one of the sub-action isn't possible, we roll back
        try {
            // Direction: down
            if (pwa.startPosition.direction() == Direction.Down) {
                // Place letter
                int letter_index = 0;
                int row = pwa.startPosition.row();
                // place every letter on the grid
                while (letter_index < pwa.toPlace.length && row < this.size) {
                    // if there is no other letter
                    if (this.placedLetters[row][pwa.startPosition.col()] == null) {
                        // place letter
                        this.placedLetters[row][pwa.startPosition.col()] = pwa.toPlace[letter_index];
                        letter_index++;
                    }
                    row++;
                }


                // Find the new word created
                String vertical_created_word = getVerticalWord(pwa.startPosition.row(), pwa.startPosition.col(), pwa.startPosition.direction());

//                System.out.println("DEBUG vertical_created_word: " + vertical_created_word);
//                System.out.println("DEBUG contains word: " + GameModel.dictionary.containsWord(new Word(vertical_created_word)));

                if (GameModel.dictionary.containsWord(new Word(vertical_created_word))) {
                    this.placedWords.add(new OwnedWord(pwa.startPosition,
                            new Word(vertical_created_word),
                            pwa.player));
                    result.add(new OwnedWord(pwa.startPosition,
                            new Word(vertical_created_word),
                            pwa.player));
                } else {
                    throw new CustomError(NotAValidWord, "NotAValidWord", "NotAValidWord");
                }

                // Check new horizontal word
                int start = findStart(pwa.startPosition.row(), pwa.startPosition.col(), pwa.startPosition.direction());

//                System.out.println("DEBUG start: " + start);
                while (start < this.size
                        && this.placedLetters[start][pwa.startPosition.col()] != null) {
                    String new_horizontal_word = getHorizontalWord(start, pwa.startPosition.col(), Direction.Right);
//                    System.out.println("DEBUG new_vertical_word: " + new_horizontal_word);
                    if (new_horizontal_word.length() >= 2) {
                        if (GameModel.dictionary.containsWord(new Word(new_horizontal_word))) {
                            this.placedWords.add(new OwnedWord(pwa.startPosition,
                                    new Word(new_horizontal_word),
                                    pwa.player));
                            result.add(new OwnedWord(pwa.startPosition,
                                    new Word(new_horizontal_word),
                                    pwa.player));
                        } else {
                            throw new CustomError(NotAValidWord, "NotAValidWord", "NotAValidWord");
                        }
                    }
                    start++;
                }

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
                String sb = getHorizontalWord(pwa.startPosition.col(), pwa.startPosition.row(), pwa.startPosition.direction());

//                System.out.println("DEBUG vertical formed word: " + sb);
//                System.out.println("DEBUG contains word: " + GameModel.dictionary.containsWord(new Word(sb)));
                if (GameModel.dictionary.containsWord(new Word(sb))) {
                    this.placedWords.add(new OwnedWord(pwa.startPosition,
                            new Word(sb),
                            pwa.player));
                } else {
                    throw new CustomError(NotAValidWord, "NotAValidWord", "NotAValidWord");
                }

                // Check new vertical word
                int start = findStart(pwa.startPosition.row(), pwa.startPosition.col(), pwa.startPosition.direction());

//                System.out.println("DEBUG start: " + start);
                while (start < this.size
                        && this.placedLetters[pwa.startPosition.row()][start] != null) {
                    String new_vertical_word = getVerticalWord(start, pwa.startPosition.col(), Direction.Right);
//                    System.out.println("DEBUG new_vertical_word: " + new_vertical_word);
                    if (new_vertical_word.length() >= 2) {
                        if (GameModel.dictionary.containsWord(new Word(new_vertical_word))) {
                            this.placedWords.add(new OwnedWord(pwa.startPosition,
                                    new Word(new_vertical_word),
                                    pwa.player));
                            result.add(new OwnedWord(pwa.startPosition,
                                    new Word(new_vertical_word),
                                    pwa.player));
                        } else {
                            throw new CustomError(NotAValidWord, "NotAValidWord", "NotAValidWord");
                        }
                    }
                    start++;
                }
            }
            return result;
        } finally {
            this.placedLetters = placedLettersCopy;
            this.placedWords = placedWordsCopy;
        }
    }

    private String getVerticalWord(int row, int col, Direction direction) {
        int start = findStart(row, col, direction);

        StringBuilder sb = new StringBuilder();
        while (start < this.size && this.placedLetters[start][col] != null) {
            sb.append(this.placedLetters[start][col]);
            start++;
        }
        return sb.toString();
    }

    private String getHorizontalWord(int row, int col, Direction direction) {
        int start = findStart(row, col, direction);

        StringBuilder sb = new StringBuilder();
        while (start < this.size
                && this.placedLetters[row][start] != null) {
            sb.append(this.placedLetters[row][start]);
            start++;
        }
        return sb.toString();
    }


    private int findStart(int row, int col, Direction direction) {
        // find start
        int start = 0;
        if (direction == Direction.Down) {
            start = row;
            while (this.placedLetters[start][col] != null
                    && row != 0
                    && start > 0) {
                start--;
            }
            if (start > 0) {
                start++;
            }
            return start;
        } else { // direction == Direction.Right
            start = col;
            while (this.placedLetters[row][start] != null
                    && col != 0
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