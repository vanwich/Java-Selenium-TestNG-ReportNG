package help.utils.meters;

import help.metrics.ReportingContext;
import help.metrics.names.DurationType;

public class ReportingMeter extends Meter{
    private final DurationType dtype;
    private final String metricName;

    ReportingMeter(String meterName, DurationType dtype, String metricName) {
        super(meterName);
        this.dtype = dtype;
        this.metricName = metricName;
    }

    @Override
    protected long capture() {
        long delta = super.capture();
        if (delta > 0) {
            ReportingContext.get().addDuration(dtype, metricName, delta);
        }
        return delta;
    }
}
