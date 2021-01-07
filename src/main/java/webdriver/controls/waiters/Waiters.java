package webdriver.controls.waiters;

import config.ClassConfigurator;

public class Waiters {
    @ClassConfigurator.Configurable
    private static String applicationHasFinishedDataLoading =
            "return document.readyState === 'complete' && "
                    + "((typeof jQuery == 'undefined') || ("
                    + "!$('#ajaxLoadingModalBox').is(':visible') && "
                    + "($('#ajaxStatus').text() != 'on') && "
                    + "(jQuery.active == 0)))";

    /**
     * Default waiter: either one of ['AJAX', 'SLEEP', 'NONE'] or fully qualified name of Waiter subclass (must have default constructor!)
     */
    @ClassConfigurator.Configurable
    private static String defaultWaiter = "NONE";

    @ClassConfigurator.Configurable
    static long pollingInterval = 500;

    /**
     * Waiter instance with default waitmode and timeout
     */
    public static final Waiter DEFAULT;

    /**
     * Waiter instance with AJAX waitmode and default Ajax timeout
     */
    public static final Waiter AJAX;

    /**
     * Waiter instance with SLEEP waitmode and default sleep timeout
     */
    public static final Waiter SLEEP;

    /**
     * Waiter instance with NONE waitmode
     */
    public static final Waiter NONE;

    static {
        ClassConfigurator configurator = new ClassConfigurator(Waiters.class);
        configurator.applyConfiguration();

        AJAX = new JSWaiter("Ajax", applicationHasFinishedDataLoading);
        SLEEP = new SleepWaiter();
        NONE = new NullWaiter();

        switch (defaultWaiter) {
            case "AJAX":
                DEFAULT = AJAX;
                break;
            case "SLEEP":
                DEFAULT = SLEEP;
                break;
            case "NONE":
                DEFAULT = NONE;
                break;
            default:
                try {
                    @SuppressWarnings("unchecked")
                    Class<? extends Waiter> waiterClass = (Class<? extends Waiter>) Class.forName(defaultWaiter);
                    DEFAULT = waiterClass.newInstance();
                } catch (ClassNotFoundException e) {
                    throw new IllegalArgumentException("Configurable field 'defaultWaiter' should contain either one of ['AJAX', 'SLEEP', 'NONE'] or fully qualified name of desired Waiter class.", e);
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new IllegalArgumentException("Class " + defaultWaiter + " specified in 'defaultWaiter' does not have default constructor");
                }
        }
    }

    /**
     * Waiter instance with AJAX waitmode and specified timeout
     * @param timeout wait timeout
     * @return Waiter instance
     */
    public static Waiter AJAX(long timeout) {
        return new JSWaiter("Ajax", applicationHasFinishedDataLoading, timeout);
    }

    /**
     * Waiter instance with SLEEP waitmode and specified timeout
     * @param timeout wait timeout
     * @return Waiter instance
     */
    public static Waiter SLEEP(long timeout) {
        return new SleepWaiter(timeout);
    }
}
