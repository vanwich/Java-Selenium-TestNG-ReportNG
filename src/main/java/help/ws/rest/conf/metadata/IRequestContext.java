package help.ws.rest.conf.metadata;

import java.util.Map;
import javax.ws.rs.core.MultivaluedMap;
import help.application.users.User;

public interface IRequestContext {
    User getUser();

    Map<String, String> getPathParams();

    MultivaluedMap<String, String> getQueryParams();

    MultivaluedMap<String, String> getHeaders();
}
