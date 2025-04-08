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

  @Test
  public void testToDevFormatedText() {
    CustomError fatalError =
        new CustomError(
            ErrorType.IncompleteConfigException, "verifier le message", "verifier le detail");
    StringBuilder stackTrace = new StringBuilder();
    assertEquals(
        "Stack Trace:\n"
            + "pdp.scrabble.errorsHandling.CustomError - 37 : toDevFormatedText\n"
            + "pdp.scrabble.errorsHandling.ErrorTest - 68 : testToDevFormatedText\n"
            + "jdk.internal.reflect.NativeMethodAccessorImpl - -2 : invoke0\n"
            + "jdk.internal.reflect.NativeMethodAccessorImpl - 77 : invoke\n"
            + "jdk.internal.reflect.DelegatingMethodAccessorImpl - 43 : invoke\n"
            + "java.lang.reflect.Method - 569 : invoke\n"
            + "org.junit.runners.model.FrameworkMethod$1 - 59 : runReflectiveCall\n"
            + "org.junit.internal.runners.model.ReflectiveCallable - 12 : run\n"
            + "org.junit.runners.model.FrameworkMethod - 56 : invokeExplosively\n"
            + "org.junit.internal.runners.statements.InvokeMethod - 17 : evaluate\n"
            + "org.junit.runners.ParentRunner$3 - 306 : evaluate\n"
            + "org.junit.runners.BlockJUnit4ClassRunner$1 - 100 : evaluate\n"
            + "org.junit.runners.ParentRunner - 366 : runLeaf\n"
            + "org.junit.runners.BlockJUnit4ClassRunner - 103 : runChild\n"
            + "org.junit.runners.BlockJUnit4ClassRunner - 63 : runChild\n"
            + "org.junit.runners.ParentRunner$4 - 331 : run\n"
            + "org.junit.runners.ParentRunner$1 - 79 : schedule\n"
            + "org.junit.runners.ParentRunner - 329 : runChildren\n"
            + "org.junit.runners.ParentRunner - 66 : access$100\n"
            + "org.junit.runners.ParentRunner$2 - 293 : evaluate\n"
            + "org.junit.runners.ParentRunner$3 - 306 : evaluate\n"
            + "org.junit.runners.ParentRunner - 413 : run\n"
            + "org.eclipse.jdt.internal.junit4.runner.JUnit4TestReference - 93 : run\n"
            + "org.eclipse.jdt.internal.junit.runner.TestExecution - 40 : run\n"
            + "org.eclipse.jdt.internal.junit.runner.RemoteTestRunner - 520 : runTests\n"
            + "org.eclipse.jdt.internal.junit.runner.RemoteTestRunner - 748 : runTests\n"
            + "org.eclipse.jdt.internal.junit.runner.RemoteTestRunner - 443 : run\n"
            + "org.eclipse.jdt.internal.junit.runner.RemoteTestRunner - 211 : main\n"
            + "ErrorType: IncompleteConfigException\n"
            + "Message: verifier le message\n"
            + "Details: verifier le detail",
        fatalError.toDevFormatedText());
  }

  @Test
  public void testToUserFormatedText() {
    CustomError fatalError =
        new CustomError(
            ErrorType.IncompleteConfigException, "verifier le message", "verifier le detail");
    assertEquals(
        "ErrorType: IncompleteConfigException" + "\nMessage: verifier le message",
        fatalError.toUserFormatedText());
  }
}
