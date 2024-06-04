package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class LoginPage extends BasePage {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginPage.class); // Logger

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

    public void LogInWithCredentials(String username, String password) {
        browserActions.inputText(usernameField, username, "Username");
        browserActions.inputText(passwordField, password, "Password");
        browserActions.clickElement(loginButton);
        LOGGER.info("Logged in with credentials");
    }

    public boolean isDefaultTabDisplayed() {
        LOGGER.info("Default tab is displayed");
        return browserActions.isElementDisplayed(dashboardLabel);
    }

    public WebElement getErrorMessage() {
        // Wait for the error message element to be visible before returning it
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30)); // Adjust the timeout as needed
        wait.until(ExpectedConditions.visibilityOf(errorMessage));

        // Log if the error message is "Invalid credentials"
        String errorMessageText = errorMessage.getText();
        if(errorMessageText.contains("Invalid credentials")) {
            LOGGER.warn("Unsuccessful login attempt: {}", errorMessageText);
        }
        return errorMessage;
    }
}
