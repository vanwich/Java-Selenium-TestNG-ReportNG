package help.metrics.names;

public final class MetricNames {
    private MetricNames() {}

    /**
     * Timeshift phase marker AND waiting for the next timeshift phase
     */
    public static final String TIMESHIFT_PHASE = "timeshift_phase";
    /**
     * Waiting to get browser from Grid/remote WebDriver node or PEF
     */
    public static final String GET_BROWSER = "get_browser";
    /**
     * Job phase marker AND waiting for job execution
     */
    public static final String EXECUTE_JOB = "execute_job";
    /**
     * Waiting for thread limiter
     */
    public static final String THREAD_LIMITER = "thread_limiter";
    /**
     * Total test duration
     */
    public static final String TOTAL_DURATION = "total_duration";
}
