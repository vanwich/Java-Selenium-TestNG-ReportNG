package help.ws.rest.comparison;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ModelDiffModel {
    @JsonProperty
    private String path;
    @JsonProperty
    private String op;

    public String getPath() {
        return path;
    }

    public boolean isNodeAddedOrRemoved() {
        return op.equals("add") || op.equals("remove");
    }
}
