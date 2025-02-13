package pdp.scrabble.errorsHandling;

public final class CustomError extends Error {
  public final ErrorType type; /* Type of error that happends */
  public final String message; /* Short explaination of what happend, for the user */
  public final String details; /*
                                * a message as long as needed, to explain all is needed to understand what
                                * happend,if the error is expected or if it is a bug, this message is intended
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
    /* To read the data of the attributs of an object. for a development optical */
    return "ErrorType: "
        + this.type.name()
        + "\nMessage: "
        + this.message
        + "\nDetails: "
        + this.details;
  }

  public String toDevFormatedText() {
    /*
     * For the developer to read what he need: stack trace, type of error, message
     * and details
     */
    Exception exception = new Exception();
    StringBuilder stackTrace = new StringBuilder();
    for (StackTraceElement element : exception.getStackTrace()) {
      stackTrace.append(
          element.getClassName()
              + " - "
              + element.getLineNumber()
              + " : "
              + element.getMethodName());
      stackTrace.append("\n");
    }

    return "Stack Trace:\n" + stackTrace.toString() + toString();
  }

  public String toUserFormatedText() {
    /* For the user to read what he need: type of error and message only */
    return "ErrorType: " + this.type.name() + "\nMessage: " + this.message;
  }
}
