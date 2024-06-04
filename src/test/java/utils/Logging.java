package utils;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.encoder.LayoutWrappingEncoder;
import ch.qos.logback.classic.PatternLayout;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logging {
    // File appenders for logging to feature-specific and archive log files (one for the current execution and one for archive).
    private FileAppender<ILoggingEvent> featureLogAppender;
    private FileAppender<ILoggingEvent> archiveLogAppender;
    private String featureName;
    // The logger instance that will be used to log messages for the feature.
    public Logger featureLogger;

    @Before //
    public void beforeScenario(Scenario scenario) {
        // Extract the feature name from the scenario's URI.
        featureName = extractFeatureName(scenario);
        // Create a timestamp to uniquely identify this scenario run.
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss.SS"));
        // Construct the filenames for the feature-specific and archived logs using the feature name and timestamp.
        String featureLogFileName = featureName + "_" + timestamp + ".log";
        String archiveLogFileName = featureName + "_" + timestamp + "_archived.log";

        String featureLogDirectoryPath = "target/logs/featureLogs";
        String archiveLogDirectoryPath = "ArchFeatureLogs";
        // Ensure that the directories for the log files exist (create them if they don't exist).
        ensureDirectoriesExist(featureLogDirectoryPath, archiveLogDirectoryPath);


        // Get the logger context
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        // Get the logger instance based on the package name
        featureLogger = loggerContext.getLogger(getClass().getPackage().getName());




        // Set up the file appenders for logging.
        featureLogAppender = setupLogging(featureLogDirectoryPath, featureLogFileName);
        archiveLogAppender = setupLogging(archiveLogDirectoryPath, archiveLogFileName);
    }

    // Method to extract the feature name from the scenario URI.
    private String extractFeatureName(Scenario scenario) {
        // Get the path part of the URI and extract the feature file name.
        String featureName = scenario.getUri().getPath();
        // Remove the ".feature" extension and return the feature name.
        return featureName.substring(featureName.lastIndexOf('/') + 1).replace(".feature", "");
    }

    // Method to set up a FileAppender for logging.
    private FileAppender<ILoggingEvent> setupLogging(String logDirectoryPath, String logFileName) {
        // Get the current logging context.
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

        // Set up the log message format.
        PatternLayout layout = new PatternLayout();
        layout.setContext(loggerContext);
        layout.setPattern("%date{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n");
        layout.start();

        // Set up the encoder to convert log events into byte streams.
        LayoutWrappingEncoder<ILoggingEvent> encoder = new LayoutWrappingEncoder<>();
        encoder.setLayout(layout);
        encoder.setContext(loggerContext);
        encoder.start();

        // Create and configure the file appender.
        FileAppender<ILoggingEvent> appender = new FileAppender<>();
        appender.setContext(loggerContext);
        appender.setName("FileAppender-" + logFileName);
        appender.setFile(logDirectoryPath + "/" + logFileName);
        appender.setEncoder(encoder);
        appender.start();

        // Add the file appender to the feature logger.
        featureLogger.addAppender(appender);
        // Allow the logger to pass logs up the hierarchy.
        featureLogger.setAdditive(true);

        return appender;
    }

    private void ensureDirectoriesExist(String... paths) {
        for (String path : paths) {
            try {
                Files.createDirectories(Paths.get(path));
            } catch (Exception e) {
                throw new RuntimeException("Could not create directories for logging", e);
            }
        }
    }

    @After
    public void afterScenario(Scenario scenario) {
        // Stop the file appenders, ensuring that all logs are flushed to disk.
        if (featureLogAppender != null) {
            featureLogAppender.stop();
            // Detach the appender from the logger.
            featureLogger.detachAppender(featureLogAppender);
        }
        if (archiveLogAppender != null) {
            archiveLogAppender.stop();
            featureLogger.detachAppender(archiveLogAppender);
        }
    }
}