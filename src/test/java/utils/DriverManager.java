package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DriverManager {
    private static final Logger logger = LoggerFactory.getLogger(DriverManager.class);
    public static WebDriver driver;

    // private constructor to prevent instantiation from other classes
    private DriverManager() { }

    // Method to get the browser type from properties
    private static String getBrowserType() {
        return PropertiesUtil.getProperty("browser.type").toUpperCase();
    }

    // Method to determine if the browser should run in headless mode
    private static boolean isHeadless() {
        return Boolean.parseBoolean(PropertiesUtil.getProperty("browser.headless"));
    }

    private static WebDriver initChromeDriver(boolean headless) {
        ChromeOptions options = new ChromeOptions();
        if (headless) {
            options.addArguments("--headless");
        }
        logger.info("Starting Chrome browser");
        return new ChromeDriver(options);
    }

    private static WebDriver initFirefoxDriver(boolean headless) {
        FirefoxOptions options = new FirefoxOptions();
        if (headless) {
            options.addArguments("--headless");
        }
        logger.info("Starting Firefox browser");
        return new FirefoxDriver(options);
    }

    public static WebDriver getDriver() {
        if (driver == null) {
            String browserType = getBrowserType();
            boolean headless = isHeadless();

            switch (browserType) {
                case "CHROME":
                    driver = initChromeDriver(headless);
                    break;
                case "FIREFOX":
                    driver = initFirefoxDriver(headless);
                    break;
                default:
                    logger.error("Unsupported browser type: {}", browserType);
                    throw new IllegalArgumentException("Unsupported browser type: " + browserType);
            }
        }
        return driver;
    }

    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}