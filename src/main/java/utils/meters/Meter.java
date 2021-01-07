package utils.meters;

import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Meter {
    protected ThreadLocal<Long> startTime = new ThreadLocal<Long>() {
        @Override protected Long initialValue() {
            return -1L;
        }
    };

    protected ThreadLocal<ResettableMeasurement> threadTotal = new ThreadLocal<ResettableMeasurement>() {
        @Override protected ResettableMeasurement initialValue() {
            return new ResettableMeasurement();
        }
    };

    /**
     * Total for all threads, never reset
     */
    protected UpdateableMeasurement total = new UpdateableMeasurement();

    protected String name;

    Meter(String name) {
        this.name = name;
    }

    private static final Logger log = LoggerFactory.getLogger(Meter.class);

    protected void activate() {
        Long st = startTime.get();
        if (st < 0) {
            log.trace("Activating meter " + name);
            startTime.set(System.nanoTime() / 1000000);
        }
    }

    protected long capture() {
        long st = startTime.get();
        long delta = 0;
        if (st >= 0) {
            startTime.set(-1L);
            delta = System.nanoTime() / 1000000 - st;
            if (delta > 0) {
                total.update(delta);
                threadTotal.get().update(delta);
                log.trace("Incrementing " + name + " meter by " + delta);
            }
        }
        return delta;
    }

    protected UpdateableMeasurement getTotal() {
        capture();
        return total;
    }

    protected UpdateableMeasurement getThreadTotal() {
        capture();
        return threadTotal.get();
    }

    protected void resetThreadTotal() {
        threadTotal.get().reset();
    }

    static class UpdateableMeasurement implements Measurement {
        AtomicLong waitTime = new AtomicLong(0L);
        AtomicLong waitCount = new AtomicLong(0L);

        void update(Long delta) {
            waitTime.addAndGet(delta);
            waitCount.incrementAndGet();
        }

        @Override
        public String toString() {
            //	this is not exactly atomic, but meters are not supposed to be active when total is retrieved anyway.
            return String.format("%1$s/%2$s", getMillis(), getCount());
        }

        @Override
        public Long getMillis() {
            return waitTime.get();
        }

        @Override
        public Long getCount() {
            return waitCount.get();
        }
    }

    static class ResettableMeasurement extends UpdateableMeasurement {
        public void reset() {
            waitTime.set(0L);
            waitCount.set(0L);
        }
    }
}
