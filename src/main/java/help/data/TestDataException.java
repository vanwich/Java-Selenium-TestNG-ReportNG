package help.data;

import help.exceptions.JstrException;

public class TestDataException extends JstrException {
    private static final long serialVersionUID = -9001497811645582676L;

    public TestDataException(String format) {
        super(format);
    }

    public TestDataException(String string, Throwable e) {
        super(string, e);
    }
}
