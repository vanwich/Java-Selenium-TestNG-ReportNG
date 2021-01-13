package help.ws.rest.model;

import help.data.TestData;
import help.ws.rest.RestClient;
import help.ws.rest.conf.client.ConfigurationFactory;
import help.ws.rest.conf.client.DefaultConfiguration;

import help.ws.rest.conf.client.IClientConfiguration;
import help.ws.rest.conf.client.JsonConfiguration;
import help.ws.rest.util.RestUtil;

@SuppressWarnings("unchecked")
public interface Adjustable<T extends Model> {
    /**
     * Adjust testData to existing model.
     * @see <a href="https://fasterxml.github.io/jackson-annotations/javadoc/2.9/com/fasterxml/jackson/annotation/JsonMerge.html">doc link</a>
     *
     * @param testData to adjust
     * @return source model with updated values
     */
    default T adjust(TestData testData) {
        return RestUtil.adjust(testData, (T) this, setUpAdjustConfiguration());
    }

    default T adjust(Model model) {
        return RestUtil.adjust(model, (T) this, setUpAdjustConfiguration());
    }

    /**
     *
     * @return default configuration to be used during adjust
     * In case {@link  RestClient defaultConfiguration} field is not configured inside the project
     * {@link JsonConfiguration} is returned because of {@link DefaultConfiguration} doesn't contains {@link TestData} mapping type
     *
     */
    default IClientConfiguration setUpAdjustConfiguration() {
        return ConfigurationFactory.getDefault().getClass().isAssignableFrom(DefaultConfiguration.class)?
                ConfigurationFactory.get(JsonConfiguration.class) : ConfigurationFactory.getDefault();
    }
}
