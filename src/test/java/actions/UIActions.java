package actions;

import exceptions.FieldPopulationException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.PropertiesUtil;

import java.time.Duration;

public class UIActions {
    private final WebDriverWait wait;
    private static final Logger logger = LoggerFactory.getLogger(UIActions.class); // Logger

    public UIActions(WebDriver driver) {
        int timeoutSeconds = Integer.parseInt(PropertiesUtil.getProperty("timeout.seconds"));
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
    }

    public void clickElement(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element)).click();
        logger.info(String.format("Clicked on element: %s", element));
    }

    public void inputText(WebElement element, String text, String fieldName) {
        wait.until(ExpectedConditions.elementToBeClickable(element)).clear();
        element.sendKeys(text);
        logger.info(String.format("Entering text '%s' into element '%s' with field name '%s'", text, element, fieldName));
    }

    public void inputTextWithRetry(WebElement element, String text) {
        int attempts = 0;
        while (attempts < 3) { // Retry up to 3 times
            try {
                logger.info(String.format("Attempt %s: Waiting for element to be clickable", attempts + 1));
                WebElement field = wait.until(ExpectedConditions.elementToBeClickable(element));

                logger.info(String.format("Attempt %s: Clearing the field", attempts + 1));
                field.clear();

                // Wait until the "value" attribute of the element is empty to ensure it's cleared
                wait.until(ExpectedConditions.attributeToBe(field, "value", ""));

                // Wait until the field is enabled and interactable
                wait.until(ExpectedConditions.elementToBeClickable(field));

                logger.info(String.format("Attempt %s: Sending keys to the field", attempts + 1));
                field.sendKeys(text);

                // Wait until the text is present in the element's "value" attribute
                boolean textPresent = wait.until(ExpectedConditions.textToBePresentInElementValue(field, text));
                if (textPresent) {
                    logger.info(String.format("Attempt %s: Successfully populated the field with text", attempts + 1));
                    return; // Exit from while loop after successful input
                } else {
                    logger.warn(String.format("Attempt %s: Text was not present in the field after sending keys", attempts + 1));
                }
            } catch (Exception e) {
                logger.error(String.format("Attempt %s: Exception occurred while populating the field. Retrying...", attempts + 1), e);
                attempts++;
                if (attempts == 3) {
                    throw new FieldPopulationException(String.format("Failed to populate field after %s attempts", attempts), e);
                }
            }
        }
    }

    public boolean isElementDisplayed(WebElement element) {
        try {
            return wait.until(ExpectedConditions.visibilityOf(element)).isDisplayed();
        } catch (org.openqa.selenium.TimeoutException e) {
            return false;
        }
    }

    public static void navigateToLoginPage(WebDriver driver, String baseUrl) {
        driver.get(baseUrl);
        String currentUrl = driver.getCurrentUrl();
        logger.info(String.format("Navigated to the login page. Current URL: %s", currentUrl));
    }
}