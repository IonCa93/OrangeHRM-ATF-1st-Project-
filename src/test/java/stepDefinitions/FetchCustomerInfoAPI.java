package stepDefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.datatable.DataTable;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.PropertiesUtil;
import java.util.List;
import java.util.Map;

public class FetchCustomerInfoAPI {
    private static final Logger logger = LoggerFactory.getLogger(FetchCustomerInfoAPI.class);

    private static final String API_ENDPOINT = PropertiesUtil.getProperty("api.customer.endpoint");
    private HttpResponse lastResponse;

    @When("I send a GET request to retrieve customer information")
    public void fetchCustomerInfo() throws Exception {
        logger.info("Sending GET request to retrieve customer information...");
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet(API_ENDPOINT);

        // Retrieve token from CommonSteps class
        String token = CommonSteps.getBearerToken();

        // Set Bearer Token in Authorization header
        httpGet.setHeader("Authorization", "Bearer " + token);

        // Execute the request
        lastResponse = httpClient.execute(httpGet);
        logger.info("GET request executed");

        // Handle response
        int statusCode = lastResponse.getStatusLine().getStatusCode();
        if (statusCode != 200) {
            logger.error("GET request Failed with status code: {}", statusCode);
            throw new Exception("GET Request Failed with status code: " + statusCode);
        } else {
            logger.info("GET request successful with status code: {}", statusCode);
        }
    }

    @Then("The response status code should be {int}")
    public void verifyResponseStatusCode(int expectedStatusCode) throws Exception {
        if (lastResponse == null) {
            logger.error("No previous response to verify status code");
            throw new Exception("No previous response to verify status code");
        }

        // Verify response status code
        int actualStatusCode = lastResponse.getStatusLine().getStatusCode();
        if (actualStatusCode != expectedStatusCode) {
            logger.error("Expected status code: {}, Actual status code: {}", expectedStatusCode, actualStatusCode);
            throw new Exception("Expected status code: " + expectedStatusCode + ", Actual status code: " + actualStatusCode);
        } else {
            logger.info("Response status code verified successfully: {}", actualStatusCode);
        }
    }

    @And("The response body should contain the following customer data:")
    public void getResponseBody(DataTable dataTable) throws Exception {
        if (lastResponse == null) {
            throw new Exception("No previous response to verify");
        }

        HttpEntity entity = lastResponse.getEntity();
        if (entity == null) {
            throw new Exception("Response body is null");
        }

        String responseBody = EntityUtils.toString(entity);
        logger.info("Response body received: {}", responseBody);

        // Check if the response body is empty
        if (responseBody.isEmpty()) {
            throw new Exception("Response body is empty");
        }

        // Parse the response body as a JSON object
        JSONObject jsonObject = new JSONObject(responseBody);

        // Get the "data" array from the JSON object
        if (!jsonObject.has("data")) {
            throw new Exception("Response body does not contain any customer data");
        }

        JSONArray dataArray = jsonObject.getJSONArray("data");

        // Convert DataTable to a List of Maps for easier comparison
        List<Map<String, String>> expectedCustomers = dataTable.asMaps();

        // Iterate over each expected customer from DataTable
        for (Map<String, String> expectedCustomer : expectedCustomers) {
            boolean found = false;
            // Iterate over each customer object in the response
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject customer = dataArray.getJSONObject(i);
                // Check if the customer data matches the expected data
                if (compareCustomerData(customer, expectedCustomer)) {
                    found = true;
                    logger.info("Expected customer data found in response: {}", expectedCustomer);
                    break; // Stop searching if a match is found
                }
            }
            // If no match found for the current expected customer, throw an exception
            if (!found) {
                throw new Exception("Customer data not found in response: " + expectedCustomer);
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