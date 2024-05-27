package Hooks;

import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import utils.DriverManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TearDownHook {
    private static final Logger logger = LoggerFactory.getLogger(TearDownHook.class);

    @After(value = "@ui")
    public void tearDown(Scenario scenario) {
        // Close the web browser after each scenario
        logger.info("Taking screenshot before closing the browser for scenario: {}", scenario.getName());
        WebDriver driver = DriverManager.getDriver();
        byte[] screenshotBytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        if (scenario.isFailed()) {
            logger.info("UI test failed. Attempting to take a screenshot...");
            // Save screenshot for failed scenarios
            scenario.attach(screenshotBytes, "image/png", "Failed Screenshot");
            logger.info("Screenshot taken for failed scenario: {}", scenario.getName());
        } else {
            // Save screenshot for passed scenarios
            scenario.attach(screenshotBytes, "image/png", "Passed Screenshot");
            logger.info("Screenshot taken for passed scenario: {}", scenario.getName());
        }
        DriverManager.quitDriver(); // Close the browser after taking the screenshot
    }
}
