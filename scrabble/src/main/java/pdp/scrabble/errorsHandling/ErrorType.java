package pdp.scrabble.errorsHandling;

/*
Errors occur at runtime when it can't be recovered (=>fatal errors only)
Exceptions occur both at runtime and compile time, can be caught and handled (=> can be fatal or not)
*/

public enum ErrorType {
  FailedConfigFileParsing(true),
  ConfigParsingException(true),
  UncompletConfigexception(true),
  InvalideLetterException(true),
  MalformedDictionaryError(true),
  InvalidGameStatusError(true),
  InvalidLetterCountError(true),
  NotAValidWord(false),
  InvalidCliArgs(true),
  DrawerError(true),
  InvalidLangError(true),
  UnsupportedMultiplier(true),
  MalformedLangError(true);

  public final boolean isFatal;

  ErrorType(boolean isFatal) {
    this.isFatal = isFatal;
  }
}
