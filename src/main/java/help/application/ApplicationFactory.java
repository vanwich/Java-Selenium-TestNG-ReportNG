package help.application;

import java.util.Objects;
import help.application.impl.DxpApplication;
import help.application.impl.MainApplication;

public class ApplicationFactory {
    public static ApplicationFactory getInstance() {
        return InstanceHolder.HOLDER_INSTANCE;
    }

    private static ThreadLocal<DxpApplication> dxpApp;
    private static ThreadLocal<MainApplication> mainApp;


    public DxpApplication getDxpApplication() {
        if (Objects.isNull(dxpApp)) {
            dxpApp = ThreadLocal.withInitial(DxpApplication::new);
        }
        return dxpApp.get();
    }

    public MainApplication getMainApplication() {
        if (Objects.isNull(mainApp)) {
            mainApp = ThreadLocal.withInitial(MainApplication::new);
        }
        return mainApp.get();
    }


    private static class InstanceHolder {
        public static final ApplicationFactory HOLDER_INSTANCE = new ApplicationFactory();
    }
}
