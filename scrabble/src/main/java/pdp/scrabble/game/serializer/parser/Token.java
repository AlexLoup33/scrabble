package pdp.scrabble.game.serializer.parser;

public class Token {

  public String type;
  public Object value;

  public Token(String type, Object value) {
    this.type = type;
    this.value = value;
  }

  public String toString() {
    return "Token{" + this.type + ", " + this.value.toString() + "}";
  }
}
