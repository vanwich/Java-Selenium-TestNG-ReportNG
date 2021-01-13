package help.utils;

import static java.lang.String.format;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import help.exceptions.JstrException;
import help.ws.rest.model.FailureData;

public class ResponseContainer<T> {
    private Response response;
    private Object entity;
    private FailureData failure = new FailureData();

    public ResponseContainer(Response response, GenericType<T> genericType) {
        this.response = response;
        if (!isResponseSuccess()) {
            this.failure = response.readEntity(FailureData.class);
        } else {
            this.entity = response.readEntity(genericType);
        }
    }

    public ResponseContainer(Response response, Class<?> clazz) {
        this.response = response;

        if (!isResponseSuccess()) {
            this.failure = response.readEntity(FailureData.class);
        } else {
            this.entity = response.readEntity(clazz);
        }

    }

    public Response getResponse() {
        return response;
    }

    public int getHttpStatus() {
        return response.getStatus();
    }

    @SuppressWarnings("unchecked")
    public T getModel() {
        return (T) entity;
    }

    public String getFailureMessage() {
        return failure.getMessage();
    }

    public FailureData getFailure() {
        try {
            if (failure.getMessage() == null) {
                throw new JstrException("Response is not failure as expected.");
            }
        }catch (NullPointerException e){
            throw new JstrException("Response is NullPointerException");
        }
        return failure;
    }

    public T getAnalyzedSuccess() {
        if(!isResponseSuccess()) {
            throw new JstrException(format("Response is not success: [%s]", getFailure().toString()));
        }
        return getModel();
    }

    public boolean isResponseSuccess() {
        return String.valueOf(response.getStatus()).matches("20.");
    }
}
