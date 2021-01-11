package help.config;

import java.io.*;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.common.reflect.TypeToken;
import com.google.gson.*;
import help.exceptions.JstrException;
import help.webdriver.ByT;

public class ClassConfigurator {
    private static final Logger LOG = LoggerFactory.getLogger(ClassConfigurator.class);

    //配置文件存放位置。
    private static final String configStoreResourceDir = "config/class";
    private static final Optional<String> configStoreOverride = Optional.ofNullable(PropertyProvider.getProperty(TestProperties.CLASS_CONFIGURATION_OVERRIDE_PATH, null));
    private static final Set<Class<?>> alreadyConfiguredClasses = new HashSet<>();

    private final List<Class<?>> classHolders;
    private final Object instanceHolder;
    private final Map<Class<?>, Map<String, Class<?>>> fieldTypes;
    private final Map<Class<?>, Map<String, Object>> configValues;
    private final Map<Class<?>, Set<String>> configurableByClassNameFields;

    /**
     * Creates the class configurator linked to the provided configurable class
     * @param configurableClass class to be configured
     */
    public ClassConfigurator(Class<?> configurableClass) {
        this(configurableClass, null);
    }

    /**
     * Creates the class configurator linked to the provided configurable instance
     * @param configurableInstance class instance to be configured
     */
    public ClassConfigurator(Object configurableInstance) {
        this(configurableInstance.getClass(), configurableInstance);
    }

    private ClassConfigurator(Class<?> configurableClass, Object configurableInstance) {
        if (configurableClass == null) {
            throw new IllegalArgumentException("Configurable class cannot be null!");
        }
        classHolders = getClassHolders(configurableClass);
        instanceHolder = configurableInstance;
        fieldTypes = new HashMap<>();
        configValues = new HashMap<>();
        configurableByClassNameFields = new HashMap<>();
        String shortName;
        boolean isStaticConfig, isStaticField;
        isStaticConfig = (instanceHolder == null);

        for (Class<?> classHolder : classHolders) {
            Map<String, Object> currFieldValues = new HashMap<>();
            Map<String, Object> currConfigValues = new HashMap<>();
            Map<String, Class<?>> currFieldTypes = new HashMap<>();
            Set<String> currConfByClassNameFields = new HashSet<>();

            for (Field clsField : classHolder.getDeclaredFields()) {
                if (clsField.isAnnotationPresent(Configurable.class)){
                    shortName = clsField.getName();
                    if (!clsField.isAccessible()){
                        clsField.setAccessible(true);
                    }

                    isStaticField = Modifier.isStatic(clsField.getModifiers());

                    if (!(isStaticConfig ^ isStaticField)) {
                        try {
                            currFieldTypes.put(shortName, clsField.getType());
                            currFieldValues.put(shortName, clsField.get(instanceHolder));
                            if (clsField.getAnnotation(Configurable.class).byClassName()) {
                                currConfByClassNameFields.add(shortName);
                            }
                        } catch (IllegalArgumentException | IllegalAccessException e) {
                            LOG.debug("Failed to retrieve initial value from field " + shortName, e);
                        }
                    }
                }
            }
            //Pre-load configuration with current field values
            currConfigValues.putAll(currFieldValues);

            fieldTypes.put(classHolder, currFieldTypes);
            configValues.put(classHolder, currConfigValues);
            configurableByClassNameFields.put(classHolder, currConfByClassNameFields);
        }
    }

    /**
     * Applies the default configuration from the class configuration file (if exist) to the fields marked as "Configurable"
     *
     * @return instance of the configured object
     */
    public Object applyConfiguration() {
        return applyConfiguration("");
    }

    /**
     * Applies specific configuration from the class configuration file to the fields marked as "Configurable"
     * @param configurationName name of configuration going to be applied
     *
     * @return instance of the configured object
     */
    public Object applyConfiguration(String configurationName) {
        for (Class<?> classHolder : classHolders) {
            applyConfiguration(classHolder, configurationName);
        }
        return instanceHolder;
    }

    /**
     * Stores the current values of "Configurable" fields to the default class configuration file
     */
    public void storeConfiguration(){
        storeConfiguration("");
    }

