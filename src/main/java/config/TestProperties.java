package config;

import webdriver.OptionsModifier;

public class TestProperties {

    /**
     * Project name to be passed as a test run property to Test Analytics
     */
    static public final String PROJECT_NAME = "project.name";
    /**
     * Generic application host name (e.g. "sfoeiswas999")
     */
    static public final String APP_HOST = "app.host";

    //	new style properties to replace the deprecated ones above
    /**
     * Path to test artifacts (e.g. logs), relative to the working directory
     */
    static public final String OUTPUT = "test.output";
    /**
     * Path to screenshots, relative to {@link #OUTPUT}
     */
    static public final String SCREENSHOTS = "test.screenshots";
    /**
     * Webdriver hub URL including username/password if necessary (e.g. "http://myuser:mypwd@myhubhost:4444/wd/hub")
     */
    static public final String WEBDRIVER_HUB_URL = "webdriver.hub.url";
    /**
     * Default timeout, in milliseconds, to be used in various waiting conditions.
     * Not to be confused with {@link #WEBDRIVER_AJAX_TIMEOUT}.
     */
    static public final String WEBDRIVER_TIMEOUT = "webdriver.timeout";
    /**
     * Timeout, in milliseconds, to be used in Ajax waiter.
     */
    static public final String WEBDRIVER_AJAX_TIMEOUT = "webdriver.ajaxtimeout";
    /**
     * Timeout, in milliseconds, to be used in Sleep waiter.
     */
    static public final String WEBDRIVER_SLEEP_TIMEOUT = "webdriver.sleeptimeout";
    /**
     * Browser type. Possible values: "ff"/"firefox", "ie"/"iexplore", "chrome"/"googlechrome".
     */
    static public final String WEBDRIVER_PROFILE = "webdriver.profile";
    /**
     * Implicit wait timeout, in milliseconds.
     */
    static public final String WEBDRIVER_IMPLICIT_WAIT = "webdriver.implicit.wait";
    /**
     * Enable or disable WebElement caching (to prevent element lookup on every operation).
     */
    static public final String WEBDRIVER_ELEMENT_CACHE_ENABLE = "webdriver.elementcache.enable";
    /**
     * Path to resource (not a file system path!) with custom WebDriver capabilities, should point to a Java properties file.
     * Only String capabilities can be specified in this fashion; for more complex cases it is necessary
     * to override and configure custom {@link OptionsModifier}.
     */
    static public final String WEBDRIVER_CUSTOM_CAPABILITIES_RESOURCE = "webdriver.capabilities.resourcepath";
    /**
     * Path to IEDriverServer.exe for local test execution
     */
    static public final String WEBDRIVER_IE_DRIVER_SERVER = "webdriver.iedriverserver";
    /**
     * Path to chromedriver.exe for local test execution
     */
    static public final String WEBDRIVER_CHROME_DRIVER = "webdriver.chromedriver";
    /**
     * Path to geckodriver.exe for local test execution
     */
    static public final String WEBDRIVER_GECKO_DRIVER = "webdriver.geckodriver";
    /**
     * Boolean property that instructs FirefoxDriver to use Marionette backend.
     * Used only if system property "webdriver.firefox.marionette" is not set.
     */
    static public final String WEBDRIVER_USE_MARIONETTE = "webdriver.usemarionette";
    /**
     * Boolean property to enable JavaScript scrolling prior to clicking Web elements
     */
    static public final String WEBDRIVER_ELEMENT_ENSURE_VISIBLE = "webdriver.element.ensurevisible";
    /**
     * Boolean property to enable JavaScript scrolling prior to setting Web elements values (only if #WEBDRIVER_ELEMENT_ENSURE_VISIBLE == true)
     */
    static public final String WEBDRIVER_ELEMENT_ENSURE_VISIBLE_EAGERLY = "webdriver.element.ensurevisibleeagerly";
    /**
     * Boolean property to include locators in log or error messages referring to Web elements.
     */
    public static final String WEBDRIVER_ELEMENT_RENDER_LOCATORS = "webdriver.element.renderlocators";
    /**
     * Connection timeout, in milliseconds, to be used upon creation of remote WebDriver instance
     */
    public static final String WEBDRIVER_REMOTE_CONNECTION_TIMEOUT = "webdriver.remote.connectiontimeout";
    /**
     * Socket timeout, in milliseconds, to be used upon creation of remote WebDriver instance
     */
    public static final String WEBDRIVER_REMOTE_SOCKET_TIMEOUT = "webdriver.remote.sockettimeout";
    /**
     * RemoteWebDriver instantiation timeout (in seconds); use 0 to disable retries.
     */
    public static final String WEBDRIVER_REMOTE_SESSION_CREATION_TIMEOUT = "webdriver.remote.sessioncreationtimeout";
    /**
     * Maximum wait (in seconds) between attempts to instantiate RemoteWebDriver with default retry strategy.
     */
    public static final String WEBDRIVER_REMOTE_SESSION_CREATION_MAX_WAIT = "webdriver.remote.sessioncreationmaxwait";
    /**
     * Enable/disable headless mode (Chrome only)
     */
    public static final String WEBDRIVER_HEADLESS_MODE = "webdriver.headless";
    /**
     * Logging level for {@link org.openqa.selenium.logging.LogType#BROWSER} (Chrome only). If omitted, browser logs will not be fetched.
     * See {@link java.util.logging.Level} for available logging levels.
     */
    public static final String WEBDRIVER_LOG_LEVEL_BROWSER = "webdriver.loglevel.browser";
    /**
     * User custom HTTP client factory for WebDriver
     */
    public static final String USE_CUSTOM_HTTP_CLIENT_FACTORY = "webdriver.usecustomhttpclientfactory";
    /**
     * Test log file name
     */
    static public final String LOG_FILE_NAME = "test.logfilename";
    /**
     * Logging level used in Logback configuration
     */
    static public final String LOG_LEVEL = "test.loglevel";
    /**
     * Name of the binary logger configured in logback.xml
     */
    static public final String BINARY_LOGGER_NAME = "test.binaryloggername";
    /**
     * Name of custom properties file resource, relative to /config folder
     */
    public static final String CUSTOMPROPS_PATH = "test.customprops";
    /**
     * Subfolder name under /config/class resource folder to get class configuration overrides from
     */
    public static final String CLASS_CONFIGURATION_OVERRIDE_PATH = "test.classconfig.overridepath";
    /**
     * File system path to buglist (list of known issues mapped to test error messages).
     */
    public static final String BUGLIST = "test.buglist";
    /**
     * Boolean property to enable buglist functionality.
     */
    public static final String BUGLIST_ENABLED = "test.buglist.enabled";
    /**
     * Boolean property to specify whether UI errors (messages fetched from UI at the moment of test failure)
     * should be matched against the buglist along with regular error messages.
     */
    public static final String BUGLIST_MATCH_UI_ERRORS = "test.buglist.matchuierrors";
    /**
     * Comma-separated list of weekday names (case-insensitive full English names) that are deemed weekends
     * in a regular week (i.e. when there are no public holidays);
     * e.g. SATURDAY,SUNDAY
     */
    public static final String DATE_WEEKENDS = "date.weekends";
    /**
     * Comma-separated list of non-working non-weekend days in MM/DD/YYYY format,
     * e.g. 01/01/2018,01/02/2018,02/06/2018,03/30/2018,04/02/2018,04/25/2018,06/04/2018,10/22/2018
     */
    public static final String DATE_NON_WORKING_DAYS = "date.nonWorkingDays";
    /**
     * Comma-separated list of working weekend days in MM/DD/YYYY format,
     * e.g. 07/07/2018,14/07/2018
     */
    public static final String DATE_WORKING_WEEKENDS = "date.workingWeekends";

