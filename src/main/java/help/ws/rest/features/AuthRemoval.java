package help.ws.rest.features;

import java.io.IOException;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import help.application.users.User;

public class AuthRemoval implements ClientRequestFilter {
    public static final String NO_DATA = "no data";
    private static final String AUTHORIZATION = "Authorization";
    private static final String basicAuthValue = new User(NO_DATA, NO_DATA).getBasicAuthString();


    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        if(requestContext.getHeaders().containsKey(AUTHORIZATION) &&
                requestContext.getHeaders().getFirst(AUTHORIZATION).equals(basicAuthValue)){
            requestContext.getHeaders().remove(AUTHORIZATION);
        }
    }
}