    /**
     * Stores the current values of "Configurable" fields to the class configuration file with provided name
     * @param configurationName name of configuration going to be stored
     */
    public void storeConfiguration(String configurationName){
        String configFileName = classHolders.get(0).getCanonicalName();
        if (configurationName.isEmpty()){
            configFileName = configFileName + ".configuration";
        } else {
            configFileName = configFileName + "." + configurationName + ".configuration";
        }

        File configFile = new File(configFileName);

        try (OutputStreamWriter fosw = new OutputStreamWriter(new FileOutputStream(configFile.getAbsolutePath()))) {
            // Configure GSON
            GsonBuilder gsonBuilder = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting()
                    .registerTypeAdapterFactory(ByAdapterFactory.INSTANCE)
                    .registerTypeAdapter(By.class, new ByAdapter())
                    .registerTypeAdapter(ByT.class, new ByTAdapter());
            Gson gson = gsonBuilder.create();

            // Extract fields
            gson.toJson(configValues, fosw);
        } catch (FileNotFoundException e) {
            LOG.error("Configuration file " + configFile.getAbsolutePath() + " can't be created.", e);
        } catch (IOException e) {
            LOG.error("Configuration file " + configFile.getAbsolutePath() + " can't be properly stored.", e);
        } catch (Exception e) {
            LOG.error("Unexpected error processing configuration file " + configFile.getAbsolutePath(), e);
        }
    }

    /**
     * Checks if the configuration with provided name exists
     * @param configurationName name of configuration to check
     * @return true in case of requested configuration present
     */
    public boolean isConfigurationExist(String configurationName) {
        return isConfigurationExist(configurationName, false);
    }

    public boolean isConfigurationExist(String configurationName, boolean checkSuperClasses) {
        Stream<Class<?>> classStream = classHolders.stream();
        if (!checkSuperClasses) {
            classStream = classStream.limit(1);
        }

        return classStream.anyMatch(classHolder -> isConfigurationExistForClass(configurationName, classHolder));
    }

    private boolean isConfigurationExistForClass(String configurationName, Class<?> classHolder) {
        Function<Optional<String>, Boolean> checkConfig = ovrNameOpt -> {
            String configFileName = buildConfigurationResourceName(classHolder, configurationName, ovrNameOpt);
            URL url = getClass().getClassLoader().getResource(configFileName.replace(File.separator, "/"));
            return url != null;
        };

        boolean existsInOverride = configStoreOverride.map(ovrName -> checkConfig.apply(Optional.of(ovrName))).orElse(false);
        return existsInOverride || checkConfig.apply(Optional.empty());
    }

    private static List<Class<?>> getClassHolders(Class<?> klass) {
        List<Class<?>> classes = new ArrayList<>();
        //	collect only classes that have @Configurable fields
        while (klass != null) {
            for (Field clsField : klass.getDeclaredFields()) {
                if (clsField.isAnnotationPresent(Configurable.class)) {
                    classes.add(klass);
                    break;
                }
            }
            klass = klass.getSuperclass();
        }
        return classes;
    }

    private void applyConfiguration(Class<?> classHolder, String configName) {
        //Check if the class is already configured
        if (!(instanceHolder == null && alreadyConfiguredClasses.contains(classHolder))){
            Map<String, Object> currConfigValues = configValues.get(classHolder);
            currConfigValues.putAll(loadConfigurationResource(classHolder, configName));

            //Apply the configuration to the class
            for (Map.Entry<String, Object> entry : currConfigValues.entrySet()){
                try {
                    Field classField = classHolder.getDeclaredField(entry.getKey());
                    if (!classField.isAccessible()){
                        classField.setAccessible(true);
                    }
                    classField.set(instanceHolder, entry.getValue());
                } catch (Exception e){
                    LOG.debug("Configuration could not be applied for field " + entry.getKey(), e);
                }
            }

            //Mark class as configured to avoid double configuration
            if (instanceHolder == null) {
                alreadyConfiguredClasses.add(classHolder);
            }
        }
    }

    private String buildConfigurationResourceName(Class<?> classHolder, String configName, Optional<String> overrideName) {
        String configPath;
        if (StringUtils.isBlank(configName)) {	//	static config
            configPath = classHolder.getCanonicalName();
        } else {	//	instance config
            configPath = configName.replaceAll("\\\\", "/").replaceFirst("([^/]+)$", classHolder.getCanonicalName() + ".$1");
        }
        return String.format("%1$s/%2$s%3$s.configuration", configStoreResourceDir, overrideName.map(s -> s + "/").orElse(""), configPath);
    }

