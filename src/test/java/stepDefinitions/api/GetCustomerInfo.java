package stepDefinitions.api;

import actions.ApiActions;
import exceptions.NoPreviousResponseException;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.PropertiesUtil;
import api.CustomerInfo;

import java.util.List;
import java.util.Map;

public class GetCustomerInfo {
    private static final Logger logger = LoggerFactory.getLogger(GetCustomerInfo.class);

    private static final String API_ENDPOINT = PropertiesUtil.getProperty("api.customer.endpoint");
    private Response lastResponse;
    private int lastStatusCode;

    @When("user sends a GET request")
    public void getCustomerInfo() {
        // Retrieve token from ApiActions class
        String token = ApiActions.getBearerToken();

        // Send GET request and save the response
        Response response = ApiActions.sendGetRequest(API_ENDPOINT);

        lastResponse = response;
        lastStatusCode = response.getStatusCode();

        logger.info("GET request executed");
    }

    @Then("the response status code should be {int}")
    public void verifyResponseStatusCode(int expectedStatusCode) {
        Assert.assertEquals("Response status code does not match expected", expectedStatusCode, lastStatusCode);
        logger.info(String.format("Response status code verified successfully: %s", lastStatusCode));
    }

    @And("the response body should contain the following customer data:")
    public void getResponseBody(DataTable dataTable) {
        if (lastResponse == null) {
            throw new NoPreviousResponseException("No previous response to verify");
        }

        String responseBody = lastResponse.getBody().asString();
        logger.info(String.format("Response body received: %s", responseBody));

        // Map response directly to CustomerInfo list
        List<CustomerInfo> customerInfos = lastResponse.jsonPath().getList("data", CustomerInfo.class);

        // Compare with expected data from DataTable
        List<Map<String, String>> expectedCustomers = dataTable.asMaps();
        boolean found;
        for (Map<String, String> expectedCustomer : expectedCustomers) {
            found = false;
            for (CustomerInfo customer : customerInfos) {
                if (compareCustomerData(customer, expectedCustomer)) {
                    found = true;
                    logger.info(String.format("Expected customer data found in response: %s", expectedCustomer));
                    break;
                }
            }
            if (!found) {
                throw new NoPreviousResponseException("Customer data not found in response: " + expectedCustomer);
            }
        }
    }

    private boolean compareCustomerData(CustomerInfo customer, Map<String, String> expectedCustomer) {
        // Compare fields from CustomerInfo object with expectedCustomer map
        String customerId = expectedCustomer.get("customerId");
        String isDeleted = expectedCustomer.get("isDeleted");
        String name = expectedCustomer.get("name"); // Use "name" key from DataTable
        String description = expectedCustomer.get("description");

        return customer.getCustomerId().equals(customerId)
                && customer.getIsDeleted().equals(isDeleted)
                && customer.getName().equals(name) // Use getName() for "name" field
                && customer.getDescription().equals(description);
    }
}
