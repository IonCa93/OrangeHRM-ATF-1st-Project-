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
    private static final Logger logger = LoggerFactory.getLogger(LoginPage.class);

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

    private final int TIMEOUT_SECONDS = Integer.parseInt(PropertiesUtil.getProperty("timeout.seconds"));

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void logInWithCreds(Credentials credentials) {
        UIActions.inputText(usernameField, credentials.getUsername(), "Username");
        UIActions.inputText(passwordField, credentials.getPassword(), "Password");
        UIActions.clickElement(loginButton);
        logger.info("Logged in with ADMIN credentials");
    }

    public void logInCustomCreds(String username, String password) {
        UIActions.inputText(usernameField, username, "Username");
        UIActions.inputText(passwordField, password, "Password");
        UIActions.clickElement(loginButton);
        logger.info("Logged in with custom credentials");
    }



    public boolean isDefaultTabDisplayed() {
        logger.info("Default tab is displayed");
        return UIActions.waitForElementVisibility(dashboardLabel);
    }

    public WebElement getErrorMessage() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_SECONDS));
            wait.until(ExpectedConditions.visibilityOf(errorMessage));

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
