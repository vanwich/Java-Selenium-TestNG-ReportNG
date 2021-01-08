package help.config;

public final class CustomTestProperties {
    //Application related properties
    /**
     * Generic application protocol
     */
    public static final String APP_PROTOCOL = "app.protocol";
    /**
     * Generic application host name (e.g. "sfoeiswas999")
     */
    public static final String APP_HOST = "app.host";
    /**
     * Generic application port (e.g. "8010")
     */
    public static final String APP_PORT = "app.port";
    /**
     * Generic application URL path (e.g. "/ipb-app/login.xhtml")
     */
    public static final String APP_PATH = "app.path";
    /**
     * User name to login to the application (e.g. "qa")
     */
    public static final String APP_USER = "app.user";
    /**
     * Password to login to the application (e.g. "qa")
     */
    public static final String APP_PASSWORD = "app.password";
    /**
     * Login through URL or not
     */
    public static final String APP_URL_LOGIN = "app.url.login";
    /**
     * Login through URL or not
     */
    public static final String APP_URL_LOGOUT = "app.url.logout";
    /**
     * Application Webservices port
     */
    public static final String WS_PORT = "app.ws.port";
    /**
     * Application Webservices path
     */
    public static final String WS_PATH = "app.ws.path";
    public static final String WS_PROTOCOL = "app.ws.protocol";
    public static final String WS_HOST = "app.ws.host";
    public static final String WS_USER = "app.ws.user";
    public static final String WS_PASSWORD = "app.ws.password";
    public static final String WS_URL_LOGIN = "app.ws.url.login";

    public static final String APP_ADMIN_PROTOCOL = "app.admin.protocol";
    public static final String APP_ADMIN_HOST = "app.admin.host";
    public static final String APP_ADMIN_PORT = "app.admin.port";
    public static final String APP_ADMIN_PATH = "app.admin.path";
    public static final String APP_ADMIN_USER = "app.admin.user";
    public static final String APP_ADMIN_PASSWORD = "app.admin.password";
    public static final String APP_ADMIN_URL_LOGIN = "app.admin.url.login";

    public static final String SISENSE_HOST = "app.sisense.host";
    public static final String SISENSE_PORT = "app.sisense.port";
    public static final String SISENSE_PATH = "app.sisense.path";
    public static final String SISENSE_USER = "app.sisense.user";
    public static final String SISENSE_PASSWORD = "app.sisense.password";
    public static final String SISENSE_PROTOCOL = "app.sisense.protocol";

    public static final String PERSONA_HOST = "app.persona.host";
    public static final String PERSONA_PORT = "app.persona.port";
    public static final String PERSONA_PATH = "app.persona.path";
    public static final String PERSONA_USER = "app.persona.user";
    public static final String PERSONA_PASSWORD = "app.persona.password";
    public static final String PERSONA_PROTOCOL = "app.persona.protocol";

    public static final String TALEND_USER = "app.talend.user";
    public static final String TALEND_PASSWORD = "app.talend.password";

    public static final String KIBANA_HOST = "app.kibana.host";
    public static final String KIBANA_PROTOCOL = "app.kibana.protocol";
    public static final String KIBANA_PORT = "app.kibana.port";
    public static final String KIBANA_PATH = "app.kibana.path";
    public static final String KIBANA_API = "app.kibana.api";
    public static final String KIBANA_CERTIFICATE_ENABLED = "app.kibana.cert.enabled";

    public static final String OR_URL_PATH = "app.or.path";
    public static final String OR_PORT = "app.or.port";
    public static final String OR_HOST = "app.or.host";
    public static final String OR_PROTOCOL = "app.or.protocol";
    public static final String OR_USER= "app.or.user";
    public static final String OR_PASSWORD = "app.or.password";
    public static final String OR_URL_LOGIN = "app.or.url.login";

    public static final String CLAIM_ADJUSTER_PROTOCOL = "claim.adjuster.protocol";
    public static final String CLAIM_ADJUSTER_HOST = "claim.adjuster.host";
    public static final String CLAIM_ADJUSTER_PATH = "claim.adjuster.path";
    public static final String CLAIM_ADJUSTER_PORT = "claim.adjuster.port";

