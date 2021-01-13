package help.ws.rest.conf.client;

import static help.config.PropertyProvider.getProperty;
import java.util.logging.Level;
import javax.ws.rs.core.Configuration;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.media.multipart.internal.MultiPartWriter;
import org.zalando.jackson.datatype.money.MoneyModule;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import help.application.users.User;
import help.application.users.Users;
import help.config.ClassConfigurator;
import help.config.CustomTestProperties;
import help.config.PropertyProvider;
import help.ws.rest.conf.client.conf.IntegerModule;
import help.ws.rest.features.AdditionalLogging;
import help.ws.rest.features.AuthRemoval;
import help.ws.rest.features.RestLogger;
import help.ws.rest.features.mapper.AbstractContextResolver;
import help.ws.rest.features.mapper.JsonContextResolver;

public class DXPRestConfiguration implements IClientConfiguration{
    @ClassConfigurator.Configurable
    protected static String username = PropertyProvider.getProperty("dxp.rest.user");
    @ClassConfigurator.Configurable
    protected static String password = PropertyProvider.getProperty("dxp.rest.password");
    public static final DXPRestConfiguration INSTANCE = new DXPRestConfiguration();

    private AbstractContextResolver contextResolver = initContextResolver();
    private ClientConfig clientConfig = new ClientConfig();

    @SuppressWarnings("unused")
    public DXPRestConfiguration() {
        this(Users.QA);
    }

    public DXPRestConfiguration(User user) {
        clientConfig
                .property(ClientProperties.CONNECT_TIMEOUT, getProperty(CustomTestProperties.REST_CLIENT_CONNECTION_TIMEOUT, "0"))
                .property(ClientProperties.READ_TIMEOUT, getProperty(CustomTestProperties.REST_CLIENT_READ_TIMEOUT, "0"))
                .property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION, true)
                .register(HttpAuthenticationFeature.basic(user.getLogin(), user.getPassword()))
                .register(new LoggingFeature(new RestLogger(), Level.INFO, LoggingFeature.Verbosity.PAYLOAD_TEXT, 30000000))
                .register(new AuthRemoval())
                .register(new AdditionalLogging())
                .register(MultiPartWriter.class)
                .register(contextResolver);
    }

    static {
        ClassConfigurator configurator = new ClassConfigurator(DXPRestConfiguration.class);
        configurator.applyConfiguration();
    }

    @Override
    public Configuration getConfig() {
        return clientConfig;
    }

    public ObjectMapper getObjectMapper() {
        return contextResolver.getObjectMapper();
    }

    private AbstractContextResolver initContextResolver() {
        AbstractContextResolver contextResolver = new JsonContextResolver();

        contextResolver.getObjectMapper()
                .registerModule(new MoneyModule())
                .setFilterProvider(new SimpleFilterProvider().setFailOnUnknownId(false))
                .registerModule(new IntegerModule())
                .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
                .configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .setDefaultMergeable(true)
                .configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
                .configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false)
                .configure(DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES, false);

        return contextResolver;
    }
}
