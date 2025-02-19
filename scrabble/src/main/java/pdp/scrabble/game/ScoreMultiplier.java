package pdp.scrabble.game;

public enum ScoreMultiplier {
  SIMPLE_LETTER(1, true, LetterColor.EMPTY, LetterColor.EMPTY),
  DOUBLE_LETTER(2, true, LetterColor.CYAN, LetterColor.CYAN_LIGHT),
  TRIPLE_LETTER(3, true, LetterColor.BLUE, LetterColor.CYAN_BRIGHT),
  QUADRUPLE_LETTER(4, true, LetterColor.EMPTY, LetterColor.PURPLE),
  DOUBLE_WORD(2, false, LetterColor.RED, LetterColor.RED_LIGHT),
  TRIPLE_WORD(3, false, LetterColor.PURPLE, LetterColor.RED_BRIGHT),
  QUADRUPLE_WORD(4, false, LetterColor.EMPTY, LetterColor.PURPLE);

  private final int multi;
  private final boolean letterMultiplier;
  private final LetterColor defaultColor;
  private final LetterColor superScrabbleColor;

  ScoreMultiplier(int multi, boolean letterMultiplier, LetterColor defaultColor, LetterColor superScrabbleColor) {
    this.multi = multi;
    this.letterMultiplier = letterMultiplier;
    this.defaultColor = defaultColor;
    this.superScrabbleColor = superScrabbleColor;
  }

  public int getMultiplier() {
    return this.multi;
  }

  public boolean isLetterMultiplier() {
    return this.letterMultiplier;
  }

  public LetterColor getLetterColor(Boolean isSuperScrabble){
    if (isSuperScrabble)
      return this.superScrabbleColor;
    return this.defaultColor;
  }
}
