package help.utils.i18n;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snakeyaml.engine.v2.api.Load;
import org.snakeyaml.engine.v2.api.LoadSettings;
import help.config.ClassConfigurator;
import help.config.CustomTestProperties;
import help.config.PropertyProvider;

public class I18nResolver {
    public static final String DEFAULT_I18N_DICTIONARY = "default";
    public static final String DEFAULT_I18N_PATH = "i18n";
    public static final String FILE_EXTENSION = "yaml";
    @ClassConfigurator.Configurable
    public static String defaultLocale = Locale.US.toString();
    @ClassConfigurator.Configurable
    public static String locale = PropertyProvider.getProperty(CustomTestProperties.TESTS_LOCALE, defaultLocale);
    @ClassConfigurator.Configurable
    public static String defaultDateFormatPattern = "MM/dd/yyyy";
    public static String dateFormatPattern;

    private static final Logger LOGGER = LoggerFactory.getLogger(I18nResolver.class);
    private static Map<String, Object> i18nMap = new ConcurrentHashMap<>();

    static {
        ClassConfigurator configurator = new ClassConfigurator(I18nResolver.class);
        configurator.applyConfiguration();
        if (!isDefaultLocale()) {
            parseI18nResources();
            defaultDateFormatPattern = initializeDateFormat();
        }
        dateFormatPattern = defaultDateFormatPattern;
    }

    /**
     * Get a translation for provided {@link String} key in accordance to selected locale (tests.locale property) from default.yaml dictionary file    *
     *
     * @param key what should be translated
     * @return Same value as provided key in case if tests.locale=en_US(default) or dictionary does not contain mapping to translation
     * otherwise - return translation for selected locale
     */
    public static String i18n(String key) {
        return i18n(DEFAULT_I18N_DICTIONARY, key);
    }

    /**
     * Get a translation for provided {@link String} key in accordance to selected locale (tests.locale property) from {@link String} dictionaryPath.yaml dictionary file    *
     *
     * @param key            what should be translated
     * @param dictionaryPath path to dictionary starting from default dictionary folder path (resources/i18n/{@literal <locale>}/)
     * @return Same value as provided key in case if tests.locale=en_US(default) or dictionary does not contain mapping to translation
     * otherwise - return translation for selected locale
     */
    public static String i18n(String dictionaryPath, String key) {
        // TODO Uncomment trim functionality once EISDEV-220390 is fixed
        //key = key.trim();
        if (!isDefaultLocale()) {
            Map<String, String> dictionary = (Map<String, String>) i18nMap.get(String.join("/", DEFAULT_I18N_PATH, locale, String.format("%1$s.%2$s", dictionaryPath, FILE_EXTENSION)));
            if (Objects.nonNull(dictionary) && Objects.nonNull(dictionary.get(key))) {
                key = dictionary.get(key);
            } else {
                LOGGER.debug("Unable to find translation for key[{}] in dictionary[{}] for [{}] locale.", key, dictionaryPath, locale);
                LOGGER.debug("Returning default key value [{}]", key);
            }
        }
        return key;
    }

    /**
     * Returns {@link String} representation of a LocalDateTime object using format in accordance to locale selected
     * to specify date format for required locale add tests.%1$s.locale.dateformat property with needed format if not specified standard date format for locale will be used
     * i.e. if date format for zh_TW locale differs from expected we have to add tests.zh_tw.locale.dateformat=MM/dd/yyyy to properties file or as -D mvn parameter (Note: here locale code should be in lower case)
     *
     * @param localeDateTime object to format
     * @return {@link String} in format according to locale selected
     */
    public static String i18n(LocalDateTime localeDateTime) {
        return DateTimeFormatter.ofPattern(dateFormatPattern).format(localeDateTime);
    }

    /**
     * Check if {@link I18nResolver} class configured with default locale
     *
     * @return true if default locale, otherwise false
     */
    public static boolean isDefaultLocale() {
        if (defaultLocale.equals(locale)) {
            return true;
        }
        if (!localeExist(locale)) {
            throw new I18nException("Configured locale[%1$s] does not exist. Please ensure that you are using correct code.", locale);
        }
        return false;
    }

    private static boolean localeExist(String keyLocale) {
        return StringUtils.isNotEmpty(keyLocale) && LocaleUtils.isAvailableLocale(LocaleUtils.toLocale(keyLocale));
    }

    private static void parseI18nResources() {
        Set<String> resources = new Reflections(String.join(".", DEFAULT_I18N_PATH, locale), new ResourcesScanner()).getResources(Pattern.compile(".*." + FILE_EXTENSION));
        resources.forEach(s -> {
            i18nMap.put(s, new Load(
                    LoadSettings.builder().build())
                    .loadFromInputStream(Objects.requireNonNull(I18nResolver.class.getClassLoader().getResourceAsStream(s))));
        });
    }

    private static String initializeDateFormat() {
        String i18nDateFormat;
        Locale locale;
        if (PropertyProvider.isDefined(String.format(CustomTestProperties.DATE_FORMAT_LOCALE, I18nResolver.locale.toLowerCase()))) {
            String dateFormat = PropertyProvider.getPropertyOrThrow(String.format(CustomTestProperties.DATE_FORMAT_LOCALE, I18nResolver.locale.toLowerCase()));
            i18nDateFormat = dateFormat;
        } else {
            locale = LocaleUtils.toLocale(I18nResolver.locale);
            i18nDateFormat = ((SimpleDateFormat) DateFormat.getDateInstance(DateFormat.SHORT, locale)).toPattern();
        }
        return i18nDateFormat;
    }
}
