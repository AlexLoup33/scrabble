package pdp.scrabble.game.serializer.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import pdp.scrabble.Utilities;
import pdp.scrabble.configuration.ConfigurationTemplate;
import pdp.scrabble.dictionary.Letter;
import pdp.scrabble.dictionary.Word;
import pdp.scrabble.errorsHandling.CustomError;
import pdp.scrabble.game.Direction;
import pdp.scrabble.game.GameModel;
import pdp.scrabble.game.GridWordPos;
import pdp.scrabble.game.Player;
import pdp.scrabble.game.actions.ExchangeLetterAction;
import pdp.scrabble.game.actions.ForfeitGameAction;
import pdp.scrabble.game.actions.PassTurnAction;
import pdp.scrabble.game.actions.PlaceWordAction;
import pdp.scrabble.game.actions.RenamePlayerAction;

/**
 * Parses a save file into a GameModel object, which can then be used to restore the game decribed
 * in the save file.
 *
 * <p>This parser uses tokens, which are: - str: string surrounded by " - var: symbol name, string
 * that begins by upper case char followed by any simple ascii char -> /[A-Z][0-z]+/ - punc: one of
 * ".{}<>[],;" - op: = - num: any unsigned digit - lng: any unsigned long - negNum: any negative
 * digit (digit preceded by "-") - negLng: any negative long (long preceded by "-") - move: any
 * value that matches /[a-z][0-9]+[a-z]/
 *
 * <p>Any other value inside of save file will get rejected.
 *
 * <p>TODO: block description
 *
 * <p>The state is recreated from a blank model, move by move, using the rng seed of the save file.
 * Our parser will throw if the state of the game is not coherent with the moves described in the
 * move list of the history block. This means that for most saves, you cannot arbitrarily modify the
 * game portion of the save file.
 */
public class Parser {

  private TokenInputStream input;
  private ConfigurationTemplate confStub;
  private GameModel loadedModel;

  public Parser(TokenInputStream input) {
    this.input = input;
    this.confStub = new ConfigurationTemplate();
    this.loadedModel = null;
    parseTopLevel();
  }

  private boolean isPunc(char ch) {
    Token tok = input.peek();
    return (tok != null)
        && tok.type == "punc"
        && tok.value.getClass() == Character.class
        && (char) tok.value == ch;
  }

  private boolean isOp() {
    Token tok = input.peek();
    return (tok != null)
        && tok.type == "op"
        && tok.value.getClass() == Character.class
        && (char) tok.value == '=';
  }

  private void skipPunc(char ch) {
    if (isPunc(ch)) input.next();
    else input.croak("Expecting punctuation: \"" + ch + "\"");
  }

  private void skipPuncUntilDifferent(char ch) {
    while (isPunc(ch)) input.next();
  }

  private void skipOp() {
    if (isOp()) input.next();
    else input.croak("Expecting operator: =");
  }

  private void skipWhiteSpaceIfEof() {
    if (input.peek() == null) input.next();
  }

  private void unexpected() {
    input.croak("Unexpected token: " + input.peek().toString());
  }

  private void parseTopLevel() {
    while (!input.eof()) {
      delimitedBlock('<', '>');
      skipWhiteSpaceIfEof();
    }
  }

  private List<Object> delimited(char start, char stop, char separator, Supplier<Object> parser) {
    List<Object> elements = new ArrayList<>();
    boolean first = true;
    skipPunc(start);
    while (!input.eof()) {
      if (isPunc(stop)) break;
      if (first) first = false;
      else skipPunc(separator);
      if (isPunc(stop)) break;
      elements.add(parser.get());
    }
    skipPunc(stop);
    return elements;
  }

  private void delimitedBlock(char start, char stop) {
    skipPunc(start);
    skipPuncUntilDifferent(start);

    String name = parseIdent();
    if (name.equals("HISTORY")) {
      // TODO when history is written before game
      parseHistory();
    }
    if (name.equals("GAME")) {
      parseGame();
      this.loadedModel = new GameModel(this.confStub.getConfiguration());
    }

    skipPunc(stop);
    skipPuncUntilDifferent(stop);
  }