    public static final String DXP_TEST_APP = "test.dxp.app";
    public static final String DXP_TOMCAT_PORT = "app.dxp.tomcat.port";
    public static final String DXP_NETTY_PORT = "app.dxp.netty.port";
    public static final String DXP_APP_HOST = "app.host.dxp";

    //Configuration related properties(misc)
    /**
     * Either use or not local time. if false - server side time will be used
     */
    public static final String USE_LOCAL_TIME_AS_SERVER = "time.uselocal";
    /**
     * Enable customer creation via rest
     */
    public static final String REST_CUSTOMER_ENABLED = "rest.customer.enabled";
    /**
     * List of policies which will be created via rest, if absent - only UI creation is available
     */
    public static final String REST_POLICY_SUPPORTED_TYPES = "rest.policy.supported.types";
    /**
     * Enable case profile creation via rest
     */
    public static final String REST_CASE_PROFILE_ENABLED = "rest.caseprofile.enabled";
    /**
     * Enable agency creation via rest
     */
    public static final String REST_AGENCY_ENABLED = "rest.agency.enabled";
    /**
     * Retry test immediately if error 500 occur - workaround for cases once application freshly deployed to bypass occasional errors 500
     */
    public static final String RETRY_ON_500 = "test.repeat.on500";

    /**
     * minimal pass rate for teammerge suite for jenkins job success
     */
    public static final String TEAMMERGE_PASSRATE = "test.teammerge.passrate";
    /**
     * flag which allow mark test failed by bug as passed
     */
    public static final String TEAMMERGE_FBB_PASS = "test.teammerge.fbb_pass";
    /**
     * Property responsible for saving entities on test failure.
     */
    public static final String SAVE_ON_FAILURE = "test.save.onfailure";
    /**
     * Property responsible for saving entities on test failure.
     */
    public static final String DEBUG_MODE = "test.debug.mode";
    /**
     * Property responsible for exception throwing onExecutionFinish. </br>
     * The main goal is mark CI job as failed for required pass rate {@code test.teammerge.passrate}.
     */
    public static final String LISTENER_ON_FINISH_THROW = "listener.onfinish.throw.exception";

    //DB Service properties
    /**
     * URL to the application database
     */
    public static final String DB_URL = "test.db.url";
    /**
     * User name to login to the application database
     */
    public static final String DB_USER = "test.db.user";
    /**
     * Password to login to the application database
     */
    public static final String DB_PASSWORD = "test.db.password";

    //SSH Client properties
    /**
     * Property responsible for application server timezone used in accurate system date/time calculations
     */
    public static final String ENV_TIME_ZONE = "test.environment.timezone";
    /**
     * User name for SSH connection
     */
    public static final String SSH_USER = "ssh.user";
    /**
     * Password for SSH connection
     */
    public static final String SSH_PASSWORD = "ssh.password";
    /**
     * Host for SSH connection (if not set then 'app.host' property will be used by default)
     */
    public static final String SSH_HOST = "ssh.host";
    /**
     * Custom port for SSH connection (22 by default)
     */
    public static final String SSH_PORT = "ssh.port";
    /**
     * Used for SSH connection, can replace {@link #SSH_PASSWORD} and should define project path to '.pem' file with rsa private key.</b>
     * For using this property make sure that host ssh server is supporting this feature and configure 'sessionConfigDefault' map with
     * 'PreferredAuthentications:publickey' value in {@link com.exigen.ipb.eisa.utils.ssh.ConnectionParameters}
     */
    public static final String SSH_AUTH_KEY_PATH = "ssh.authkeypath";
    /**
     * Session timeout for SSH connection in milliseconds. 600000 is used by default
     */
    public static final String SSH_SESSION_TIMEOUT = "ssh.session.timeout";
    /**
     * Maximum amount of time (in milliseconds) the {@link ConnectionManager#getConnection()} method should block before throwing an exception</br>
     * when the sessions pool is exhausted. When less than 0, the {@link ConnectionManager#getConnection()} method may block indefinitely. 600000 is used by default
     */
    public static final String SSH_POOL_MAX_WAIT = "ssh.pool.maxwait";
    /**
     * Limit on the number of SSH sessions allocated by the pool (checked out or idle) per each {@link com.exigen.ipb.eisa.utils.ssh.ConnectionParameters} instance.</br>
     * A negative value indicates no limit. 8 is used by default
     */
    public static final String SSH_POOL_MAX_TOTAL = "ssh.pool.maxtotal";
    /**
     * Command execution timeout via SSH connection in milliseconds. 600000 is used by default
     */
    public static final String SSH_EXEC_TIMEOUT = "ssh.exec.timeout";
    /**
     * Command execution retry interval via SSH connection in milliseconds. 100 is used by default
     */
    public static final String SSH_EXEC_RETRY_INTERVAL = "ssh.exec.retry.interval";

