package help.exceptions;

public class JstrException extends RuntimeException{
    private static final long serialVersionUID = 7485221613062008869L;

    /**
     * Create new instance of exception using {@link String#format(String, Object...)} logic
     * @param format a format string
     * @param args arguments for the format string
     * @return instance of IstfException
     */
    public static JstrException format(String format, Object... args) {
        return new JstrException(String.format(format, args));
    }

    /**
     * Create new instance of exception using {@link String#format(String, Object...)} logic
     * @param cause exception cause
     * @param format a format string
     * @param args arguments for the format string
     * @return instance of IstfException
     */
    public static JstrException format(Throwable cause,String format, Object... args) {
        return new JstrException(String.format(format, args), cause);
    }

    /**
     * Constructs a new runtime exception with the specified detail message.
     * @param message detail message
     */
    public JstrException(String message) {
        super(message);
    }

    /**
     * Constructs a new runtime exception with the specified detail message and cause.
     * @param message detail message
     * @param cause exception cause
     */
    public JstrException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new runtime exception with the specified cause.
     * @param cause exception cause
     */
    public JstrException(Throwable cause) {
        super(cause);
    }
}
