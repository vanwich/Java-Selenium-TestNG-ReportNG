package help;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import help.data.DataFormat;
import help.data.DataProviderFactory;
import help.data.TestData;
import help.data.TestDataException;
import help.utils.i18n.I18nResolver;

public final class TestDataProvider {
    public static DataProviderFactory getDefaultTestDataProvider() {
        return InstanceHolder.DATA_PROVIDER_FACTORY;
    }

    public static DataProviderFactory getJSONTestDataProvider() {
        return JSONInstanceHolder.DATA_PROVIDER_FACTORY;
    }

    /**
     * Returns all TestData under specified path
     *
     * @param path - path to folder with Test Data files
     * @return {@link TestData}
     */
    public static TestData getTestData(String path) {
        if (!I18nResolver.isDefaultLocale()) {
            path = Paths.get(I18nResolver.DEFAULT_I18N_PATH, I18nResolver.locale, path).toString();
        }
        return getDefaultTestDataProvider().get(Paths.get(path).toString());
    }

    /**
     * Gets specific test data according to provided class name and its package with ability to truncate extra packages
     *
     * @param clazz                     test class
     * @param amountOfPackagesToExclude amount of packages to exclude (starting from left)
     *                                  (i.e.: com.exigen.ipb.eisa.TestDataProvider class with amountOfPackagesToExclude=2 will became ipb.eisa.TestDataProvider and look for TestDataProvider.yaml in ibp/eisa/ folder)
     * @return {@link TestData}
     */
    public static TestData getTestData(Class<?> clazz, int amountOfPackagesToExclude) {
        List<String> path = Arrays.asList(clazz.getName().split("\\."));
        if (path.size() - 1 < amountOfPackagesToExclude || amountOfPackagesToExclude < 0) {
            throw new RuntimeException(String.format("Unable to exclude %d packages from [%s]", amountOfPackagesToExclude, clazz.getName()));
        }
        String resolvedPath = String.join(File.separator, path.subList(amountOfPackagesToExclude, path.size() - 1));
        if (!I18nResolver.isDefaultLocale()) {
            resolvedPath = Paths.get(I18nResolver.DEFAULT_I18N_PATH, I18nResolver.locale, resolvedPath).toString();
        }
        TestData testData;
        try {
            testData = getDefaultTestDataProvider().get(resolvedPath, path.get(path.size() - 1));
        } catch (TestDataException e) {
            throw new TestDataException(String.format("Specific test data is absent for class [%1$s]", clazz.getName()));
        }
        return testData;
    }

    private static class InstanceHolder {
        private static final DataProviderFactory DATA_PROVIDER_FACTORY = new DataProviderFactory().applyConfiguration(DataFormat.YAML.name());
    }

    private static class JSONInstanceHolder {
        private static final DataProviderFactory DATA_PROVIDER_FACTORY = new DataProviderFactory().applyConfiguration(DataFormat.JSON.name());
    }
}
