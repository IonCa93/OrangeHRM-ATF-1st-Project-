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

    private final ScenarioContext scenarioContext = ScenarioContext.getInstance();

    @When("user makes a post in Buzz Newsfeed")
    public void userMakesPost() {
        buzzPage.clickBuzzMenuItem();
        String randomText = faker.lorem().sentence();
        buzzPage.populatePostField(randomText);
        buzzPage.clickPostButton();
        scenarioContext.saveContext(ContextKey.RANDOM_POST_TEXT, randomText);
    }

    @Then("a new post is displayed on the top of the page")
    public void newPostDisplayed() {
        String expectedText = scenarioContext.getContext(ContextKey.RANDOM_POST_TEXT, String.class);
        Assert.assertTrue("New Post is not displayed", buzzPage.getNewPostText(expectedText));
    }
}