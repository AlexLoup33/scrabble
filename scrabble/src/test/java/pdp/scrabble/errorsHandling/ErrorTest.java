package pdp.scrabble.errorsHandling;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ErrorTest {

  @Test
  public void testIsFatal() {
    CustomError fatalError = new CustomError(ErrorType.IncompleteConfigException, null, null);
    assertEquals(true, fatalError.isFatal());
    CustomError noFatalErrror = new CustomError(ErrorType.UnknownWord, null, null);
    assertEquals(false, noFatalErrror.isFatal());
  }

  @Test
  public void testToString() {
    CustomError fatalError =
        new CustomError(
            ErrorType.IncompleteConfigException, "verifier le message", "verifier le detail");
    assertEquals(
        "ErrorType: IncompleteConfigException\n"
            + "Message: verifier le message\n"
            + "Details: verifier le detail",
        fatalError.toString());
  }
}
