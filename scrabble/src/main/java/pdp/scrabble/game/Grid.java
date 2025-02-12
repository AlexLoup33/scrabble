package pdp.scrabble.game;

import pdp.scrabble.dictionary.IDictionary;
import pdp.scrabble.dictionary.Letter;
import pdp.scrabble.dictionary.Word;
import pdp.scrabble.errorsHandling.CustomError;
import pdp.scrabble.game.actions.PlaceWordAction;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import static pdp.scrabble.errorsHandling.ErrorType.LetterCantBePlacedOnAnotherLetter;
import static pdp.scrabble.errorsHandling.ErrorType.LetterPlacedOutOfRange;

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
                    System.out.print(this.placedLetters[i][j] + " ");
                }
            }
            System.out.println();
        }
    }

    public List<OwnedWord> placeLetters(PlaceWordAction pwa) {
        // todo
        // try to place the letter

        Letter[][] placedLettersCopy = this.placedLetters.clone();
        LinkedList<OwnedWord> placedWordsCopy = (LinkedList<OwnedWord>) this.placedWords.clone();

        try {
            // Direction: down
            if (pwa.startPosition.direction() == Direction.Down) {
                // 1: check if the word fit the grid
                if (pwa.startPosition.row() + pwa.toPlace.length > this.size) {
                    throw new CustomError(
                            LetterPlacedOutOfRange,
                            "LetterPlacedOutOfRange",
                            "Can not place a letter out of the grid");
                }

                // 2 Place letter
                int letter_index = 0;
                int row = pwa.startPosition.row();
                int timeout = 0;
                while (letter_index < pwa.toPlace.length && timeout < 50) {
                    // if there is no other letter
                    if (this.placedLetters[row][pwa.startPosition.col()] == null) {
                        // place letter
                        this.placedLetters[row][pwa.startPosition.col()] = pwa.toPlace[letter_index];
                        letter_index++;
                    }
                    row++;
                    timeout++;
                }
                if (GameModel.dictionary.containsWord(new Word(pwa.toPlace))) {
                    System.out.println("Word in dictionary: ok");
                }

//            this.placedWords.add(new OwnedWord(pwa.startPosition, ));
            }
        } finally {
            this.placedLetters = placedLettersCopy;
        }


        return null;
    }
}