package help.utils.meters;

public interface Measurement {
    /**
     * Get total measured time
     * @return time in milliseconds
     */
    Long getMillis();
    /**
     * Get number of measurement invocations
     * @return number of invocations
     */
    Long getCount();
}
