package webdriver.controls.waiters;

import java.util.ArrayList;
import java.util.List;
import utils.meters.WaitMeters;

public abstract class Waiter {
    protected long timeout;
    protected List<Waiter> chainedWaits = new ArrayList<>();

    protected Waiter(long timeout) {
        this.timeout = (timeout < 0) ? 0 : timeout;
    }

    /**
     * Create copy of this instance without chained waits
     * @return new Waiter instance
     */
    protected abstract Waiter spawn();

    /**
     * Perform wait (without chained waits)
     */
    protected abstract void doWait();

    /**
     * Perform wait (including chained waits)
     */
    public void go() {
        WaitMeters.activate(WaitMeters.UI_UPDATE);
        try {
            doWait();
        } finally {
            WaitMeters.capture(WaitMeters.UI_UPDATE);
        }
        for (Waiter cw : chainedWaits) {
            cw.go();
        }
    }

    /**
     * Chain another Waiter instance to this.
     * @param nextWait Waiter instance to be added to the chain
     * @return new Waiter instance with chained Waiter
     */
    public Waiter then(Waiter nextWait) {
        Waiter wb = spawn();
        wb.chainedWaits.addAll(chainedWaits);
        wb.chainedWaits.add(nextWait);
        return wb;
    }
}
