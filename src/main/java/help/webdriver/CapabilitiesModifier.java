package help.webdriver;

import java.io.File;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import help.config.PropertyProvider;
import help.config.TestProperties;
import help.metrics.ReportingContext;

public class CapabilitiesModifier extends OptionsModifier{
    private static Logger LOG = LoggerFactory.getLogger(CapabilitiesModifier.class);

    @Override
    public ChromeOptions chrome(ChromeOptions options) {
        Map<String, Object> prefs = new HashMap<>();
        if (StringUtils.isBlank(PropertyProvider.getProperty(TestProperties.WEBDRIVER_HUB_URL))) {
            if (!(new File(PropertyProvider.getProperty(TestProperties.BROWSER_DOWNLOAD_FILES_LOCATION)).mkdirs())) {
                LOG.warn("Dirs were not created");
            }
            options.addArguments("disable-infobars");
            String localDownloadLocation = Paths.get(System.getProperty("user.dir"), PropertyProvider.getProperty(TestProperties.BROWSER_DOWNLOAD_FILES_LOCATION)).toString();
            prefs.put("profile.default_content_settings.popups", 0);
            prefs.put("safebrowsing.enabled", true);
            prefs.put("download.default_directory", localDownloadLocation);
        }
        prefs.put("plugins.always_open_pdf_externally", true);
        options.setExperimentalOption("prefs", prefs);
        options.setCapability("name", ReportingContext.get().getCurrentTestName());
        options.setCapability("enableVNC", true);
        options.setCapability("chrome.switches", Collections.singletonList("--ignore-certificate-errors"));
        return allBrowsers(options);
    }

    @Override
    public FirefoxOptions firefox(FirefoxOptions options) {
        options.setCapability("name", ReportingContext.get().getCurrentTestName());
        options.setCapability("enableVNC", true);
        return allBrowsers(options);
    }
}
