package help.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.commons.lang3.StringUtils;

public class DefaultPropertyLoader implements PropertyLoader{
    private static final String ROOT_FOLDER = "help/config";
    private static final String MAIN_PROPERTIES = ROOT_FOLDER + "/config.properties";
    private static final String PROJECT_PROPERTIES = ROOT_FOLDER + "/project.properties";
    private static final String LOCAL_PROPERTIES = ROOT_FOLDER + "/local.properties";

    protected final Properties props;

    public DefaultPropertyLoader() {
        props = new Properties();
    }

    @Override
    public Properties load() {
        loadFromResource(props, MAIN_PROPERTIES);
        loadFromResource(props, PROJECT_PROPERTIES);
        loadFromResource(props, LOCAL_PROPERTIES);
        props.putAll(System.getProperties());
        String customConfigPath = props.getProperty(TestProperties.CUSTOMPROPS_PATH);
        if (!StringUtils.isBlank(customConfigPath)) {
            loadFromResource(props, ROOT_FOLDER + "/" + customConfigPath);
        }
        return props;
    }

    private void loadFromResource(Properties props, String path) {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(path)) {
            if (is != null) {
                props.load(is);
            }
        } catch (IOException e) {
            //	cannot use logger because logger uses property provider for initialization
            e.printStackTrace();
        }
    }
}
