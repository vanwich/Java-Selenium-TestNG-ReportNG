package help.webdriver.controls.waiters;

import help.config.PropertyProvider;
import help.config.TestProperties;

public class SleepWaiter extends Waiter{
    public SleepWaiter() {
        super(PropertyProvider.getProperty(TestProperties.WEBDRIVER_SLEEP_TIMEOUT, 5000));
    }

    public SleepWaiter(long timeout) {
        super(timeout);
    }

    @Override
    protected Waiter spawn() {
        return new SleepWaiter(timeout);
    }

    @Override
    protected void doWait() {
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            //	NOOP - people will ignore this exception anyway
        }
    }
}
