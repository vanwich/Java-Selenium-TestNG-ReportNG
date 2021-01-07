package help.metrics.impl;

import java.util.UUID;
import org.testng.ITestResult;
import help.metrics.Reporter;
import help.metrics.names.DurationType;
import help.metrics.names.Measurement;

public class NullReporter implements Reporter {
    @Override public void addGlobalPoint(Measurement msmt, String name, String value, long time) {}
    @Override public void addTestPoint(Measurement msmt, DurationType dtype, String name, long value, long time) {}
    @Override public void methodFinished(UUID testId, ITestResult testResult) {}
    @Override public void close() throws Exception {}
    @Override public void resetRunId(UUID runId) {}
}
