package stepDefinitions;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class CreateEmployeeAPI {
    private final Logger logger = LoggerFactory.getLogger(CreateEmployeeAPI.class);
    private static final String EMPLOYEE_API_ENDPOINT = PropertiesUtil.getProperty("api.employee.endpoint");
    private String requestBodySent;
    private static HttpResponse lastResponse;
    private static CloseableHttpClient httpClient = HttpClients.createDefault();

    @When("A POST request is performed with following request body:")
    public void sendEmployeeData(DataTable dataTable) throws Exception {
        // Convert DataTable to Map for request body
        Map<String, String> employeeData = dataTable.asMap(String.class, String.class);

        // Generate request body with dynamic values
        JSONObject requestBody = generateEmployeeRequestBody(employeeData);

        // Perform POST request
        performPostRequest(EMPLOYEE_API_ENDPOINT, requestBody.toString());

        // Log the request body
        logger.info("Request body sent: {}", requestBody.toString());

        // Save the request body to Scenario Context
        requestBodySent = requestBody.toString();
        ScenarioContext.getInstance().saveValueToScenarioContext(ScenarioContextKeys.ScenarioContextKey.REQUEST_BODY_SENT, requestBodySent);
    }

    private JSONObject generateEmployeeRequestBody(Map<String, String> employeeData) {
        JSONObject requestBody = new JSONObject();

        // Set keys and values from DataTable
        for (Map.Entry<String, String> entry : employeeData.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            // Check if the value is a dynamic placeholder
            if (value.startsWith("<dynamic_") && value.endsWith(">")) {
                // Generate dynamic value based on placeholder
                switch (value) {
                    case "<dynamic_employeeId>":
                        value = String.valueOf(getRandomNumber(1000, 9999));
                        break;
                    case "<dynamic_locationId>":
                        value = String.valueOf(getRandomNumber(1, 5));
                        break;
                    case "<dynamic_joinedDate>":
                        value = getRandomDateInYear(2024);
                        break;
                    default:
                        // Handle unknown dynamic placeholder
                        throw new IllegalArgumentException("Unknown dynamic placeholder: " + value);
                }
            }
            // Add key-value pair to request body
            requestBody.put(key, value);
        }
        return requestBody;
    }

    @Then("The response status code is {int}")
    public void verifyStatusCode(int expectedStatusCode) throws Exception {
        if (lastResponse == null) {
            throw new Exception("No previous response to verify status code");
        }
        int actualStatusCode = lastResponse.getStatusLine().getStatusCode();
        if (actualStatusCode != expectedStatusCode) {
            throw new Exception("Expected status code: " + expectedStatusCode + ", Actual status code: " + actualStatusCode);
        }
        logger.info("Response status code is {}", expectedStatusCode);
    }

    private Map<String, String> extractValuesFromResponseBody(JSONObject responseData, String requestBody) {
        Map<String, String> extractedValues = new HashMap<>();
        JSONObject requestBodyObject = new JSONObject(requestBody);

        // Iterate over the keys in the request body to extract corresponding values from the response body
        for (String parameter : requestBodyObject.keySet()) {
            // Adjust parameter name to match response body
            String responseParameter = parameter.replace("Date", "_date"); // Adjust for response body format
            if (responseData.has(responseParameter)) {
                extractedValues.put(parameter, responseData.getString(responseParameter));
            }
        }
        return extractedValues;
    }

    @And("The response body parameters should match the request body")
    public void verifyResponseBodyParametersMatchRequest() throws Exception {
        // Retrieve the saved request body from Scenario Context
        String requestBody = ScenarioContext.getInstance().getValueFromScenarioContext(ScenarioContextKeys.ScenarioContextKey.REQUEST_BODY_SENT);

        // Check if the response body is null or empty
        if (lastResponse == null || lastResponse.getEntity() == null) {
            throw new Exception("Response body is null or empty");
        }

        try {
            // Parse the response body
            String responseBody = EntityUtils.toString(lastResponse.getEntity());
            JSONObject responseData = new JSONObject(responseBody).getJSONObject("data");

            // Consume the response entity to prevent resource leaks
            EntityUtils.consume(lastResponse.getEntity());

            // Extract values from the response body
            Map<String, String> extractedValues = extractValuesFromResponseBody(responseData, requestBody);

            // Extract values from the request body
            JSONObject requestBodyObject = new JSONObject(requestBody);

            // Initialize a map to store matched parameters and their values
            Map<String, String> matchedValues = new HashMap<>();

            // Initialize a flag to track if all fields match
            boolean allFieldsMatch = true;

            // Iterate over the keys in the request body to extract corresponding values from the response body
            for (String parameter : requestBodyObject.keySet()) {
                if (extractedValues.containsKey(parameter)) {
                    // Compare the values from the request and response bodies
                    String requestValue = requestBodyObject.getString(parameter);
                    String responseValue = extractedValues.get(parameter);
                    if (!requestValue.equals(responseValue)) {
                        allFieldsMatch = false;
                        logger.error("Response body field value mismatch for: {}. Expected: '{}', Actual: '{}'", parameter, requestValue, responseValue);
                    } else {
                        // If the values match, add them to the matchedValues map
                        matchedValues.put(parameter, responseValue);
                    }
                } else {
                    allFieldsMatch = false;
                    logger.error("Parameter '{}' in request body not found in response body", parameter);
                }
            }

            // Log the matched parameters and their values
            if (allFieldsMatch) {
                logger.info("All parameters in the response body match the parameters in the request body:");
                for (String parameter : matchedValues.keySet()) {
                    logger.info("{}: {}", parameter, matchedValues.get(parameter));
                }
            }

            // If any field does not match, throw an exception
            if (!allFieldsMatch) {
                throw new Exception("Response body parameters do not match the parameters in the request body");
            }
        } catch (IOException e) {
            throw new Exception("Failed to read response body: " + e.getMessage());
        }
    }

    private void performPostRequest(String url, String requestBody) throws Exception {
        HttpPost httpPost = new HttpPost(url);
        // Retrieve token from TokenUtility
        String token = TokenUtility.generateBearerToken();

        // Set headers
        httpPost.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        httpPost.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

        // Set request body
        httpPost.setEntity(new StringEntity(requestBody));

        // Execute POST request
        lastResponse = httpClient.execute(httpPost);
        int statusCode = lastResponse.getStatusLine().getStatusCode();
        if (statusCode != 201) {
            logger.error("POST request failed with status code: {}", statusCode);
            throw new Exception("POST request failed with status code: " + statusCode);
        }
        logger.info("POST request performed successfully");
    }

    private int getRandomNumber(int min, int max) {
        return new Random().nextInt(max - min + 1) + min;
    }

    private String getRandomDateInYear(int year) {
        long startTimestamp = new Date(year - 1900, 0, 1).getTime();
        long endTimestamp = new Date(year - 1900, 11, 31).getTime();
        long randomTimestamp = startTimestamp + (long) (Math.random() * (endTimestamp - startTimestamp));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date(randomTimestamp));
    }
}
