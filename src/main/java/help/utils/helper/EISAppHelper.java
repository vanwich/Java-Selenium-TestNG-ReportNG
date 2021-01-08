package help.utils.helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ByAll;
import org.openqa.selenium.support.pagefactory.ByChained;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import help.application.ApplicationFactory;
import help.config.ClassConfigurator;
import help.config.PropertyProvider;
import help.metrics.ReportingContext;
import help.webdriver.BrowserController;

public class EISAppHelper implements IAppHelper{
    protected static final Logger LOGGER = LoggerFactory.getLogger(EISAppHelper.class);

    @ClassConfigurator.Configurable
    private static String urlPostfix = "/login.xhtml";
    @ClassConfigurator.Configurable
    private static String locatorContainer = "//body";
    @ClassConfigurator.Configurable
    private static String locatorApplicationException = "//form[@id='loginForm' or @id='errorForm']/table/tbody/tr/td";
    @ClassConfigurator.Configurable
    private static String locatorEISException = "//form[@id='errorsForm']//table/tbody[1]//td[3]";
    @ClassConfigurator.Configurable
    private static String locatorFormException = ".//div[@class='error-container']//span[text() != '']";

    private static final ThreadLocal<String> uniqueId = ThreadLocal.withInitial(() -> Long.toString(System.currentTimeMillis()));

    static {
        ClassConfigurator configurator = new ClassConfigurator(EISAppHelper.class);
        configurator.applyConfiguration();
    }

    public static EISAppHelper getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public String getGroupId() {
        String groupId = System.getenv("RUN_GROUP_ID");
        if (groupId == null) {
            groupId = PropertyProvider.getProperty("storage.runProperties.groupId", (String) null);
        }

        return groupId;
    }

    public String getTestId() {
        String groupId = getGroupId();
        String testId = ReportingContext.get().getCurrentTestName().replace(".", "_");
        if (testId.contains("(")) {
            testId = testId.substring(0, testId.indexOf("("));
        }
        if (groupId != null) {
            testId = groupId.concat("_").concat(testId);
        }
        return testId.concat("_").concat(uniqueId.get());
    }

    public String getBuildInfo(String url) {
        String buildInfo = null;
        try {
            buildInfo = Jsoup.connect(url).get().title();
        } catch (IOException e) {
            LOGGER.warn("Can't find build info on the page for specified URL {}. Original exception message: {}", url, e.getMessage());
        }
        return buildInfo;
    }

    @Override
    public String getBuildInfo() {
        return getBuildInfo(urlPostfix);
    }

    @Override
    public String fetchKnownException() {
        String exception = "";
        List<WebElement> elements;
        List<String> errors = new ArrayList<>();

        try {
            WebDriver driver = BrowserController.get().driver();
            elements = driver.findElements(new ByAll(By.xpath(locatorApplicationException), By.xpath(locatorEISException)));
            if (elements.isEmpty()) {
                elements = driver.findElements(new ByChained(By.xpath(locatorContainer), By.xpath(locatorFormException)));
            }

            elements.forEach(el -> errors.add(el.getText()));

            if (!errors.isEmpty()) {
                exception = "Errors: " + StringUtils.join(errors, "; ");
            }
        } catch (Exception e) {
            //	NOOP
        }
        return exception;
    }

    private static class InstanceHolder {
        private static EISAppHelper INSTANCE = new EISAppHelper();
    }
}
