package help.application.impl;

import static help.config.CustomTestProperties.*;
import static help.config.TestProperties.APP_HOST;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import help.application.Application;
import help.application.pages.LoginPage;
import help.config.CustomTestProperties;
import help.config.PropertyProvider;
import help.webdriver.BrowserController;
import help.webdriver.controls.Link;

public class MainApplication extends Application {
    private static final Logger LOGGER = LoggerFactory.getLogger(MainApplication.class);

    public MainApplication() {
        this.host = PropertyProvider.getProperty(APP_HOST);
        this.name = "MAIN_APP";
        this.path = PropertyProvider.getProperty(APP_PATH, "ipb-app");
        this.port = PropertyProvider.getProperty(APP_PORT, -1);
        this.protocol = PropertyProvider.getProperty(APP_PROTOCOL, "http");
        Boolean loginThroughUrl = PropertyProvider.getProperty(APP_URL_LOGIN, false);
        this.login = new LoginPage(PropertyProvider.getProperty(APP_USER),
                PropertyProvider.getProperty(APP_PASSWORD), this.formatUrl(), loginThroughUrl);
    }

    @Override
    public void switchPanel() {
        LOGGER.debug("Switch to Main App");
        LOGGER.debug("New session id: {}", ((RemoteWebDriver) BrowserController.get().driver()).getSessionId());
        Link linkSwitch = new Link(By.id("logoutForm:switchToApp"));
        if (linkSwitch.isPresent() && linkSwitch.isVisible()) {
            linkSwitch.click();
        }
    }
}
