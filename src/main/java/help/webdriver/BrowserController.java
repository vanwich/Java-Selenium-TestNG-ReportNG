package help.webdriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;

import help.config.ClassConfigurator;
import help.config.PropertyProvider;
import help.config.TestProperties;
import help.exceptions.JstrException;

public final class BrowserController {
    private static final boolean ELEMENT_CACHE_ENABLE = PropertyProvider.getProperty(TestProperties.WEBDRIVER_ELEMENT_CACHE_ENABLE, false);

    private static ThreadLocal<Browser> browserInstance = new ThreadLocal<Browser>();
    @ClassConfigurator.Configurable(byClassName = true) private static Supplier<WebDriver> wdSupplier = () -> WebDriverFactory.getDriver();
    @ClassConfigurator.Configurable(byClassName = true) private static BiConsumer<String, Boolean> sessionListener = (sid, isOpening) -> {};

    private static ThreadLocal<List<String>> sessionStorage = ThreadLocal.withInitial(() -> new ArrayList<>());

    static {
        ClassConfigurator configurator = new ClassConfigurator(BrowserController.class);
        configurator.applyConfiguration();
    }

    private BrowserController() {}

    /**
     * Get browser instance
     * @return Browser
     */
    public static Browser get() {
        if (isInitialized()) {
            return browserInstance.get();
        } else {
            throw new JstrException("Browser has not been initialized!");
        }
    }

    /**
     * Initialize browser with newly constructed WebDriver instance (for use with local execution or Selenium Grid)
     */
    public static void initBrowser() {
        initBrowser(wdSupplier.get());
    }

    /**
     * Initialize browser using existing WebDriver instance (for use with PEF)
     * @param newDriver WebDriver instance
     */
    public static void initBrowser(WebDriver newDriver) {
        Browser browser;
        if (ELEMENT_CACHE_ENABLE) {
            browser = new CachingBrowser(newDriver);
        } else {
            browser = new Browser(newDriver);
        }
        browserInstance.set(browser);
        if (newDriver instanceof RemoteWebDriver) {
            String sessionId = ((RemoteWebDriver) newDriver).getSessionId().toString();
            sessionListener.accept(sessionId, true);
            sessionStorage.get().add(sessionId);
        }
    }

    /**
     * Check whether browser has been initialized
     * @return true if browser is initialized
     */
    public static boolean isInitialized() {
        return browserInstance.get() != null;
    }

    /**
     * Perform some operation with browser if it is initialized and return its result
     * @param <T> operation return type
     * @param fn operation to perform
     * @return result of the operation
     */
    public static <T> Optional<T> withBrowser(Function<Browser, T> fn) {
        if (isInitialized()) {
            return Optional.ofNullable(fn.apply(browserInstance.get()));
        } else {
            return Optional.empty();
        }
    }

    /**
     * Perform some operation with browser if it is initialized
     * @param fn operation to perform
     */
    public static void withBrowser(Consumer<Browser> fn) {
        if (isInitialized()) {
            fn.accept(browserInstance.get());
        }
    }

    /**
     * Get host name where WebDriver should be launched
     * @return host part of the currently configured WebDriver URL
     */
    public static String getHost() {
        String wdUrl = PropertyProvider.getProperty(TestProperties.WEBDRIVER_HUB_URL, "");
        if (wdUrl.isEmpty()) {
            return "localhost";
        } else {
            try {
                return new URL(wdUrl).getHost();
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException("Failed to extract host from string " + wdUrl, e);
            }
        }
    }

    /**
     * Get current session id
     * @return optional of session id or empty optional if browser is not initialized or not a RemoteWebDriver
     */
    public static Optional<String> getSessionId() {
        return withBrowser(b -> {
            WebDriver wd = b.driver();
            if (wd instanceof RemoteWebDriver) {
                SessionId sid = ((RemoteWebDriver) wd).getSessionId();
                return (sid == null) ? null : sid.toString();
            } else {
                return null;
            }
        });
    }

    /**
     * Get ids of all sessions created by this controller (since last reset)
     * @return map of start time - session ids
     */
    public static List<String> getStoredSessionIds() {
        return new ArrayList<>(sessionStorage.get());
    }

    /**
     * Reset session id storage (should be done at the very beginning or very end of a test)
     */
    public static void resetSessionIdStorage() {
        sessionStorage.get().clear();
    }

    /**
     * Notify listeners of session creation or transfer session to another method in the same thread (if exists)
     */
    public static void sessionAdded() {
        getSessionId().ifPresent(sid -> sessionListener.accept(sid, true));
    }

    /**
     * Notify listeners of session closure (if exists)
     */
    public static void sessionClosed() {
        getSessionId().ifPresent(sid -> sessionListener.accept(sid, false));
    }
}
