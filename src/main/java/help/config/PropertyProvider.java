package help.config;

import java.util.Properties;
import org.apache.commons.lang3.StringUtils;

public class PropertyProvider {
    private static Properties propsStore = new Properties();

    private PropertyProvider() {}

    static {
        //	cannot use class configurator here because of transitive cyclic dependency through CustomLoggerPropertyProvider
        String loaderClass = System.getProperty("propertyProvider.loaderClass", DefaultPropertyLoader.class.getName());
        PropertyLoader loader;

        try {
            @SuppressWarnings("unchecked")
            Class<? extends PropertyLoader> clazz = (Class<? extends PropertyLoader>) Class.forName(loaderClass);
            loader = clazz.newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Cannot instantiate property loader class " + loaderClass, e);
        }

        propsStore = loader.load();
    }


    /** Checks if specified property defined into system properties or configuration file
     * @param propertyName name of the property
     * @return true if property defined, false - otherwise
     */
    public static boolean isDefined(String propertyName) {
        return propsStore.containsKey(propertyName);
    }

    /** Returns value of specified property from system properties or configuration file
     * @param propertyName name of the property
     * @return value of the specified property
     */
    public static String getProperty(String propertyName) {
        return propsStore.getProperty(propertyName, "");
    }

    /** Returns value of specified property from system properties or configuration file
     * or default value in case of property isn't defined
     * @param propertyName name of the property
     * @param defaultValue property default value
     * @return value of the specified property or provided default value in case of property absence
     */
    public static String getProperty(String propertyName, String defaultValue) {
        return propsStore.getProperty(propertyName, defaultValue);
    }

    /**
     * Return value of specified property as boolean value
     * @param propertyName name of the property
     * @param defaultValue default value
     * @return property value of type boolean
     */
    public static boolean getProperty(String propertyName, boolean defaultValue) {
        String value = getProperty(propertyName);
        return StringUtils.isBlank(value) ? defaultValue : Boolean.parseBoolean(value);
    }

    /**
     * Return value of specified property as int value
     * @param propertyName name of the property
     * @param defaultValue default value
     * @return property value of type int
     */
    public static int getProperty(String propertyName, int defaultValue) {
        String value = getProperty(propertyName);
        return StringUtils.isBlank(value) ? defaultValue : Integer.parseInt(value);
    }

    /**
     * Return value of specified property as long value
     * @param propertyName name of the property
     * @param defaultValue default value
     * @return property value of type long
     */
    public static long getProperty(String propertyName, long defaultValue) {
        String value = getProperty(propertyName);
        return StringUtils.isBlank(value) ? defaultValue : Long.parseLong(value);
    }

    /** Returns value of specified property from system properties or configuration file
     * or throws an exception if property is not defined
     * @param propertyName name of the property
     * @return value of the specified property
     */
    public static String getPropertyOrThrow(String propertyName) {
        String prop = getProperty(propertyName);
        if (StringUtils.isBlank(prop)) {
            throw new IllegalStateException("Required property " + propertyName + " is empty or undefined");
        } else {
            return prop;
        }
    }

    /**
     * Returns all defined properties (union of system and config properties, the former taking precedence over the latter).
     * @return Properties collection
     */
    public static Properties getAllProperties() {
        Properties newProps = new Properties();
        newProps.putAll(propsStore);
        return newProps;

    }
}
