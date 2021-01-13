package help.ws.rest.model;

import java.util.List;
import java.util.Objects;
import help.data.TestData;
import help.utils.constant.ErrorConstant;

public class FailureData extends DXPModel implements Adjustable<FailureData> {
    private String errorCode;
    private String message;
    private String logReference;
    private List<Error> errors;

    public FailureData() {
    }

    public FailureData(TestData testData) {
        super(testData);
    }

    public FailureData(String code, String message) {
        setErrorCode(code);
        setMessage(message);
    }

    public FailureData(ErrorConstant error) {
        setErrorCode(error.getCode());
        setMessage(error.getMessage());
    }

    public FailureData(ErrorConstant error, Object... variables) {
        setErrorCode(error.getCode());
        setMessage(String.format(error.getMessage(), variables));
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLogReference() {
        return logReference;
    }

    public void setLogReference(String logReference) {
        this.logReference = logReference;
    }

    public List<Error> getErrors() {
        return errors;
    }

    public void setErrors(List<Error> errors) {
        this.errors = errors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FailureData that = (FailureData) o;
        return Objects.equals(errorCode, that.errorCode) &&
                Objects.equals(message, that.message) &&
                Objects.equals(logReference, that.logReference) &&
                Objects.equals(errors, that.errors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(errorCode, message, logReference, errors);
    }

    @Override
    public String toString() {
        return String.format("%s: %s%s", getErrorCode(), getMessage(), errors == null ? "" : errors.toString());
    }
}
