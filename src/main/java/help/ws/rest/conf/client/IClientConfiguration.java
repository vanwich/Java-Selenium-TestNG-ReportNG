package help.ws.rest.conf.client;

import javax.ws.rs.core.Configuration;
import com.fasterxml.jackson.databind.ObjectMapper;

public interface IClientConfiguration {
    Configuration getConfig();

    ObjectMapper getObjectMapper();
}
