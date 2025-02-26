package pdp.scrabble;

import java.util.Properties;

import pdp.scrabble.errorsHandling.CustomError;
import pdp.scrabble.errorsHandling.ErrorType;

/**
 * YOU will have the Nachos debugging system ported to java and YOU will be happy
 */
public class Utilities {

    private static String enableFlags = null;

    /**
     * Initialize so that only debug messages with a flag in flagList will be printed.
     *
     * <p>If the flag is "+", we enable all debug messages.
     *
     * <p>"flagList" is a string of characters for whose debug messages are to be enabled.
     */
    public static void debugInit(String flagList) {
        enableFlags = flagList;
    }

    /**
     * Return TRUE if debug messages with "flag" are to be printed.
     */
    private static boolean debugIsEnabled(char flag) {
        if (enableFlags != null)
            return (enableFlags.indexOf(flag) != -1 || enableFlags.indexOf('+') != -1);
        else return false;
    }

    /**
     * Print a debug message, if flag is enabled. Like printf, only with an extra argument on the
     * front.
     *
     * <p>There are some formatting differences with C's printf, you can Read The Friendly Manual:
     * https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Formatter.html#syntax
     */
    public static void debug(char flag, String format, Object... args) {
        if (debugIsEnabled(flag)) {
            System.out.printf(format, args);
        }
    }

    /**
     * Return the pom.xml project version
     */
    public static String getProjetVersion() throws CustomError {
        try {
            Properties props = new Properties();
            props.load(Utilities.class.getResourceAsStream("/properties"));
            return props.getProperty("version");
        } catch (Exception e) {
            // this should never happen
            throw new CustomError(
                    ErrorType.IO, "don't mess with the contents of the jar file", e.getMessage());
        }
    }
}
