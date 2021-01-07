package help.application;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.function.Supplier;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.*;

import org.openqa.selenium.remote.internal.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.github.rholder.retry.*;
import help.config.ClassConfigurator;
import help.config.PropertyProvider;
import help.config.TestProperties;
import help.exceptions.JstrException;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobilePlatform;
import help.utils.meters.WaitMeters;
import help.webdriver.OptionsModifier;
import help.webdriver.WebDriverRetryPredicate;

public class WebDriverFactory {
    private static final Logger log = LoggerFactory.getLogger(WebDriverFactory.class);

    private static final String PROFILE = PropertyProvider.getProperty(TestProperties.WEBDRIVER_PROFILE);
    private static final String HUB_URL = PropertyProvider.getProperty(TestProperties.WEBDRIVER_HUB_URL);
    private static final long IMPLICIT_WAIT = PropertyProvider.getProperty(TestProperties.WEBDRIVER_IMPLICIT_WAIT, 0L);

    private static final String IE_DRIVER_SYSTEM_PROPERTY = "webdriver.ie.driver";
    private static final String CHROME_DRIVER_SYSTEM_PROPERTY = "webdriver.chrome.driver";
    private static final String GECKO_DRIVER_SYSTEM_PROPERTY = "webdriver.gecko.driver";
    @ClassConfigurator.Configurable(byClassName = true)
    private static OptionsModifier optionsModifier = new OptionsModifier();
    @ClassConfigurator.Configurable(byClassName = true)
    private static Predicate<Throwable> retryPredicate = new WebDriverRetryPredicate();
    @ClassConfigurator.Configurable(byClassName = true)
    private static Supplier<WaitStrategy> waitStrategy = () -> WaitStrategies.fixedWait(PropertyProvider.getProperty(TestProperties.WEBDRIVER_REMOTE_SESSION_CREATION_MAX_WAIT, 60L), TimeUnit.SECONDS);
    @ClassConfigurator.Configurable(byClassName = true)
    private static Supplier<StopStrategy> stopStrategy = () -> StopStrategies.stopAfterDelay(PropertyProvider.getProperty(TestProperties.WEBDRIVER_REMOTE_SESSION_CREATION_TIMEOUT, 12 * 60 * 60), TimeUnit.SECONDS);

    private static final boolean useCustomHttpClientFactory = PropertyProvider.getProperty(TestProperties.USE_CUSTOM_HTTP_CLIENT_FACTORY, true);
    private static Duration connTimeout = Duration.ofMillis(PropertyProvider.getProperty(TestProperties.WEBDRIVER_REMOTE_CONNECTION_TIMEOUT, TimeUnit.MINUTES.toMillis(2)));
    private static Duration soTimeout = Duration.ofMillis(PropertyProvider.getProperty(TestProperties.WEBDRIVER_REMOTE_SOCKET_TIMEOUT, TimeUnit.HOURS.toMillis(3)));

    static {
        ClassConfigurator configurator = new ClassConfigurator(WebDriverFactory.class);
        configurator.applyConfiguration();
    }

    private WebDriverFactory() {}

    //	TODO remove this after migration to Selenium 4.x
    public static class CustomHttpClientFactory extends OkHttpClient.Factory {
        //	https://github.com/square/okhttp/issues/2846
        //	in reality this map is unlikely to ever contain more than one entry. this is just a temporary hack.
        private static ConcurrentHashMap<String, HttpClient> clientMap = new ConcurrentHashMap<>();

        private final Duration connTimeout;
        private final Duration soTimeout;

        public CustomHttpClientFactory(Duration connTimeout, Duration soTimeout) {
            this.connTimeout = connTimeout;
            this.soTimeout = soTimeout;
        }

        @Override
        public org.openqa.selenium.remote.http.HttpClient createClient(final URL url) {
            return (org.openqa.selenium.remote.http.HttpClient) clientMap.computeIfAbsent(url.toString(), strUrl -> (HttpClient) super.createClient(url));
        }

        @Override
        public org.openqa.selenium.remote.http.HttpClient.Builder builder() {
            return super.builder().connectionTimeout(connTimeout).readTimeout(soTimeout);
        }
    }

    private static boolean isChromeMobile(Capabilities caps) {
        return MobilePlatform.ANDROID.equals(caps.getCapability("platformName")) &&
                BrowserType.CHROME.equals(caps.getCapability("browserName"));
    }

