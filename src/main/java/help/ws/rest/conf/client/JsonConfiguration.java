package help.ws.rest.conf.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import help.application.users.User;
import help.ws.rest.features.mapper.AbstractContextResolver;
import help.ws.rest.features.mapper.JsonContextResolver;

public class JsonConfiguration extends DefaultConfiguration{
    protected AbstractContextResolver contextResolver = initContextResolver();

    protected JsonConfiguration(){
        this(new User(username, password));
    }

    protected JsonConfiguration(User user) {
        super(user);
        clientConfig.register(contextResolver);
    }

    public ObjectMapper getObjectMapper() {
        return contextResolver.getObjectMapper();
    }

    protected AbstractContextResolver initContextResolver() {
        return new JsonContextResolver();
    }
}
