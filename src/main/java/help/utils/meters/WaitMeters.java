package help.utils.meters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import help.config.ClassConfigurator;
import help.metrics.names.DurationType;
import help.metrics.names.MetricNames;

public class WaitMeters {
    private static ConcurrentHashMap<String, Meter> meters = new ConcurrentHashMap<>();

    /**
     * Meter name for page loads (should not be used for other meters!)
     */
    public static final String PAGE_LOAD = "PAGE_LOAD";
    /**
     * Meter name for UI page updates (not page reloads!)
     */
    public static final String UI_UPDATE = "UI_UPDATE";
    /**
     * Meter names for PEF queues (getBrowser, nextPhase, executeJob etc.)
     */
    public static final String PEF_GET_BROWSER = "PEF.GET_BROWSER";
    public static final String PEF_NEXT_PHASE = "PEF.NEXT_PHASE";
    public static final String PEF_EXECUTE_JOB = "PEF.EXECUTE_JOB";
    public static final String PEF_THREAD_LIMITER = "PEF.THREAD_LIMITER";

    @ClassConfigurator.Configurable
    private static List<String> registeredMeters = new ArrayList<>();

    static {
        ClassConfigurator configurator = new ClassConfigurator(WaitMeters.class);
        configurator.applyConfiguration();

        //	standard AJAX meter
        register(UI_UPDATE);

        //	InfluxDB meters
        meters.put(PEF_GET_BROWSER, new ReportingMeter(PEF_GET_BROWSER, DurationType.QUEUE, MetricNames.GET_BROWSER));
        meters.put(PEF_NEXT_PHASE, new ReportingMeter(PEF_NEXT_PHASE, DurationType.QUEUE, MetricNames.TIMESHIFT_PHASE));
        meters.put(PEF_EXECUTE_JOB, new ReportingMeter(PEF_EXECUTE_JOB, DurationType.QUEUE, MetricNames.EXECUTE_JOB));
        meters.put(PEF_THREAD_LIMITER, new ReportingMeter(PEF_THREAD_LIMITER, DurationType.QUEUE, MetricNames.THREAD_LIMITER));

        for (String meterName : registeredMeters) {
            if (PAGE_LOAD.equals(meterName)) {
                meters.put(PAGE_LOAD, new PageLoadMeter());
            } else {
                register(meterName);
            }
        }
    }

    /**
     * Register new meter under specified name unless it is already registered.
     * @param name meter name
     */
    public static void register(String name) {
        meters.putIfAbsent(name, new Meter(name));
    }

    /**
     * Start measuring time for specified meter. If measurement is already started, does nothing.
     * Does nothing if meter with such name has not been registered.
     * @param name meter name
     */
    public static void activate(String name) {
        Meter meter = meters.get(name);
        if (meter != null) {
            meter.activate();
        }
    }

    /**
     * Take measurement for specified meter, reset it and update the all-threads total.
     * Does nothing if meter with such name has not been registered.
     * @param name meter name
     */
    public static void capture(String name) {
        Meter meter = meters.get(name);
        if (meter != null) {
            meter.capture();
        }
    }

    private static Measurement getTotal(Meter meter, boolean isGlobal) {
        return (meter == null) ? new Meter.UpdateableMeasurement() : (isGlobal ? meter.getTotal() : meter.getThreadTotal());
    }

    private static synchronized Map<String, Measurement> getAllTotals(boolean isGlobal) {
        Map<String, Measurement> totals = new HashMap<>();
        for (Map.Entry<String, Meter> entry : meters.entrySet()) {
            totals.put(entry.getKey(), getTotal(entry.getValue(), isGlobal));
        }
        return totals;
    }

    /**
     * Get measurement for current thread for meter with specified name.
     * @param name meter name
     * @return measurement instance
     */
    public static Measurement getThreadTotal(String name) {
        return getTotal(meters.get(name), false);
    }

    /**
     * Get measurement for all threads for meter with specified name.
     * @param name meter name
     * @return measurement instance
     */
    public static Measurement getTotal(String name) {
        return getTotal(meters.get(name), true);
    }

    /**
     * Get totals for all registered meters for current thread.
     * @return map with meter names and waiting times
     */
    public static synchronized Map<String, Measurement> getAllThreadTotals() {
        return getAllTotals(false);
    }

    /**
     * Get totals for all registered meters for all threads.
     * @return map with meter names and waiting times
     */
    public static synchronized Map<String, Measurement> getAllTotals() {
        return getAllTotals(true);
    }

    public static void resetThreadTotals() {
        for (Meter meter : meters.values()) {
            meter.resetThreadTotal();
        }
    }
}
