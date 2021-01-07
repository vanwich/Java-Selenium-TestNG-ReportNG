package webdriver;

import static java.util.Objects.nonNull;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import config.PropertyProvider;
import config.TestProperties;
import utils.meters.WaitMeters;

public class Browser {
    protected static final Logger log = LoggerFactory.getLogger(Browser.class);
    private WebDriver driver;
    private Optional<LogEntries> logEntries = Optional.empty();
    private final static Optional<BrowserWindow> browserWindowOpt;
    public final static boolean isLoggingEnabled =
            StringUtils.isNotBlank(PropertyProvider.getProperty(TestProperties.WEBDRIVER_LOG_LEVEL_BROWSER, null));

    static {
        if (PropertyProvider.getProperty(TestProperties.WEBDRIVER_HUB_URL).isEmpty()) {
            browserWindowOpt = Optional.of(new BrowserWindow());
        } else {
            browserWindowOpt = Optional.empty();
        }
    }

    /**
     * Create Browser instance from the provided driver
     * @param driver WebDriver instance
     */
    Browser(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Return underlying WebDriver instance
     * @return WebDriver instance
     */
    public WebDriver driver() {
        return driver;
    }

    /**
     * Return WebElement for specified BaseElement
     * @param be BaseElement
     * @return WebElement
     */
    public WebElement getElement(BaseElement<?, ?> be) {
        return findElement(be);
    }

    /**
     * Find element for specified BaseElement
     * @param be BaseElement
     * @return WebElement
     */
    protected WebElement findElement(BaseElement<?, ?> be) {
        BaseElement<?, ?> pbe = be.getParent();
        SearchContext sc = (pbe == null) ? driver() : getElement(pbe);
        return sc.findElement(be.getLocator());
    }

    /**
     * Load specified URL
     * @param url URL to load
     */
    public void open(String url) {
        driver().get(url);
        WaitMeters.capture(WaitMeters.PAGE_LOAD);
    }

    /**
     * Close browser and quit
     */
    public void quit() {
        if (isLoggingEnabled) {
            try {
                logEntries = Optional.of(driver().manage().logs().get(LogType.BROWSER));
            } catch (Exception e) {
                log.warn("Unexpected exception during browser log retrieval", e);
            }
        }
        BrowserController.sessionClosed();
        try {
            driver().quit();
        } catch (Exception e) {
            log.warn("Unexpected exception during WebDriver#quit", e);
        }
    }

    /**
     * Maximize current browser window
     */
    public void maximize() {
        driver().manage().window().maximize();
    }

    /**
     * Refresh browser page
     */
    public void refresh() {
        driver().navigate().refresh();
        WaitMeters.capture(WaitMeters.PAGE_LOAD);
    }

    /**
     * Execute JavaScript code
     * @param script JS code to execute
     * @param args script arguments
     * @return script return value
     */
    public Object executeScript(String script, Object... args) {
        return ((JavascriptExecutor) driver()).executeScript(script, args);
    }

    /**
     * Get browser name
     * @return browser name (one of BrowserType constants)
     */
    public String getBrowserName() {
        return ((RemoteWebDriver) driver()).getCapabilities().getBrowserName();
    }

    /**
     * Get platform
     * @return platform
     */
    public Platform getPlatform() {
        return ((RemoteWebDriver) driver()).getCapabilities().getPlatform();
    }

    /**
     * Get actions builder
     * @return instance of Actions
     */
    public Actions getActions() {
        return new Actions(driver());
    }

    /**
     * Check if current browser is Chrome
     * @return true for Chrome
     */
    public boolean isChrome() {
        String name = getBrowserName();
        return BrowserType.CHROME.equals(name) || BrowserType.GOOGLECHROME.equals(name);
    }

    /**
     * Check if current browser is Firefox
     * @return true for Firefox
     */
    public boolean isFF() {
        return BrowserType.FIREFOX.equals(getBrowserName());
    }

    /**
     * Check if current browser is Internet Explorer
     * @return true for Internet Explorer
     */
    public boolean isIE() {
        return BrowserType.IE.equals(getBrowserName());
    }

    /**
     * Set browser window layout for local runs
     * See {@link TestProperties#TEST_BROWSER_MONITOR_NUMBER}
     * and {@link TestProperties#TEST_BROWSER_WINDOW_LAYOUT}
     */
    public void setBrowserWindowLayout() {
        browserWindowOpt.ifPresent(window -> {
            if (nonNull(window.getTopLeftPoint())) {
                Point windowPosition = window.getTopLeftPoint();
                BrowserController.get().driver().manage().window().setPosition(windowPosition);
                if (!BrowserController.get().driver().manage().window().getPosition().equals(windowPosition)) {
                    // after moving window to non-default monitor sometimes we need to set position twice
                    BrowserController.get().driver().manage().window().setPosition(windowPosition);
                }
            }
            if (nonNull(window.getSize())) {
                BrowserController.get().driver().manage().window().setSize(window.getSize());
            } else {
                BrowserController.get().driver().manage().window().maximize();
            }
        });
    }

    /**
     * Get browser logs (if enabled)
     * @return optional of browser log entries
     */
    public Optional<LogEntries> getLogs() {
        if (isLoggingEnabled) {
            try {
                return Optional.of(driver().manage().logs().get(LogType.BROWSER));
            } catch (NoSuchSessionException ne) {
                //	if it was called after quit(), fetch previously stashed log entries
                return logEntries;
            } catch (Exception e) {
                log.error("Failed to retrieve browser logs", e);
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }
}
