package utils;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;

public class ApiTokenGenerator {

    private static final String TOKEN_ENDPOINT = PropertiesUtil.getProperty("token.endpoint");
    private static final String CLIENT_ID = PropertiesUtil.getProperty("client.id");
    private static final String CLIENT_SECRET = PropertiesUtil.getProperty("client.secret");
    private static final String USERNAME = PropertiesUtil.getProperty("username");
    private static final String PASSWORD = PropertiesUtil.getProperty("password");

    public static String generateBearerToken() throws Exception {
        // Send POST request to get token
        Response response = RestAssured
                .given()
                .formParam("client_id", CLIENT_ID)
                .formParam("client_secret", CLIENT_SECRET)
                .formParam("grant_type", "client_credentials")
                .formParam("username", USERNAME)
                .formParam("password", PASSWORD)
                .post(TOKEN_ENDPOINT);

        if (response.getStatusCode() != 200) {
            throw new RuntimeException("Failed to generate bearer token");
        }

        // Parse the response to get the token
        JSONObject jsonResponse = new JSONObject(response.getBody().asString());
        return jsonResponse.getString("access_token");
    }
}