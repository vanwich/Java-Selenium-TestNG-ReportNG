package data;

import java.io.File;
import java.util.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import com.google.common.collect.ImmutableMap;
import config.ClassConfigurator;
import config.HasConfiguration;
import data.impl.JSONDataProvider;
import data.impl.SimpleDataProvider;
import data.impl.XLSDataProvider;
import data.impl.YAMLDataProvider;

public class DataProviderFactory implements HasConfiguration {
    /**
     * Root test data path, relative to the working directory
     */
    @ClassConfigurator.Configurable
    protected static String rootPath = FilenameUtils.separatorsToSystem("src\\test\\resources\\testdata\\");

    /**
     * Name of default collection
     */
    @ClassConfigurator.Configurable
    private static String defaultCollectionName = "TestData";

    /**
     * Type of data provider to use (one of "XLS", "YAML", "JSON", etc.)
     */
    @ClassConfigurator.Configurable
    private static DataFormat baseProviderType = DataFormat.YAML;
    @ClassConfigurator.Configurable
    private DataFormat providerType = baseProviderType;

    private ClassConfigurator config;

    static {
        ClassConfigurator configurator = new ClassConfigurator(DataProviderFactory.class);
        configurator.applyConfiguration();
    }

    public DataProviderFactory() {
        config = new ClassConfigurator(this);
    }

    @Override
    public DataProviderFactory applyConfiguration(String configurationName){
        return (DataProviderFactory) config.applyConfiguration(configurationName);
    }

    /**
     * Create TestData object containing all collections under specified path.
     * Equivalent to {@link DataProviderFactory#get(String, boolean)} with globalOnly = false.
     * @param groupPath - path to test data group, relative to {@link #rootPath}.
     * @return created TestData object
     */
    public TestData get(String groupPath) {
        return get(groupPath, false);
    }

    /**
     * Create TestData object containing all collections under specified path.
     * For Excel groupPath should point to a workbook (without extension) and returned TestData will contain tables from all sheets.
     * For other data providers groupPath should point to a folder and returned TestData will contain data from all files in this folder.
     * @param groupPath - path to test data group, relative to {@link #rootPath}.
     * @param globalOnly - if true, only tables with workbook scope will be retrieved for Excel; for other providers - all children tables are lifted one level up (warning upon non-unique keys).
     * @return TestData object
     */
    public TestData get(String groupPath, boolean globalOnly) {
        String newPath = new File(DataProviderFactory.rootPath, groupPath).getPath();
        DataFormat effectiveProviderType = resolveMode(newPath, null);
        TestData provider;

        switch (effectiveProviderType) {
            case XLS:
                provider = globalOnly ? new XLSDataProvider(newPath, XLSDataProvider.GLOBAL_SCOPE) : new XLSDataProvider(newPath);
                break;
            case YAML:
                provider = new YAMLDataProvider(newPath, globalOnly);
                break;
            case JSON:
                provider = new JSONDataProvider(newPath, globalOnly);
                break;
            default:
                throw new IllegalArgumentException("Unsupported data format " + effectiveProviderType);
        }

        return provider;
    }

    /**
     * Create TestData object with specified collection.
     * For Excel groupPath should point to a workbook (without extension) and collectionName - to a sheet. Returned TestData will contain only tables with sheet scope.
     * For other data providers groupPath should point to a folder and collectionName - to file name (without extension). Returned TestData will contain data from this file.
     * @param groupPath - path to test data group, relative to {@link #rootPath}
     * @param collectionName - collection name. Can be null - equivalent to {@link #get(String)}.
     * @return TestData object
     */
    public TestData get(String groupPath, String collectionName) {
        String newPath = new File(DataProviderFactory.rootPath, groupPath).getPath();
        DataFormat effectiveProviderType = resolveMode(newPath, collectionName);
        TestData provider;

        switch (effectiveProviderType) {
            case XLS:
                provider = new XLSDataProvider(newPath, collectionName);
                break;
            case YAML:
                provider = new YAMLDataProvider(newPath, collectionName);
                break;
            case JSON:
                provider = new JSONDataProvider(newPath, collectionName);
                break;
            default:
                throw new IllegalArgumentException("Unsupported data format " + effectiveProviderType);
        }

        return provider;
    }

