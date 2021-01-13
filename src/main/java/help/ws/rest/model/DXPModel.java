package help.ws.rest.model;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import help.data.TestData;
import help.ws.rest.conf.client.DXPRestConfiguration;
import help.ws.rest.conf.client.IClientConfiguration;
import help.ws.rest.filter.AbstractFilter;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFilter(AbstractFilter.NAME)
public class DXPModel extends Model implements Serializable {
    public DXPModel(TestData testData) {
        super(testData);
    }

    public DXPModel() {
    }

    public IClientConfiguration setUpAdjustConfiguration() {
        return DXPRestConfiguration.INSTANCE;
    }
}
