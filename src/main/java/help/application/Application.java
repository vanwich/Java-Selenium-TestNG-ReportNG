package help.application;

import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import help.config.PropertyProvider;
import help.config.TestProperties;
import help.webdriver.BrowserController;
import help.webdriver.controls.Button;

public abstract class Application {
    protected static final Logger LOGGER = LoggerFactory.getLogger(Application.class);
    /**
     * Thread local boolean flag to determine is web-application opened or not
     */
    protected static ThreadLocal<Boolean> isApplicationOpened = ThreadLocal.withInitial(() -> false);
    /**
     * Hyper Text Transfer Protocol used for web application. Recommended to get it from properties files.
     */
    protected String protocol;
    /**
     * Server host address used for web application. Recommended to get it from properties files.
     */
    protected String host;
    /**
     * Server path used for web application. Recommended to get it from properties files.
     */
    protected String path;
    /**
     * Web application name could be defined if needed.
     */
    protected String name;
    /**
     * Server port used for web application. Recommended to get it from properties files. Leave default value -1 if port is not defined
     */
    protected int port = -1;
    /**
     * {@link ILogin} interface implementation with authentification functionality. See ILogin
     */
    protected ILogin login;

    /**
     * Should be enhanced by each child class with all required fields definition.
     * Please see {@link } as example
     */
    protected Application() {
    }

    /**
     * WebDriver instance creation and registration in istf-core {@link BrowserController}
     * Maximise browser window afterwards, only for remote Chrome profile 1920x1080 resolution is used because of selenoid's specific
     */
    public void openSession() {
        BrowserController.initBrowser();
        BrowserController.get().open(formatUrl());
        if (StringUtils.isNotEmpty(PropertyProvider.getProperty(TestProperties.WEBDRIVER_HUB_URL)) && BrowserController.get().isChrome()) {
            BrowserController.get().driver().manage().window().setSize(new Dimension(1920, 1080));
        } else {
            BrowserController.get().driver().manage().window().maximize();
        }
        // changing browser window position and resizing (works only for local runs)
        BrowserController.get().setBrowserWindowLayout();
        setApplicationOpened(true);
    }

    /**
     * Opens web application and log in if not opened yet using standard user defined in properties and initialized in {@link ILogin implementation}.
     * Note: If already opened {@link Application#switchPanel()} method will be invoked. Used to click on link which will redirect to required application if present.
     * Example : Main application if already opened has direct link to Admin application
     */
    public void open() {
        if (!isApplicationOpened()) {
            LOGGER.debug("App is not opened");
            openSession();
            LOGGER.debug("new session id: " + ((RemoteWebDriver) BrowserController.get().driver()).getSessionId());
            getLogin().login();
        }
        switchPanel();
        LOGGER.info("[{}] Application opened", getName());
    }

    /**
     * Opens web application and bypass log in using provided user's credentials
     *
     * @param username
     * @param password
     */
    public void open(String username, String password) {
        if (!isApplicationOpened()) {
            openSession();
            getLogin().login(username, password);
        }
        switchPanel();
    }

    /**
     * Opens web application and bypass log in using provided {@link IUser object}
     *
     * @param user
     */
    public void open(IUser user) {
        open(user.getLogin(), user.getPassword());
    }

    /**
     * Used to click on application re-direction link if present or any other way to open desired application to avoid extra {@link Application#openSession()} and {@link ILogin#login()} invocation.
     * Keep in mind that on first {@link Application#open()} invocation we should not do any actions cause application will be opened from scratch.
     * i.e. add some checks like : if re-direction link is present - then click on it, otherwise do nothing.
     */
    public abstract void switchPanel();

    /**
     * Close web application by doing a {@link ILogin#logout()} and {@link WebDriver#quit()}. Attempt to save current UI state will take place as well.
     */
    public void close() {
        if (isApplicationOpened()) {
            setApplicationOpened(false);
            try {
                Button buttonSaveAndExit = new Button(By.id("topSaveAndExitLink"));
                if (buttonSaveAndExit.isPresent() && buttonSaveAndExit.isVisible()) {
                    buttonSaveAndExit.click();
                }
                getLogin().logout();
            } catch (Exception e) {
                LOGGER.info("Cannot close application: ", e);
            }
            BrowserController.get().quit();
            LOGGER.info("[{}] Application closed", getName());
        }
    }

    /**
     * Reopen the web application by sequential close() and open() methods invocation using standard user
     */
    public void reopen() {
        close();
        open();
    }

    /**
     * Reopen the web application by sequential close() and open(String username,String password) methods invocation using provided credentials
     *
     * @param username
     * @param password
     */
    public void reopen(String username, String password) {
        close();
        open(username, password);
    }

    /**
     * Reopen the web application by sequential close() and open(IUser user) methods invocation using provided IUser implementation object
     *
     * @param user
     */
    public void reopen(IUser user) {
        reopen(user.getLogin(), user.getPassword());
    }

    /**
     * Get {@link ILogin} object for requested application
     *
     * @return {@link ILogin} implementation object
     */
    public ILogin getLogin() {
        return login;
    }

    /**
     * Get {@link IUser} object for requested application
     *
     * @return {@link IUser} implementation object. Default user will be returned.
     * Note: Requires enhancement to return actual information of currently used user instead of always return default user
     */
    public IUser getUser() {
        return login.getUser();
    }

    /**
     * Whether application opened or not
     *
     * @return <b>true</b> if opened, otherwise <b>false</b>
     */
    public boolean isApplicationOpened() {
        return isApplicationOpened.get();
    }

    /**
     * Get application server host
     *
     * @return {@link String} host
     */
    public String getHost() {
        return host;
    }

    /**
     * Get application server path
     *
     * @return {@link String} path
     */
    public String getPath() {
        return path;
    }

    /**
     * Get application server port
     *
     * @return <b>int</b> port
     */
    public int getPort() {
        return port;
    }

    /**
     * Get application server protocol
     *
     * @return {@link String} protocol
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * Get complete web application server URL
     *
     * @return {@link String} complete web-application URL
     */
    public String formatUrl() {
        return new URIBuilder()
                .setScheme(getProtocol())
                .setHost(getHost())
                .setPort(getPort())
                .setPath(getPath())
                .toString();
    }

    /**
     * Get web application name if defined
     *
     * @return {@link} web application name
     */
    public String getName() {
        return Optional.ofNullable(name).orElse(this.getClass().getSimpleName());
    }

    /**
     * Set application status for each thread.
     *
     * @param status true - opened, false - closed
     */
    protected void setApplicationOpened(boolean status) {
        isApplicationOpened.set(status);
    }
}
