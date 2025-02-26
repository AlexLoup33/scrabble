package pdp.scrabble.game;

public enum LetterColor {
    EMPTY("", ""),
    CYAN("\u001B[36m", "\u001B[46m"),
    CYAN_LIGHT("\u001B[36m", "\u001B[46m"),
    CYAN_BRIGHT("\u001B[36;1m", "\u001B[46;1m"),

    BLUE("\u001B[34m", "\u001B[44m"),

    PURPLE("\u001B[35m", "\u001B[45m"),

    RED("\u001B[31m", "\u001B[41m"),
    RED_LIGHT("\u001B[31m", "\u001B[41m"),
    RED_BRIGHT("\u001B[31;1m", "\u001B[41;1m");

    private final String textColor;
    private final String backgroundColor;

    LetterColor(String textColor, String backgroundColor) {
        this.textColor = textColor;
        this.backgroundColor = backgroundColor;
    }

    public String getTextColor() {
        return this.textColor;
    }

    public String getBackgroundColor() {
        return this.backgroundColor;
    }
}
