package config;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.classic.PatternLayout;
import io.cucumber.java.Scenario;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logging {
    // File appender for logging to feature-specific log file.
    private FileAppender<ILoggingEvent> featureLogAppender;
    private String featureName;
    // The logger instance that will be used to log messages for the feature.
    public Logger featureLogger;

    public void startLogging(String featureName, String scenarioName) {
        // Extract the feature name from the scenario's URI.
        this.featureName = featureName + "_" + scenarioName;
        // Create a timestamp to uniquely identify this scenario run.
        LocalDateTime currentTime = LocalDateTime.now(); // Get the current timestamp
        String timestamp = currentTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss.SS")); // Format the timestamp
        // Construct the filename for the feature-specific log using the feature name and timestamp.
        String featureLogFileName = this.featureName + "_" + timestamp + ".log";
        String featureLogDirectoryPath = "target/logs/featureLogs";

        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        featureLogger = loggerContext.getLogger(this.featureName);
        featureLogAppender = setupLogging(featureLogDirectoryPath, featureLogFileName);
    }

    // Method to set up a FileAppender for logging.
    private FileAppender<ILoggingEvent> setupLogging(String logDirectoryPath, String logFileName) {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

        PatternLayout layout = new PatternLayout();
        layout.setContext(loggerContext);
        layout.setPattern("%date{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n");
        layout.start();

        FileAppender<ILoggingEvent> appender = new FileAppender<>();
        appender.setContext(loggerContext);
        appender.setName("FileAppender-" + logFileName);
        appender.setFile(logDirectoryPath + "/" + logFileName);

        // Set up encoder
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(loggerContext);
        encoder.setPattern("%date [%thread] %-5level %logger{35} - %msg%n");
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
}