  private String parseIdent() {
    Token tok = input.next();
    if (!tok.type.equals("var")) {
      input.croak("Expecting identifier");
    }
    return (String) tok.value;
  }

  // # is the joker letter
  // but our parser ignores # as comments
  // so we serialize them as $
  // An ExchangeLetter can be any identifier or $
  private String parseExchangeLetters() {
    Token tok = input.next();
    if (tok.type == "punc" && (char) tok.value == '$') {
      return "#";
    }
    if (!tok.type.equals("var")) {
      input.croak("Expecting identifier");
    }
    return (String) tok.value;
  }

  private int parseNumber() {
    Token tok = input.next();
    if (!tok.type.equals("num")) {
      input.croak("Expecting positive number");
    }
    return (int) tok.value;
  }

  private long parseSignedLong() {
    Token tok = input.next();
    if (tok.type.equals("lng") || tok.type.equals("negLng")) {
      return (long) tok.value;
    }
    if (tok.type.equals("num") || tok.type.equals("negNum")) {
      return ((Integer) tok.value).longValue();
    }
    input.croak("Expecting a number");
    return 0;
  }

  private String parseString() {
    Token tok = input.next();
    if (!tok.type.equals("str")) {
      input.croak("Expecting a String");
    }
    return (String) tok.value;
  }

  private GridWordPos parseMove() {
    Token expectedMove = input.next();
    if (!expectedMove.type.equals("move")) input.croak("Expecting move");
    String move = (String) expectedMove.value;
    int maxRange = this.loadedModel.config.getSuperScrabble() ? 20 : 14;
    int row = (Character.toLowerCase(move.charAt(0)) - 97);
    if (row < 0 || row > maxRange) {
      input.croak("row must be between a and " + (char) (97 + maxRange));
    }
    int col = 0;
    try {
      // actual col value from model starts from 0
      // [\\D] = remove everything but numbers
      col = Integer.parseInt(move.replaceAll("[\\D]", "")) - 1;
    } catch (NumberFormatException e) {
      input.croak("pattern for move is `yxdirection WORD`(eg. `a6h photo` or `b7v STICK` )");
    }
    if (col < 0 || col > maxRange) {
      input.croak("col must be between 1 and " + (maxRange + 1));
    }
    final char direction = move.charAt(move.length() - 1);
    switch (direction) {
      case 'h':
        return new GridWordPos(row, col, Direction.Right);
      case 'v':
        return new GridWordPos(row, col, Direction.Down);
      default:
        input.croak("invalid direction");
        return null;
    }
  }

  private Letter[] parseLetters() {
    Word word = new Word(parseIdent());
    Letter[] toPlace = new Letter[word.getLength()];
    for (int i = 0; i < word.getLength(); i++) {
      toPlace[i] = word.getLetterAt(i);
    }
    return toPlace;
  }

