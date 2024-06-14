package pages;

import enums.Credentials;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.PropertiesUtil;

import java.time.Duration;


public class LoginPage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(LoginPage.class); // Logger

    @FindBy(name = "username")
    private WebElement usernameField;

    @FindBy(name = "password")
    private WebElement passwordField;

    @FindBy(xpath = "//button[@type='submit']")
    private WebElement loginButton;

    @FindBy(xpath = "//*[@class='oxd-topbar-header-breadcrumb']")
    private WebElement dashboardLabel;

    @FindBy(css = ".oxd-alert.oxd-alert--error")
    private WebElement errorMessage;

    public LoginPage(WebDriver driver) {
        super(driver);
    } //intialize constructor of parent class

    public void logInWithCreds(Credentials credentials) {
        browserActions.inputText(usernameField, credentials.getUsername(), "Username");
        browserActions.inputText(passwordField, credentials.getPassword(), "Password");
        browserActions.clickElement(loginButton);
        logger.info("Logged in with ADMIN credentials");
    }

    public void logInCustomCreds(String username, String password) {
        browserActions.inputText(usernameField, username, "Username");
        browserActions.inputText(passwordField, password, "Password");
        browserActions.clickElement(loginButton);
        logger.info("Logged in with custom credentials");
    }



    public boolean isDefaultTabDisplayed() {
        logger.info("Default tab is displayed");
        return browserActions.isElementDisplayed(dashboardLabel);
    }

    public WebElement getErrorMessage() {
        try {
            // Retrieve the wait timeout from the configuration
            int timeoutSeconds = Integer.parseInt(PropertiesUtil.getProperty("timeout.seconds"));

            // Use the retrieved timeout for WebDriverWait
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            wait.until(ExpectedConditions.visibilityOf(errorMessage));

            // Log if the error message is "Invalid credentials"
            String errorMessageText = errorMessage.getText();
            if (errorMessageText.contains("Invalid credentials")) {
                logger.warn(String.format("Unsuccessful login attempt: %s", errorMessageText));
            }
            return errorMessage;
        } catch (Exception e) {
            logger.error(String.format("Failed to retrieve the error message element %s", e));
            throw e;
        }
    }

}
