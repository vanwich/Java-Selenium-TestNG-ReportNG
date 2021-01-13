package help.ws.rest.features;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.core.MediaType;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import help.config.CustomTestProperties;
import help.config.PropertyProvider;

public class AdditionalLogging implements ClientResponseFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdditionalLogging.class);
    private static final boolean isAdditionalLoggingEnabled = PropertyProvider.getProperty(CustomTestProperties.REST_ADDITION_LOGGING, false);

    @Override
    public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {

        if (isAdditionalLoggingEnabled && getCondition(responseContext)) {
            String responseBody = "EMPTY";
            LOGGER.info("An exception occurs during communication with service");
            LOGGER.info("Response body:");
            if (responseContext.hasEntity()) {
                try (InputStream is = responseContext.getEntityStream(); BufferedInputStream bis = new BufferedInputStream(is, is.available())) {
                    responseBody = IOUtils.toString(bis, StandardCharsets.UTF_8);
                } catch (Exception e) {
                    LOGGER.info("Unable to log response body");
                }

            }
            LOGGER.info(responseBody);
        }
    }

    protected boolean getCondition(ClientResponseContext responseContext) {
        return responseContext.getStatus() >= 400 && !responseContext.getMediaType().equals(MediaType.MULTIPART_FORM_DATA_TYPE);
    }
}
