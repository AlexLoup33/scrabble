package pdp.scrabble.game;

import static pdp.scrabble.errorsHandling.ErrorType.NotAValidWord;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import pdp.scrabble.dictionary.Letter;
import pdp.scrabble.dictionary.LettersPoints;
import pdp.scrabble.dictionary.Word;
import pdp.scrabble.errorsHandling.CustomError;
import pdp.scrabble.game.actions.PlaceWordAction;

public class Grid {
  public final int size;
  private Letter[][] placedLetters;
  private LinkedList<OwnedWord> placedWords;
  private final Hashtable<GridPos, ScoreMultiplier> multiplicators;
  private final LettersPoints pointsPerLetter;
  // in order to cach the points for the letters
  private final GameModel gameModel;

  public Grid(GameModel gameModel, int size) {
    this.gameModel = gameModel;
    this.size = size;
    this.multiplicators = new Hashtable<>();
    this.placedLetters = new Letter[size][size];
    this.pointsPerLetter = this.gameModel.lang.getLettersPoints();
    this.placedWords = new LinkedList<>();

    // TODO: modifier position des scores multiplicateurs si super-scrabble

    try {
      this.loadMultoplicators("multiplicators_default.conf");
    } catch (IOException e) {
      System.out.println(e);
    }
    /*
    this.multiplicators.put(new GridPos(0, 3), ScoreMultiplier.DOUBLE_LETTER);
    this.multiplicators.put(new GridPos(0, 11), ScoreMultiplier.DOUBLE_LETTER);
    this.multiplicators.put(new GridPos(2, 6), ScoreMultiplier.DOUBLE_LETTER);
    this.multiplicators.put(new GridPos(2, 8), ScoreMultiplier.DOUBLE_LETTER);
    this.multiplicators.put(new GridPos(3, 0), ScoreMultiplier.DOUBLE_LETTER);
    this.multiplicators.put(new GridPos(3, 7), ScoreMultiplier.DOUBLE_LETTER);
    this.multiplicators.put(new GridPos(3, 14), ScoreMultiplier.DOUBLE_LETTER);
    this.multiplicators.put(new GridPos(6, 2), ScoreMultiplier.DOUBLE_LETTER);
    this.multiplicators.put(new GridPos(6, 6), ScoreMultiplier.DOUBLE_LETTER);
    this.multiplicators.put(new GridPos(6, 8), ScoreMultiplier.DOUBLE_LETTER);
    this.multiplicators.put(new GridPos(6, 12), ScoreMultiplier.DOUBLE_LETTER);
    this.multiplicators.put(new GridPos(7, 3), ScoreMultiplier.DOUBLE_LETTER);
    this.multiplicators.put(new GridPos(7, 11), ScoreMultiplier.DOUBLE_LETTER);
    this.multiplicators.put(new GridPos(8, 2), ScoreMultiplier.DOUBLE_LETTER);
    this.multiplicators.put(new GridPos(8, 6), ScoreMultiplier.DOUBLE_LETTER);
    this.multiplicators.put(new GridPos(8, 8), ScoreMultiplier.DOUBLE_LETTER);
    this.multiplicators.put(new GridPos(8, 12), ScoreMultiplier.DOUBLE_LETTER);
    this.multiplicators.put(new GridPos(11, 0), ScoreMultiplier.DOUBLE_LETTER);
    this.multiplicators.put(new GridPos(11, 7), ScoreMultiplier.DOUBLE_LETTER);
    this.multiplicators.put(new GridPos(11, 14), ScoreMultiplier.DOUBLE_LETTER);
    this.multiplicators.put(new GridPos(12, 6), ScoreMultiplier.DOUBLE_LETTER);
    this.multiplicators.put(new GridPos(12, 8), ScoreMultiplier.DOUBLE_LETTER);
    this.multiplicators.put(new GridPos(14, 3), ScoreMultiplier.DOUBLE_LETTER);
    this.multiplicators.put(new GridPos(14, 11), ScoreMultiplier.DOUBLE_LETTER);

    this.multiplicators.put(new GridPos(1, 5), ScoreMultiplier.TRIPLE_LETTER);
    this.multiplicators.put(new GridPos(1, 9), ScoreMultiplier.TRIPLE_LETTER);
    this.multiplicators.put(new GridPos(5, 1), ScoreMultiplier.TRIPLE_LETTER);
    this.multiplicators.put(new GridPos(5, 5), ScoreMultiplier.TRIPLE_LETTER);
    this.multiplicators.put(new GridPos(5, 9), ScoreMultiplier.TRIPLE_LETTER);
    this.multiplicators.put(new GridPos(5, 13), ScoreMultiplier.TRIPLE_LETTER);
    this.multiplicators.put(new GridPos(9, 1), ScoreMultiplier.TRIPLE_LETTER);
    this.multiplicators.put(new GridPos(9, 5), ScoreMultiplier.TRIPLE_LETTER);
    this.multiplicators.put(new GridPos(9, 9), ScoreMultiplier.TRIPLE_LETTER);
    this.multiplicators.put(new GridPos(9, 13), ScoreMultiplier.TRIPLE_LETTER);
    this.multiplicators.put(new GridPos(13, 5), ScoreMultiplier.TRIPLE_LETTER);
    this.multiplicators.put(new GridPos(13, 9), ScoreMultiplier.TRIPLE_LETTER);

    this.multiplicators.put(new GridPos(1, 1), ScoreMultiplier.DOUBLE_WORD);
    this.multiplicators.put(new GridPos(1, 13), ScoreMultiplier.DOUBLE_WORD);
    this.multiplicators.put(new GridPos(2, 2), ScoreMultiplier.DOUBLE_WORD);
    this.multiplicators.put(new GridPos(2, 12), ScoreMultiplier.DOUBLE_WORD);
    this.multiplicators.put(new GridPos(3, 3), ScoreMultiplier.DOUBLE_WORD);
    this.multiplicators.put(new GridPos(3, 11), ScoreMultiplier.DOUBLE_WORD);
    this.multiplicators.put(new GridPos(4, 4), ScoreMultiplier.DOUBLE_WORD);
    this.multiplicators.put(new GridPos(4, 10), ScoreMultiplier.DOUBLE_WORD);
    this.multiplicators.put(new GridPos(7, 7), ScoreMultiplier.DOUBLE_WORD);
    this.multiplicators.put(new GridPos(10, 4), ScoreMultiplier.DOUBLE_WORD);
    this.multiplicators.put(new GridPos(10, 10), ScoreMultiplier.DOUBLE_WORD);
    this.multiplicators.put(new GridPos(11, 3), ScoreMultiplier.DOUBLE_WORD);
    this.multiplicators.put(new GridPos(11, 11), ScoreMultiplier.DOUBLE_WORD);
    this.multiplicators.put(new GridPos(12, 2), ScoreMultiplier.DOUBLE_WORD);
    this.multiplicators.put(new GridPos(12, 12), ScoreMultiplier.DOUBLE_WORD);
    this.multiplicators.put(new GridPos(13, 1), ScoreMultiplier.DOUBLE_WORD);
    this.multiplicators.put(new GridPos(13, 13), ScoreMultiplier.DOUBLE_WORD);

    this.multiplicators.put(new GridPos(0, 0), ScoreMultiplier.TRIPLE_WORD);
    this.multiplicators.put(new GridPos(0, 7), ScoreMultiplier.TRIPLE_WORD);
    this.multiplicators.put(new GridPos(0, 14), ScoreMultiplier.TRIPLE_WORD);
    this.multiplicators.put(new GridPos(7, 0), ScoreMultiplier.TRIPLE_WORD);
    this.multiplicators.put(new GridPos(7, 14), ScoreMultiplier.TRIPLE_WORD);
    this.multiplicators.put(new GridPos(14, 0), ScoreMultiplier.TRIPLE_WORD);
    this.multiplicators.put(new GridPos(14, 7), ScoreMultiplier.TRIPLE_WORD);
    this.multiplicators.put(new GridPos(14, 14), ScoreMultiplier.TRIPLE_WORD);*/
  }

