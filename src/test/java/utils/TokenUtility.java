package utils;

import api.ApiTokenGenerator;

public class TokenUtility {
    public static String generateBearerToken() {
        try {
            return ApiTokenGenerator.generateBearerToken();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate bearer token", e);
        }
    }
}