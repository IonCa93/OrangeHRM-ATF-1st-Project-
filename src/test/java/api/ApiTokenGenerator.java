package api;

import enums.ApiPropertyValues;
import enums.ErrorMessages;
import enums.FormParams;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;

public class ApiTokenGenerator {

    private static final String TOKEN_ENDPOINT = ApiPropertyValues.TOKEN_ENDPOINT.getValue();
    private static final String CLIENT_ID = ApiPropertyValues.CLIENT_ID.getValue();
    private static final String CLIENT_SECRET = ApiPropertyValues.CLIENT_SECRET.getValue();
    private static final String USERNAME = ApiPropertyValues.USERNAME.getValue();
    private static final String PASSWORD = ApiPropertyValues.PASSWORD.getValue();

    public static String generateBearerToken() {
        // Send POST request to get token
        Response response = RestAssured
                .given()
                .formParam(FormParams.CLIENT_ID.getParam(), CLIENT_ID)
                .formParam(FormParams.CLIENT_SECRET.getParam(), CLIENT_SECRET)
                .formParam(FormParams.GRANT_TYPE.getParam(), "client_credentials")
                .formParam(FormParams.USERNAME.getParam(), USERNAME)
                .formParam(FormParams.PASSWORD.getParam(), PASSWORD)
                .post(TOKEN_ENDPOINT);

        if (response.getStatusCode() != 200) {
            throw new RuntimeException(ErrorMessages.TOKEN_GENERATION_FAILED.getMessage());
        }

        // Parse the response to get the token
        JSONObject jsonResponse = new JSONObject(response.getBody().asString());
        return jsonResponse.getString("access_token");
    }
}
