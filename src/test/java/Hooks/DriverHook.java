package Hooks;

import io.cucumber.java.Before;
import io.cucumber.java.After;
import utils.DriverManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DriverHook {
    private static final Logger logger = LoggerFactory.getLogger(DriverHook.class);
    @Before(value = "@ui")
    public void setUp() {
        logger.info("Initializing WebDriver for UI testing");
        try {
            DriverManager.getDriver();
            logger.info("WebDriver initialized successfully");
        } catch (Exception e) {
            logger.error("Error during WebDriver initialization", e);
        }
    }

    @After(value = "@ui", order = 1) // Order set to 1 to ensure WebDriver teardown happens before scenario context teardown
    public void tearDown() {
        logger.info("Starting WebDriver teardown");
        try {
            DriverManager.quitDriver();
            logger.info("WebDriver successfully torn down");
        } catch (Exception e) {
            logger.error("Error during WebDriver teardown", e);
        }
    }
}

