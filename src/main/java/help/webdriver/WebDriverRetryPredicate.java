package help.webdriver;

import java.util.function.Predicate;
import java.util.regex.Pattern;
import org.apache.http.client.ClientProtocolException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebDriverRetryPredicate implements Predicate<Throwable> {
    private static final Logger log = LoggerFactory.getLogger(WebDriverRetryPredicate.class);

    @Override
    public boolean test(Throwable ex) {
        String message = ex.getMessage();
        log.trace("Error upon RemoteWebDriver instantiation: " + message);
        if (ex.getClass().equals(WebDriverException.class)) {	//	not a subclass!
            Pattern ggrPattern = Pattern.compile("cannot create session .* on any hosts after \\d+ attempt\\(s\\).*", Pattern.DOTALL);
            return message.contains("Request timed out waiting for a node to become available") || 	//	Selenium Grid
                    ggrPattern.matcher(message).matches() || 										//	Ggr
                    message.contains("Queue Is Full");												//	Selenoid without Ggr
        } else if (ex instanceof UnreachableBrowserException) {
            return message.contains("Could not start a new session. " +
                    "Possible causes are invalid address of the remote server or browser start-up failure.") &&
                    ex.getCause() instanceof ClientProtocolException;	//	Selenoid without Ggr (different version?)
        } else {
            return false;
        }
    }
}
