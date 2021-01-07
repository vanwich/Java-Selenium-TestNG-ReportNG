package metrics.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import config.PropertyProvider;
import config.TestProperties;
import metrics.Reporter;
import metrics.TestNameExtractor;
import metrics.names.DurationType;
import metrics.names.Measurement;

public class InfluxDBReporter implements Reporter {
    private static final Logger log = LoggerFactory.getLogger(InfluxDBReporter.class);

    private static final String TAG_TEST_NAME = "testName";
    private static final String TAG_SUITE_NAME = "suiteName";
    private static final String TAG_GROUP_NAME = "groupName";	/* name of TestContext */
    private static final String TAG_PROJECT_ID = "projectId";
    private static final String TAG_METRIC_TYPE = "metricType";
    private static final String TAG_METRIC_NAME = "metricName";
    private static final String TAG_IS_TEST = "isTest";

    private static final String FIELD_VALUE = "value";
    private static final String FIELD_RUN_ID = "runId";
    private static final String FIELD_TEST_ID = "testId";

    private static final ThreadLocal<List<Point.Builder>> currentTestPoints = ThreadLocal.withInitial(ArrayList::new);
    private static final boolean escapeTestParams = PropertyProvider.getProperty(TestProperties.METRICS_INFLUXDB_ESCAPE_PARAMS, false);

    protected InfluxDB influxDB;
    protected String runId;
    protected String projectId;

    protected String dbName;
    protected String retentionPolicy;

    public InfluxDBReporter() {
        this(PropertyProvider.getPropertyOrThrow(TestProperties.METRICS_INFLUXDB_URL));
    }

    public InfluxDBReporter(String url) {
        buildInfluxDB(url);
        this.projectId = PropertyProvider.getPropertyOrThrow(TestProperties.METRICS_PROJECT_ID);
    }

    @Override
    public void resetRunId(UUID runId) {
        this.runId = runId.toString();
    }

    protected void buildInfluxDB(String url) {
        String user = PropertyProvider.getPropertyOrThrow(TestProperties.METRICS_INFLUXDB_USER);
        String pwd = PropertyProvider.getPropertyOrThrow(TestProperties.METRICS_INFLUXDB_PASSWORD);
        this.dbName = PropertyProvider.getPropertyOrThrow(TestProperties.METRICS_INFLUXDB_DBNAME);
        this.retentionPolicy = PropertyProvider.getPropertyOrThrow(TestProperties.METRICS_INFLUXDB_RETENTION_POLICY);

        influxDB = InfluxDBFactory.connect(url, user, pwd);
        if (databaseExists()) {
            influxDB = influxDB.setDatabase(dbName).setRetentionPolicy(retentionPolicy);
        } else {
            throw new IllegalStateException("Database " + dbName + " does not exist!");
        }
    }

    private boolean databaseExists() {
        QueryResult result = influxDB.query(new Query("SHOW DATABASES", ""));
        return result.getResults().get(0).getSeries().get(0).getValues().stream()
                .flatMap(Collection::stream).anyMatch(dbName::equals);
    }

    private Point.Builder preparePoint(Measurement msmt, long time) {
        if (runId == null) {
            throw new IllegalStateException("runId has not been initialized");
        } else {
            return Point.measurement(msmt.toString())
                    .tag(TAG_PROJECT_ID, projectId)
                    .addField(FIELD_RUN_ID, runId)
                    .time(time, TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public void addGlobalPoint(Measurement msmt, String name, String value, long time) {
        try {
            influxDB.write(preparePoint(msmt, time).tag(TAG_METRIC_NAME, name).addField(FIELD_VALUE, value).build());
        } catch (Exception e) {
            log.error("Failed to write to InfluxDB the following measurement: msmt={}, name={}, value={}, time={}", msmt, name, value, time, e);
        }
    }

    @Override
    public void addTestPoint(Measurement msmt, DurationType dtype, String name, long value, long time) {
        Point.Builder pb = preparePoint(msmt, time)
                .tag(TAG_METRIC_TYPE, dtype.toString())
                .tag(TAG_METRIC_NAME, name)
                .addField(FIELD_VALUE, value);
        currentTestPoints.get().add(pb);
    }

    @Override
    public void methodFinished(UUID testId, ITestResult testResult) {
        if (!currentTestPoints.get().isEmpty()) {
            BatchPoints batchPoints = BatchPoints
                    .database(dbName)
                    .tag(TAG_TEST_NAME, TestNameExtractor.getFullTestName(testResult, escapeTestParams))
                    .tag(TAG_SUITE_NAME, testResult.getTestContext().getSuite().getName())
                    .tag(TAG_GROUP_NAME, testResult.getTestContext().getName())
                    .tag(TAG_IS_TEST, String.valueOf(testResult.getMethod().isTest()))
                    .consistency(InfluxDB.ConsistencyLevel.ALL)
                    .retentionPolicy(retentionPolicy)
                    .build();

            currentTestPoints.get().stream().forEach(pb -> batchPoints.point(pb.addField(FIELD_TEST_ID, testId.toString()).build()));
            currentTestPoints.remove();
            try {
                influxDB.write(batchPoints);
            } catch (Exception e) {
                log.error("Failed to write to InfluxDB the following batch points: {}", batchPoints, e);
            }
        }
    }

    @Override
    public void close() throws Exception {
        try {
            influxDB.close();
        } catch (Exception e) {
            log.error("Failed to close InfluxDB", e);
        }
    }
}
