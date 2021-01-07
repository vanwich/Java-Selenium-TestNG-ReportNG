package utils.meters;

import java.util.List;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webdriver.BrowserController;

public class PageLoadMeter extends Meter{
    PageLoadMeter() {
        super(WaitMeters.PAGE_LOAD);
    }

    private static final Logger log = LoggerFactory.getLogger(PageLoadMeter.class);

    @Override
    protected void activate() {
        //	NOOP
    }

    @Override
    protected long capture() {
        long delta = 0;
        try {
            WebDriver driver = BrowserController.get().driver();
            if (driver != null && ((RemoteWebDriver) driver).getSessionId() != null) {
                @SuppressWarnings("unchecked")
                List<Long> data = (List<Long>) ((JavascriptExecutor) driver).executeScript("var t = window.performance.timing; return [t.navigationStart, t.loadEventEnd];");
                long start = data.get(0);
                long end = data.get(1);
                log.trace("Received " + name + " metrics: " + data);
                if (!startTime.get().equals(start)) {
                    startTime.set(start);
                    delta = end - start;
                    total.update(delta);
                    log.trace("Incrementing " + name + " meter total by " + delta);
                }
            }
        } catch (Exception e) {
            log.warn("Exception on measuring page load times: " + e.getMessage());
        }
        return delta;
    }

    @Override
    protected UpdateableMeasurement getTotal() {
        //	no capture() before returning total because browser may already be closed!
        return total;
    }
}
