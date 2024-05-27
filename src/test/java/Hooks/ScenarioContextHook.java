package Hooks;

import io.cucumber.java.After;
import utils.ScenarioContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScenarioContextHook {
    private static final Logger logger = LoggerFactory.getLogger(ScenarioContextHook.class);

    @After(order = 0) // This will ensure this hook runs after the WebDriver teardown
    public void clearScenarioContext() {
        logger.info("Starting Scenario Context teardown");
        try {
            ScenarioContext.getInstance().clearContext();
            logger.info("Scenario Context successfully cleared");
        } catch (Exception e) {
            logger.error("Error during Scenario Context teardown", e);
        }
    }
}