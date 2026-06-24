package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BuzzPage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(BuzzPage.class);

    private static final int POST_CREATION_ATTEMPTS = 2;

    @FindBy(xpath = "//span[normalize-space()='Buzz']")
    private WebElement buzzMenuItem;

    @FindBy(xpath = "//textarea[@placeholder=\"What's on your mind?\"]")
    private WebElement whatsOnYourMindField;

    private static final By POST_BUTTON = By.xpath("//textarea[@placeholder=\"What's on your mind?\"]/following::button[normalize-space()='Post'][1]");

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
        UIActions.clickElement(POST_BUTTON);
        logger.info("Clicked on the post button");
    }

    public void createPost(String text) {
        for (int attempt = 1; attempt <= POST_CREATION_ATTEMPTS; attempt++) {
            logger.info(String.format("Attempt %s: Creating Buzz post with text: %s", attempt, text));
            populatePostField(text);
            clickPostButton();

            if (getNewPostText(text)) {
                logger.info(String.format("Buzz post with text '%s' was created successfully", text));
                return;
            }

            logger.warn(String.format("Attempt %s: Buzz post with text '%s' was not visible after submitting", attempt, text));
        }

        throw new AssertionError(String.format("Buzz post with text '%s' was not created after %s attempts",
                text, POST_CREATION_ATTEMPTS));
    }

    public boolean getNewPostText(String expectedText) {
        String xpath = "//p[normalize-space()=" + xpathLiteral(expectedText) + "]";
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

    private String xpathLiteral(String text) {
        if (!text.contains("'")) {
            return "'" + text + "'";
        }
        if (!text.contains("\"")) {
            return "\"" + text + "\"";
        }

        String[] parts = text.split("'");
        StringBuilder literal = new StringBuilder("concat(");
        for (int i = 0; i < parts.length; i++) {
            if (i > 0) {
                literal.append(", \"'\", ");
            }
            literal.append("'").append(parts[i]).append("'");
        }
        literal.append(")");
        return literal.toString();
    }
}