    private static RemoteWebDriver buildRemoteWebDriver(String wdUrl, Capabilities caps) {
        URL url;
        try {
            url = new URL(wdUrl);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Failed to build remote WebDriver URL from string " + wdUrl, e);
        }

        Callable<RemoteWebDriver> rwdSupplier;
        if (useCustomHttpClientFactory) {
            rwdSupplier = () -> {
                //	https://github.com/SeleniumHQ/selenium/issues/6786
                OkHttpClient.Factory clientFactory = new CustomHttpClientFactory(connTimeout, soTimeout);
                HttpCommandExecutor executor = new HttpCommandExecutor(new HashMap<String, CommandInfo>(), url, clientFactory);
                log.debug("Attempting to create RemoteWebDriver session");
                if (isChromeMobile(caps)) {
                    return new AndroidDriver<>(executor, caps);
                } else {
                    return new RemoteWebDriver(executor, caps);
                }
            };
        } else {
            rwdSupplier = () -> {
                if (isChromeMobile(caps)) {
                    return new AndroidDriver<>(url, caps);
                } else {
                    return new RemoteWebDriver(url, caps);
                }
            };
        }

        RemoteWebDriver rwd;
        Retryer<RemoteWebDriver> retryer = RetryerBuilder.<RemoteWebDriver>newBuilder()
                .retryIfException(retryPredicate::test)
                .withWaitStrategy(waitStrategy.get())
                .withStopStrategy(stopStrategy.get())
                .build();

        WaitMeters.activate(WaitMeters.PEF_GET_BROWSER);
        try {
            rwd = retryer.call(rwdSupplier);
        } catch (ExecutionException e) {
            throw new JstrException("Unexpected exception on RemoteWebDriver instantiation", e.getCause());
        } catch (RetryException e) {
            throw new JstrException("Failed to instantiate RemoteWebDriver", e);
        } finally {
            WaitMeters.capture(WaitMeters.PEF_GET_BROWSER);
        }
        log.debug("Created remote WebDriver session " + rwd.getSessionId());
        return rwd;
    }

    /**
     * Get WebDriver instance using current properties
     * @return WebDriver instance
     */
    public static WebDriver getDriver() {
        return getDriver(HUB_URL, PROFILE);
    }

    /**
     * Get WebDriver using explicit arguments
     * @param wdUrl WebDriver Hub URL
     * @param profile desired browser type
     * @return WebDriver instance
     */
    public static WebDriver getDriver(String wdUrl, String profile) {
        boolean isLocal = StringUtils.isBlank(wdUrl);
        WebDriver wd = null;

        switch (profile.toLowerCase()) {
            case "ff":
            case "firefox":
                FirefoxOptions fo = new FirefoxOptions();
                if (System.getProperty(FirefoxDriver.SystemProperty.DRIVER_USE_MARIONETTE) == null) {
                    //	default to legacy firefoxdriver
                    fo.setLegacy(!PropertyProvider.getProperty(TestProperties.WEBDRIVER_USE_MARIONETTE, false));
                }
                fo = optionsModifier.firefox(fo);
                if (isLocal) {
                    setSystemProperty(GECKO_DRIVER_SYSTEM_PROPERTY, TestProperties.WEBDRIVER_GECKO_DRIVER);
                    wd = new FirefoxDriver(fo);
                } else {
                    wd = buildRemoteWebDriver(wdUrl, fo);
                }
                break;
            case "ie":
            case "iexplore":
                InternetExplorerOptions io = optionsModifier.iexplore(new InternetExplorerOptions());

                if (isLocal) {
                    setSystemProperty(IE_DRIVER_SYSTEM_PROPERTY, TestProperties.WEBDRIVER_IE_DRIVER_SERVER);
                    wd = new InternetExplorerDriver(io);
                } else {
                    wd = buildRemoteWebDriver(wdUrl, io);
                }
                break;
            case "chrome":
            case "googlechrome":
                ChromeOptions co = optionsModifier.chrome(new ChromeOptions());
                if (isLocal) {
                    setSystemProperty(CHROME_DRIVER_SYSTEM_PROPERTY, TestProperties.WEBDRIVER_CHROME_DRIVER);
                    wd = new ChromeDriver(co);
                } else {
                    wd = buildRemoteWebDriver(wdUrl, co);
                }
                break;
            case "chromemobile":
                ChromeOptions cao = optionsModifier.chromeMobile(new ChromeOptions());
                if (isLocal) {
                    throw new IllegalArgumentException("Local runs on Chrome Mobile are not supported");
                } else {
                    wd = buildRemoteWebDriver(wdUrl, cao);
                }
                break;
            default:
                throw new IllegalArgumentException("Unsupported browser '" + profile + "'");
        }

        if (IMPLICIT_WAIT > 0) {
            wd.manage().timeouts().implicitlyWait(IMPLICIT_WAIT, TimeUnit.MILLISECONDS);
        }

        if (!isLocal) {
            ((RemoteWebDriver) wd).setFileDetector(new LocalFileDetector());
        }
        return wd;
    }

    private static void setSystemProperty(String sysPropName, String confPropName) {
        String propVal = PropertyProvider.getProperty(confPropName);
        if (!StringUtils.isBlank(propVal)) {
            System.setProperty(sysPropName, propVal);
        }
    }
}
