package utils;

import io.cucumber.java.Scenario;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

public class ScreenshotUtils {
    private static final Logger logger = LoggerFactory.getLogger(ScreenshotUtils.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HHmmss");

    private static final String ATTACHMENT_TYPE = "image/png"; // Constant for attachment type
    private static final String ATTACHMENT_DESCRIPTION = "Screenshot"; // Constant for attachment description
    private static final String SCREENSHOT_DIRECTORY = "target/screenshots"; // Constant for screenshot directory

    public static void takeScreenshot(WebDriver driver, Scenario scenario) {
        if (driver instanceof TakesScreenshot) {
            try {
                // Retrieve the wait timeout from the configuration
                int timeoutSeconds = Integer.parseInt(PropertiesUtil.getProperty("timeout.seconds"));

                // Add an explicit wait for a certain condition before taking the screenshot
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("body")));

                byte[] screenshotBytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshotBytes, ATTACHMENT_TYPE, ATTACHMENT_DESCRIPTION);

                String scenarioName = scenario.getName();
                String timestamp = dateFormat.format(new Date());
                String screenshotFilename = scenarioName + "_" + timestamp + ".png";

                saveScreenshotToLocalFile(screenshotBytes, screenshotFilename);
                logger.info(String.format("Screenshot taken for scenario: %s", scenarioName));
            } catch (Exception e) {
                logger.error(String.format("Failed to capture screenshot for scenario: %s", scenario.getName(), e));
            }
        } else {
            logger.warn("Driver does not support taking screenshots.");
        }
    }

    private static void saveScreenshotToLocalFile(byte[] screenshotBytes, String screenshotFilename) {
        File screenshotDirectory = new File(SCREENSHOT_DIRECTORY);
        if (!screenshotDirectory.exists()) {
            screenshotDirectory.mkdirs();
        }
        File screenshotFile = new File(screenshotDirectory, screenshotFilename);
        try (FileOutputStream outputStream = new FileOutputStream(screenshotFile)) {
            outputStream.write(screenshotBytes);
            logger.info(String.format("Screenshot saved to file: %s", screenshotFile.getAbsolutePath()));
        } catch (IOException e) {
            logger.error("Failed to save screenshot to file.", e);
        }
    }
}
