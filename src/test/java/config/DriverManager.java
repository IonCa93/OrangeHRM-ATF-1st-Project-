package config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.PropertiesUtil;

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

    public static WebDriver getDriver() {
        if (driver == null) {
            String browserType = getBrowserType();
            boolean headless = isHeadless();

            driver = switch (browserType) {
                case "CHROME" -> initChromeDriver(headless);
                case "FIREFOX" -> initFirefoxDriver(headless);
                default -> {
                    logger.error("Unsupported browser type: {}", browserType);
                    throw new IllegalArgumentException("Unsupported browser type: " + browserType);
                }
            };
        }
        return driver;
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

    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}