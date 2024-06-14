package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.PropertiesUtil;

import java.time.Duration;

public class BuzzPage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(BuzzPage.class); // Logger

    @FindBy(xpath = "//span[normalize-space()='Buzz']")
    private WebElement buzzMenuItem;

    @FindBy(xpath = "//textarea[@placeholder=\"What's on your mind?\"]")
    private WebElement whatsOnYourMindField;

    @FindBy(xpath = "//button[@type='submit']")
    private WebElement postButton;

    public BuzzPage(WebDriver driver) {
        super(driver);
    }

    public void clickBuzzMenuItem() {
        browserActions.clickElement(buzzMenuItem);
        logger.info("Clicked on Buzz menu item");
    }

    public void populatePostField(String text) {
        browserActions.inputTextWithRetry(whatsOnYourMindField, text);
        logger.info(String.format("Populated 'What's on your mind' field with text: %s", text));
    }

    public void clickPostButton() {
        browserActions.clickElement(postButton);
        logger.info("Clicked on the post button");
    }

    public boolean getNewPostText(String expectedText) {
        int timeoutSeconds = Integer.parseInt(PropertiesUtil.getProperty("timeout.seconds"));
        String xpath = "//p[normalize-space()='" + expectedText + "']";
        logger.info(String.format("Checking if new post with text '%s' is present on the page", expectedText));

        try {
            // Wait for the presence of the post element with the configured wait time
            WebElement postElement = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
                    .until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));

            // Wait for the element to be visible
            new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
                    .until(ExpectedConditions.visibilityOf(postElement));

            return true;
        } catch (org.openqa.selenium.TimeoutException e) {
            logger.warn(String.format("Timeout waiting for new post with text '%s' to be visible", expectedText));
            return false;
        }
    }

}
