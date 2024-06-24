package api;

import enums.ApiProperties;
import enums.ErrorMessages;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;

public class ApiTokenGenerator {

    private static final String TOKEN_ENDPOINT = ApiProperties.TOKEN_ENDPOINT.getValue();

    private static final String CLIENT_ID = ApiProperties.CLIENT_ID.getValue();

    private static final String CLIENT_SECRET = ApiProperties.CLIENT_SECRET.getValue();

    private static final String USERNAME = ApiProperties.USERNAME.getValue();

    private static final String PASSWORD = ApiProperties.PASSWORD.getValue();

    private static final String GRANT_TYPE = "client_credentials";

    private static final int SUCCESS_STATUS_CODE = 200;

    public static String generateBearerToken() {
        Response response = RestAssured
                .given()
                .formParam("client_id", CLIENT_ID)
                .formParam("client_secret", CLIENT_SECRET)
                .formParam("grant_type", GRANT_TYPE)
                .formParam("username", USERNAME)
                .formParam("password", PASSWORD)
                .post(TOKEN_ENDPOINT);

        if (response.getStatusCode() != SUCCESS_STATUS_CODE) {
            throw new RuntimeException(ErrorMessages.TOKEN_GENERATION_FAILED.getMessage());
        }

        JSONObject jsonResponse = new JSONObject(response.getBody().asString());
        return jsonResponse.getString(ApiProperties.ACCESS_TOKEN.getKey());
    }
}