package pdp.scrabble.game.serializer.parser;

import java.util.function.Predicate;

public class TokenInputStream {

  private Token current;
  private CharInputStream input;

  public TokenInputStream(CharInputStream input) {
    current = null;
    this.input = input;
  }

  private boolean isDigit(char ch) {
    return Character.isDigit(ch);
  }

  private boolean isMoveStart(char ch) {
    return Character.isLowerCase(ch);
  }

  private boolean isIdStart(char ch) {
    return Character.isUpperCase(ch);
  }

  private boolean isId(char ch) {
    return Character.isLetter(ch);
  }

  private boolean isOpChar(char ch) {
    return ch == '=';
  }

  private boolean isPunc(char ch) {
    return ".{}<>[],;".indexOf(ch) >= 0;
  }

  private boolean isWhiteSpace(char ch) {
    return " \t\n".indexOf(ch) >= 0;
  }

  private String readWhile(Predicate<Character> predicate) {
    StringBuilder str = new StringBuilder();
    while (!this.input.eof() && predicate.test(this.input.peek())) str.append(this.input.next());
    return str.toString();
  }

  private Token readNumber() {
    int[] count = {0};
    // int count = 0;
    String number =
        readWhile(
            (ch) -> {
              count[0]++; // Array is a hack to access external var inside of lambda
              return isDigit(ch);
            });
    if (count[0] > 9) return new Token("lng", Long.parseLong(number));
    else return new Token("num", Integer.parseInt(number));
  }

  private Token readNegativeNumber() {
    input.next(); // Skipping the "-"
    int[] count = {0};
    if (!isDigit(input.peek())) input.croak("Unexpected token after negative sign");
    String number =
        readWhile(
            (ch) -> {
              count[0]++; // Array is a hack to access external var inside of lambda
              return isDigit(ch);
            });
    if (count[0] > 9) return new Token("negLng", 0L - Long.parseLong(number));
    else return new Token("negNum", 0 - Integer.parseInt(number));
  }

  private String readEscaped(char end) {
    StringBuilder str = new StringBuilder();
    boolean escaped = false;
    input.next(); // Skipping the first "
    while (!input.eof()) {
      char ch = input.next();
      if (escaped) {
        str.append(ch);
        escaped = false;
      } else if (ch == '\\') {
        escaped = true;
      } else if (ch == end) {
        break;
      } else {
        str.append(ch);
      }
    }
    return str.toString();
  }

  private Token readString() {
    return new Token("str", readEscaped('"'));
  }

  private Token readIdent() {
    String id = readWhile((ch) -> isId(ch));
    return new Token("var", id);
  }

  private Token readMove() {
    char row = input.next();
    if (!isDigit(input.peek())) {
      input.croak("Invalid move");
    }
    String number = readWhile((ch) -> isDigit(ch));
    if (!Character.isLowerCase(input.peek())) {
      input.croak("Invalid move");
    }
    return new Token("move", row + number + this.input.next());
  }

  private void skipComment() {
    readWhile((ch) -> ch != '\n');
    this.input.next();
  }

  private void skipInlineComment() {
    readWhile((ch) -> "}\n".indexOf(ch) < 0);
    this.input.next();
  }

  public Token readNext() throws Error {
    readWhile((ch) -> isWhiteSpace(ch));
    if (input.eof()) return null;
    char ch = this.input.peek();
    if (ch == '#') {
      skipComment();
      return readNext();
    }
    if (ch == '{') {
      skipInlineComment();
      return readNext();
    }
    if (ch == '"') return readString();
    if (ch == '-') return readNegativeNumber();
    if (isDigit(ch)) return readNumber();
    if (isMoveStart(ch)) return readMove();
    if (isIdStart(ch)) return readIdent();
    if (isPunc(ch)) return new Token("punc", this.input.next());
    if (isOpChar(ch)) return new Token("op", this.input.next());
    input.croak("Can't handle character: " + ch);
    return null;
  }

  public Token peek() {
    if (current == null) current = readNext();
    return current;
  }

  public Token next() {
    Token tok = current;
    current = null;
    return (tok != null) ? tok : readNext();
  }

  public void croak(String msg) {
    input.croak(msg);
  }

  public boolean eof() {
    return input.eof();
  }
}
