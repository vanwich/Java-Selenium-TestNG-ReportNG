package help.ws.rest.comparison;

import java.util.List;
import com.fasterxml.jackson.databind.JsonNode;

public class ModelComparisonResult {
    private List<ModelDiffModel> diff;
    private JsonNode expected;
    private JsonNode actual;

    public ModelComparisonResult(JsonNode expected, JsonNode actual) {
        this.expected = expected;
        this.actual = actual;
    }

    public void setDiff(List<ModelDiffModel> diff) {
        this.diff = diff;
    }

    /**
     * Shows if two pojo are equals. It means no differences are present between two models after applying equality filter. </br>
     * Only field marked with @Equality(enabled = true) are considered.
     *
     * @return true if two pojo equals and false otherwise
     */
    public boolean isEquals() {
        if(isEmpty(expected) && isEmpty(actual)){
            throw new IllegalStateException("Equality can't be processed. Possible reasons: 1) Fields for comparison are not defined 2) EqualityFilter condition misconfiguration");
        }
        return diff.isEmpty();
    }

    private boolean isEmpty(JsonNode jsonNode) {
        return jsonNode.toString().equals("{}");
    }
}
