package vvl.lisp;

public class LispRuntimeError extends RuntimeException {
    /**
     * Fake serial version UID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Create a new exception with a message and a cause.
     *
     * @param message
     *            a detailed message intended to the end user.
     * @param cause
     *            the reason of the exception (e.g. another exception).
     */
    public LispRuntimeError(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Create a new exception with a message.
     *
     * @param message
     *            a detailed message intended to the end user.
     */
    public LispRuntimeError(String message) {
        super(message);
    }

    /**
     * Create a new exception with a cause.
     *
     * @param cause
     *            the reason of the exception (e.g. another exception).
     */
    public LispRuntimeError(Throwable cause) {super(cause);}
}