  public void loadMultoplicators(String conf) throws IOException {
    File file = new File(conf);

    Scanner scan = new Scanner(file);
    while (scan.hasNextLine()) {
      String[] mult = scan.nextLine().split("-");

      String[] parts = mult[0].split("\\.");
      int[] p = new int[parts.length];
      for (int i = 0; i < parts.length; i++) {
        p[i] = Integer.parseInt(parts[i]);
      }
      GridPos pos = new GridPos(p[0], p[1]);
      ScoreMultiplier multiplier = ScoreMultiplier.SIMPLE_LETTER;
      switch (mult[1]) {
        case "DOUBLE_LETTER" -> {
          multiplier = ScoreMultiplier.DOUBLE_LETTER;
        }
        case "DOUBLE_WORD" -> {
          multiplier = ScoreMultiplier.DOUBLE_WORD;
        }
        case "TRIPLE_LETTER" -> {
          multiplier = ScoreMultiplier.TRIPLE_LETTER;
        }
        case "TRIPLE_WORD" -> {
          multiplier = ScoreMultiplier.TRIPLE_WORD;
        }
        case "QUADRUPULE_LETTER" -> {
          multiplier = ScoreMultiplier.QUADRUPLE_LETTER;
        }
        case "QUADRUPULE_WORD" -> {
          multiplier = ScoreMultiplier.QUADRUPLE_WORD;
        }
      }
      this.multiplicators.put(pos, multiplier);
    }
    scan.close();
  }

