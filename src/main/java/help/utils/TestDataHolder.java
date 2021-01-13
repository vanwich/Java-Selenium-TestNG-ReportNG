package help.utils;

import static java.util.Arrays.asList;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import help.TestDataProvider;
import help.data.DataProviderFactory;
import help.data.TestData;
import help.data.TestDataException;

public class TestDataHolder {
    public static DataProviderFactory dataProviderJson = TestDataProvider.getJSONTestDataProvider();


    public static TestData tdAgentCustomers = dataProviderJson.get("modules/dxp/agent/customers");
    public static TestData tdAgentDocuments = dataProviderJson.get("modules/dxp/agent/documents");
    public static TestData tdAgentNotes = dataProviderJson.get("modules/dxp/agent/notes");
    public static TestData tdAgentPolicies = dataProviderJson.get("modules/dxp/agent/policies");
    public static TestData tdAgentTask = dataProviderJson.get("modules/dxp/agent/task");
    public static TestData tdCustomer = dataProviderJson.get("modules/customer");
    public static TestData tdLead = dataProviderJson.get("modules/dxp/lead");
    public static TestData tdPolicies = dataProviderJson.get("modules/dxp/policies");
    public static TestData tdAuto = dataProviderJson.get("modules/dxp/quickquote/auto");
    public static TestData tdQuickquote = dataProviderJson.get("modules/dxp/quickquote");
    public static TestData tdRenewalsAuto = dataProviderJson.get("modules/dxp/renewals/auto");



    protected TestData tdSpecific() {
        try {
            List<String> path = asList(this.getClass().getName().split("\\."));
            return dataProviderJson.get(StringUtils.join(path.subList(0, path.size() - 1), File.separator).replace(FilenameUtils.separatorsToSystem("com\\exigen\\hot"), ""),
                    path.get(path.size() - 1));
        } catch (TestDataException tde) {
            throw new TestDataException("Specified TestData for test is absent for test class " + this.getClass().getName(), tde);
        }
    }

    public TestData tdSpecific(Class<?> clazz) {
        List<String> path = Arrays.asList(clazz.getName().split("\\."));
        TestData testData;
        testData = dataProviderJson.get(
                StringUtils.join(path.subList(0, path.size() - 1), File.separator)
                        .replace(FilenameUtils.separatorsToSystem("com\\exigen\\hot"), ""),
                path.get(path.size() - 1));
        return testData;
    }
}
