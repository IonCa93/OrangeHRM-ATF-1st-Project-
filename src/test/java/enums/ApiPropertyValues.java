package enums;

import utils.PropertiesUtil;

public enum ApiPropertyValues {
    TOKEN_ENDPOINT(ApiProperties.TOKEN_ENDPOINT),
    CLIENT_ID(ApiProperties.CLIENT_ID),
    CLIENT_SECRET(ApiProperties.CLIENT_SECRET),
    USERNAME(ApiProperties.USERNAME),
    PASSWORD(ApiProperties.PASSWORD);

    private final ApiProperties property;

    ApiPropertyValues(ApiProperties property) {
        this.property = property;
    }

    public String getValue() {
        return PropertiesUtil.getProperty(property.getKey());
    }
}