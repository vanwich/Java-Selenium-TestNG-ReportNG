package help.application.pages;

import static help.config.TestProperties.WEBDRIVER_AJAX_TIMEOUT;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import help.application.ILogin;
import help.application.IUser;
import help.application.users.User;
import help.config.ClassConfigurator;
import help.config.CustomTestProperties;
import help.config.PropertyProvider;
import help.config.TestProperties;
import help.exceptions.JstrException;
import help.utils.helper.EISAppHelper;
import help.webdriver.BrowserController;
import help.webdriver.ByT;
import help.webdriver.controls.Button;
import help.webdriver.controls.Link;
import help.webdriver.controls.StaticElement;
import help.webdriver.controls.TextBox;
import help.webdriver.controls.waiters.Waiters;

public class LoginPage implements ILogin {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginPage.class);
    private static final String SSO_LOGIN_NAVIGATE_TEMPLATE = "?j_username={0}&j_password={1}&_j_acegi_security_check=_j_acegi_security_check";
    private static final String SECURITY_CHECK_PARAMETER = "_j_acegi_security_check";

    private static ThreadLocal<Boolean> logFileNameDefined = ThreadLocal.withInitial(() -> false);

    @ClassConfigurator.Configurable
    private static ByT textBoxLoginLocator = ByT.id("loginForm:j_username");
    @ClassConfigurator.Configurable
    private static ByT textBoxPasswordLocator = ByT.id("loginForm:j_password");
    @ClassConfigurator.Configurable
    private static ByT textBoxUserLoginInFormLocator = ByT.id("changeForm:usernameLogin");
    @ClassConfigurator.Configurable
    private static ByT textBoxCurrentPasswordInFormLocator = ByT.id("changeForm:currentPassword");
    @ClassConfigurator.Configurable
    private static ByT textBoxNewPasswordInFormLocator = ByT.id("changeForm:newPassword");
    @ClassConfigurator.Configurable
    private static ByT textBoxReEnterNewPasswordInFormLocator = ByT.id("changeForm:confirmPassword");

    @ClassConfigurator.Configurable
    private static ByT buttonChangePasswordInFormLocator = ByT.id("changeForm:changeBundleChangePassword");
    @ClassConfigurator.Configurable
    private static ByT buttonClearInFormLocator = ByT.id("changeForm:changeBundleClear");
    @ClassConfigurator.Configurable
    private static ByT buttonBackInFormLocator = ByT.id("changeForm:changeBundleBack");
    @ClassConfigurator.Configurable
    private static ByT buttonLoginLocator = ByT.id("loginForm:submitForm");
    @ClassConfigurator.Configurable
    private static ByT buttonChangePasswordLocator = ByT.linkText("Change Password");
    @ClassConfigurator.Configurable
    private static ByT buttonConfirmLogoutLocator = ByT.id("logoutConfirmDialogDialog_form:buttonYes");

    @ClassConfigurator.Configurable
    private static ByT linkLogoutLocator = ByT.id("logoutForm:logout_link");

    @ClassConfigurator.Configurable
    private static ByT errorMessageLocator = ByT.xpath("//span[@class='error_message']");
    @ClassConfigurator.Configurable
    private static ByT staticElementUserDetailsLocator = ByT.id("logoutForm:userDetails");

    public static TextBox textBoxLogin = new TextBox(textBoxLoginLocator.format());
    public static TextBox textBoxPassword = new TextBox(textBoxPasswordLocator.format());
    public static TextBox textBoxUserLoginInForm = new TextBox(textBoxUserLoginInFormLocator.format());
    public static TextBox textBoxCurrentPasswordInForm = new TextBox(textBoxCurrentPasswordInFormLocator.format());
    public static TextBox textBoxNewPasswordInForm = new TextBox(textBoxNewPasswordInFormLocator.format());
    public static TextBox textBoxReEnterNewPasswordInForm = new TextBox(textBoxReEnterNewPasswordInFormLocator.format());

    public static Button buttonChangePasswordInForm = new Button(buttonChangePasswordInFormLocator.format());
    public static Button buttonClearInForm = new Button(buttonClearInFormLocator.format());
    public static Button buttonBackInForm = new Button(buttonBackInFormLocator.format());
    public static Button buttonLogin = new Button(buttonLoginLocator.format());
    public static Button buttonChangePassword = new Button(buttonChangePasswordLocator.format());
    public static Button buttonConfirmLogout = new Button(buttonConfirmLogoutLocator.format());

    public static Link linkLogout = new Link(linkLogoutLocator.format());

    public static StaticElement errorMessage = new StaticElement(errorMessageLocator.format());
    public static StaticElement staticElementUserDetails = new StaticElement(staticElementUserDetailsLocator.format());

    private IUser user;
    private String loginPageUrl;
    private Boolean loginThroughUrl;

    static {
        ClassConfigurator configurator = new ClassConfigurator(LoginPage.class);
        configurator.applyConfiguration();
    }

    public LoginPage(String username, String password, String loginPageUrl, Boolean loginThroughUrl) {
        this.user = new User(username, password);
        this.loginThroughUrl = loginThroughUrl;
        this.loginPageUrl = loginPageUrl;
    }

    @Override
    public void login() {
        login(user.getLogin(), user.getPassword(), loginThroughUrl);
    }

    @Override
    public void login(Boolean loginViaURL) {
        login(user.getLogin(), user.getPassword(), loginViaURL);
    }

    @Override
    public void login(String username, String password) {
        login(username, password, loginThroughUrl);
    }

    @Override
    public void login(String username, String password, Boolean loginThroughURL) {

        if (BrowserController.get().driver().getCurrentUrl().endsWith("/Authn/UserPassword")) {
            loginViaSso(username, password);
        } else if (loginThroughURL) {
            loginViaUrl(username, password);
        } else {
            textBoxLogin.waitForAccessible(PropertyProvider.getProperty(WEBDRIVER_AJAX_TIMEOUT, 5000));
            textBoxLogin.setValue(username);
            textBoxPassword.setValue(password);
            buttonLogin.click();
            setApplicationLogFileName();
            return;
        }
        setApplicationLogFileName();
    }

    public static void setApplicationLogFileName() {
        if (!logFileNameDefined.get()) {
            staticElementUserDetails.waitForAccessible(PropertyProvider.getProperty(WEBDRIVER_AJAX_TIMEOUT, 5000));
            String url = BrowserController.get().driver().getCurrentUrl().replace("#noback", "");
            url = url.contains("?") ? url.concat("&scenarioName=").concat(EISAppHelper.getInstance().getTestId()) : url.concat("?scenarioName=").concat(EISAppHelper.getInstance().getTestId());
            LOGGER.info("Triggering log file creation using following [{}] url", url);
            BrowserController.get().driver().navigate().to(url);
            logFileNameDefined.set(true);
        }
        staticElementUserDetails.waitForAccessible(PropertyProvider.getProperty(WEBDRIVER_AJAX_TIMEOUT, 5000));
    }

    @Override
    public void logout() {
        if (PropertyProvider.getProperty(CustomTestProperties.APP_URL_LOGOUT, true)) {
            try {
                logoutViaURL();
            } catch (URISyntaxException e) {
                throw new JstrException("Unable to logout using url");
            }
        } else {
            if (linkLogout.isPresent() && linkLogout.isVisible()) {
                linkLogout.click();
                buttonConfirmLogout.click();
            }
        }
    }

    @Override
    public IUser getUser() {
        return user;
    }

    protected void loginViaUrl(String username, String password) {
        String loginUrl = null;
        try {
            loginUrl = addAuthParams(username, password).toString();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        LOGGER.debug("Login via url {}", loginUrl);
        BrowserController.get().open(loginUrl);
    }

    protected void loginViaSso(String username, String password) {
        BrowserController.get().open(loginPageUrl + "/username.xhtml?logout=1");
        Waiters.AJAX.go();
        BrowserController.get().open(loginPageUrl);
        Waiters.AJAX.go();
        BrowserController.get().open(BrowserController.get().driver().getCurrentUrl() + MessageFormat.format(SSO_LOGIN_NAVIGATE_TEMPLATE, username, password));
    }

    protected URI addAuthParams(String username, String password) throws URISyntaxException {
        return new URIBuilder(loginPageUrl)
                .setFragment(null)
                .addParameter("loginForm:j_username", username)
                .addParameter("loginForm:j_password", password)
                .addParameter(SECURITY_CHECK_PARAMETER, SECURITY_CHECK_PARAMETER).build();
    }

    protected void logoutViaURL() throws URISyntaxException {
        String logoutUrl = new URIBuilder(loginPageUrl).addParameter("_j_acegi_logout", "_j_acegi_logout").toString();
        BrowserController.get().open(logoutUrl);
        buttonLogin.waitForAccessible(PropertyProvider.getProperty(TestProperties.WEBDRIVER_AJAX_TIMEOUT, 5000));
        LOGGER.info("Logout successfully using url script");
    }
}
