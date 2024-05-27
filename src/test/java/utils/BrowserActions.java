package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BrowserActions { //TODO create abstract BasePage class - common elements (as super for login and buzz)-driver, initelem
    private final WebDriverWait wait;

    public BrowserActions(WebDriver driver) {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    public void clickElement(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element)).click();
    }

    public void inputText(WebElement element, String text) {
        wait.until(ExpectedConditions.elementToBeClickable(element)).clear();
        element.sendKeys(text);
    }

    public boolean isElementDisplayed(WebElement element) {
        try {
            return wait.until(ExpectedConditions.visibilityOf(element)).isDisplayed();
        } catch (org.openqa.selenium.TimeoutException e) {
            return false;
        }
    }
}