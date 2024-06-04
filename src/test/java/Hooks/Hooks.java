package Hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.*;

public class Hooks {
    private static final Logger LOGGER = LoggerFactory.getLogger(Hooks.class);

    @Before(value = "@UI")
    public void setUp(Scenario scenario) {
        LOGGER.info("Initializing WebDriver for UI testing");
        String baseUrl = PropertiesUtil.getProperty("baseUrl"); // Get the base URL first
        WebDriver driver = DriverManager.getDriver();
        driver.manage().window().maximize(); // Maximize the browser window
        ScenarioContext.getInstance().saveValueToScenarioContext(ContextKey.WEBDRIVER_INSTANCE, driver); // Store the driver instance in Scenario Context
        LOGGER.info("WebDriver initialized and browser window maximized successfully");
        navigateToLoginPage(driver, baseUrl); // Pass the driver instance
    }

    public static void navigateToLoginPage(WebDriver driver, String baseUrl) {
        driver.get(baseUrl);
        String currentUrl = driver.getCurrentUrl();
        LOGGER.info("Navigated to the login page. Current URL: {}", currentUrl);
    }

    @After(value = "@UI", order = 1)
    public void tearDown(Scenario scenario) {
        if (scenario.getSourceTagNames().contains("@UI")) {
            LOGGER.info("Taking screenshot before closing the browser for scenario: {}", scenario.getName());
            WebDriver driver = ScenarioContext.getInstance().getValueFromScenarioContext(ContextKey.WEBDRIVER_INSTANCE); // Retrieve the driver instance from Scenario Context
            ScreenshotUtils.takeScreenshot(driver, scenario);
            DriverManager.quitDriver();
        }
    }

    @After(order = 0)
    public void clearScenarioContext() {
        LOGGER.info("Starting Scenario Context teardown");
        ScenarioContext.getInstance().clearContext();
        LOGGER.info("Scenario Context successfully cleared");
    }
}