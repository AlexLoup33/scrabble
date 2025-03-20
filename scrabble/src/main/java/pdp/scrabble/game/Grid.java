package pdp.scrabble.game;

import java.io.File;
import java.io.IOException;
import java.util.*;
import pdp.scrabble.dictionary.Letter;
import pdp.scrabble.dictionary.LettersPoints;
import pdp.scrabble.dictionary.Word;
import pdp.scrabble.errorsHandling.CustomError;
import pdp.scrabble.errorsHandling.ErrorType;
import pdp.scrabble.game.actions.PlaceWordAction;

public class Grid {
  public final int size;
  private Letter[][] placedLetters;
  private final Hashtable<GridPos, ScoreMultiplier> multiplicators;
  private final boolean[][] multiplicatorsUsed;
  private final LettersPoints pointsPerLetter;
  // in order to cach the points for the letters
  private final GameModel gameModel;

  public Grid(GameModel gameModel, int size) {
    this.gameModel = gameModel;
    this.size = size;
    this.multiplicators = new Hashtable<>();
    this.placedLetters = new Letter[size][size];
    this.multiplicatorsUsed = new boolean[size][size];
    this.pointsPerLetter = this.gameModel.lang.getLettersPoints();

    try {
      this.loadMultoplicators("multiplicators_default.conf");
    } catch (IOException e) {
      System.out.println(e);
    }
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

  public boolean isInBounds(GridPos pos) {
    return (pos.col() >= 0 && pos.col() < this.size) && (pos.row() >= 0 && pos.row() < this.size);
  }

  private GridPos assertInBound(GridPos pos) {
    if (!this.isInBounds(pos)) {
      throw new CustomError(
          ErrorType.PositionOutOfGird,
          "position out of bounds",
          "position: " + pos + " is out of the grid");
    }
    return pos;
  }

  /**
   * Gets the next position in the grid based on the current position and
   * direction.
   *
   * @param currentPos The current position in the grid, must not be null.
   * @param direction  The direction to move, must be a valid word direction.
   * @return A new GridPos representing the next position if the move is valid.
   * @throws CustomError If the next position is out of bounds.
   */
  private GridPos getNextPos(GridPos currentPos, Direction direction) {
    if (direction == Direction.Down) {
      return assertInBound(new GridPos(currentPos.row() + 1, currentPos.col()));
    } else if (direction == Direction.Right) {
      return assertInBound(new GridPos(currentPos.row(), currentPos.col() + 1));
    } else {
      throw new CustomError(
          ErrorType.PositionOutOfGird,
          "weird direction for a word",
          "invalide word direction: " + direction);
    }
  }

  /**
   * Gets the previous position in the grid based on the current position and
   * direction.
   *
   * @param currentPos The current position in the grid, must not be null.
   * @param direction  The of the word (will move the opposit of direction), must
   *                   be a valid word
   *                   direction.
   * @return A new GridPos representing the previous position if the move is
   *         valid.
   * @throws CustomError If the next position is out of bounds.
   */
  private GridPos getPrevPos(GridPos currentPos, Direction direction) {
    if (direction == Direction.Down) {
      return assertInBound(new GridPos(currentPos.row() - 1, currentPos.col()));
    } else if (direction == Direction.Right) {
      return assertInBound(new GridPos(currentPos.row(), currentPos.col() - 1));
    } else {
      throw new CustomError(
          ErrorType.InvalidWordDirection,
          "weird direction for a word",
          "invalide word direction: " + direction);
    }
  }

  /**
   * @param currPos is the position of the letter to be placed
   * @return the position of the first letter of the word before the letter to
   *         place currPos will be
   *         returned if there is no word before
   */
  private GridPos getWordFirstLetterPos(final GridPos currPos, Direction wordDir) {
    GridPos lastWordPos = currPos;
    GridPos prevPos = lastWordPos;
    do {
      // => the prevPos is on the word
      lastWordPos = prevPos;
      // determine next prevPos
      try {
        prevPos = this.getPrevPos(lastWordPos, wordDir);
      } catch (CustomError e) {
        e.handleIfErrorType(ErrorType.PositionOutOfGird);
        // => there is no prevPos
        return lastWordPos;
      }
      // => prevPos got updated
    } while (this.placedLetters[prevPos.row()][prevPos.col()] != null);
    return lastWordPos;
  }

  /**
   * @param currPos is the position of the letter to be placed
   * @return the position of the last letter of the word after the letter to place
   *         currPos will be
   *         returned if there is no word after
   */
  private GridPos getWordLastLetterPos(final GridPos currPos, Direction wordDir) {
    GridPos lastWordPos = currPos;
    GridPos nextPos = lastWordPos;
    do {
      // => the nextPos is on the word
      lastWordPos = nextPos;
      // determine next nextPos
      try {
        nextPos = this.getNextPos(lastWordPos, wordDir);
      } catch (CustomError e) {
        e.handleIfErrorType(ErrorType.PositionOutOfGird);
        // => there is no nextPos
        return lastWordPos;
      }
      // => prevPos got updated
    } while (this.placedLetters[nextPos.row()][nextPos.col()] != null);
    return lastWordPos;
  }

  private OwnedWord constructMainNewWord(
      final GridPos firstPos,
      final GridPos lastPos,
      final List<GridPos> lettersPlaced,
      PlaceWordAction pwa) {
    final LinkedList<Letter> newWord = new LinkedList<>();
    final Direction wordDir = pwa.startPosition.direction();
    int indexNextNewLetter = 0;
    for (GridPos letterPos : firstPos.getPath(lastPos)) {
      Letter letter = this.placedLetters[letterPos.row()][letterPos.col()];
      if (letter == null) {
        assert indexNextNewLetter < pwa.toPlace.length : "has alredy put all the new letters";
        letter = pwa.toPlace[indexNextNewLetter];
        indexNextNewLetter++;
      }
      newWord.add(letter);
    }
    // construct the word
    Word w = new Word(newWord.toArray(new Letter[0]));
    GridWordPos gwp = new GridWordPos(firstPos.row(), firstPos.col(), wordDir);
    return new OwnedWord(gwp, w, pwa.player);
  }

  private OwnedWord constructAuxNewWord(
      final GridPos firstPos, final GridPos lastPos, Letter newLetter, PlaceWordAction pwa) {
    final LinkedList<Letter> newWord = new LinkedList<>();
    final Direction wordDir = pwa.startPosition.direction().getOppositDirection();
    boolean hasPlacedNewLetter = false;
    for (GridPos letterPos : firstPos.getPath(lastPos)) {
      Letter letter = this.placedLetters[letterPos.row()][letterPos.col()];
      if (letter == null) {
        assert !hasPlacedNewLetter : "has alredy put the new letter";
        letter = newLetter;
        hasPlacedNewLetter = true;
      }
      newWord.add(letter);
    }
    // construct the word
    Word w = new Word(newWord.toArray(new Letter[0]));
    GridWordPos gwp = new GridWordPos(firstPos.row(), firstPos.col(), wordDir);
    return new OwnedWord(gwp, w, pwa.player);
  }

  public List<OwnedWord> placeLetters(PlaceWordAction pwa) {
    if (pwa.toPlace.length == 0) {
      throw new CustomError(
          ErrorType.UnknownWord, "can't place no letters", "trying to place 0 letters");
    }
    if (!this.isInBounds(pwa.startPosition.toGridPos())) {
      throw new CustomError(
          ErrorType.InvalidePlaceLetterAction,
          "can't place the letters out of the grid",
          "cant execute a placeWordAction that starts out of the grid: " + pwa);
    }
    {
      GridPos startPos = pwa.startPosition.toGridPos();
      if (this.placedLetters[startPos.row()][startPos.col()] != null) {
        throw new CustomError(
            ErrorType.InvalidePlaceLetterAction,
            "there is alredy a letter here",
            "a placeWordAction must start on an empty spot: " + pwa);
      }
    }

    final LinkedList<OwnedWord> result = new LinkedList<>();
    final Direction wordDirection = pwa.startPosition.direction();
    final Direction wordOppositDirection = wordDirection.getOppositDirection();

    // ** determine the position where the letters will be placed
    final ArrayList<GridPos> lettersPos = new ArrayList<>();
    // index -> pos of the letter of the same index in pwa
    {
      GridPos currPos = pwa.startPosition.toGridPos();
      for (int index = 0; index < pwa.toPlace.length; index++) {
        final boolean islastLetterOfWord = (index + 1 == pwa.toPlace.length);
        // => currPos is the pos of the letter at index
        lettersPos.add(currPos);
        if (!islastLetterOfWord) {
          // => not the last letter to place, search the pos of next letter
          currPos = getWordLastLetterPos(currPos, wordDirection);
          // => currPos is one before next position
          try {
            currPos = this.getNextPos(currPos, wordDirection);
          } catch (CustomError e) {
            e.handleIfErrorType(ErrorType.PositionOutOfGird);
            throw new CustomError(
                ErrorType.InvalidePlaceLetterAction,
                "can't place the letters on the grid",
                "reached the end of the grid while trying to execute: " + pwa);
          }
          // => currPos is on the next empty case to place a letter
        }
      }
    }

    // ** determine all the words constructed
    {
      // the main word placed on the grid
      final GridPos wordStartPos = this.getWordFirstLetterPos(lettersPos.get(0), wordDirection);
      final GridPos wordLastPos = this.getWordLastLetterPos(lettersPos.get(lettersPos.size() - 1), wordDirection);
      result.add(this.constructMainNewWord(wordStartPos, wordLastPos, lettersPos, pwa));
    }
    for (int indexNextNewLetter = 0; indexNextNewLetter < pwa.toPlace.length; indexNextNewLetter++) {
      // the auxiliary word placed on the grid
      final GridPos letterPos = lettersPos.get(indexNextNewLetter);
      final Letter letter = pwa.toPlace[indexNextNewLetter];
      final GridPos wordStartPos = this.getWordFirstLetterPos(letterPos, wordOppositDirection);
      final GridPos wordLastPos = this.getWordLastLetterPos(letterPos, wordOppositDirection);
      if (wordStartPos == wordLastPos) {
        continue; // => not a new word
      }
      result.add(this.constructAuxNewWord(wordStartPos, wordLastPos, letter, pwa));
    }

    // ** check the new words all are in the dictionary
    for (Word newWord : result.stream().map(word -> word.word).toList()) {
      if (!this.gameModel.dictionary.containsWord(newWord)) {
        throw new CustomError(
            ErrorType.UnknownWord,
            "word not in the dictionary",
            "the word: '" + newWord + "' isn't in the dictionary");
      }
    }

    /*
    int score_tot = 0;
    boolean playCenter = false;
    for (Player onGames : this.gameModel.players) {
      score_tot += onGames.getScore();
    }
    if (score_tot == 0 && this.gameModel.getTurn() == 0) {
      for (int index = 0; index < pwa.toPlace.length; index++) {
        final GridPos currPos = lettersPos.get(index);
        if (currPos.row() == 8 && currPos.col() == 8) {
          playCenter = true;
        }
      }
      if (playCenter == false) {
        throw new CustomError(
            ErrorType.FirstMoveIllegal,
            "Not the right move for the first move",
            "the first move: '" + score_tot + " not play to the center (need to touch h8)");
      }
    }
    */

    // ** place the letters on the grid
    for (int index = 0; index < pwa.toPlace.length; index++) {
      final GridPos currPos = lettersPos.get(index);
      this.placedLetters[currPos.row()][currPos.col()] = pwa.toPlace[index];
    }

    return result;
  }

  private int getLetterPoints(Letter letter) {
    return this.pointsPerLetter.getLetterCount(letter);
  }

  /**
   * The function `getMultiplier` returns the ScoreMultiplier associated with a
   * given GridPos or a
   * default value if none is found.
   *
   * @param pos The `pos` parameter in the `getMultiplier` method represents the
   *            position on the
   *            grid for which you want to retrieve the score multiplier. It is of
   *            type `GridPos`, which
   *            likely contains the coordinates or location information necessary
   *            to identify a specific
   *            cell or tile on the game grid.
   * @return The method `getMultiplier` is returning a `ScoreMultiplier` object
   *         associated with the
   *         given `GridPos` `pos`. If there is no mapping found for the `pos`, it
   *         will return the
   *         default `ScoreMultiplier` object `SIMPLE_LETTER`.
   */
  public ScoreMultiplier getMultiplier(GridPos pos) {
    if (this.multiplicatorsUsed[pos.row()][pos.col()]) {
      return ScoreMultiplier.SIMPLE_LETTER;
    }
    return this.multiplicators.getOrDefault(pos, ScoreMultiplier.SIMPLE_LETTER);
  }

  /**
   * This function calculates the score of a word on a grid, taking into account
   * any score
   * multipliers.
   *
   * @param word OwnedWord - a class representing a word that is owned by a player
   *             in a game. It
   *             contains information about the word itself and its position on
   *             the game board.
   * @return The method is returning the product of the `score` and `multi_word`
   *         variables. However,
   *         the `score` variable is never updated within the method, so it will
   *         always be 0. Therefore,
   *         the method will always return 0.
   */
  public int getScore(OwnedWord word) {
    int score = 0;
    int multi_word = 1;
    // TODO: ajouter le suport des mots scrables (7+ letters)
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
      /*
       * Utilities.verbose(
       * "adding points for letter: "
       * + letter + " at " + pos + " basePoints=" + lettreBasePoints
       * + " and with letterMult=" + letterMult +
       * " (multi_word=" + multi_word + ")");
       */
      score += (lettreBasePoints * letterMult);
    }
    // Utilities.verbose("-> score=" + score + ", multi_word=" + multi_word + ")");
    return score * multi_word;
  }

  /**
   * set the multiplicators that has been used with the following words
   *
   * @param wordsPlaced all the words placed this turn (probably overkill but
   *                    safe)
   */
  public void useMultiplicators(List<OwnedWord> wordsPlaced) {
    for (OwnedWord word : wordsPlaced) {
      for (int i = 0; i < word.word.getLength(); i++) {
        GridPos pos = word.position.getPosAtIndex(i);
        this.multiplicatorsUsed[pos.row()][pos.col()] = true;
      }
    }
  }

  public void reset() {
    for (int i = 0; i < this.size; i++) {
      for (int j = 0; j < this.size; j++) {
        this.placedLetters[i][j] = null;
        this.multiplicatorsUsed[i][j] = false;
      }
    }
  }

  public int getGridSize() {
    return this.size;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < this.size; i++) {
      for (int j = 0; j < this.size; j++) {
        if (this.placedLetters[i][j] != null) {
          sb.append(this.placedLetters[i][j].toString() + " ");
        } else {
          sb.append("_ ");
        }
      }
      sb.append("\n");
    }

    return sb.toString();
  }
}
