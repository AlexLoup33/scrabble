package pdp.scrabble.errorsHandling;

public final class CustomError extends Error {
  public final ErrorType type; /* Type of error that happends */
  public final String message; /* Short explaination of what happend, for the user */
  public final String details; /*
     * a message as long as needed, to explain all is needed to understand what
     * happened, if the error is expected or if it is a bug, this message is intended
     * for the devs and will be logged later
     */

  public CustomError(ErrorType type, String message, String details) {
    this.type = type;
    this.message = message;
    this.details = details;
  }

  public boolean isFatal() {
    return this.type.isFatal;
  }

  public String toString() {
    /* To read the data of the attributs of an object. For a development optical */
    return "ErrorType: "
        + this.type.name()
        + "\nMessage: "
        + this.message
        + "\nDetails: "
        + this.details;
  }

  /**
   * re-throw all the CustomError that dont have expectedType
   *
   * @param expectedType to type of error to let pass
   * @throws CustomError
   */
  public void handleIfErrorType(ErrorType expectedType) throws CustomError {
    if (this.type != expectedType) {
      throw this;
    }
  }
}
