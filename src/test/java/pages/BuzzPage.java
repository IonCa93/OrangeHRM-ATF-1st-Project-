package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.BrowserActions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.Duration;

public class BuzzPage {

    private final WebDriver driver;
    private final BrowserActions browserActions;
    private final WebDriverWait wait; // Include WebDriverWait object
    private static final Logger LOGGER = LoggerFactory.getLogger(BuzzPage.class); // Logger

    @FindBy(xpath = "//span[normalize-space()='Buzz']")
    private WebElement buzzMenuItem;

    @FindBy(xpath = "//textarea[@placeholder=\"What's on your mind?\"]")
    private WebElement whatsOnYourMindField;

    @FindBy(xpath = "//button[@type='submit']")
    private WebElement postButton;

    public BuzzPage(WebDriver driver) {
        this.driver = driver;
        this.browserActions = new BrowserActions(driver);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30)); // Initialize WebDriverWait
        PageFactory.initElements(driver, this);
    }

    public void clickBuzzMenuItem() {
        browserActions.clickElement(buzzMenuItem);
        LOGGER.info("Clicked on Buzz menu item");
    }

    public void populateWhatsOnYourMindField(String text) {//TODO BrowserAction 2nd input method with waiter
        int attempts = 0;
        while (attempts < 3) { // Retry up to 3 times
            try {
                WebElement field = wait.until(ExpectedConditions.visibilityOf(whatsOnYourMindField));
                field.clear();
                Thread.sleep(700); // Introduce a brief pause //TODO wait until isVisible/isEnabled/isReadyToInteract //Exclude thread sleep
                field.sendKeys(text);
                LOGGER.info("Populated 'What's on your mind' field");
                return; //exits from while cycle.
            } catch (Exception e) {
                System.out.println("Exception occurred while populating 'What's on your mind' field. Retrying...");
                e.printStackTrace(); // Add more detailed logging
            }
            attempts++;
        }
        throw new RuntimeException("Failed to populate 'What's on your mind' field after multiple attempts.");
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
