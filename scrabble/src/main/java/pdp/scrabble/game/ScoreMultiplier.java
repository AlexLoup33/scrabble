package pdp.scrabble.game;

/*associate the scoremultiplier with the bonus of a case */
public enum ScoreMultiplier {
  SIMPLE_LETTER(
      1, true, LetterColor.EMPTY, LetterColor.EMPTY, LetterColor.EMPTY, LetterColor.EMPTY),
  DOUBLE_LETTER(
      2, true, LetterColor.CYAN, LetterColor.CYAN_LIGHT, LetterColor.CYAN, LetterColor.CYAN_LIGHT),
  TRIPLE_LETTER(
      3,
      true,
      LetterColor.BLUE,
      LetterColor.CYAN_BRIGHT,
      LetterColor.BLUE,
      LetterColor.CYAN_BRIGHT),
  QUADRUPLE_LETTER(
      4, true, LetterColor.EMPTY, LetterColor.PURPLE, LetterColor.EMPTY, LetterColor.PURPLE),
  DOUBLE_WORD(
      2, false, LetterColor.RED, LetterColor.RED_LIGHT, LetterColor.RED, LetterColor.RED_LIGHT),
  TRIPLE_WORD(
      3,
      false,
      LetterColor.PURPLE,
      LetterColor.RED_BRIGHT,
      LetterColor.PURPLE,
      LetterColor.RED_BRIGHT),
  QUADRUPLE_WORD(
      4, false, LetterColor.EMPTY, LetterColor.PURPLE, LetterColor.EMPTY, LetterColor.PURPLE);

  private final int multi;
  private final boolean letterMultiplier;
  private final LetterColor defaultColor;
  private final LetterColor superScrabbleColor;
  private final LetterColor guiColor;
  private final LetterColor superScrabbleGuiColor;

  ScoreMultiplier(
      int multi,
      boolean letterMultiplier,
      LetterColor defaultColor,
      LetterColor superScrabbleColor,
      LetterColor guiColor,
      LetterColor superScrabbleGuiColor) {
    this.multi = multi;
    this.letterMultiplier = letterMultiplier;
    this.defaultColor = defaultColor;
    this.superScrabbleColor = superScrabbleColor;
    this.guiColor = guiColor;
    this.superScrabbleGuiColor = superScrabbleGuiColor;
  }

  public int getMultiplier() {
    return this.multi;
  }

  public boolean isLetterMultiplier() {
    return this.letterMultiplier;
  }

  public LetterColor getLetterColor(Boolean isSuperScrabble, Boolean isGui) {
    if (isGui) {
      if (isSuperScrabble) {
        return this.superScrabbleGuiColor;
      }
      return this.guiColor;
    } else {
      if (isSuperScrabble) {
        return this.superScrabbleColor;
      }
      return this.defaultColor;
    }
  }
}
