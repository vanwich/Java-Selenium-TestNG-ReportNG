package help.ws.rest.features;

import java.util.logging.Level;
import java.util.logging.Logger;
import help.utils.logging.CustomLogger;

public class RestLogger extends Logger {
    org.slf4j.Logger LOGGER = CustomLogger.getInstance();

    public RestLogger() {
        super("Jersey Logger", null);
        setLevel(Level.ALL); //this level is used to filter messages with lower severity by org.glassfish.jersey.logging.LoggingFeature.createLoggingFilter
    }

    protected RestLogger(String name, String resourceBundleName) {
        super(name, resourceBundleName);
    }

    public void log(Level level, String msg) {
        switch (level.getName()) {
            case "ALL":
            case "FINEST":
                LOGGER.trace("\n" + msg);
                break;
            case "FINER":
            case "FINE":
            case "CONFIG":
                LOGGER.debug("\n" + msg);
                break;
            case "INFO":
                LOGGER.info("\n" + msg);
                break;
            case "WARNING":
                LOGGER.warn("\n" + msg);
                break;
            case "SEVERE":
                LOGGER.error("\n" + msg);
                break;
            case "OFF":
                break;
            default:
                LOGGER.debug("\n" + msg);
                break;
        }
    }
}
