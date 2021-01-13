package help.ws.rest.features.mapper;

import javax.ws.rs.ext.Provider;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Class define basic strategy of serialization/deserialization json-response to pojo and vise versa.<br/>
 */
@Provider
public class JsonContextResolver extends AbstractContextResolver{
    @Override
    protected ObjectMapper defineMapper() {
        return new ObjectMapper();
    }
}
