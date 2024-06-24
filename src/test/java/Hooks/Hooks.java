package Hooks;

import actions.UIActions;
import config.DriverManager;
import config.Logging;
import context.ScenarioContext;
import enums.ContextKey;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Hooks {

    private static final Logger logger = LoggerFactory.getLogger(Hooks.class);

    private Logging logging = new Logging();

    private static final String LOG_FILE_PATH = "target/Evidence/logs/featureLogs";

    @Before
    public void beforeScenario(Scenario scenario) {
        String featureName = extractFeatureName(scenario);
        String scenarioName = scenario.getName();
        logging.startLogging(featureName, scenarioName);
    }

    @Before(value = "@UI")
    public void setUp() {
        logger.info("Initializing WebDriver for UI testing");
        String baseUrl = PropertiesUtil.getProperty("baseUrl");
        WebDriver driver = DriverManager.getDriver();
        driver.manage().window().maximize();
        ScenarioContext.getInstance().saveContext(ContextKey.WEB_DRIVER_INSTANCE, driver);
        logger.info("WebDriver initialized and browser window maximized successfully");
        UIActions.navigateToLoginPage(driver, baseUrl);
    }

    @After(value = "@UI")
    public void tearDown(Scenario scenario) {
        if (scenario.getSourceTagNames().contains("@UI")) {
            logger.info(String.format("Taking screenshot before closing the browser for scenario: %s", scenario.getName()));
            WebDriver driver = ScenarioContext.getInstance().getContext(ContextKey.WEB_DRIVER_INSTANCE, WebDriver.class);
            ScreenshotUtils.takeScreenshot(driver, scenario);
            DriverManager.quitDriver();
        }
    }

    @After
    public void clearScenarioContext() {
        ScenarioContext.getInstance().clearContext();
        logger.info("Scenario Context successfully cleared");
    }

    @After
    public void afterScenario(Scenario scenario) {
        logging.stopLogging();
        attachLogsToReport(scenario);
    }

    private void attachLogsToReport(Scenario scenario) {
        String logFileName = logging.getFeatureLogFileName();
        String logFilePath = LOG_FILE_PATH + "/" + logFileName;

        try {
            byte[] logsBytes = Files.readAllBytes(Paths.get(logFilePath));
            String logs = new String(logsBytes);
            scenario.attach(logs, "text/plain", "Logs");
        } catch (IOException e) {
            logger.error("Failed to read log file: " + logFilePath, e);
            logger.warn("Logs will not be attached to the report due to the error.");
        }
    }

    private String extractFeatureName(Scenario scenario) {
        String featureName = scenario.getUri().getPath();
        return featureName.substring(featureName.lastIndexOf('/') + 1).replace(".feature", "");
    }
}