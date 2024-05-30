package stepDefinitions;

import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pages.LoginPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import utils.DriverManager;
import utils.PropertiesUtil;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UILoginSteps {
    LoginPage loginPage = new LoginPage(DriverManager.getDriver());
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginPage.class); // Logger

    @Given("the OrangeHRM home page is opened")
    public void openOrangeHRMHomePage() {
    }

    @When("the user initiates the login process")
    public void initiateLoginProcess() {
        String username = PropertiesUtil.getProperty("username");
        String password = PropertiesUtil.getProperty("password");
        loginPage.LogInWithCredentials(username, password);
    }

    @Then("the [Default] tab is displayed")
    public void checkDefaultTabIsDisplayed() {
        assertTrue("Dashboard tab is not displayed", loginPage.isDefaultTabDisplayed());
    }

    @When("the user initiates the login process with invalid credentials {string} and {string}")
    public void initiateLoginProcessWithInvalidCredentials(String user, String pass) {
        loginPage.LogInWithCredentials(user, pass);
    }


    @Then("the {string} error message is displayed")
    public void checkErrorMessageIsDisplayed(String expectedErrorMessage) {
        WebElement errorMessage = loginPage.getErrorMessage();
        assertTrue("Error message is not displayed", errorMessage.isDisplayed());
        String actualErrorMessage = errorMessage.getText();
        assertEquals("Error message text does not match", actualErrorMessage, expectedErrorMessage);
    }


    // Custom GIVEN step as pre-req for Feature #2
    @Given("user is logged in OrangeHRM system")
    public void userLogsInToOrangeHRMSystem () {
        String username = PropertiesUtil.getProperty("username");
        String password = PropertiesUtil.getProperty("password");
        loginPage.LogInWithCredentials(username, password);
    }
}