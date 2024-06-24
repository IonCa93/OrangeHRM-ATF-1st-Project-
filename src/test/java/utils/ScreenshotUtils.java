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

    private static final String ATTACHMENT_TYPE = "image/png";

    private static final String ATTACHMENT_DESCRIPTION = "Screenshot";

    private static final String SCREENSHOT_DIRECTORY = "target/Evidence/screenshots";

    private static final Duration TIMEOUT_DURATION = Duration.ofSeconds(Integer.parseInt(PropertiesUtil.getProperty("timeout.seconds")));

    public static void takeScreenshot(WebDriver driver, Scenario scenario) {
        if (driver instanceof TakesScreenshot) {
            try {
                WebDriverWait wait = new WebDriverWait(driver, TIMEOUT_DURATION);;
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("body")));

                byte[] screenshotBytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshotBytes, ATTACHMENT_TYPE, ATTACHMENT_DESCRIPTION);

                String scenarioName = scenario.getName();
                String timestamp = dateFormat.format(new Date());
                String screenshotFilename = scenarioName + "_" + timestamp + ".png";

                saveScreenshotToLocalFile(screenshotBytes, screenshotFilename);
                logger.info(String.format("Screenshot taken for scenario: %s", scenarioName));
            } catch (Exception e) {
                logger.error("Failed to capture screenshot or save to file.", e);
            }
        } else {
            logger.warn("Driver does not support taking screenshots.");
        }
    }

    private static void saveScreenshotToLocalFile(byte[] screenshotBytes, String screenshotFilename) {
        File screenshotDirectory = new File(SCREENSHOT_DIRECTORY);
        if (!screenshotDirectory.exists()) {
            boolean dirCreated = screenshotDirectory.mkdirs();
            if (!dirCreated) {
                logger.error("Failed to create screenshot directory: " + SCREENSHOT_DIRECTORY);
                return;
            }
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