    /**
     * Number of test retries upon failures. Defaults to 0 (no retries).
     */
    public static final String TEST_REPEAT_MAX_TRIES = "test.repeat.maxtries";

    /**
     * Boolean property to enable screenshot making
     */
    public static final String SCREENSHOT_ENABLED = "storage.screenshot.enabled";
    /**
     * Boolean property to enable screenshot making for individual failures in soft assertion mode
     */
    public static final String SOFT_MODE_SCREENSHOTS_ENABLED = "storage.softmode.screenshots.enabled";
    /**
     * Path to the folder with files to be used in upload tests.
     */
    public static final String UPLOAD_FILES_LOCATION = "test.uploadfiles.location";
    /**
     * Boolean property to enable highlighting Web element currently interacted with
     */
    public static final String ENABLE_ELEMENT_HIGHLIGHTING = "test.highlighting.enabled";
    /**
     * Boolean property to enable checking whether Web element is enabled prior to setting its value
     */
    public static final String ENABLE_ELEMENT_INTERACTIVITY_CHECK = "test.interactivitycheck.enabled";
    /**
     * Path to the browser's downloads folder.
     * Should be used only for local or non-Selenoid test runs.
     */
    public static final String BROWSER_DOWNLOAD_FILES_LOCATION = "test.downloadfiles.location";
    /**
     * File download timeout for browser containers (used only with Selenoid), in milliseconds.
     */
    public static final String BROWSER_FILE_DOWNLOAD_TIMEOUT = "test.downloadfiles.timeout";
    /**
     * Initial delay before file download for browser containers (used only with Selenoid), in milliseconds.
     */
    public static final String BROWSER_FILE_DOWNLOAD_DELAY = "test.downloadfiles.delay";
    /**
     * Boolean property to enable video recording (Selenoid only)
     */
    public static final String ENABLE_VIDEO_RECORDING = "test.video.enabled";
    /**
     * Initial delay before test video download from Selenoid, in milliseconds.
     */
    public static final String TEST_VIDEO_DOWNLOAD_DELAY = "test.downloadvideo.delay";
    /**
     * Monitor number for local runs
     */
    public static final String TEST_BROWSER_MONITOR_NUMBER = "test.browser.monitornumber";
    /**
     * Browser window layout for local runs
     * possible values: half-left, half-right, half-top, half-bottom, quarter-upper-left, quarter-upper-right, quarter-lower-left, quarter-lower-right
     */
    public static final String TEST_BROWSER_WINDOW_LAYOUT = "test.browser.windowlayout";
    /**
     * Browser window width in pixels for local runs
     */
    public static final String TEST_BROWSER_WINDOW_WIDTH = "test.browser.windowwidth";
    /**
     * Browser window height in pixels for local runs
     */
    public static final String TEST_BROWSER_WINDOW_HEIGHT = "test.browser.windowheight";

