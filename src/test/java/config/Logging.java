package config;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.classic.PatternLayout;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logging {

    private static final String TIMESTAMP_PATTERN = "yyyy-MM-dd_HH-mm-ss.SS";

    private static final String FEATURE_LOG_DIRECTORY_PATH = "target/Evidence/logs/featureLogs";

    private static final String LAYOUT_PATTERN = "%date{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n";

    private static final String ENCODER_PATTERN = "%date [%thread] %-5level %logger{35} - %msg%n";

    private FileAppender<ILoggingEvent> featureLogAppender;

    private String featureName;

    public Logger featureLogger;

    private String featureLogFileName;

    public void startLogging(String featureName, String scenarioName) {
        this.featureName = featureName + "_" + scenarioName;
        LocalDateTime currentTime = LocalDateTime.now();
        String timestamp = currentTime.format(DateTimeFormatter.ofPattern(TIMESTAMP_PATTERN));
        this.featureLogFileName = this.featureName + "_" + timestamp + ".log";

        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        featureLogger = loggerContext.getLogger(this.featureName);
        featureLogAppender = setupLogging(FEATURE_LOG_DIRECTORY_PATH, featureLogFileName);
    }

    private FileAppender<ILoggingEvent> setupLogging(String logDirectoryPath, String logFileName) {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

        PatternLayout layout = new PatternLayout();
        layout.setContext(loggerContext);
        layout.setPattern(LAYOUT_PATTERN);
        layout.start();

        FileAppender<ILoggingEvent> appender = new FileAppender<>();
        appender.setContext(loggerContext);
        appender.setName("FileAppender-" + logFileName);
        appender.setFile(logDirectoryPath + "/" + logFileName);

        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(loggerContext);
        encoder.setPattern(ENCODER_PATTERN);
        encoder.start();
        appender.setEncoder(encoder);
        appender.start();
        Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.addAppender(appender);

        return appender;
    }

    public void stopLogging() {
        if (featureLogAppender != null) {
            featureLogAppender.stop();
            featureLogger.detachAppender(featureLogAppender);
        }
    }
    public String getFeatureLogFileName() {
        return featureLogFileName;
    }
}