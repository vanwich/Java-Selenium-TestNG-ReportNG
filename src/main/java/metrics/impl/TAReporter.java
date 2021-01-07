package metrics.impl;

import java.util.Arrays;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.influxdb.InfluxDBFactory;
import org.testng.ITestResult;
import config.PropertyProvider;
import metrics.Reporter;
import metrics.names.DurationType;
import metrics.names.Measurement;

public class TAReporter implements Reporter {
    private Set<Reporter> reporters;

    public TAReporter() {
        String names = PropertyProvider.getProperty("storage.names.rest", "");
        Stream<String> nameStream;

        if (names.isEmpty()) {
            nameStream = Stream.of();
        } else {
            nameStream = Arrays.stream(names.split("\\s*,\\s*"));
        }
        reporters = nameStream
                .filter(name -> PropertyProvider.getProperty("storage." + name + ".enabled", false))
                .map(name -> new TASingleReporter(PropertyProvider.getPropertyOrThrow("storage." + name + ".endpoint") + "/metrics/"))
                .collect(Collectors.toSet());
    }

    private class TASingleReporter extends InfluxDBReporter {
        TASingleReporter(String url) {
            super(url);
        }

        @Override
        protected void buildInfluxDB(String url) {
            //	db and rp will be replaced by TA proxy
            this.dbName = "dummydb";
            this.retentionPolicy = "dummyrp";
            influxDB = InfluxDBFactory.connect(url).setDatabase(dbName).setRetentionPolicy(retentionPolicy);
        }
    }

    @Override
    public void close() throws Exception {
        for (Reporter r : reporters) {
            r.close();
        }
    }

    @Override
    public void addGlobalPoint(Measurement msmt, String name, String value, long time) {
        reporters.stream().forEach(r -> r.addGlobalPoint(msmt, name, value, time));
    }

    @Override
    public void addTestPoint(Measurement msmt, DurationType dtype, String name, long value, long time) {
        reporters.stream().forEach(r -> r.addTestPoint(msmt, dtype, name, value, time));
    }

    @Override
    public void methodFinished(UUID testId, ITestResult testResult) {
        reporters.stream().forEach(r -> r.methodFinished(testId, testResult));
    }

    @Override
    public void resetRunId(UUID runId) {
        reporters.stream().forEach(r -> r.resetRunId(runId));
    }

}