    //Jira client properties
    public static final String JIRA_URL = "jira.url";
    public static final String JIRA_LOGIN = "jira.login";
    public static final String JIRA_PASSWORD = "jira.password";

    //Jenkins client properties
    public static final String JENKINS_URL = "jenkins.url";
    public static final String JENKINS_TOKEN = "jenkins.token";
    public static final String JENKINS_USER = "jenkins.user";
    public static final String JENKINS_PASSWORD = "jenkins.password";

    //SOAP properties
    public static final String SOAP_BATCHJOB_ENDPOINT = "soap.batchjob.endpoint";

    //REST properties
    public static final String REST_CLIENT_CONNECTION_TIMEOUT = "rest.client.connect.timeout";
    public static final String REST_CLIENT_READ_TIMEOUT = "rest.client.read.timeout";
    public static final String REST_ADDITION_LOGGING = "rest.additional.logging";
    public static final String REST_XML_JAXB_USING = "rest.jaxb.using";

    //Application logs grabber's properties
    /**
     * Enable performance log fetching and storage to test analytics
     */
    public static final String PERFORMANCE_LOGGING_ENABLED = "performance.logging.enabled";
    /**
     * Enable ELK(KIBANA) log fetching and storage to test analytics
     */
    public static final String ELK_LOGGING_ENABLED = "elk.logging.enabled";
    /**
     * Do not save application log files for passed tests even if main app log contains errors not related to the test itself
     */
    public static final String LISTENER_ON_SUCCESS_DISABLE_LOGS = "listener.onsuccess.disable.logs";
    /**
     * Property responsible for time shift on execution finish.
     */
    public static final String TEST_IGNORE_APP_LOG = "test.ignore.applog";
    /**
     * Specify default log grabber class to be used for overall report. i.e.(TomcatLogGrabber etc.)
     */
    public static final String DEFAULT_LOG_GRABBER = "log.default.grabber";

    //i18n related properties
    /**
     * Determine which locale to use for testing (i.e. for China - zh_CN, for USA - en_US etc)
     */
    public static final String TESTS_LOCALE = "tests.locale";
    /**
     * Determine which locale to use for Currency , where %1$s should be replaced by relevant locale used for current run, i.e. for China tests.zh_CN.currency.locale=en_US id all values are in $
     */
    public static final String CURRENCY_LOCALE = "tests.%1$s.currency.locale";
    /**
     * Determine which Date format should be used for testing, where %1$s should be replaced by relevant locale used for current run, i.e. for China tests.zh_CN.locale.dateformat=yyyy-MM-dd
     */
    public static final String DATE_FORMAT_LOCALE = "tests.%1$s.locale.dateformat";

    //Data population related properties
    /**
     * Determine how many scenarios should be executed. max = 300
     */
    public static final String DATA_POPULATION_PREC_AU = "datapopulation.prec_au.amount";
    /**
     * Whether report generation for data population is needed or not
     */
    public static final String DATA_POPULATION_REPORT_ENABLED = "data.population.report.enabled";

    //PEF related properties
    /**
     * For old PEF and current isba tests set to true, cause currentTime treated as phase start time. For normal people should be set to false. Cause we have getPhaseStartTime for such purpose
     */
    public static final String TIME_SETTER_UTIL_PHASE_TIME_IS_ON = "timesetter.util.phase.time.is.on";
    /**
     * Determine how many permits could be processed in parallel
     * Default value : 30
     */
    public static final String PEF_MAX_PERMITS = "pef.maxpermits";
    /**
     * Determine phase timeout. i.e. how long SyncManager will wait before starting new phase/job
     * Default value: 2000L
     */
    public static final String PEF_PHASE_TIMEOUT = "pef.phasetimeout";

