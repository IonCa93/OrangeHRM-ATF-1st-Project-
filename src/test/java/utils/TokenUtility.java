package utils;

public class TokenUtility {
    public static String generateBearerToken() {
        try {
            // Your token generation logic here
            return ApiTokenGenerator.generateBearerToken();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate bearer token", e);
        }
    }
}