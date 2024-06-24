package api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerInfo {
    @JsonProperty("customerId")
    private String customerId;

    @JsonProperty("isDeleted")
    private String isDeleted;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    // Getters and setters
    public String getCustomerId() {
        return customerId;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}