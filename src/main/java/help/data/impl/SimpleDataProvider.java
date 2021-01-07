package help.data.impl;

import java.util.Map;
import help.data.TestData;

public class SimpleDataProvider extends TestData {
    /**
     * Construct empty data provider
     */
    public SimpleDataProvider() {
        super("", "");
    }

    /**
     * Construct data provider from hash
     * @param rawData hash with initial data
     */
    public SimpleDataProvider(Map<String, ? extends Object> rawData) {
        this();
        @SuppressWarnings("unchecked")
        Map<String, Object> tmp = (Map<String, Object>) stringify(rawData);
        internalData = tmp;
    }

    @Override
    protected TestData spawn(String groupPath, String collectionName) {
        throw new UnsupportedOperationException("Linked data is not allowed in " + this.getClass().getSimpleName());
    }

    @Override
    protected TestData spawn() {
        SimpleDataProvider td = new SimpleDataProvider();
        td.groupPath = this.groupPath;
        td.collectionName = this.collectionName;
        return td;
    }
}
