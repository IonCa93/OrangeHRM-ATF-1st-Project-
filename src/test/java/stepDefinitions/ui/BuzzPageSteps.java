package stepDefinitions.ui;

import com.github.javafaker.Faker;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import pages.BuzzPage;
import config.DriverManager;
import context.ScenarioContext;
import enums.ContextKey;

public class BuzzPageSteps {

    private final BuzzPage buzzPage = new BuzzPage(DriverManager.getDriver());
    private final Faker faker = new Faker();
    private final ScenarioContext scenarioContext = ScenarioContext.getInstance(); // Initialize ScenarioContext

    @When("user makes a post in Buzz Newsfeed")
    public void userMakesPostInBuzzNewsfeed() {
        // Click on Buzz menu item
        buzzPage.clickBuzzMenuItem();
        // Generate random text
        String randomText = faker.lorem().sentence();
        // Populate post field with random text
        buzzPage.populatePostField(randomText);
        // Click on Post button
        buzzPage.clickPostButton();
        // Store the random text in scenario context for verification
        scenarioContext.saveContext(ContextKey.RANDOM_POST_TEXT, randomText);
    }

    @Then("a new post is displayed on the top of the page")
    public void newPostDisplayed() {//TODO remove comments on assessment
        // Retrieve the expected dynamic text ---
        String expectedText = scenarioContext.getContext(ContextKey.RANDOM_POST_TEXT);

        // Verify that the new post displayed on the page contains the expected dynamic text
        Assert.assertTrue("New Post is not displayed", buzzPage.getNewPostText(expectedText));
    }
}