package help.metrics;

import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import help.config.ClassConfigurator;
import help.config.PropertyProvider;
import help.config.TestProperties;
import help.metrics.impl.InfluxDBReporter;
import help.metrics.impl.NullReporter;
import help.metrics.impl.TAReporter;
import help.metrics.names.DurationType;
import help.metrics.names.Measurement;
import help.utils.ThrowingRunnable;
import help.metrics.names.MetricNames;
import help.utils.ThrowingSupplier;
import help.verification.StackTraceUtils;

public class ReportingContext {
    private static final Logger log = LoggerFactory.getLogger(ReportingContext.class);

    private static boolean testsStarted = false;
    private static final ThreadLocal<UUID> testId = ThreadLocal.withInitial(UUID::randomUUID);
    private static final ThreadLocal<Optional<String>> testName = ThreadLocal.withInitial(Optional::empty);
    @ClassConfigurator.Configurable
    private static String reporter = null;
    private static final boolean escapeTestParams = PropertyProvider.getProperty(TestProperties.METRICS_INFLUXDB_ESCAPE_PARAMS, false);

    private UUID runId;
    private final Reporter reporterInstance;

    static {
        ClassConfigurator configurator = new ClassConfigurator(ReportingContext.class);
        configurator.applyConfiguration();
    }
    private static final ReportingContext instance = new ReportingContext();

    public static ReportingContext get() {
        return instance;
    }

    private static Reporter initReporter() {
        Reporter reporterInst = null;
        if (reporter == null) {
            if (PropertyProvider.getProperty(TestProperties.METRICS_ENABLED, false)) {
                try {
                    if (PropertyProvider.getProperty(TestProperties.METRICS_DIRECT_CONNECTION, true)) {
                        reporterInst = new InfluxDBReporter();
                    } else {
                        reporterInst = new TAReporter();
                    }
                } catch (Exception e) {
                    log.error("Failed to initialize InfluxDB reporter. Metrics will not be collected", e);
                }
            }

            if (reporterInst == null) {
                reporterInst = new NullReporter();
            }
        } else {
            try {
                reporterInst = (Reporter) Class.forName(reporter).newInstance();
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                throw new IllegalArgumentException("Cannot instantiate configured reporter by class name: " + reporter);
            }
        }

        return reporterInst;
    }

    static {
        ClassConfigurator configurator = new ClassConfigurator(ReportingContext.class);
        configurator.applyConfiguration();
    }

    private ReportingContext() {
        reporterInstance = initReporter();
        resetRunId(UUID.randomUUID());	//	just in case, to be reset by listener
    }

    /**
     * Get current run id
     * @return run id
     */
    public UUID getRunId() {
        return runId;
    }

    /**
     * Get current test invocation id
     * @return test invocation id
     */
    public UUID getTestId() {
        return testId.get();
    }

    /**
     * Reset current run id. Should never be called if at least one test has already started
     * @param runId new run id
     */
    public void resetRunId(UUID runId) {
        if (testsStarted) {
            throw new IllegalStateException("Cannot reset run id once tests have already started!");
        } else {
            this.runId = runId;
            reporterInstance.resetRunId(runId);
        }
    }

    /**
     * Close reporter
     * @throws Exception if this resource cannot be closed
     */
    public void close() throws Exception {
        reporterInstance.close();
    }

    /**
     * Add global tag with current timestamp
     * @param name metric name
     * @param value metric value
     */
    public void addTag(String name, String value) {
        addTag(name, value, System.currentTimeMillis());
    }

    /**
     * Add global tag with specified timestamp
     * @param name metric name
     * @param value metric value
     * @param time point timestamp
     */
    public void addTag(String name, String value, long time) {
        reporterInstance.addGlobalPoint(Measurement.TAG, name, value, time);
    }

    /**
     * Add test-related duration with current timestamp
     * @param dtype duration type
     * @param name metric name
     * @param value metric value
     */
    public void addDuration(DurationType dtype, String name, long value) {
        addDuration(dtype, name, value, System.currentTimeMillis());
    }


    /**
     * Add test-related duration with specified timestamp
     * @param dtype duration type
     * @param name metric name
     * @param value metric value
     * @param time metric timestamp
     */
    public void addDuration(DurationType dtype, String name, long value, long time) {
        reporterInstance.addTestPoint(Measurement.DURATION, dtype, name, value, time);
    }

    /**
     * Invoke from TestNG listener when new test or configuration method is started
     * @param testResult test result object from TestNG
     * @param newTestId new test id
     */
    public void methodStarted(ITestResult testResult, UUID newTestId) {
        testId.set(newTestId);
        testName.set(Optional.of(TestNameExtractor.getFullTestName(testResult, escapeTestParams)));
        testsStarted = true;
    }

    /**
     * Invoke from TestNG listener when test or configuration method is finished
     * @param testResult test result object from TestNG
     */
    public void methodFinished(ITestResult testResult) {
        addDuration(DurationType.TEST, MetricNames.TOTAL_DURATION, testResult.getEndMillis() - testResult.getStartMillis(), testResult.getEndMillis());
        reporterInstance.methodFinished(getTestId(), testResult);
        testId.remove();
        testName.remove();
    }

    /**
     * Helper method to measure and report duration of a specific block of code
     * @param <E> checked exception type
     * @param dtype duration type
     * @param metricName metric name
     * @param runnable block of code to execute
     * @throws E exception thrown by the code block
     */
    public <E extends Throwable> void measure(DurationType dtype, String metricName, ThrowingRunnable<E> runnable) throws E {
        long start = System.currentTimeMillis();
        try {
            runnable.run();
        } finally {
            long now = System.currentTimeMillis();
            addDuration(dtype, metricName, now - start, now);
        }
    }

    /**
     * Helper method to measure and report duration of a specific block of code and then return its result
     * @param <T> return type of result supplier
     * @param <E> checked exception type
     * @param dtype duration type
     * @param metricName metric name
     * @param supplier result supplier
     * @return return value of the supplier
     * @throws E exception thrown by the code block
     */
    public <T, E extends Throwable> T measure(DurationType dtype, String metricName, ThrowingSupplier<T, E> supplier) throws E {
        long start = System.currentTimeMillis();
        try {
            return supplier.get();
        } finally {
            long now = System.currentTimeMillis();
            addDuration(dtype, metricName, now - start, now);
        }
    }

    /**
     * Get optional of current test method name
     * @return optional of test method name
     */
    public Optional<String> getCurrentTestNameOpt() {
        return testName.get();
    }

    /**
     * Get current test method name with fallback on stack trace analysis
     * @return test method name or "_none_" if no test method is currently active
     */
    public String getCurrentTestName() {
        return testName.get().orElse(StackTraceUtils.findTestMethodName(Thread.currentThread().getStackTrace()).orElse("_none_"));
    }
}
