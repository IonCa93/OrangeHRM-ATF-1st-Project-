package stepDefinitions;

import io.cucumber.java.en.Given;
import utils.TokenUtility;

public class CommonSteps {
    private static String bearerToken;

    @Given("Bearer token is generated")
    public void generateBearerToken() {
        bearerToken = TokenUtility.generateBearerToken();
    }

    public static String getBearerToken() {
        return bearerToken;
    }
}