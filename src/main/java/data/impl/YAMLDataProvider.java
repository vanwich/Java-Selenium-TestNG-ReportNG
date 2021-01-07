package data.impl;

import java.io.File;
import java.io.InputStream;
import java.util.Map;
import org.snakeyaml.engine.v2.api.Load;
import org.snakeyaml.engine.v2.api.LoadSettings;
import data.DataFormat;
import data.FileBasedDataProvider;
import data.TestData;
import data.TestDataException;

public class YAMLDataProvider extends FileBasedDataProvider {
    private YAMLDataProvider() {
        super();
    }

    public YAMLDataProvider(String groupPath) {
        super(groupPath, false);
    }

    public YAMLDataProvider(String groupPath, boolean globalOnly) {
        super(groupPath, globalOnly);
    }

    public YAMLDataProvider(String groupPath, String collectionName) {
        super(groupPath, collectionName);
    }

    @Override
    protected String makePath() {
        return new File(groupPath, collectionName + getExtensionSuffix()).getPath();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Map<String, Object> readTestData(InputStream is) {
        LoadSettings settings = LoadSettings.builder().build();
        Load load = new Load(settings);
        Object obj = stringify(load.loadFromInputStream(is));
        if (obj instanceof Map) {
            return (Map<String, Object>) obj;
        } else {
            throw new TestDataException("File " + filePath + " does not contain valid test data");
        }
    }

    @Override
    protected String getExtensionSuffix() {
        return "." + DataFormat.YAML.getExtension();
    }

    @Override
    protected TestData spawn() {
        YAMLDataProvider td = new YAMLDataProvider();
        td.groupPath = this.groupPath;
        td.collectionName = this.collectionName;
        return td;
    }
}
