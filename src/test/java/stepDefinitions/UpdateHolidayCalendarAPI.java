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
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.PropertiesUtil;
import utils.ScenarioContext;
import utils.ScenarioContextKeys;
import utils.TokenUtility;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;
import java.util.Random;

public class UpdateHolidayCalendarAPI {
    private final Logger logger = LoggerFactory.getLogger(UpdateHolidayCalendarAPI.class);
    private static final String HOLIDAY_API_ENDPOINT = PropertiesUtil.getProperty("api.holiday.endpoint");

    private static final CloseableHttpClient httpClient = HttpClients.createDefault();
    private static HttpResponse lastResponse;
    private String requestBodySent;

    @When("POST request is performed with following request body:")
    public void sendHolidayData(DataTable dataTable) throws Exception {
        // Convert DataTable to Map for request body
        Map<String, String> holidayData = dataTable.asMap(String.class, String.class);

        // Generate request body with dynamic values
        JSONObject requestBody = generateHolidayRequestBody(holidayData);

        // Perform POST request
        performPostRequest(HOLIDAY_API_ENDPOINT, requestBody.toString());

        // Log the request body
        logger.info("Request body sent: {}", requestBody.toString());

        // Save the request body to Scenario Context
        requestBodySent = requestBody.toString();
        ScenarioContext.getInstance().saveValueToScenarioContext(ScenarioContextKeys.ScenarioContextKey.REQUEST_BODY_SENT, requestBodySent);
    }

    private JSONObject generateHolidayRequestBody(Map<String, String> holidayData) {
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
        return requestBody;
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
    public void checkStatusCode(int expectedStatusCode) throws Exception {
        if (lastResponse == null) {
            throw new Exception("No previous response to verify status code");
        }
        int actualStatusCode = lastResponse.getStatusLine().getStatusCode();
        if (actualStatusCode != expectedStatusCode) {
            throw new Exception("Expected status code: " + expectedStatusCode + ", Actual status code: " + actualStatusCode);
        }
        logger.info("Response status code is {}", expectedStatusCode);
    }

    @And("the response body parameters match the request body")
    public void verifyResponseBodyMatchesRequest() throws Exception {
        if (lastResponse == null || lastResponse.getEntity() == null) {
            throw new Exception("Response body is null or empty");
        }

        try {
            // Parse the response body
            String responseBody = EntityUtils.toString(lastResponse.getEntity());
            JSONObject responseJson = new JSONObject(responseBody);

            // Retrieve the data object from the response body
            JSONObject responseData = responseJson.getJSONObject("data");

            // Retrieve the request body from Scenario Context
            String requestBodyString = ScenarioContext.getInstance().<String>getValueFromScenarioContext(ScenarioContextKeys.ScenarioContextKey.REQUEST_BODY_SENT);
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
        if (statusCode != 200) {
            logger.error("POST request failed with status code: {}", statusCode);
            throw new Exception("POST request failed with status code: " + statusCode);
        }
        logger.info("POST request performed successfully");
    }
}
