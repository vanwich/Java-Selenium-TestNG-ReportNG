package webdriver;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import config.PropertyProvider;
import config.TestProperties;
import exceptions.JstrException;
import io.appium.java_client.remote.MobilePlatform;
import metrics.ReportingContext;
import webdriver.video.VideoService;

public class OptionsModifier {
    private static final Logger log = LoggerFactory.getLogger(OptionsModifier.class);

    protected <C extends MutableCapabilities> C allBrowsers(C options) {
        options.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.IGNORE);
        options.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);

        Properties capProps = new Properties();
        String capsResource = PropertyProvider.getProperty(TestProperties.WEBDRIVER_CUSTOM_CAPABILITIES_RESOURCE);

        if (!StringUtils.isBlank(capsResource)) {
            try {
                capProps.load(this.getClass().getResourceAsStream(capsResource));
            } catch (IOException e) {
                throw new JstrException("Failed to load browser capabilities from " + capsResource, e);
            }

            for (Map.Entry<Object, Object> capEntry : capProps.entrySet()) {
                options.setCapability(capEntry.getKey().toString(), capEntry.getValue());
            }
        }
        //	Selenoid-specific capabilities
        options.setCapability("enableVNC", true);
        if (VideoService.isVideoRecordingEnabled()) {
            options.setCapability("enableVideo", true);
            //	NOTE: If this pattern is changed, you will also have to reconfigure sessionArtifactUriProvider in TA listener! Seriously, why not leave it alone?
            options.setCapability("s3KeyPattern", String.format("%1$s/%2$s/$fileName", ReportingContext.get().getRunId(), ReportingContext.get().getTestId()));
        }
        return options;
    }

    /**
     * Modify options for Firefox
     * @param options FirefoxOptions object
     * @return modified options object
     */
    public FirefoxOptions firefox(FirefoxOptions options) {
        return allBrowsers(options);

		/*	//	to enable sending browser console logs to geckodriver stdout
		FirefoxOptions retOptions = allBrowsers(options);
		retOptions.setLogLevel(FirefoxDriverLogLevel.TRACE);
		retOptions.addPreference("devtools.console.stdout.content", true);
		return retOptions;*/
    }

    /**
     * Modify options for IE
     * @param options InternetExplorerOptions object
     * @return modified options object
     */
    public InternetExplorerOptions iexplore(InternetExplorerOptions options) {
        options.setCapability(InternetExplorerDriver.REQUIRE_WINDOW_FOCUS, false);
        //	if nativeEvents are off, ctrl-a does not work ('a' character is typed)
        //options.setCapability(CapabilityType.HAS_NATIVE_EVENTS, false);
        options.setCapability(CapabilityType.SUPPORTS_JAVASCRIPT, true);
        options.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
        options.setCapability(InternetExplorerDriver.IE_SWITCHES, Arrays.asList("-private"));
        options.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
        options.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
        options.setCapability(InternetExplorerDriver.IE_USE_PER_PROCESS_PROXY, true);
        options.takeFullPageScreenshot();
        return allBrowsers(options);
    }

    /**
     * Modify options for Chrome
     * @param options ChromeOptions object
     * @return modified options object
     */
    public ChromeOptions chrome(ChromeOptions options) {
        ChromeOptions retOptions = allBrowsers(options);
        options.setCapability("chrome.switches", Collections.singletonList("--ignore-certificate-errors"));
        options.setHeadless(PropertyProvider.getProperty(TestProperties.WEBDRIVER_HEADLESS_MODE, false));

        String strLogLevel = PropertyProvider.getProperty(TestProperties.WEBDRIVER_LOG_LEVEL_BROWSER, null);
        if (StringUtils.isNotBlank(strLogLevel)) {
            try {
                Level logLevel = Level.parse(strLogLevel);
                LoggingPreferences logs = new LoggingPreferences();
                logs.enable(LogType.BROWSER, logLevel);
                //	https://github.com/SeleniumHQ/selenium/issues/7342
                options.setCapability("goog:loggingPrefs", logs);
            } catch (IllegalArgumentException e) {
                log.error("Failed to parse value of " + TestProperties.WEBDRIVER_LOG_LEVEL_BROWSER, e);
            }
        }
        return retOptions;
    }

    /**
     * Modify options for Chrome Mobile (Android)
     * @param options ChromeOptions object
     * @return modified options object
     */
    public ChromeOptions chromeMobile(ChromeOptions options) {
        ChromeOptions retOptions = allBrowsers(options);
        retOptions.setCapability("platformName", MobilePlatform.ANDROID);
        retOptions.setCapability("deviceName", "chrome");
        retOptions.setCapability("androidInstallTimeout", 150000);
        retOptions.setCapability("noReset", true);
        retOptions.setCapability("autoGrantPermissions", true);
        retOptions.setCapability("adbExecTimeout", 20000);
        retOptions.setCapability("forceMjsonwp",true);
        retOptions.setExperimentalOption("androidPackage", "com.android.chrome");
        return retOptions;
    }
}
