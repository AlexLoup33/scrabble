package pdp.scrabble;

import java.io.PrintStream;
import java.util.LinkedList;

public class Logger {
  public static int LOGGING_SIZE = 10;
  private boolean enableRedirect;
  private PrintStream redirectTo;
  public LinkedList<String> loggedOuts;

  public Logger(PrintStream redirectTo) {
    this.enableRedirect = true;
    this.redirectTo = redirectTo;
    this.loggedOuts = new LinkedList<>();
  }

  /**
   * generic methode that redirect and log something
   *
   * @param s the string to be logged
   */
  private void redirect(String s) {
    this.loggedOuts.addLast(s);
    if ((this.redirectTo != null) && this.enableRedirect) {
      this.redirectTo.print(s);
    }
    if (this.loggedOuts.size() > Logger.LOGGING_SIZE) {
      // remove the oldest one if too old
      this.loggedOuts.removeFirst();
    }
  }

  public void print(String s) {
    this.redirect(s);
  }

  public void println(String s) {
    this.redirect(s + '\n');
  }

  public void printf(String format, Object... args) {
    this.redirect(String.format(format, args));
  }

  public void setRedirectState(boolean state) {
    this.enableRedirect = state;
  }
}
