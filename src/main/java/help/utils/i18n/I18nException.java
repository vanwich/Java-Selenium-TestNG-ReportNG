package help.utils.i18n;

import help.exceptions.JstrException;

public class I18nException extends JstrException {
    public I18nException(String message) {
        super(message);
    }

    public I18nException(String message, Object... args) {
        super(String.format(message, args));
    }

    public I18nException(String message, Throwable cause, Object... args) {
        super(String.format(message, args), cause);
    }

    public I18nException(String message, Throwable cause) {
        super(message, cause);
    }

    public I18nException(Throwable cause) {
        super(cause);
    }
}
