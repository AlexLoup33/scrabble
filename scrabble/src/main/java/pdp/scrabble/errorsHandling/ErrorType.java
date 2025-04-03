package pdp.scrabble.errorsHandling;

/*
Errors occur at runtime when it can't be recovered (=>fatal errors only)
Exceptions occur both at runtime and compile time, can be caught and handled (=> can be fatal or not)
*/

public enum ErrorType {
  FailedConfigFileParsing(true),
  ConfigParsingException(true),
  IncompleteConfigException(true),
  InvalidLetterException(true),
  MalformedDictionaryError(true),
  InvalidGameStatusError(true),
  InvalidLetterCountError(true),
  UnknownWord(false),
  InvalidCliArgs(true),
  DrawerError(true),
  InvalidLangError(true),
  UnsupportedMultiplier(true),
  MalformedLangError(true),
  IO(true),
  ForfeitTwiceError(true),
  InvalidLangPackIdentifier(true),
  InvalidKeyArgument(true),
  InvalidMoveFormat(false),
  InvalidActionType(false),
  InvalidWordDirection(true),
  IndexOutOfWordBounds(false),
  PlayerDoesntHaveLettersInIsBag(false),
  InvalidePlaceLetterAction(false),
  PositionOutOfGird(true),
  InvalidIndexOutOfGird(false),
  NoHistoryAvailable(false),
  FirstMoveIllegal(false),
  NotPlaceNextToAWord(false),
  InvalideLetter(false),
  CanceledAskAction(false);

  public final boolean isFatal;

  ErrorType(boolean isFatal) {
    this.isFatal = isFatal;
  }
}
