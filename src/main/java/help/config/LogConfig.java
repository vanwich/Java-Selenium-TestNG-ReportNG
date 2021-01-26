package help.config;

import static help.utils.file.FileUtils.deleteDir;
import java.io.File;
import com.aventstack.extentreports.utils.FileUtil;

/**
 * Created by lgu2 on 11/28/2019.
 */
public class LogConfig {
    /**日志类型 ，默认为extentreports， 0 = extentreports， 1 = log4j **/
    public static int logType = 1;
    /**失败重跑次数，默认0，失败不重跑**/
    public static int retryTimes = 0;
    /**日志存放路径，最好是一个共享路径**/
    public static String sharedFolder = System.getProperty("user.dir");
    /**如需发送邮件，请配置SMTP host，比如163邮箱：smtp.163.com**/
    public static String smtpHost;
    /**发送邮箱的账号**/
    public static String sender;
    /**发送邮箱的账号密码**/
    public static String sendPassword;
    /**邮件接收者账号**/
    public static String receivers;
    /**邮件主题，默认："Test Report Generated By Log4Reports"**/
    public static String subject;

    private String sharedPath;
    private String log4jFolder;
    private String log4jPath;
    private String extentFolder;
    private String extentLogPath;
    private String snapshotFolder;

    public LogConfig(Builder builder) {
        LogConfig.logType = builder.logType;
        LogConfig.retryTimes = builder.retryTimes;
        LogConfig.sharedFolder = builder.sharedFolder;
        LogConfig.smtpHost = builder.smtpHost;
        LogConfig.sender = builder.sender;
        LogConfig.sendPassword = builder.sendPassword;
        LogConfig.receivers = builder.receivers;
        LogConfig.subject = builder.subject;
        initLogPath(); //初始化日志文件夹
    }

    public static class Builder {
        private int logType = 0;
        private int retryTimes = 0;
        private String sharedFolder = "";

        private String smtpHost;
        private String sender;
        private String sendPassword;

        private String receivers;
        private String subject = "Test Report Generated By Log4Reports";

        public Builder(String sharedFolder){
            this.sharedFolder = sharedFolder;
        }

        public Builder setLogType(int logType) {
            this.logType = logType;
            return this;
        }

        public Builder setRetryTimes(int retryTimes) {
            this.retryTimes = retryTimes;
            return this;
        }

        public Builder setSmtpHost(String smtpHost) {
            this.smtpHost = smtpHost;
            return this;
        }

        public Builder setSender(String sender) {
            this.sender = sender;
            return this;
        }


        public Builder setSendPassword(String sendPassword) {
            this.sendPassword = sendPassword;
            return this;
        }

        public Builder setReceivers(String receivers) {
            this.receivers = receivers;
            return this;
        }

        public Builder setSubject(String subject) {
            this.subject = subject;
            return this;
        }

        public LogConfig build(){
            return new LogConfig(this);
        }
    }
    private void initLogPath(){
        sharedPath =  FileUtil.getFileName(LogConfig.sharedFolder);
        log4jFolder = sharedPath + "\\logs\\";
        log4jPath = sharedPath + "\\logs\\output.log";
        extentFolder = sharedPath + "\\reports\\";
        extentLogPath = sharedPath + "\\reports\\reports.html";
        snapshotFolder = sharedPath + "\\reports\\snapshot\\";

        File file;
        if(LogConfig.logType == 0){
            file = new File(extentFolder);
            if(!file.exists()) file.mkdir();
        }else{
            file = new File(log4jFolder);
            if(!file.exists())file.mkdir();
        }

        if(LogConfig.logType == 0 && new File(extentLogPath).exists() ||
                LogConfig.logType == 1 && new File(log4jPath).exists()){
            if(deleteDir(file)) file.mkdir();
            if(file.exists() && file.list().length == 0){
                System.out.println("初始化日志文件夹【成功】");
            }else{
                System.out.println("初始化日志文件夹【失败】");
            }
        }
    }

    /**
     * @return the log4jPath
     */
    public String getLog4jPath() {
        return log4jPath;
    }

    /**
     * @return the extentLogPath
     */
    public String getExtentLogPath() {
        return extentLogPath;
    }

    /**
     * @return the snapshotFolder
     */
    public String getSnapshotFolder() {
        return snapshotFolder;
    }
}
