package pdp.scrabble.game;

import pdp.scrabble.dictionary.Letter;
import pdp.scrabble.errorsHandling.CustomError;
import pdp.scrabble.errorsHandling.ErrorType;
import pdp.scrabble.game.actions.PlaceWordAction;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import static pdp.scrabble.errorsHandling.ErrorType.LetterCantBePlacedOnAnOtherLetter;
import static pdp.scrabble.errorsHandling.ErrorType.LetterPlacedOutOfRange;

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

    public List<OwnedWord> placeLetters(PlaceWordAction pwa) {
        // todo
        // try to place the letter

        Letter[][] gridCopy = this.placedLetters.clone();


        if (pwa.startPosition.direction() == Direction.Down) {
            if (pwa.startPosition.row() + pwa.toPlace.length > this.size) {
                throw new CustomError(
                        LetterPlacedOutOfRange,
                        "Index out of range",
                        "Can not place a letter out of the grid");
            }

            int letter_index = 0;
            for (int row = pwa.startPosition.row();
                 row < pwa.startPosition.row() + pwa.toPlace.length;
                 row++) {

                if (this.placedLetters[row][pwa.startPosition.col()] != null &&
                        this.placedLetters[row][pwa.startPosition.col()] != pwa.toPlace[letter_index]) {
                    // if there is already another letter and this letter isn't the same
                    throw new CustomError(LetterCantBePlacedOnAnOtherLetter,
                            "LetterCantBePlacedOnAnOtherLetter",
                            "LetterCantBePlacedOnAnOtherLetter");
                }

                this.placedLetters[row][pwa.startPosition.col()] = pwa.toPlace[letter_index];
                letter_index++;
            }


//            this.placedWords.add(new OwnedWord(pwa.startPosition, ));
        }


        return null;
    }


}