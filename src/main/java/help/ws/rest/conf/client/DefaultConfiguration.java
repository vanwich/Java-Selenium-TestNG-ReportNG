package help.ws.rest.conf.client;

import static help.config.CustomTestProperties.APP_PASSWORD;
import static help.config.CustomTestProperties.APP_USER;
import java.util.logging.Level;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.media.multipart.internal.MultiPartWriter;
import com.fasterxml.jackson.databind.ObjectMapper;
import help.application.users.User;
import help.config.ClassConfigurator;
import help.config.CustomTestProperties;
import help.config.PropertyProvider;
import help.ws.rest.features.AdditionalLogging;
import help.ws.rest.features.AuthRemoval;
import help.ws.rest.features.RestLogger;

public class DefaultConfiguration implements IClientConfiguration{
    @ClassConfigurator.Configurable
    protected static String username = PropertyProvider.getProperty(APP_USER);
    @ClassConfigurator.Configurable
    protected static String password = PropertyProvider.getProperty(APP_PASSWORD);

    //Use level names from java.util.logging.Level
    @ClassConfigurator.Configurable
    protected static String logLevelDefault = "FINE";
    @ClassConfigurator.Configurable
    protected String logLevel = logLevelDefault;

    protected ClientConfig clientConfig = new ClientConfig();
    protected Level logLvl = Level.parse(logLevel);

    static {
        ClassConfigurator configurator = new ClassConfigurator(DefaultConfiguration.class);
        configurator.applyConfiguration();
    }

    public ClientConfig getConfig() {
        return clientConfig;
    }

    protected DefaultConfiguration() {
        this(new User(username, password));
    }

    protected DefaultConfiguration(User user) {
        clientConfig
                .property(ClientProperties.CONNECT_TIMEOUT, PropertyProvider.getProperty(CustomTestProperties.REST_CLIENT_CONNECTION_TIMEOUT, "0"))
                .property(ClientProperties.READ_TIMEOUT, PropertyProvider.getProperty(CustomTestProperties.REST_CLIENT_READ_TIMEOUT, "0"))

                .register(HttpAuthenticationFeature.basic(user.getLogin(), user.getPassword()))
                .register(new LoggingFeature(new RestLogger(), logLvl, LoggingFeature.Verbosity.PAYLOAD_TEXT, 30000000))
                .register(new AuthRemoval())
                .register(new AdditionalLogging())
                .register(MultiPartWriter.class);
    }

    public ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }
}