  public Letter getLetter(int row, int col) {
    return this.placedLetters[row][col];
  }

  public List<OwnedWord> placeLetters(PlaceWordAction pwa) {
    Letter[][] placedLettersCopy = this.placedLetters.clone();
    LinkedList<OwnedWord> placedWordsCopy = (LinkedList<OwnedWord>) this.placedWords.clone();

    List<OwnedWord> result = new LinkedList<>();

    // try to place the letter
    // we will try every action, and only if one of the sub-action isn't possible,
    // we roll back
    try {
      // Direction: down
      if (pwa.startPosition.direction() == Direction.Down) {
        // 1 Place letter
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

        // 2 Find the new word created
        String vertical_created_word =
            getVerticalWord(
                pwa.startPosition.row(), pwa.startPosition.col(), pwa.startPosition.direction());
        Word vertical_word = new Word(vertical_created_word);

        // System.out.println("DEBUG vertical_created_word: " +
        // vertical_created_word);
        // System.out.println("DEBUG contains word: " +
        // GameModel.dictionary.containsWord(new Word(vertical_created_word)));

        if (this.gameModel.dictionary.containsWord(vertical_word)) {
          OwnedWord to_add = new OwnedWord(pwa.startPosition, vertical_word, pwa.player);
          this.placedWords.add(to_add);
          result.add(to_add);
        } else {
          throw new CustomError(NotAValidWord, "NotAValidWord", "NotAValidWord");
        }

        // Check new horizontal word
        int start =
            findStart(
                pwa.startPosition.row(), pwa.startPosition.col(), pwa.startPosition.direction());

        // Check for each row if a new word is created
        while (start < this.size && this.placedLetters[start][pwa.startPosition.col()] != null) {
          String new_horizontal_word =
              getHorizontalWord(start, pwa.startPosition.col(), Direction.Right);
          // System.out.println("DEBUG new_vertical_word: " +
          // new_horizontal_word);
          if (new_horizontal_word.length() >= 2) {
            if (this.gameModel.dictionary.containsWord(new Word(new_horizontal_word))) {
              OwnedWord tmp =
                  new OwnedWord(pwa.startPosition, new Word(new_horizontal_word), pwa.player);
              this.placedWords.add(tmp);
              result.add(tmp);
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
        // place every letter on the grid
        while (letter_index < pwa.toPlace.length && col < this.size) {
          // if there is no other letter
          if (this.placedLetters[pwa.startPosition.row()][col] == null) {
            // place letter
            this.placedLetters[pwa.startPosition.row()][col] = pwa.toPlace[letter_index];
            letter_index++;
          }
          col++;
        }

        // 2 Find the new word created
        String horizontal_created_word =
            getHorizontalWord(
                pwa.startPosition.row(), pwa.startPosition.col(), pwa.startPosition.direction());
        Word horizontal_word = new Word(horizontal_created_word);

        // System.out.println("DEBUG vertical formed word: " +
        // horizontal_created_word);
        // System.out.println("DEBUG contains word: " +
        // GameModel.dictionary.containsWord(new Word(horizontal_created_word)));

        if (this.gameModel.dictionary.containsWord(horizontal_word)) {
          OwnedWord to_place = new OwnedWord(pwa.startPosition, horizontal_word, pwa.player);
          this.placedWords.add(to_place);
          result.add(to_place);
        } else {
          throw new CustomError(NotAValidWord, "NotAValidWord", "NotAValidWord");
        }

        // Check new vertical word
        int start =
            findStart(
                pwa.startPosition.row(), pwa.startPosition.col(), pwa.startPosition.direction());

        // Check for each row if a new word is created
        while (start < this.size && this.placedLetters[pwa.startPosition.row()][start] != null) {
          String new_horizontal_word =
              getVerticalWord(pwa.startPosition.row(), start, Direction.Down);
          // System.out.println("DEBUG new_vertical_word: " +
          // new_horizontal_word);
          if (new_horizontal_word.length() >= 2) {
            if (this.gameModel.dictionary.containsWord(new Word(new_horizontal_word))) {
              OwnedWord tmp =
                  new OwnedWord(pwa.startPosition, new Word(new_horizontal_word), pwa.player);
              this.placedWords.add(tmp);
              result.add(tmp);
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

  private String getVerticalWord(int row, int col_inv, Direction direction) {
    int start = findStart(row, col_inv, direction);

    StringBuilder sb = new StringBuilder();
    while (start < this.size && this.placedLetters[start][col_inv] != null) {
      sb.append(this.placedLetters[start][col_inv]);
      start++;
    }
    return sb.toString();
  }

  private String getHorizontalWord(int row_inv, int col, Direction direction) {
    int start = findStart(row_inv, col, direction);

    StringBuilder sb = new StringBuilder();
    while (start < this.size && this.placedLetters[row_inv][start] != null) {
      sb.append(this.placedLetters[row_inv][start]);
      start++;
    }
    return sb.toString();
  }

  private int findStart(int row, int col, Direction direction) {
    // find start
    int start;
    if (direction == Direction.Down) {
      start = row;
      while (this.placedLetters[start][col] != null && row != 0 && start > 0) {
        start--;
      }
      if (start > 0) {
        start++;
      }
      return start;
    } else { // direction == Direction.Right
      start = col;
      while (this.placedLetters[row][start] != null && col != 0 && start > 0) {
        start--;
      }
      if (start > 0) {
        start++;
      }
      return start;
    }
  }

  private int getLetterPoints(Letter letter) {
    return this.pointsPerLetter.getLetterCount(letter);
  }

  /**
   * The function `getMultiplier` returns the ScoreMultiplier associated with a given GridPos or a
   * default value if none is found.
   *
   * @param pos The `pos` parameter in the `getMultiplier` method represents the position on the
   *     grid for which you want to retrieve the score multiplier. It is of type `GridPos`, which
   *     likely contains the coordinates or location information necessary to identify a specific
   *     cell or tile on the game grid.
   * @return The method `getMultiplier` is returning a `ScoreMultiplier` object associated with the
   *     given `GridPos` `pos`. If there is no mapping found for the `pos`, it will return the
   *     default `ScoreMultiplier` object `SIMPLE_LETTER`.
   */
  public ScoreMultiplier getMultiplier(GridPos pos) {
    return this.multiplicators.getOrDefault(pos, ScoreMultiplier.SIMPLE_LETTER);
  }

  /**
   * This function calculates the score of a word on a grid, taking into account any score
   * multipliers.
   *
   * @param word OwnedWord - a class representing a word that is owned by a player in a game. It
   *     contains information about the word itself and its position on the game board.
   * @return The method is returning the product of the `score` and `multi_word` variables. However,
   *     the `score` variable is never updated within the method, so it will always be 0. Therefore,
   *     the method will always return 0.
   */
  public int getScore(OwnedWord word) {
    int score = 0;
    int multi_word = 1;

    for (int i = 0; i < word.word.getLength(); i++) {
      GridPos pos = word.position.getPosAtIndex(i);
      ScoreMultiplier multi = this.getMultiplier(pos);
      int letterMult = 1;
      if (!multi.isLetterMultiplier()) {
        multi_word *= multi.getMultiplier();
      } else {
        // => is a letter mult
        letterMult = multi.getMultiplier();
      }
      Letter letter = word.word.getLetterAt(i);
      int lettreBasePoints = this.getLetterPoints(letter);
      score += (lettreBasePoints * letterMult);
    }

    return score * multi_word;
  }

  /**
   * The `getScore` function calculates the total score of a player based on the words they own in a
   * game.
   *
   * @param player The `player` parameter in the `getScore` method represents the player for whom
   *     you want to calculate the score. The method iterates through the `placedWords` and checks
   *     if the owner of each word is the same as the provided player. If it is, the score of that
   *     word is
   * @return The `getScore` method is returning the total score of the player based on the words
   *     they own in the `placedWords` list.
   */
  public int getScore(Player player) {
    int score = 0;
    for (OwnedWord word : this.placedWords) {
      if (word.owner.equals(player)) {
        score += this.getScore(word);
      }
    }
    return score;
  }

  /**
   * The `applyGameAction` function in Java applies a `PlaceWordAction` by calling the
   * `placeLetters` method.
   *
   * @param action The parameter `action` in the `applyGameAction` method is of type
   *     `PlaceWordAction`. This parameter likely contains information about a word placement action
   *     in a game, such as the word to be placed and the location where it should be placed.
   */
  public void applyGameAction(PlaceWordAction action) {
    this.placeLetters(action);
  }
}
