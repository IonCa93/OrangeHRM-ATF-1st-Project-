package enums;

public enum ApiProperties {
    TOKEN_ENDPOINT("token.endpoint"),
    CLIENT_ID("client.id"),
    CLIENT_SECRET("client.secret"),
    USERNAME("username"),
    PASSWORD("password");

    private final String key;

    ApiProperties(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
