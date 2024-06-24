package stepDefinitions.api;

import actions.ApiActions;
import api.HolidayRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import context.ScenarioContext;
import enums.ContextKey;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.PropertiesUtil;
import java.util.*;

public class PostHolidayDay {
    private final Logger logger = LoggerFactory.getLogger(PostHolidayDay.class);
    private static final String HOLIDAY_API_ENDPOINT = PropertiesUtil.getProperty("api.holiday.endpoint");

    private Response lastResponse;
    private String requestBodySent;

    @When("POST request is performed with following request body:")
    public void sendHolidayData(DataTable dataTable) throws Exception {
        Map<String, String> holidayData = new HashMap<>(dataTable.asMap(String.class, String.class));
        ApiActions.replaceDynamicFields(holidayData);
        HolidayRequest holidayRequest = ApiActions.generateHolidayRequest(holidayData);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBodyJson = objectMapper.writeValueAsString(holidayRequest);
        logger.info(String.format("Request body JSON: %s", requestBodyJson));

        lastResponse = ApiActions.sendPostRequest(HOLIDAY_API_ENDPOINT, requestBodyJson);

        requestBodySent = requestBodyJson;
        ScenarioContext.getInstance().saveContext(ContextKey.REQUEST_BODY_SENT, requestBodySent);
    }

    @Then("the status code received in the response is {int}")
    public void checkStatusCode(int expectedStatusCode) throws Exception {
        if (lastResponse == null) {
            throw new Exception("No previous response to verify status code");
        }
        int actualStatusCode = lastResponse.getStatusCode();
        String responseBody = lastResponse.getBody().asString();
        logger.info(String.format("Response body: %s", responseBody));
        Assert.assertEquals("Response status code does not match expected", expectedStatusCode, actualStatusCode);
        logger.info(String.format("Response status code is %s", expectedStatusCode));
    }

    @Then("the response body parameters match the request body")
    public void verifyResponseBodyMatchesRequest() throws Exception {
        if (lastResponse == null) {
            throw new Exception("Response body is null or empty");
        }

        String requestBodyString = ScenarioContext.getInstance().<String>getContext(ContextKey.REQUEST_BODY_SENT, String.class);

        JsonPath responseJson = lastResponse.jsonPath();
        logger.info("Response body parameters match the request body:");

        logger.info(String.format("date: %s (Request) mapped with %s (Response)",
                ApiActions.extractFieldFromRequestBody("date", requestBodyString), responseJson.get("data.date")));
        logger.info(String.format("operational_country_id: %s (Request) mapped with %s (Response)",
                ApiActions.extractFieldFromRequestBody("operational_country_id", requestBodyString), responseJson.get("data.operational_country_id")));
        logger.info(String.format("length: %s (Request) mapped with %s (Response)",
                ApiActions.extractFieldFromRequestBody("length", requestBodyString), responseJson.get("data.length")));
        logger.info(String.format("description: %s (Request) mapped with %s (Response)",
                ApiActions.extractFieldFromRequestBody("description", requestBodyString), responseJson.get("data.description")));

        List<Map<String, String>> holidayLocations = responseJson.getList("data.HolidayLocation");
        logger.info(String.format("location: %s (Request), mapped with location_id: %s",
                ApiActions.extractFieldFromRequestBody("location", requestBodyString), ApiActions.getLocationIds(holidayLocations)));

        boolean editable = responseJson.get("data.editable");
        logger.info(String.format("adjustLeave: %s (Request), mapped with editable: %s (Response)",
                ApiActions.extractFieldFromRequestBody("adjustLeave", requestBodyString), editable));
    }
}
