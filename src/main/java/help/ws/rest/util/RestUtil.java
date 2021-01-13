package help.ws.rest.util;

import java.io.IOException;
import java.util.Collection;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import help.ws.rest.conf.client.ConfigurationFactory;
import help.ws.rest.conf.client.IClientConfiguration;
import help.ws.rest.conf.client.JsonConfiguration;

public class RestUtil {
    /**
     * Convert object to pojo
     *
     * @param object object to convert
     * @param targetClass target type
     * @param clientConfiguration rest configuration
     * @return pojo with defined type
     */
    public static <T> T convert(Object object, Class<T> targetClass, IClientConfiguration clientConfiguration) {
        return clientConfiguration.getObjectMapper().convertValue(object, targetClass);
    }

    /**
     * Convert object to pojo using JsonConfiguration
     *
     * @param object object to convert
     * @param targetClass target type
     * @return pojo with defined type
     */
    public static <T> T convert(Object object, Class<T> targetClass) {
        return convert(object, targetClass, ConfigurationFactory.get(JsonConfiguration.class));
    }

    /**
     * Convert object to collection using JsonConfiguration
     *
     * @param object object to convert
     * @param targetClass target type
     * @return pojo with defined type
     */
    public static <T extends Collection> T convert(Object object, Class<?> targetClass, Class<? extends Collection> genericType) {
        return convert(object, targetClass, genericType, ConfigurationFactory.get(JsonConfiguration.class));
    }

    /**
     * Convert object to collection
     *
     * @param object object to convert
     * @param targetClass target type
     * @return pojo with defined type
     */
    public static <T extends Collection> T convert(Object object, Class<?> targetClass, Class<? extends Collection> genericType, IClientConfiguration clientConfiguration) {
        ObjectMapper mapper = clientConfiguration.getObjectMapper();
        JavaType type = mapper.getTypeFactory().constructCollectionType(genericType, targetClass);
        return mapper.convertValue(object, type);
    }

    public static <T> T adjust(Object src, T target) {
        try {
            String json = ConfigurationFactory.getDefault().getObjectMapper().writeValueAsString(src);
            ConfigurationFactory.getDefault().getObjectMapper().readerForUpdating(target).readValue(json);
        } catch (IOException e) {
            throw new IllegalStateException("Can't adjust TestData to POJO", e);
        }
        return target;
    }

    public static <T> T adjust(Object src, T target, IClientConfiguration configuration) {
        try {
            String json = configuration.getObjectMapper().writeValueAsString(src);
            configuration.getObjectMapper().readerForUpdating(target).readValue(json);
        } catch (IOException e) {
            throw new IllegalStateException("Can't adjust TestData to POJO", e);
        }
        return target;
    }
}
