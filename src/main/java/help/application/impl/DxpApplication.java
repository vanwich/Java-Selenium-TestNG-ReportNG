package help.application.impl;

import static help.config.CustomTestProperties.*;
import static help.config.PropertyProvider.getProperty;
import static help.config.TestProperties.APP_HOST;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import help.application.Application;
import help.application.ILogin;
import help.application.IUser;
import help.config.PropertyProvider;

public class DxpApplication extends Application {
    private static final Logger LOGGER = LoggerFactory.getLogger(DxpApplication.class);

    public DxpApplication() {
        boolean isTomcat = PropertyProvider.getProperty("test.dxp.app", "netty")
                .equalsIgnoreCase("tomcat");

        this.host = PropertyProvider.getProperty("app.host.dxp", PropertyProvider.getProperty(APP_HOST));
        this.name = "DXP_APP";
        this.port = Integer.parseInt(getProperty(isTomcat? "app.dxp.tomcat.port": "app.dxp.netty.port", "9090"));
        this.path = PropertyProvider.getProperty("app.dxp.path", "/hot-gateway");
        this.protocol = PropertyProvider.getProperty(APP_PROTOCOL, "http");
    }

    @Override
    public void switchPanel() {
        throw new UnsupportedOperationException("DXP is rest app, method is not applicable");
    }

    @Override
    public ILogin getLogin() {
        throw new UnsupportedOperationException("DXP is rest app, method is not applicable");
    }

    @Override
    public void open() {
        throw new UnsupportedOperationException("DXP is rest app, method is not applicable");
    }

    @Override
    public void open(String username, String password) {
        throw new UnsupportedOperationException("DXP is rest app, method is not applicable");
    }

    @Override
    public void open(IUser user) {
        throw new UnsupportedOperationException("DXP is rest app, method is not applicable");
    }

    @Override
    public void reopen() {
        throw new UnsupportedOperationException("DXP is rest app, method is not applicable");
    }

    @Override
    public void reopen(String username, String password) {
        throw new UnsupportedOperationException("DXP is rest app, method is not applicable");
    }

    @Override
    public void reopen(IUser user) {
        throw new UnsupportedOperationException("DXP is rest app, method is not applicable");
    }

    @Override
    public void close() {
        throw new UnsupportedOperationException("DXP is rest app, method is not applicable");
    }

    @Override
    public void openSession() {
        throw new UnsupportedOperationException("DXP is rest app, method is not applicable");
    }

    @Override
    public boolean isApplicationOpened() {
        throw new UnsupportedOperationException("DXP is rest app, method is not applicable");
    }
}
