package help.application;

import help.data.TestData;
import help.exceptions.JstrException;

public interface ILogin {
    /**
     * Log in to the desired web application Login Page using default user's credentials.
     * It is expected that Login Page is opened before current method invocation
     */
    void login();

    /**
     * Log in to the desired web application Login Page using default user's credentials and URL based authorization to bypass UI interactions
     *
     * @param loginViaURL <b>true</b> - login via URL, <b>false</b> - login via UI
     */
    void login(Boolean loginViaURL);

    /**
     * Log in to the desired web application Login Page using provided user's credentials
     *
     * @param username
     * @param password
     */
    void login(String username, String password);

    /**
     * Log in to the desired web application Login Page using provided user's credentials + ability to use URL based authentication
     *
     * @param username
     * @param password
     */
    void login(String username, String password, Boolean loginViaURL);

    /**
     * Please inform iguliam if smbdy using that, will be removed in a while
     */
    @Deprecated
    default void login(TestData td) {
    }

    /**
     * Please inform iguliam if smbdy using that, will be removed in a while
     */
    @Deprecated
    default void login(TestData td, Boolean loginViaURL) {
    }

    /**
     * Log in to the desired web application Login Page using provided {@link IUser}
     *
     * @param user
     */
    default void login(IUser user) {
        login(user.getLogin(), user.getPassword());
    }

    /**
     * Logout from web application logic
     */
    void logout();

    /**
     * Sequential execution of the {@link ILogin#logout()} and {@link ILogin#login()}
     */
    default void reLogin() {
        logout();
        login();
    }

    /**
     * Sequential execution of the {@link ILogin#logout()} and {@link ILogin#login(Boolean)}
     */
    default void reLogin(Boolean loginViaURL) {
        logout();
        login(loginViaURL);
    }

    /**
     * Sequential execution of the {@link ILogin#logout()} and {@link ILogin#login(String, String)}
     */
    default void reLogin(String username, String password) {
        logout();
        login(username, password);
    }

    /**
     * Sequential execution of the {@link ILogin#logout()} and {@link ILogin#login(String, String, Boolean)}
     */
    default void reLogin(String username, String password, Boolean loginViaURL) {
        logout();
        login(username, password, loginViaURL);
    }

    /**
     * Get IUser used to bypass authentication logic
     *
     * @return {@link IUser} object used to initialize ILogin implementation object
     */
    default IUser getUser() {
        throw new JstrException("Please implement method");
    }
}
