package help.ws.rest.conf.client;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import help.application.users.User;
import help.ws.rest.RestClient;

public class ConfigurationFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationFactory.class);


    /**
     *
     * @return configuration for predefined class
     */
    public static <T extends IClientConfiguration> T get(Class<T> configClass) {
        T configuration;
        try {
            configuration = configClass.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new IllegalStateException("Can't initiate configuration",e);
        }
        return configuration;
    }

    /**
     *
     * @return configuration for predefined class and user
     */
    public static <T extends IClientConfiguration> T get(Class<T> configClass, User user) {
        T configuration;
        try {
            configuration = configClass.getDeclaredConstructor(new Class[]{User.class}).newInstance(user);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new IllegalStateException("Can't initiate configuration",e);
        }
        return configuration;
    }

    /**
     *
     * @return default configuration or DefaultConfiguration in case of exception
     */
    public static IClientConfiguration getDefault() {
        Field field;
        IClientConfiguration conf = null;
        try {
            field = RestClient.class.getDeclaredField("defaultConfiguration");
            field.setAccessible(true);
            conf = (IClientConfiguration) field.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            LOGGER.warn("Exception during getting default configuration", e);
        }

        return Optional.ofNullable(conf).orElse(ConfigurationFactory.get(DefaultConfiguration.class));
    }

    private ConfigurationFactory() {
    }
}
