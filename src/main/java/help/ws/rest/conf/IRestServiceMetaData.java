package help.ws.rest.conf;

import javax.ws.rs.core.MediaType;
import help.ws.rest.RestClient;

public interface IRestServiceMetaData {
    String getTarget(String requestAlias);
    MediaType getMediaType(String requestAlias, RestClient.HttpMethod httpMethod);
}
