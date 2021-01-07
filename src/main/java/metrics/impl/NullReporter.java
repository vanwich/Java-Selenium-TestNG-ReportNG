package metrics.impl;

import java.util.UUID;
import org.testng.ITestResult;
import metrics.Reporter;
import metrics.names.DurationType;
import metrics.names.Measurement;

public class NullReporter implements Reporter {
    @Override public void addGlobalPoint(Measurement msmt, String name, String value, long time) {}
    @Override public void addTestPoint(Measurement msmt, DurationType dtype, String name, long value, long time) {}
    @Override public void methodFinished(UUID testId, ITestResult testResult) {}
    @Override public void close() throws Exception {}
    @Override public void resetRunId(UUID runId) {}
}
