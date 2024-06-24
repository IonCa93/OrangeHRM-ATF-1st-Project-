package enums;

import utils.PropertiesUtil;

public enum ApiProperties {
    TOKEN_ENDPOINT("token.endpoint"),
    CLIENT_ID("client.id"),
    CLIENT_SECRET("client.secret"),
    USERNAME("username"),
    PASSWORD("password"),
    ACCESS_TOKEN("access_token");

    private final String key;

    ApiProperties(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return PropertiesUtil.getProperty(key);
    }
}