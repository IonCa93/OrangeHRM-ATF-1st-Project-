package actions;

import api.CustomerInfo;
import api.HolidayRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class ApiActions {
    private static String bearerToken;

    public static String getBearerToken() {
        return bearerToken;
    }

    public static void setBearerToken(String token) {
        bearerToken = token;
    }

    public static Response sendGetRequest(String endpoint) {
        return RestAssured
                .given()
                .header("Authorization", "Bearer " + getBearerToken())
                .get(endpoint);
    }

    public static Response sendPostRequest(String endpoint, String jsonBody) {
        RequestSpecification request = RestAssured.given();
        request.header("Authorization", "Bearer " + getBearerToken());
        request.header("Content-Type", "application/json");
        request.body(jsonBody);
        return request.post(endpoint);
    }

    public static boolean compareCustomerData(CustomerInfo customer, Map<String, String> expectedCustomer) {
        String customerId = expectedCustomer.get("customerId");
        String isDeleted = expectedCustomer.get("isDeleted");
        String name = expectedCustomer.get("name");
        String description = expectedCustomer.get("description");

        return customer.getCustomerId().equals(customerId)
                && customer.getIsDeleted().equals(isDeleted)
                && customer.getName().equals(name)
                && customer.getDescription().equals(description);
    }

    public static void replaceDynamicFields(Map<String, String> holidayData) {
        for (Map.Entry<String, String> entry : holidayData.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            if (value.equals("<dynamic_description>")) {
                holidayData.put(key, getRandomDescription());
            } else if (value.equals("<dynamic_date>")) {
                holidayData.put(key, getRandomDateInYear(LocalDate.now().getYear()));
            }
        }
    }

    public static HolidayRequest generateHolidayRequest(Map<String, String> holidayData) {
        HolidayRequest request = new HolidayRequest();

        holidayData.forEach((key, value) -> {
            switch (key) {
                case "location" -> request.setLocation(convertToStringList(value));
                case "adjustLeave" -> request.setAdjustLeave(Boolean.parseBoolean(value));
                case "length" -> request.setLength(value);
                case "operational_country_id" -> request.setOperational_country_id(value);
                case "date" -> request.setDate(value);
                case "description" -> request.setDescription(getRandomDescription());
                default -> {
                }
            }
        });
        return request;
    }

    public static List<String> convertToStringList(String value) {
        String[] valueArray = value.replace("[", "").replace("]", "").split(",");
        List<String> stringList = new ArrayList<>();
        for (String num : valueArray) {
            stringList.add(num.trim());
        }
        return stringList;
    }

    public static String getRandomDateInYear(int year) {
        Random random = new Random();
        int minDay = (int) LocalDate.of(year, 1, 1).toEpochDay();
        int maxDay = (int) LocalDate.of(year, 12, 31).toEpochDay();
        long randomDay = minDay + random.nextInt(maxDay - minDay);
        LocalDate randomDate = LocalDate.ofEpochDay(randomDay);
        return randomDate.toString();
    }

    public static String getRandomDescription() {
        List<String> descriptions = Arrays.asList(
                "Holiday in the mountains",
                "Beach vacation",
                "City sightseeing tour",
                "Cultural trip"
        );
        Random random = new Random();
        return descriptions.get(random.nextInt(descriptions.size()));
    }

    public static String extractFieldFromRequestBody(String field, String requestBodyString) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        HolidayRequest requestBody = objectMapper.readValue(requestBodyString, HolidayRequest.class);

        return switch (field) {
            case "date" -> requestBody.getDate();
            case "operational_country_id" -> requestBody.getOperational_country_id();
            case "length" -> requestBody.getLength();
            case "description" -> requestBody.getDescription();
            case "location" -> String.join(",", requestBody.getLocation());
            case "adjustLeave" -> String.valueOf(requestBody.isAdjustLeave());
            default -> "Field '" + field + "' not found or not supported";
        };
    }

    public static List<String> getLocationIds(List<Map<String, String>> holidayLocationList) {
        List<String> locationIds = new ArrayList<>();
        for (Map<String, String> location : holidayLocationList) {
            locationIds.add(location.get("location_id"));
        }
        return locationIds;
    }
}
