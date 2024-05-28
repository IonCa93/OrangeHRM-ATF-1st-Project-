package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.Duration;

public class BuzzPage extends BasePage {
    private static final Logger LOGGER = LoggerFactory.getLogger(BuzzPage.class); // Logger

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
        LOGGER.info("Clicked on Buzz menu item");
    }

    public void populateWhatsOnYourMindField(String text) {
        browserActions.inputTextWithRetry(whatsOnYourMindField, text);
        LOGGER.info("Populated 'What's on your mind' field with text: {}", text);
    }

    public void clickPostButton() {
        browserActions.clickElement(postButton);
        LOGGER.info("Clicked on the post button");
    }


    public boolean getNewPostText(String expectedText) {
        // Build XPath dynamically to locate the post with the specified text
        String xpath = "//p[normalize-space()='" + expectedText + "']";
        LOGGER.info("New Post is present on the page");
        try {
            // Wait for the presence of the post element with increased wait time
            WebElement postElement = new WebDriverWait(driver, Duration.ofSeconds(20))
                    .until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
            // Wait for the element to be visible
            wait.until(ExpectedConditions.visibilityOf(postElement));
            return true;
        } catch (org.openqa.selenium.TimeoutException e) {
            return false;
        }
    }
}
