package stepDefinitions;

import exceptions.NoPreviousResponseException;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.PropertiesUtil;
import context.ScenarioContext;
import enums.ContextKey;
import utils.TokenUtility;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;
import java.util.Random;

public class PostHolidayDay {
    private final Logger logger = LoggerFactory.getLogger(PostHolidayDay.class);
    private static final String HOLIDAY_API_ENDPOINT = PropertiesUtil.getProperty("api.holiday.endpoint");

    private static Response lastResponse;
    private String requestBodySent;

    @When("POST request is performed with following request body:")
    public void sendHolidayData(DataTable dataTable) {
        // Convert DataTable to Map for request body
        Map<String, String> holidayData = dataTable.asMap(String.class, String.class);

        // Generate request body with dynamic values
        String requestBody = generateHolidayRequestBody(holidayData);

        // Perform POST request
        lastResponse = RestAssured
                .given()
                .header("Authorization", "Bearer " + TokenUtility.generateBearerToken())
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post(HOLIDAY_API_ENDPOINT);

        // Log the request body
        logger.info("Request body sent: {}", requestBody);

        // Save the request body to Scenario Context
        requestBodySent = requestBody;
        ScenarioContext.getInstance().saveContext(ContextKey.REQUEST_BODY_SENT, requestBodySent);
    }

    private String generateHolidayRequestBody(Map<String, String> holidayData) {
        JSONObject requestBody = new JSONObject();

        // Handle the location field separately as it is an array
        for (Map.Entry<String, String> entry : holidayData.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if ("location".equals(key)) {
                // Convert value to array of integers
                JSONArray locationArray = new JSONArray();
                for (String loc : value.replace("[", "").replace("]", "").split(",")) {
                    locationArray.put(Integer.parseInt(loc.trim().replaceAll("\"", "")));
                }
                requestBody.put(key, locationArray);
            } else if ("adjustLeave".equals(key)) {
                // Convert adjustLeave to boolean
                requestBody.put(key, Boolean.parseBoolean(value));
            } else if ("length".equals(key) || "operational_country_id".equals(key)) {
                // Convert length and operational_country_id to integers
                requestBody.put(key, Integer.parseInt(value));
            } else if ("date".equals(key)) {
                // Generate random date in year 2024
                value = getRandomDateInYear(2024);
                requestBody.put(key, value);
            } else if ("description".equals(key)) {
                // Generate random description
                value = generateRandomDescription();
                requestBody.put(key, value);
            } else {
                requestBody.put(key, value);
            }
        }
        return requestBody.toString(); // Convert JSONObject to String
    }

    private String getRandomDateInYear(int year) {
        // Generate a random date within the specified year
        Random random = new Random();
        int minDay = (int) LocalDate.of(year, 1, 1).toEpochDay();
        int maxDay = (int) LocalDate.of(year, 12, 31).toEpochDay();
        long randomDay = minDay + random.nextInt(maxDay - minDay);
        LocalDate randomDate = LocalDate.ofEpochDay(randomDay);
        return randomDate.toString();
    }

    private String generateRandomDescription() {
        // List of words to use in generating description
        String[] words = {"holiday", "vacation", "celebration", "event", "day", "trip", "outing"};

        // Generate a random description using words from the list
        StringBuilder description = new StringBuilder();
        Random random = new Random();
        int numWords = random.nextInt(3) + 1; // Generate 1-3 words
        for (int i = 0; i < numWords; i++) {
            int index = random.nextInt(words.length);
            description.append(words[index]);
            if (i < numWords - 1) {
                description.append(" ");
            }
        }
        return description.toString();
    }

    @Then("the status code received in the response is {int}")
    public void checkStatusCode(int expectedStatusCode) {
        if (lastResponse == null) {
            throw new NoPreviousResponseException("No previous response to verify status code");
        }

        int actualStatusCode = lastResponse.getStatusCode();
        Assert.assertEquals("Response status code does not match expected", expectedStatusCode, actualStatusCode);
        logger.info("Response status code is {}", actualStatusCode);
    }

