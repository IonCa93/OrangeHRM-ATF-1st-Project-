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

    public void clickElement(By locator) {
        retryOnStaleElement(() -> wait.until(ExpectedConditions.elementToBeClickable(locator)).click());
        logger.info(String.format("Clicked on element located by: %s", locator));
    }

    public void inputText(WebElement element, String text, String fieldName) {
        wait.until(ExpectedConditions.elementToBeClickable(element)).clear();
        element.sendKeys(text);
        logger.info(String.format("Entering text '%s' into element '%s' with field name '%s'", text, element, fieldName));
    }

    public void inputTextWithRetry(WebElement element, String text) {
        int attempts = 0;
        Exception lastException = null;
        String lastObservedValue = null;

        while (attempts < 3) {
            try {
                logger.info(String.format("Attempt %s: Waiting for element to be clickable", attempts + 1));
                WebElement field = wait.until(ExpectedConditions.elementToBeClickable(element));

                logger.info(String.format("Attempt %s: Clearing the field", attempts + 1));
                field.clear();

                wait.until(driver -> isFieldEmpty(field));

                logger.info(String.format("Attempt %s: Sending keys to the field", attempts + 1));
                field.sendKeys(text);

                boolean textPresent = wait.until(driver -> isTextPresentInField(field, text));
                if (textPresent) {
                    logger.info(String.format("Attempt %s: Successfully populated the field with text", attempts + 1));
                    return;
                }

                lastObservedValue = getInputValue(field);
                logger.warn(String.format("Attempt %s: Text was not present in the field after sending keys. Current value: '%s'",
                        attempts + 1, lastObservedValue));
            } catch (StaleElementReferenceException e) {
                lastException = e;
                logger.warn(String.format("Attempt %s: StaleElementReferenceException encountered. Retrying...", attempts + 1), e);
            } catch (Exception e) {
                lastException = e;
                logger.warn(String.format("Attempt %s: Exception occurred while populating the field. Retrying...", attempts + 1), e);
            }
            attempts++;
        }

        String errorMessage = String.format("Failed to populate field with text '%s' after %s attempts. Last observed value: '%s'",
                text, attempts, lastObservedValue);
        logger.error(errorMessage, lastException);
        throw new IllegalStateException(errorMessage, lastException);
    }

    private boolean isFieldEmpty(WebElement element) {
        String value = getInputValue(element);
        return value == null || value.isEmpty();
    }

    private boolean isTextPresentInField(WebElement element, String expectedText) {
        return expectedText.equals(getInputValue(element));
    }

    private String getInputValue(WebElement element) {
        String value = element.getDomProperty("value");
        if (value == null) {
            value = element.getAttribute("value");
        }
        if (value == null || value.isEmpty()) {
            value = element.getText();
        }
        return value;
    }

    private void retryOnStaleElement(Runnable action) {
        int attempts = 0;
        StaleElementReferenceException lastException = null;

        while (attempts < 3) {
            try {
                action.run();
                return;
            } catch (StaleElementReferenceException e) {
                lastException = e;
                logger.warn("StaleElementReferenceException encountered. Retrying...", e);
                attempts++;
            }
        }

        throw new IllegalStateException("Element stayed stale after " + attempts + " attempts", lastException);
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
