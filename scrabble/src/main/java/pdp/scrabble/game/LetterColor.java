package pdp.scrabble.game;

public enum LetterColor {
  EMPTY("", "", ""),
  CYAN("\u001B[36m", "\u001B[46m", "#00FFFF"),
  CYAN_LIGHT("\u001B[36m", "\u001B[46m", "#00FFFF"),
  CYAN_BRIGHT("\u001B[36;1m", "\u001B[46;1m", "#00FFFF"),

  BLUE("\u001B[34m", "\u001B[44m", "#0000FF"),

  PURPLE("\u001B[35m", "\u001B[45m", "#800080"),

  RED("\u001B[31m", "\u001B[41m", "#FF0000"),
  RED_LIGHT("\u001B[31m", "\u001B[41m", "#FF0000"),
  RED_BRIGHT("\u001B[31;1m", "\u001B[41;1m", "#FF0000");

  private final String textColor;
  private final String backgroundColor;
  private final String guiColor;

  LetterColor(String textColor, String backgroundColor, String guiColor) {
    this.textColor = textColor;
    this.backgroundColor = backgroundColor;
    this.guiColor = guiColor;
  }

  public String getTextColor() {
    return this.textColor;
  }

  public String getBackgroundColor() {
    return this.backgroundColor;
  }

  public String getGuiColor() {
    return this.guiColor;
  }
}
