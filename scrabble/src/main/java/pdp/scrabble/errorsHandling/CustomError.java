package pdp.scrabble.errorsHandling;

public final class CustomError extends Exception {
    public final boolean fatal_error;
    public final EnumError error_type;
    public final String message;

    public CustomError(EnumError error_type, String message, boolean fatal_error) {
        this.error_type = error_type;
        this.message = message;
        this.fatal_error = fatal_error;
    }

    public void displayError() {
        System.err.println("Error Type: " + this.error_type);
        System.err.println("Message: " + this.message);

        if (this.fatal_error) {
            System.err.println("Problem fatal error. Need to stop of the app");
            this.printStackTrace(System.err);
        } else {
            System.err.println("No fatal error. Continue running");
        }
    }

    /**
     * Add all error we need. We throw it on CustomError with what is the error, the
     * messages and if it's fatal.
     */
    private void AddError() throws CustomError {
        throw new CustomError(EnumError.FailedConfigFileParsing, "the char at i isn't valid", true);
    }
}
