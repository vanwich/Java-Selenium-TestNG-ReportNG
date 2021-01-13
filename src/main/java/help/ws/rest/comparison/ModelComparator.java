package help.ws.rest.comparison;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.zjsonpatch.JsonDiff;
import help.ws.rest.conf.client.ConfigurationFactory;
import help.ws.rest.conf.client.JsonConfiguration;
import help.ws.rest.filter.EqualityFilter;
import help.ws.rest.model.Model;

public class ModelComparator {
    private static final Logger LOGGER = LoggerFactory.getLogger(ModelComparator.class);
    private static final EqualityFilter FILTER = new EqualityFilter();

    /**
     * Compare 2 POJO's to be equals from mock perspective. It means that fields marked with @Equality(mockEnabled=false) will be ignored </br>
     * Only added or removed nodes will be considered.
     *
     * @return true if POJO's are identical and false otherwise
     */
    public static ModelComparisonResult compare(Model expectedModel, Model actualModel) {
        JsonNode actualNode;
        JsonNode expectedNode;

        ObjectMapper mapper = ConfigurationFactory.getDefault().getObjectMapper();

        try {
            actualNode = mapper.readTree(FILTER.filterModel(actualModel));
            expectedNode = mapper.readTree(FILTER.filterModel(expectedModel));
        } catch (IOException e) {
            throw new RuntimeException("Unexpected exception during comparison", e);
        }

        ModelComparisonResult result = new ModelComparisonResult(expectedNode, actualNode);
        result.setDiff(getDiff(JsonDiff.asJson(expectedNode, actualNode)));
        return result;
    }

    private static List<ModelDiffModel> getDiff(JsonNode comparisonData) {
        if (Objects.isNull(comparisonData)) {
            return null;
        }

        try {
            return ConfigurationFactory.get(JsonConfiguration.class).getObjectMapper().readerFor(new TypeReference<List<ModelDiffModel>>() {
            }).readValue(comparisonData);
        } catch (IOException e) {
            LOGGER.error("Can't generate formatted difference", e);
            return null;
        }
    }
}