  private void parseGame() {
    while (input.peek().type.equals("var")) {
      String name = parseIdent();
      skipOp();
      boolean recognized = false;
      try {
        if (name.equals("Verbose")) {
          this.confStub.setVerbose(Boolean.valueOf(parseString()));
          recognized = true;
        }
        if (name.equals("Debug")) {
          this.confStub.setDebug(parseString());
          recognized = true;
        }
        if (name.equals("Blitz")) {
          this.confStub.setBlitz(Boolean.valueOf(parseString()));
          recognized = true;
        }
        if (name.equals("SuperScrabble")) {
          this.confStub.setSuperScrabble(Boolean.valueOf(parseString()));
          recognized = true;
        }
        if (name.equals("Gui")) {
          this.confStub.setGui(Boolean.valueOf(parseString()));
          recognized = true;
        }
        if (name.equals("Language")) {
          this.confStub.setLanguage(parseString());
          recognized = true;
        }
        if (name.equals("PlayerCount")) {
          this.confStub.setPlayerCount(parseNumber());
          recognized = true;
        }
        if (name.equals("BlitzTime")) {
          this.confStub.setBlitzTime(parseNumber());
          recognized = true;
        }
        if (name.equals("Seed")) {
          this.confStub.setSeed(parseSignedLong());
          recognized = true;
        }
        if (name.equals("AiPlayers")) {
          this.confStub.setAiPlayers(
              delimited('[', ']', ',', this::parseNumber).stream()
                  .map((num) -> String.valueOf(num))
                  .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                  .toString());
          recognized = true;
        }
        if (name.equals("AiMode")) {
          this.confStub.setAiMode(parseString());
          recognized = true;
        }
        if (name.equals("AiDepth")) {
          this.confStub.setAiDepth(parseNumber());
          recognized = true;
        }
        if (name.equals("AiTime")) {
          this.confStub.setAiTime(parseNumber());
          recognized = true;
        }
        if (name.equals("HistoryFile")) {
          this.confStub.setHistoryFile(parseString());
          recognized = true;
        }
        if (name.equals("DictFile")) {
          this.confStub.setDictFile(parseString());
          recognized = true;
        }
        if (name.equals("ContestFile")) {
          this.confStub.setContestFile(parseString());
          recognized = true;
        }
        if (!recognized) unexpected();
      } catch (CustomError e) {
        input.croak(e.message);
      }
    }
  }

  // Warning : depends on loadedModel, so must be called after
  // parseGame()
  private void parseHistory() {
    assert this.loadedModel != null;
    while (input.peek().type.equals("num")) {
      int curTurn = parseNumber();
      skipPunc('.');
      if (curTurn != loadedModel.getTurn()) input.croak("Turn number doesn't make sense");
      parseActions(curTurn);
      skipPunc(';');
    }
  }

  private void parseActions(int curTurn) {
    while (!isPunc(';')) {
      int playerOrder = parseNumber();
      if (playerOrder != this.loadedModel.getCurrentPlayer().id)
        input.croak("Player id doesn't make sense");
      Player playing = this.loadedModel.getCurrentPlayer();
      Utilities.debug('s', "%s begins his turn\n", playing.getName());
      Utilities.debug('s', "%s's letters: %s \n", playing.getName(), playing.bag.toString());
      Utilities.debug(
          's', "numbers of letters in draw: %d\n", this.loadedModel.draw.getCurrentSize());
      String actionType = parseIdent();
      try {
        if (actionType.equals("PlaceWord")) {
          GridWordPos gwp = parseMove();
          PlaceWordAction placeAction = new PlaceWordAction(playing, curTurn, parseLetters(), gwp);
          placeAction.applyGameAction(loadedModel);
          continue;
        }
        if (actionType.equals("RenamePlayer")) {
          RenamePlayerAction renameAction = new RenamePlayerAction(playing, curTurn, parseString());
          renameAction.applyGameAction(this.loadedModel);
          continue;
        }
        if (actionType.equals("ExchangeLetter")) {
          List<Letter> toExchange =
              delimited('[', ']', ',', this::parseExchangeLetters).stream()
                  .map(
                      (obj) -> {
                        if (obj.equals("#")) return Letter.joker();
                        else return new Letter(((String) obj).charAt(0));
                      })
                  .collect(Collectors.toList());
          ExchangeLetterAction exchangeAction =
              new ExchangeLetterAction(playing, curTurn, toExchange);
          exchangeAction.applyGameAction(this.loadedModel);
          continue;
        }
        if (actionType.equals("ForfeitGame")) {
          ForfeitGameAction forfeitAction = new ForfeitGameAction(playing, curTurn);
          forfeitAction.applyGameAction(this.loadedModel);
          continue;
        }
        if (actionType.equals("PassTurn")) {
          PassTurnAction passTurnAction = new PassTurnAction(playing, curTurn);
          passTurnAction.applyGameAction(this.loadedModel);
          continue;
        }
        unexpected();
      } catch (CustomError e) {
        input.croak(e.message);
      }
    }
  }

  /**
   * Returns the parsed Model
   *
   * @return Parsed model
   */
  public GameModel getLoadedModel() {
    return this.loadedModel;
  }
}
