package enums;

public enum ErrorMessages {
    TOKEN_GENERATION_FAILED("Failed to generate bearer token");

    private final String message;

    ErrorMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