    /* Metric collection properties */
    /**
     * Boolean property to enable collection of test run metrics in InfluxDB storage.
     * If set to true AND {@link #METRICS_DIRECT_CONNECTION} = false, remaining properties starting with "metrics" must also be set.
     */
    public static final String METRICS_ENABLED = "metrics.enabled";
    /**
     * Boolean property to determine whether metrics are sent directly to InfluxDB or via TA proxy
     */
    public static final String METRICS_DIRECT_CONNECTION = "metrics.directconnection";
    /**
     * Project ID tag for metrics storage, should usually correspond to the delivery project name (e.g. "CSAA")
     */
    public static final String METRICS_PROJECT_ID = "metrics.projectid";
    /**
     * URL to the metrics storage
     */
    public static final String METRICS_INFLUXDB_URL = "metrics.influxdb.url";
    /**
     * User name to login to the metrics storage
     */
    public static final String METRICS_INFLUXDB_USER = "metrics.influxdb.user";
    /**
     * Password to login to the metrics storage
     */
    public static final String METRICS_INFLUXDB_PASSWORD = "metrics.influxdb.password";
    /**
     * Database name in the metrics storage
     */
    public static final String METRICS_INFLUXDB_DBNAME = "metrics.influxdb.dbname";
    /**
     * Retention policy for metrics collection
     */
    public static final String METRICS_INFLUXDB_RETENTION_POLICY = "metrics.influxdb.retentionpolicy";
    /**
     * Escape test parameters sent to InfluxDB
     */
    public static final String METRICS_INFLUXDB_ESCAPE_PARAMS = "metrics.influxdb.escapeparams";
    /**
     * SyncService cool-off interval, in ms (i.e. period after job or timeshift execution
     * during which no new job/timeshift processing can be started)
     * If omitted, no cool-off is performed
     */
    public static final String SYNC_SERVICE_COOLOFF_TIMEOUT = "syncservice.coolofftimeout";
    /**
     * SyncService permit retain timeout, in ms (i.e. period during which execution permit
     * is retained by the thread after release).
     * If omitted, permits are retained for 1/2 of the phase timeout (with which SyncService was configured)
     */
    public static final String SYNC_SERVICE_RETAIN_TIMEOUT = "syncservice.retaintimeout";
    /**
     * SyncService verbose mode on/off. In verbose mode status is logged on every watchdog task iteration
     */
    public static final String SYNC_SERVICE_VERBOSE = "syncservice.verbose";
}
