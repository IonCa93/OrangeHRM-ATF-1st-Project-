package actions;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class ApiActions {
    private static String bearerToken;

    public static String getBearerToken() {
        return bearerToken;
    }

    public static void setBearerToken(String token) {
        bearerToken = token;
    }

    public static Response sendGetRequest(String endpoint) {
        return RestAssured
                .given()
                .header("Authorization", "Bearer " + getBearerToken())
                .get(endpoint);
    }

//    public static Response sendPostRequest(String endpoint, Object requestBody) {
//        return RestAssured
//                .given()
//                .header("Authorization", "Bearer " + getBearerToken())
//                .contentType("application/json")
//                .body(requestBody)
//                .post(endpoint);
//    }
}
