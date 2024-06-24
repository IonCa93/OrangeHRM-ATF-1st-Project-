package enums;

public enum FormParams {
    CLIENT_ID("client_id"),
    CLIENT_SECRET("client_secret"),
    GRANT_TYPE("grant_type"),
    USERNAME("username"),
    PASSWORD("password");

    private final String param;

    FormParams(String param) {
        this.param = param;
    }

    public String getParam() {
        return param;
    }
}