    @And("the response body parameters match the request body")
    public void verifyResponseBodyMatchesRequest() throws Exception {
        if (lastResponse == null || lastResponse.getBody() == null) {
            throw new Exception("Response body is null or empty");
        }
        try {
            // Parse the response body
            String responseBody = lastResponse.getBody().asString();
            JSONObject responseJson = new JSONObject(responseBody);

            // Retrieve the data object from the response body
            JSONObject responseData = responseJson.getJSONObject("data");

            // Retrieve the request body from Scenario Context
            String requestBodyString = ScenarioContext.getInstance().<String>getContext(ContextKey.REQUEST_BODY_SENT);
            JSONObject requestBody = new JSONObject(requestBodyString);

            // Log the request body
            logger.info("Request body for comparison: {}", requestBody.toString());

            // Compare each key-value pair in the request body with the response body
            for (String key : requestBody.keySet()) {
                Object requestValue = requestBody.get(key);
                Object responseValue = responseData.opt(key);

                if (responseValue == null) {
                    // Skip logging for location and adjustLeave
                    if (!("location".equals(key) || "adjustLeave".equals(key))) {
                        logger.error("Mismatch found - Key: {}, Request Value: {}, Response Value: {}", key, requestValue, responseValue);
                        throw new Exception("Response body parameters do not match the request body");
                    }
                } else if (!compareValues(requestValue, responseValue)) {
                    logger.error("Mismatch found - Key: {}, Request Value: {}, Response Value: {}", key, requestValue, responseValue);
                    throw new Exception("Response body parameters do not match the request body");
                }
            }

            // Log the parameters matching between request and response
            logger.info("Response body parameters match the request body:");
            logger.info("date: {} (Request), {} (Response)", requestBody.opt("date"), responseData.optString("date"));
            logger.info("operational_country_id: {} (Request), {} (Response)", requestBody.opt("operational_country_id"), responseData.optString("operational_country_id"));
            logger.info("length: {} (Request), {} (Response)", requestBody.opt("length"), responseData.optString("length"));
            logger.info("description: {} (Request), {} (Response)", requestBody.opt("description"), responseData.optString("description"));
            logger.info("location: {} (Request), mapped with location_id: {} (Response)", requestBody.opt("location"), getLocationIds(responseData.optJSONArray("HolidayLocation")));
            logger.info("adjustLeave: {} (Request), mapped with editable: {} (Response)", requestBody.opt("adjustLeave"), responseData.optBoolean("editable"));

        } catch (IOException e) {
            throw new Exception("Failed to read response body: " + e.getMessage());
        }
    }

    // Method to extract location_ids from JSONArray of HolidayLocation objects
    private String getLocationIds(JSONArray holidayLocations) {
        if (holidayLocations == null || holidayLocations.length() == 0) {
            return "N/A";
        }
        StringBuilder locationIds = new StringBuilder();
        for (int i = 0; i < holidayLocations.length(); i++) {
            JSONObject holidayLocation = holidayLocations.optJSONObject(i);
            if (holidayLocation != null) {
                int locationId = holidayLocation.optInt("location_id");
                if (i > 0) {
                    locationIds.append(", ");
                }
                locationIds.append(locationId);
            }
        }
        return locationIds.toString();
    }

    private boolean compareValues(Object requestValue, Object responseValue) {
        if (requestValue instanceof Integer) {
            return requestValue.equals(Integer.parseInt(responseValue.toString()));
        } else if (requestValue instanceof Boolean) {
            return requestValue.equals(Boolean.parseBoolean(responseValue.toString()));
        } else if (requestValue instanceof JSONArray) {
            JSONArray requestArray = (JSONArray) requestValue;
            JSONArray responseArray = (JSONArray) responseValue;
            if (requestArray.length() != responseArray.length()) {
                return false;
            }
            for (int i = 0; i < requestArray.length(); i++) {
                if (!compareValues(requestArray.get(i), responseArray.get(i))) {
                    return false;
                }
            }
            return true;
        } else {
            return requestValue.equals(responseValue);
        }
    }
}