package stepDefinitions;

import com.github.javafaker.Faker;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import pages.BuzzPage;
import utils.DriverManager;
import utils.ScenarioContext;
import utils.ScenarioContextKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BuzzPageSteps {

    private static final Logger logger = LoggerFactory.getLogger(BuzzPageSteps.class);
    private final BuzzPage buzzPage = new BuzzPage(DriverManager.getDriver());
    private final Faker faker = new Faker();
    private final ScenarioContext scenarioContext = ScenarioContext.getInstance(); // Initialize ScenarioContext

    @When("user clicks on the Buzz menu-item from the left pane")
    public void buzz_menu_item_from_left_pane_is_clicked() {
        buzzPage.clickBuzzMenuItem();
    }

    @And("\"What’s on your mind\" field is populated with random text")
    public void whatSOnYourMindFieldIsPopulatedWithRandomText() {
        String randomText = faker.lorem().sentence(); // Generate random text using Java Faker
        buzzPage.populateWhatsOnYourMindField(randomText); // Populate the field with random text
        // Log the random text
        logger.info("Random post text added: {}", randomText);
        // Store the random text in the scenario context
        scenarioContext.saveValueToScenarioContext(ScenarioContextKeys.ScenarioContextKey.RANDOM_POST_TEXT, randomText);
    }

    @And("Post button is clicked")
    public void post_button_is_clicked() {
        buzzPage.clickPostButton();
    }

    @Then("a new post is displayed on the top of the page")
    public void new_post_is_displayed_on_the_top_of_the_page() {
        // Retrieve the expected dynamic text
        String expectedText = scenarioContext.getValueFromScenarioContext(ScenarioContextKeys.ScenarioContextKey.RANDOM_POST_TEXT);

        // Verify that the new post displayed on the page contains the expected dynamic text
        Assert.assertTrue("New Post is not displayed", buzzPage.getNewPostText(expectedText));
    }
}

