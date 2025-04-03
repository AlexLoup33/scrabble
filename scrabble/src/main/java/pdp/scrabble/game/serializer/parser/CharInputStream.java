package pdp.scrabble.game.serializer.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import pdp.scrabble.errorsHandling.CustomError;
import pdp.scrabble.errorsHandling.ErrorType;

public class CharInputStream {

  private BufferedReader input;
  private int line;
  private int col;

  public CharInputStream(Path inputPath) {
    try {
      this.input = Files.newBufferedReader(inputPath);
    } catch (IOException e) {
      throw new CustomError(ErrorType.IO, "cannot read provided history file", e.getMessage());
    }
    this.line = 1;
    this.col = 0;
  }

  public char next() {
    try {
      char ch = (char) input.read();
      if (ch == '\n') {
        line++;
        col = 0;
      } else col++;
      return ch;
    } catch (IOException e) {
      throw new CustomError(ErrorType.IO, "cannot read provided history file", e.getMessage());
    }
  }

  public char peek() {
    try {
      input.mark(1);
      int ch = input.read();
      input.reset();
      return (ch == -1) ? Character.MIN_VALUE : (char) ch;
    } catch (IOException e) {
      throw new CustomError(ErrorType.IO, "cannot read provided history file", e.getMessage());
    }
  }

  public boolean eof() {
    try {
      input.mark(1);
      int ch = input.read();
      input.reset();
      return (ch == -1);
    } catch (IOException e) {
      throw new CustomError(ErrorType.IO, "cannot read provided history file", e.getMessage());
    }
  }

  public void croak(String msg) throws Error {
    throw new CustomError(
        ErrorType.FailedConfigFileParsing, msg + " (" + line + ":" + col + ")", "needed");
  }
}