    private Map<String, Object> loadConfigurationResource(Class<?> classHolder, String configName) {
        Map<String, Object> config = null;

        InputStream configFile = configStoreOverride.flatMap(ovrName -> {
            String configResName = buildConfigurationResourceName(classHolder, configName, Optional.of(ovrName));
            return Optional.ofNullable(getClass().getClassLoader().getResourceAsStream(configResName));
        }).orElseGet(() -> {
            String configResName = buildConfigurationResourceName(classHolder, configName, Optional.empty());
            return getClass().getClassLoader().getResourceAsStream(configResName);
        });

        if (configFile == null) {
            LOG.debug("Configuration {} for class {} was not found in project resources.", StringUtils.defaultIfBlank(configName, "<unnamed>"), classHolder.getCanonicalName());
        } else {
            try (InputStreamReader fisr = new InputStreamReader(configFile)) {
                config = loadFromJsonForClass(classHolder, fisr);
            } catch (Exception e) {
                throw new JstrException(String.format("Failed to load or parse configuration %1$s for class %2$s", configName, classHolder.getCanonicalName()), e);
            }
        }

        if (config == null) {
            config = new HashMap<>();
        }
        return config;
    }

    private Map<String, Object> loadFromJsonForClass(Class<?> classHolder, Reader reader) {
        // Configure JSON
        GsonBuilder gsonBuilder = new GsonBuilder().disableHtmlEscaping();
        gsonBuilder
                .registerTypeAdapter(
                        new TypeToken<Map<String, Object>>() {}.getType(),
                        new ConfigurationDeserializer(fieldTypes.get(classHolder), configurableByClassNameFields.get(classHolder)))
                .registerTypeAdapter(By.class, new ByAdapter())
                .registerTypeAdapter(ByT.class, new ByTAdapter());
        Gson gson = gsonBuilder.create();

        // Load fields
        return gson.fromJson(reader, new TypeToken<Map<String, Object>>() {}.getType());
    }

    private class ConfigurationDeserializer implements JsonDeserializer<Map<String, Object>> {
        private Map <String, Class<?>> defaultConfig;
        private Set<String> confByClassName;

        public ConfigurationDeserializer (Map <String, Class<?>> defaultConfiguration, Set<String> confByClassName) {
            this.defaultConfig = defaultConfiguration;
            this.confByClassName = confByClassName;
        }

        @Override
        public Map<String, Object> deserialize(JsonElement json, Type typeOf, JsonDeserializationContext context)
                throws JsonParseException {

            Map<String, Object> fieldValues = new HashMap<>();
            JsonObject jsonObject = json.getAsJsonObject();
            for (Map.Entry<String, Class<?>> entry : defaultConfig.entrySet()) {
                if (jsonObject.has(entry.getKey())){
                    if (entry.getValue() == null) {
                        LOG.error("Cannot determine class of field " + entry.getKey() + ". Value " + jsonObject.get(entry.getKey()) + " will not be applied.");
                    } else {
                        try {
                            Object castObject;
                            if (confByClassName.contains(entry.getKey())) {	//	treat config value as class name
                                castObject = instantiateByClassName(context.deserialize(jsonObject.get(entry.getKey()), String.class), entry.getValue());
                            } else {
                                castObject = context.deserialize(jsonObject.get(entry.getKey()), entry.getValue());
                            }
                            fieldValues.put(entry.getKey(), castObject);
                        } catch (JsonSyntaxException e) {
                            LOG.error("Configuration file is not properly formatted. Failed to parse JSON " + json, e);
                        }
                    }
                }
            }
            return fieldValues;
        }

        private <T> T instantiateByClassName(String className, Class<T> type) {
            try {
                return type.cast(Class.forName(className).newInstance());
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                throw new JstrException("Cannot create instance of " + className, e);
            }
        }
    }

    /**
     * Indicates the field that could be configured by external configuration file
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD})
    public @interface Configurable {
        boolean byClassName() default false;
    }
}
