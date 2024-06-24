package stepDefinitions.ui;

import io.cucumber.java.en.Given;
import utils.TokenUtility;
import actions.ApiActions;

public class CommonSteps {
    @Given("Bearer token is generated")
    public void generateBearerToken() {
        String token = TokenUtility.generateBearerToken();
        ApiActions.setBearerToken(token);
    }
}
