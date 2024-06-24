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

public class Hooks {
    private static final Logger logger = LoggerFactory.getLogger(Hooks.class);
    private Logging logging = new Logging();

    @Before
    public void beforeScenario(Scenario scenario) {
        String featureName = extractFeatureName(scenario);
        String scenarioName = scenario.getName();
        logging.startLogging(featureName, scenarioName);
    }

    @Before(value = "@UI")
    public void setUp() {
        logger.info("Initializing WebDriver for UI testing");
        String baseUrl = PropertiesUtil.getProperty("baseUrl"); // Get the base URL first
        WebDriver driver = DriverManager.getDriver();
        driver.manage().window().maximize(); // Maximize the browser window
        ScenarioContext.getInstance().saveContext(ContextKey.WEB_DRIVER_INSTANCE, driver); // Store the driver instance in Scenario Context
        logger.info("WebDriver initialized and browser window maximized successfully");
        UIActions.navigateToLoginPage(driver, baseUrl); // Pass the driver instance
    }

    @After(value = "@UI")
    public void tearDown(Scenario scenario) {
        if (scenario.getSourceTagNames().contains("@UI")) {
            logger.info(String.format("Taking screenshot before closing the browser for scenario: %s", scenario.getName()));
            WebDriver driver = ScenarioContext.getInstance().getContext(ContextKey.WEB_DRIVER_INSTANCE); // Retrieve the driver instance from Scenario Context
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
    public void afterScenario() {
        logging.stopLogging();
    }

    private String extractFeatureName(Scenario scenario) {
        String featureName = scenario.getUri().getPath();
        return featureName.substring(featureName.lastIndexOf('/') + 1).replace(".feature", "");
    }
}