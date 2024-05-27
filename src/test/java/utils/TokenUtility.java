package utils;

public class TokenUtility {
    public static String generateBearerToken() {
        try {
            // Your token generation logic here
            String token = ApiTokenGenerator.generateBearerToken();
            return token;
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate bearer token", e);
        }
    }
}