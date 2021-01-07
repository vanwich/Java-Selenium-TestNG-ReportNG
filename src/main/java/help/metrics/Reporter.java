package help.metrics;

import java.util.UUID;
import org.testng.ITestResult;
import help.metrics.names.DurationType;
import help.metrics.names.Measurement;

public interface Reporter extends AutoCloseable{
    void addGlobalPoint(Measurement msmt, String name, String value, long time);
    void addTestPoint(Measurement msmt, DurationType dtype, String name, long value, long time);
    void methodFinished(UUID testId, ITestResult testResult);
    void resetRunId(UUID runId);
}
