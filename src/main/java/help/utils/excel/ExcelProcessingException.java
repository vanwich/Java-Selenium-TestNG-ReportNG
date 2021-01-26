package help.utils.excel;

public class ExcelProcessingException extends RuntimeException{


    static final long serialVersionUID = -730141123679538945L;

    public ExcelProcessingException(String message) {
        super(message);
    }

    public ExcelProcessingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExcelProcessingException(Throwable cause) {
        super(cause);
    }
}
