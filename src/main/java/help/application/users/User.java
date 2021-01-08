package help.application.users;

import org.apache.commons.codec.binary.Base64;
import help.application.IUser;
import help.config.ClassConfigurator;
import help.config.PropertyProvider;

public class User implements IUser {
    @ClassConfigurator.Configurable
    private static String userNameTmpl = "rest.user.%1$s.login";
    @ClassConfigurator.Configurable
    private static String userPassTmpl = "rest.user.%1$s.pass";

    private String login;
    private String password;

    static {
        ClassConfigurator configurator = new ClassConfigurator(User.class);
        configurator.applyConfiguration();
    }

    public User(String user) {
        setName(PropertyProvider.getProperty(String.format(userNameTmpl, user), null));
        setPassword(PropertyProvider.getProperty(String.format(userPassTmpl, user), null));
    }

    public User(String name, String password) {
        setName(name);
        setPassword(password);
    }

    public String getLogin() {
        return login;
    }

    public void setName(String name) {
        this.login = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBasicAuthString() {
        return "Basic " + new String(Base64.encodeBase64((login + ":" + password).getBytes()));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        User that = (User) obj;
        return (login != null ? login.equals(that.login) : that.login == null) &&
                (password != null ? password.equals(that.password) : that.password == null);
    }

    @Override
    public int hashCode() {
        int result = login != null ? login.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }
}
