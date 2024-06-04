package stepDefinitions;

import exceptions.NoPreviousResponseException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.datatable.DataTable;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.PropertiesUtil;
import java.util.List;
import java.util.Map;

public class FetchCustomerInfoAPI {
    private static final Logger LOGGER = LoggerFactory.getLogger(FetchCustomerInfoAPI.class);

    private static final String API_ENDPOINT = PropertiesUtil.getProperty("api.customer.endpoint");
    private Response lastResponse;

    @When("user sends a GET request to retrieve customer information")
    public void fetchCustomerInfo() {
        LOGGER.info("Sending GET request to retrieve customer information...");

        // Retrieve token from CommonSteps class
        String token = CommonSteps.getBearerToken();

        // Send GET request
        lastResponse = RestAssured
                .given()
                .header("Authorization", "Bearer " + token)
                .get(API_ENDPOINT);

        LOGGER.info("GET request executed");
    }

    @Then("the response status code should be {int}")
    public void verifyResponseStatusCode(int expectedStatusCode) {
        if (lastResponse == null) {
            LOGGER.error("No previous response to verify status code");
            throw new NoPreviousResponseException("No previous response to verify status code");
        }

        // Verify response status code using assertion
        int actualStatusCode = lastResponse.getStatusCode();
        Assert.assertEquals("Response status code does not match expected", expectedStatusCode, actualStatusCode);
        LOGGER.info("Response status code verified successfully: {}", actualStatusCode);
    }


    @And("the response body should contain the following customer data:")
    public void getResponseBody(DataTable dataTable) {
        if (lastResponse == null) {
            throw new NoPreviousResponseException("No previous response to verify");
        }

        String responseBody = lastResponse.getBody().asString();
        LOGGER.info("Response body received: {}", responseBody);

        // Parse the response body as a JSON object
        JSONObject jsonObject = new JSONObject(responseBody);

        // Get the "data" array from the JSON object
        if (!jsonObject.has("data")) {
            throw new NoPreviousResponseException("Response body does not contain any customer data");
        }

        JSONArray dataArray = jsonObject.getJSONArray("data");

        // Convert DataTable to a List of Maps for easier comparison
        List<Map<String, String>> expectedCustomers = dataTable.asMaps();

        // Iterate over each expected customer from DataTable
        for (Map<String, String> expectedCustomer : expectedCustomers) {
            boolean found = false;
            // Iterate over each customer object in the response
            for (Object obj : dataArray) {
                JSONObject customer = (JSONObject) obj;
                // Check if the customer data matches the expected data
                if (compareCustomerData(customer, expectedCustomer)) {
                    found = true;
                    LOGGER.info("Expected customer data found in response: {}", expectedCustomer);
                    break; // Stop searching if a match is found
                }
            }
            // If no match found for the current expected customer, throw an exception
            if (!found) {
                throw new NoPreviousResponseException("Customer data not found in response: " + expectedCustomer);
            }
        }
    }

    // Helper method to compare customer data from response with expected data
    private boolean compareCustomerData(JSONObject customer, Map<String, String> expectedCustomer) {
        // Iterate over expected customer data
        for (Map.Entry<String, String> entry : expectedCustomer.entrySet()) {
            String key = entry.getKey();
            String expectedValue = entry.getValue();
            // Check if the key exists in the response and its value matches the expected value
            if (!customer.has(key) || !customer.getString(key).equals(expectedValue)) {
                return false; // Return false if any field does not match
            }
        }
        return true; // All fields match, return true
    }
}