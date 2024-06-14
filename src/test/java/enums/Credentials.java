package enums;

import utils.PropertiesUtil;

public enum Credentials {
    ADMIN;

    public String getUsername() {
        return PropertiesUtil.getProperty("username");
    }

    public String getPassword() {
        return PropertiesUtil.getProperty("password");
    }
}
