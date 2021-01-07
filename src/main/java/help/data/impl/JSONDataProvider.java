package help.data.impl;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;
import com.google.gson.Gson;
import help.data.DataFormat;
import help.data.FileBasedDataProvider;
import help.data.TestData;
import help.data.TestDataException;

public class JSONDataProvider extends FileBasedDataProvider {
    private JSONDataProvider() {
        super();
    }

    public JSONDataProvider(String groupPath) {
        super(groupPath, false);
    }

    public JSONDataProvider(String groupPath, boolean globalOnly) {
        super(groupPath, globalOnly);
    }

    public JSONDataProvider(String groupPath, String collectionName) {
        super(groupPath, collectionName);
    }

    @Override
    protected String makePath() {
        return new File(groupPath, collectionName + getExtensionSuffix()).getPath();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Map<String, Object> readTestData(InputStream is) {
        Gson gson = new Gson();
        LinkedHashMap<?, ?> jsonData;
        try {
            jsonData = (LinkedHashMap<?, ?>) gson.fromJson(new InputStreamReader(is), LinkedHashMap.class);
        } catch (Exception e) {
            throw new TestDataException("Error reading test data from " + filePath, e);
        }

        return (Map<String, Object>) stringify(jsonData);
    }

    @Override
    protected String getExtensionSuffix() {
        return "." + DataFormat.JSON.getExtension();
    }

    @Override
    protected TestData spawn() {
        JSONDataProvider td = new JSONDataProvider();
        td.groupPath = this.groupPath;
        td.collectionName = this.collectionName;
        return td;
    }
}
