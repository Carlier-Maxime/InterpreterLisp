package vvl.lisp;

public class LispParseError extends LispError {
    public LispParseError(String message, Throwable cause) {
        super(message, cause);
    }

    public LispParseError(String message) {
        super(message);
    }
}