    /**
     * Determine date to be set on server before suite execution start
     */
    public static final String PEF_INITIAL_TIME = "pef.initial.time";

    /**
     * Adjust time to current on server before suite execution start (and before initial time update if present)
     */
    public static final String PEF_ADJUST_TIME_AT_START = "pef.adjust_time.at_start_tests";
    /**
     * Adjust time to current on server after suite execution finish
     */
    public static final String PEF_ADJUST_TIME_AT_FINISH = "pef.adjust_time.at_finish_tests";
    /**
     * Set start hour for working days
     */
    public static final String TIME_SETTER_WORKING_DAY_START_HH = "timesetter.working.day.startHours";
    /**
     * Set start minutes for working days
     */
    public static final String TIME_SETTER_WORKING_DAY_START_MM = "timesetter.working.day.startMinutes";

    /**
     * Set chrome mobile version to be set in capabilities
     */
    public static final String CHROME_MOBILE_VERSION = "chrome.mobile.version";

    //AWS S3 Client properties
    /**
     * Region used by S3 service
     */
    public static final String AWS_S3_REGION = "aws.s3.region";
    /**
     * S3 host home folder path
     */
    public static final String AWS_S3_HOST_FOLDER_PATH = "aws.s3.folder.path";
    /**
     * S3 connection access key
     */
    public static final String AWS_S3_ACCESS_KEY = "aws.s3.accessKey";
    /**
     * S3 connection secret key
     */
    public static final String AWS_S3_SECRET_KEY = "aws.s3.secretKey";
    /**
     * S3 bucket
     */
    public static final String AWS_S3_BUCKET = "aws.s3.bucket";
    /**
     * Use server side encryption or not, default true
     */
    public static final String AWS_S3_USE_SERVER_SIDE_ENCRYPTION = "aws.s3.useServerSideEncryption";

    //FTP Controller properties
    public static final String FTP_HOST = "ftp.host";
    public static final String FTP_PORT = "ftp.port";
    public static final String FTP_USER = "ftp.user";
    public static final String FTP_PASSWORD = "ftp.password";

    //PDF Validation related properties
    /**
     * Path to TestData files with coordinates mapping
     */
    public static final String PDF_MAPPING_DATA_PATH = "pdf.mappingdata.path";
    /**
     * Path to PDF files which will be used as "Golden source" to fetch coordinates using mapping TestData
     */
    public static final String PDF_TEMPLATE_FILES_PATH = "pdf.template.path";

    /**
     * Either use navigation before tab filling or not. default value - false
     */
    public static final String WORKSPACE_TABS_NAVIGATION = "tests.workspace.tabs.navigation";
    /**
     * Either enable or disable application logs gathering for overall report
     */
    public static final String REPORT_ENABLED = "report.app.logs.gathering";
    /**
     * Set to true if DXP deserialization report is required in overall report, default value - false
     */
    public static final String REPORT_DXP_DESERIALIZATION = "report.dxp.deserialization";
    /**
     * Set to true if DXP api coverage report is required in overall report, default value - false
     */
    public static final String REPORT_DXP_API_COVERAGE = "report.dxp.api.coverage";
    /**
     * Set to true if Jenkins integration tests report is required in overall report, default value - false
     */
    public static final String REPORT_JENKINS_INTEGRATION_TESTS = "report.jnk.integration.tests";
    /**
     * Set to true if Jenkins unit tests report is required in overall report, default value - false
     */
    public static final String REPORT_JENKINS_UNIT_TESTS = "report.jnk.unit.tests";
    /**
     * Define TA 3+ frontend url. i.e. http://sfoetcsta03.sjclab.exigengroup.com:3000 if differs from backend url
     */
    public static final String REPORT_TA_URL = "report.ta.url";

    private CustomTestProperties() {
    }
}
