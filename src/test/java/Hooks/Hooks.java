package Hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import utils.DriverManager;
import utils.PropertiesUtil;
import utils.ScenarioContext;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Hooks {
    private static final Logger LOGGER = LoggerFactory.getLogger(Hooks.class);

    @Before(value = "@ui")
    public void setUp() {
        LOGGER.info("Initializing WebDriver for UI testing");
        String baseUrl = PropertiesUtil.getProperty("baseUrl"); // Get the base URL first
        WebDriver driver = DriverManager.getDriver();
        driver.manage().window().maximize(); // Maximize the browser window
        DriverManager.getDriver(); // Selenium will handle any exceptions during driver initialization
        LOGGER.info("WebDriver initialized and browser window maximized successfully");
        navigateToLoginPage(driver, baseUrl); // Pass the driver instance
    }

    public static void navigateToLoginPage(WebDriver driver, String baseUrl) {
        driver.get(baseUrl);
        String currentUrl = driver.getCurrentUrl();
        LOGGER.info("Navigated to the login page. Current URL: {}", currentUrl);
    }

    @After(value = "@ui", order = 1)
    public void tearDown(Scenario scenario) {
        LOGGER.info("Taking screenshot before closing the browser for scenario: {}", scenario.getName());
        WebDriver driver = DriverManager.getDriver();
        byte[] screenshotBytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        if (scenario.isFailed()) {
            LOGGER.info("UI test failed. Attempting to take a screenshot...");
            scenario.attach(screenshotBytes, "image/png", "Failed Screenshot");
            LOGGER.info("Screenshot taken for failed scenario: {}", scenario.getName());
        } else {
            scenario.attach(screenshotBytes, "image/png", "Passed Screenshot");
            LOGGER.info("Screenshot taken for passed scenario: {}", scenario.getName());
        }
        DriverManager.quitDriver(); // Close the browser after taking the screenshot
    }

    @After(order = 0)
    public void clearScenarioContext() {
        LOGGER.info("Starting Scenario Context teardown");
        ScenarioContext.getInstance().clearContext();
        LOGGER.info("Scenario Context successfully cleared");
    }
}