    /**
     * Create TestData object for specified test class
     * @param klass - test class
     * @return TestData object
     */
    public TestData get(Class<?> klass) {
        List<String> path = Arrays.asList(klass.getName().split("\\."));
        return get(StringUtils.join(path.subList(0, path.size() - 1), File.separator), path.get(path.size() - 1));
    }

    /**
     * Create default test data for specified class (collection named {@link #defaultCollectionName}.
     * @param klass - test class
     * @return TestData object
     */
    public TestData getDefault(Class<?> klass) {
        return get(klass).getTestData(DataProviderFactory.defaultCollectionName);
    }

    private DataFormat resolveMode(String groupPath, final String collectionName) {
        if (providerType == DataFormat.AUTO) {
            String fileNamePattern = (collectionName == null) ? ".+" : collectionName;
            Map<DataFormat, Boolean> checks = new HashMap<DataFormat, Boolean>();

            for (DataFormat type : DataFormat.values()) {
                Boolean found;
                switch (type) {
                    case AUTO:
                        continue;
                    case XLS:
                        found = XLSDataProvider.isValidDataStorage(groupPath, fileNamePattern, type.getExtension());
                        break;
                    default:
                        found = FileBasedDataProvider.isValidDataStorage(groupPath, fileNamePattern, type.getExtension());
                        break;
                }

                checks.put(type, found);
            }

            DataFormat trueType = null;
            int total = 0;
            for (Map.Entry<DataFormat, Boolean> entry : checks.entrySet()) {
                if (entry.getValue()) {
                    trueType = entry.getKey();
                    total++;
                }
            }

            if (total == 1) {
                return trueType;
            } else {
                String msg = (total == 0) ? "no files with supported extensions " : "files with different extensions ";
                throw new TestDataException("Cannot autoresolve data provider type for group " + groupPath + " and collection " + collectionName
                        + ": " + msg);
            }
        } else {
            return providerType;
        }
    }

    /**
     * Create empty test data on the fly
     * @return empty TestData object
     */
    public static TestData emptyData() {
        return new SimpleDataProvider();
    }

    /**
     * Construct test data with one key-value pair
     * @param k1 key
     * @param v1 value
     * @return TestData object
     */
    public static TestData dataOf(String k1, Object v1) {
        return new SimpleDataProvider(ImmutableMap.of(k1, v1));
    }

    /**
     * Construct test data with two key-value pairs
     * @param k1 key #1
     * @param v1 value #1
     * @param k2 key #2
     * @param v2 value #2
     * @return TestData object
     */
    public static TestData dataOf(String k1, Object v1, String k2, Object v2) {
        return new SimpleDataProvider(ImmutableMap.of(k1, v1, k2, v2));
    }

    /**
     * Construct test data with three key-value pairs
     * @param k1 key #1
     * @param v1 value #1
     * @param k2 key #2
     * @param v2 value #2
     * @param k3 key #3
     * @param v3 value #3
     * @return TestData object
     */
    public static TestData dataOf(String k1, Object v1, String k2, Object v2, String k3, Object v3) {
        return new SimpleDataProvider(ImmutableMap.of(k1, v1, k2, v2, k3, v3));
    }

    /**
     * Construct test data with arbitrary number key-value pairs
     * @param kvs array of keys and values. Keys must be Strings and the array must have even length
     * @return TestData object
     */
    public static TestData dataOf(Object ... kvs) {
        if (kvs.length % 2 == 0) {
            Map<String, Object> rawData = new LinkedHashMap<>();
            for (int i = 0; i < kvs.length / 2; i++) {
                if (kvs[i * 2] instanceof String) {
                    rawData.put((String) kvs[i * 2], kvs[i * 2 + 1]);
                } else {
                    throw new IllegalArgumentException("Argument at position " + (i * 2) + " is not a String. Actual value: " + kvs[i * 2]);
                }
            }
            return new SimpleDataProvider(rawData);
        } else {
            throw new IllegalArgumentException("Method must have even number of arguments");
        }
    }
}
