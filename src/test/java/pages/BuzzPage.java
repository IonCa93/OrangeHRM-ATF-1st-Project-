package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BuzzPage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(BuzzPage.class);

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
        UIActions.clickElement(buzzMenuItem);
        logger.info("Clicked on Buzz menu item");
    }

    public void populatePostField(String text) {
        UIActions.inputTextWithRetry(whatsOnYourMindField, text);
        logger.info(String.format("Populated 'What's on your mind' field with text: %s", text));
    }

    public void clickPostButton() {
        UIActions.clickElement(postButton);
        logger.info("Clicked on the post button");
    }

    public boolean getNewPostText(String expectedText) {
        String xpath = "//p[normalize-space()='" + expectedText + "']";
        By locator = By.xpath(xpath);
        logger.info(String.format("Checking if new post with text '%s' is present and visible on the page", expectedText));

        if (UIActions.waitForElementPresence(locator)) {
            WebElement postElement = driver.findElement(locator);
            if (UIActions.waitForElementVisibility(postElement)) {
                logger.info(String.format("New post with text '%s' is visible on the page", expectedText));
                return true;
            } else {
                logger.warn(String.format("New post with text '%s' is present but not visible on the page", expectedText));
            }
        } else {
            logger.warn(String.format("New post with text '%s' is not present on the page", expectedText));
        }
        return false;
    }
}
