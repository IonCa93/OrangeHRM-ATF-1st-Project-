package Hooks;

import actions.UIActions;
import config.DriverManager;
import config.Logging;
import context.ScenarioContext;
import enums.ContextKey;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
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

    private static final Dimension DESKTOP_WINDOW_SIZE = new Dimension(1920, 1080);

    @Before
    public void beforeScenario(Scenario scenario) {
        String featureName = LogNameUtils.extractFeatureName(scenario);
        String scenarioName = scenario.getName();
        logging.startLogging(featureName, scenarioName);
    }

    @Before(value = "@UI")
    public void setUp() {
        logger.info("Initializing WebDriver for UI testing");
        String baseUrl = PropertiesUtil.getProperty("baseUrl");
        WebDriver driver = DriverManager.getDriver();
        configureBrowserWindow(driver);
        ScenarioContext.getInstance().saveContext(ContextKey.WEB_DRIVER_INSTANCE, driver);
        logger.info("WebDriver initialized and browser window configured successfully");
        UIActions.navigateToLoginPage(driver, baseUrl);
    }

    private void configureBrowserWindow(WebDriver driver) {
        if (!DriverManager.isHeadless()) {
            maximizeBrowserWindow(driver);
        }
    }

    private void maximizeBrowserWindow(WebDriver driver) {
        try {
            driver.manage().window().maximize();
        } catch (WebDriverException e) {
            logger.warn("Browser window maximize failed. Applying desktop window size fallback.", e);
            driver.manage().window().setSize(DESKTOP_WINDOW_SIZE);
        }
    }

    @After
    public void afterScenario(Scenario scenario) {
        try {
            tearDownDriver(scenario);
        } finally {
            try {
                clearScenarioContext();
            } finally {
                logging.stopLogging();
                attachLogsToReport(scenario);
            }
        }
    }

    private void tearDownDriver(Scenario scenario) {
        WebDriver driver = ScenarioContext.getInstance().getContext(ContextKey.WEB_DRIVER_INSTANCE, WebDriver.class);
        if (driver == null) {
            driver = DriverManager.driver;
        }

        if (driver != null) {
            logger.info(String.format("Taking screenshot before closing the browser for scenario: %s", scenario.getName()));
            ScreenshotUtils.takeScreenshot(driver, scenario);
        }

        DriverManager.quitDriver();
    }

    private void clearScenarioContext() {
        ScenarioContext.getInstance().clearContext();
        logger.info("Scenario Context successfully cleared");
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
}
