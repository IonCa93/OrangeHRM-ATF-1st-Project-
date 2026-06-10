package config;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.PropertiesUtil;

public class DriverManager {//TODO move in utils

    private static final Logger logger = LoggerFactory.getLogger(DriverManager.class);

    public static WebDriver driver;

    private static final String BROWSER_TYPE_PROPERTY = "browser.type";

    private static final String BROWSER_HEADLESS_PROPERTY = "browser.headless";

    private static final String DRIVER_CACHE_PATH = "target/webdriver-cache";

    private DriverManager() { }

    private static String getBrowserType() {
        return PropertiesUtil.getProperty(BROWSER_TYPE_PROPERTY).toUpperCase();
    }

    private static boolean isHeadless() {
        return Boolean.parseBoolean(PropertiesUtil.getProperty(BROWSER_HEADLESS_PROPERTY));
    }

    public static WebDriver getDriver() {
        if (driver == null) {
            String browserType = getBrowserType();
            boolean headless = isHeadless();

            driver = switch (browserType.toUpperCase()) {
                case "CHROME" -> initChromeDriver(headless);
                case "FIREFOX" -> initFirefoxDriver(headless);
                default -> {
                    logger.error(String.format("Unsupported browser type: %s", browserType));
                    throw new IllegalArgumentException("Unsupported browser type: " + browserType);
                }
            };
        }
        return driver;
    }

    private static WebDriver initChromeDriver(boolean headless) {
        WebDriverManager.chromedriver().cachePath(DRIVER_CACHE_PATH).setup();
        ChromeOptions options = new ChromeOptions();
        if (headless) {
            options.addArguments("--headless");
        }
        logger.info("Starting Chrome browser");
        return new ChromeDriver(options);
    }

    private static WebDriver initFirefoxDriver(boolean headless) {
        WebDriverManager.firefoxdriver().cachePath(DRIVER_CACHE_PATH).setup();
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
