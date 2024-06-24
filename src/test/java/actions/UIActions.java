package actions;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
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

    private static final Logger logger = LoggerFactory.getLogger(UIActions.class);

    private static final int TIMEOUT_SECONDS = Integer.parseInt(PropertiesUtil.getProperty("timeout.seconds"));

    public UIActions(WebDriver driver) {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_SECONDS));
    }

    public void clickElement(WebElement element) {
        retryOnStaleElement(() -> wait.until(ExpectedConditions.elementToBeClickable(element)).click());
        logger.info(String.format("Clicked on element: %s", element));
    }

    public void inputText(WebElement element, String text, String fieldName) {
        wait.until(ExpectedConditions.elementToBeClickable(element)).clear();
        element.sendKeys(text);
        logger.info(String.format("Entering text '%s' into element '%s' with field name '%s'", text, element, fieldName));
    }

    public void inputTextWithRetry(WebElement element, String text) {
        int attempts = 0;
        while (attempts < 3) {
            try {
                logger.info(String.format("Attempt %s: Waiting for element to be clickable", attempts + 1));
                WebElement field = wait.until(ExpectedConditions.elementToBeClickable(element));

                logger.info(String.format("Attempt %s: Clearing the field", attempts + 1));
                field.clear();

                wait.until(ExpectedConditions.attributeToBe(field, "value", ""));

                logger.info(String.format("Attempt %s: Sending keys to the field", attempts + 1));
                field.sendKeys(text);

                boolean textPresent = wait.until(ExpectedConditions.textToBePresentInElementValue(field, text));
                if (textPresent) {
                    logger.info(String.format("Attempt %s: Successfully populated the field with text", attempts + 1));
                    return;
                } else {
                    logger.warn(String.format("Attempt %s: Text was not present in the field after sending keys", attempts + 1));
                }
            } catch (StaleElementReferenceException e) {
                logger.warn(String.format("Attempt %s: StaleElementReferenceException encountered. Retrying...", attempts + 1), e);
            } catch (Exception e) {
                logger.error(String.format("Attempt %s: Exception occurred while populating the field. Retrying...", attempts + 1), e);
            }
            attempts++;
        }
    }

    private void retryOnStaleElement(Runnable action) {
        int attempts = 0;
        while (attempts < 3) {
            try {
                action.run();
                return;
            } catch (StaleElementReferenceException e) {
                logger.warn("StaleElementReferenceException encountered. Retrying...", e);
                attempts++;
            }
        }
    }

    public boolean waitForElementPresence(By locator) {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            logger.info(String.format("Element located successfully: %s", locator));
            return true;
        } catch (org.openqa.selenium.TimeoutException e) {
            logger.warn(String.format("Timeout waiting for presence of element: %s", locator));
            return false;
        }
    }

    public boolean waitForElementVisibility(WebElement element) {
        boolean isElementVisible = false;
        int attempts = 0;
        while (attempts < 3) {
            try {
                wait.until(ExpectedConditions.visibilityOf(element));
                isElementVisible = true;
                break;
            } catch (org.openqa.selenium.TimeoutException e) {
                logger.warn(String.format("Attempt %d: Timeout waiting for element visibility: %s", attempts + 1, element));
            }
            attempts++;
        }
        if (!isElementVisible) {
            logger.warn(String.format("Element %s is not visible after %d attempts", element, attempts));
        }
        return isElementVisible;
    }

    public static void navigateToLoginPage(WebDriver driver, String baseUrl) {
        driver.get(baseUrl);
        String currentUrl = driver.getCurrentUrl();
        logger.info(String.format("Navigated to the login page. Current URL: %s", currentUrl));
    }
}