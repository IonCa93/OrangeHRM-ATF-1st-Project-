package stepDefinitions.ui;

import enums.Credentials;
import org.openqa.selenium.WebElement;
import pages.LoginPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import config.DriverManager;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UILoginSteps {
    LoginPage loginPage = new LoginPage(DriverManager.getDriver());

    @Given("the OrangeHRM home page is opened")
    public void openOrangeHRMHomePage() {
    }

    @When("the user initiates the login process")
    public void initiateLoginProcess() {
        loginPage.logInWithCreds(Credentials.ADMIN);
    }

    @Then("the [Default] tab is displayed")
    public void checkDefaultTab() {
        assertTrue("Dashboard tab is not displayed", loginPage.isDefaultTabDisplayed());
    }

    @When("the user initiates the login process with invalid credentials {string} and {string}")
    public void loginWithInvalidCreds(String user, String pass) {
        loginPage.logInCustomCreds(user, pass);
    }


    @Then("the {string} error message is displayed")
    public void checkErrorMsg(String expectedErrorMessage) {
        WebElement errorMessage = loginPage.getErrorMessage();
        assertTrue("Error message is not displayed", errorMessage.isDisplayed());
        String actualErrorMessage = errorMessage.getText();
        assertEquals("Error message text does not match", actualErrorMessage, expectedErrorMessage);
    }

    // Custom GIVEN step as pre-req for Feature #2
    @Given("user is logged in OrangeHRM system")
    public void userLogsInHRM () {
        loginPage.logInWithCreds(Credentials.ADMIN);
    }
}