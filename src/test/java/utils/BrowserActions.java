package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class BrowserActions {
    private final WebDriverWait wait;
    private static final Logger LOGGER = LoggerFactory.getLogger(BrowserActions.class); // Logger

    public BrowserActions(WebDriver driver) {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    public void clickElement(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element)).click();
    }

    public void inputText(WebElement element, String text, String fieldName) {
        wait.until(ExpectedConditions.elementToBeClickable(element)).clear();
        element.sendKeys(text);
        LOGGER.info("Entering text '{}' into element '{}' with field name '{}'", text, element, fieldName);
    }

    public void inputTextWithRetry(WebElement element, String text) {
        int attempts = 0;
        while (attempts < 3) { // Retry up to 3 times
            try {
                LOGGER.info("Attempt {}: Waiting for element to be clickable", attempts + 1);
                WebElement field = wait.until(ExpectedConditions.elementToBeClickable(element));

                LOGGER.info("Attempt {}: Clearing the field", attempts + 1);
                field.clear();

                // Wait until the "value" attribute of the element is empty to ensure it's cleared
                wait.until(ExpectedConditions.attributeToBe(field, "value", ""));

                // Wait until the field is enabled and interactable
                wait.until(ExpectedConditions.elementToBeClickable(field));

                LOGGER.info("Attempt {}: Sending keys to the field", attempts + 1);
                field.sendKeys(text);

                // Wait until the text is present in the element's "value" attribute
                boolean textPresent = wait.until(ExpectedConditions.textToBePresentInElementValue(field, text));
                if (textPresent) {
                    LOGGER.info("Attempt {}: Successfully populated the field with text", attempts + 1);
                    return; // Exit from while loop after successful input
                } else {
                    LOGGER.warn("Attempt {}: Text was not present in the field after sending keys", attempts + 1);
                }
            } catch (Exception e) {
                LOGGER.error("Attempt {}: Exception occurred while populating the field. Retrying...", attempts + 1, e);
            }
            attempts++;
        }
        throw new RuntimeException("Failed to populate field after multiple attempts.");
    }

    public boolean isElementDisplayed(WebElement element) {
        try {
            return wait.until(ExpectedConditions.visibilityOf(element)).isDisplayed();
        } catch (org.openqa.selenium.TimeoutException e) {
            return false;
        }
    }
}