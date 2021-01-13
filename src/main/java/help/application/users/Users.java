package help.application.users;

import help.config.CustomTestProperties;
import help.config.PropertyProvider;

public class Users {
    public static final User DEFAULT = new User(PropertyProvider.getProperty(CustomTestProperties.APP_USER),
            PropertyProvider.getProperty(CustomTestProperties.APP_PASSWORD));
    public static final User QA = new User("qa", "qa");
}
