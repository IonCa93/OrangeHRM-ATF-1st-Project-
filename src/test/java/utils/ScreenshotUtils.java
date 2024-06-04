package utils;

import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScreenshotUtils.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HHmmss");

    public static void takeScreenshot(WebDriver driver, Scenario scenario) {
        if (driver instanceof TakesScreenshot) {
            byte[] screenshotBytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshotBytes, "image/png", "Screenshot");

            String scenarioName = scenario.getName();
            String timestamp = dateFormat.format(new Date());
            String screenshotFilename = scenarioName + "_" + timestamp + ".png";

            saveScreenshotToLocalFile(screenshotBytes, screenshotFilename);
            LOGGER.info("Screenshot taken for scenario: {}", scenarioName);
        } else {
            LOGGER.warn("Driver does not support taking screenshots.");
        }
    }

    private static void saveScreenshotToLocalFile(byte[] screenshotBytes, String screenshotFilename) {
        File screenshotDirectory = new File("target/screenshots");
        if (!screenshotDirectory.exists()) {
            screenshotDirectory.mkdirs();
        }
        File screenshotFile = new File(screenshotDirectory, screenshotFilename);
        try (FileOutputStream outputStream = new FileOutputStream(screenshotFile)) {
            outputStream.write(screenshotBytes);
            LOGGER.info("Screenshot saved to file: {}", screenshotFile.getAbsolutePath());
        } catch (IOException e) {
            LOGGER.error("Failed to save screenshot to file.", e);
        }
    }
}