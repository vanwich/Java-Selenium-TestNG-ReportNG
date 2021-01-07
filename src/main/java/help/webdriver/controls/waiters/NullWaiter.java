package help.webdriver.controls.waiters;

public class NullWaiter extends Waiter{
    public NullWaiter() {
        super(0);
    }

    @Override
    protected Waiter spawn() {
        return new NullWaiter();
    }

    @Override
    protected void doWait() {
        //	NOOP
    }
}
